package us.thirdbase.rspi.parts.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import us.thirdbase.rspi.parts.model.Part;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class PartRepositoryTest {
    private final Clock clock = Clock.fixed(Instant.parse("2018-08-06T05:01:00.00Z"), ZoneId.of("America/Chicago"));
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private PartRepository subject;
    private Part sample;

    @BeforeEach
    void setUp() {
        sample = Part.builder()
            .name("part name")
            .cost(new BigDecimal("2.27"))
            .quantity((short) 3)
            .shortDescription("fictitious part")
            .description("fictitious part that is really long")
            .url("http://someplace/nothere")
            .orderDate(LocalDate.now(clock))
            .orderId("23k4lj2kj432j")
            .build();

        entityManager.persist(sample);
        entityManager.flush();
    }

    @Test
    @DisplayName("View all parts")
    public void viewParts() {
        List<Part> actual = subject.findAll();
        assertThat(actual.size()).isGreaterThan(3);
        assertThat(actual).contains(sample);
    }

    @Test
    @DisplayName("View specific part")
    public void viewPart() {
        Optional<Part> actual = subject.findById(sample.getId());

        assertThat(actual.isPresent()).isTrue();
        assertThat(actual.orElse(null)).isEqualTo(sample);
    }

    @Test
    @DisplayName("View the first page")
    void view1stPage() {
        Pageable page = PageRequest.of(0, 10, Sort.by(Sort.Order.by("name")));
        Slice<Part> actual = subject.findAll(page);
        assertThat(actual.getSize()).isEqualTo(10);
        assertThat(actual.hasContent()).isTrue();
        assertThat(actual.getContent()).containsExactly(
            actual.getContent().stream()
                .sorted(Comparator.comparing(Part::getName))
                .toArray(Part[]::new));
    }

    @Test
    @DisplayName("Sort the first page")
    void sortFirstPage() {
        Pageable page = PageRequest.of(0, 10, Sort.by("name"));
        Slice<Part> actual = subject.findAll(page);
        String currentName = actual.getContent().get(0).getName();
        for (Part part : actual.getContent()) {
            assertThat(currentName).isLessThanOrEqualTo(part.getName());
            currentName = part.getName();
        }
    }

    @Test
    @DisplayName("Search for exact match in name")
    void searchExactMatch() {
        PartSpecification<Part> spec = new PartSpecification<>(new SearchCriteria("name", SearchOperation.EQUALITY, "OpenSprinkler"));
        Part part = Part.builder()
            .id(3L)
            .name("OpenSprinkler")
            .cost(new BigDecimal("77.99").setScale(2, RoundingMode.HALF_EVEN))
            .quantity((short) 1)
            .build();

        List<Part> actual = subject.findAll(spec, Sort.by(Sort.Order.by("name")));

        assertThat(actual.size()).isEqualTo(1);
            assertThat(actual.get(0))
            .usingRecursiveComparison()
            .ignoringFields("shortDescription", "description", "url")
            .isEqualTo(part);
    }

    @Test
    @DisplayName("Ensure a default data supplied")
    void savePartWithoutDate() {
        Part.PartBuilder samplePartBuilder = Part.builder()
            .cost(new BigDecimal("2.27"))
            .description("fictitious part that is really long")
            .name("part name")
            .orderId("23k4lj2kj432j")
            .quantity((short) 3)
            .shortDescription("fictitious part")
            .url("http://someplace/nothere");
        Part samplePart = samplePartBuilder.build();
        Part expected = samplePartBuilder
            .orderDate(LocalDate.of(2017, 12, 31))
            .build();

        Part actual = entityManager.persist(samplePart);
        entityManager.flush();

        assertThat(actual).usingRecursiveComparison()
            .ignoringFields("id")
            .isEqualTo(expected);
    }

    @Test
    @DisplayName("Search for contains in name")
    void searchContains() {
        PartSpecification<Part> spec = new PartSpecification<>(new SearchCriteria("name", SearchOperation.CONTAINS, "Sprinkler"));
        List<Part> actual = subject.findAll(spec, Sort.by(Sort.Order.by("name")));
        Part part = Part.builder()
            .id(3L)
            .name("OpenSprinkler")
            .cost(new BigDecimal("77.99").setScale(2, RoundingMode.HALF_EVEN))
            .quantity((short) 1)
            .build();
        assertThat(actual.size()).isEqualTo(1);
        assertThat(actual.get(0))
            .usingRecursiveComparison()
            .ignoringFields("shortDescription", "description", "url")
            .isEqualTo(part);
    }
}

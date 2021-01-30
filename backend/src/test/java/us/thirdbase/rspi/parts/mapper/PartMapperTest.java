package us.thirdbase.rspi.parts.mapper;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import us.thirdbase.rspi.parts.model.Part;
import us.thirdbase.rspi.parts.model.PartDto;

import static org.assertj.core.api.Assertions.assertThat;

class PartMapperTest {

    private PartEntityDtoMapper subject = Mappers.getMapper(PartEntityDtoMapper.class);
    private Part part;
    private PartDto partDto;

    @BeforeEach
    void setUp() {
        Clock clock = Clock.fixed(Instant.now(), ZoneOffset.UTC);
        part = Part.builder()
            .id(1L)
            .cost(new BigDecimal("2.39"))
            .name("sample part")
            .quantity((short) 2)
            .shortDescription("short desc")
            .orderDate(LocalDate.now(clock))
            .description("long description")
            .build();
        partDto = PartDto.builder()
            .id(1L)
            .cost(new BigDecimal("2.39"))
            .name("sample part")
            .quantity((short) 2)
            .shortDescription("short desc")
            .orderDate(LocalDate.now(clock))
            .description("long description")
            .build();
    }

    @Test
    void mapsPartEntityToDto() {
        PartDto actual = subject.partEntityToDto(part);

        assertThat(actual).isEqualToComparingFieldByFieldRecursively(partDto);
    }

    @Test
    void mapsPartDtoToEntity() {
        Part actual = subject.partDtoToEntity(partDto);

        assertThat(actual).isEqualToComparingFieldByFieldRecursively(part);
    }
}

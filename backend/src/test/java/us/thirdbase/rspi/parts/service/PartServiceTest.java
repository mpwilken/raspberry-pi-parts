package us.thirdbase.rspi.parts.service;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import us.thirdbase.rspi.parts.mapper.PartEntityDtoMapper;
import us.thirdbase.rspi.parts.model.Image;
import us.thirdbase.rspi.parts.model.Part;
import us.thirdbase.rspi.parts.model.PartDto;
import us.thirdbase.rspi.parts.repository.ImageRepository;
import us.thirdbase.rspi.parts.repository.PartRepository;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PartServiceTest {

    private PartService subject;
    @Mock
    private PartRepository partRepository;
    @Mock
    private ImageRepository imageRepository;
    @Captor
    private ArgumentCaptor<Part> partCaptor;
    private Clock clock;
    @Captor
    private ArgumentCaptor<Image> imageCaptor;

    @BeforeEach
    void setUp() {
        clock = Clock.fixed(Instant.now(), ZoneOffset.UTC);
        subject = new PartService(partRepository, imageRepository, Mappers.getMapper(PartEntityDtoMapper.class));
    }

    @Test
    void getAllParts() {
        Part expected = Part.builder().name("test part").build();
        given(partRepository.findAll(any(Sort.class))).willReturn(singletonList(expected));

        List<PartDto> actual = subject.getAllParts();

        assertThat(actual.get(0)).isEqualToComparingFieldByField(expected);
        verify(partRepository).findAll(any(Sort.class));
    }

    @Test
    void getAllPartsByPage() {
        Pageable page = PageRequest.of(0, 10, Sort.by("name"));
        Part expected = Part.builder().name("test part").build();
        Page<Part> slice = new PageImpl<>(singletonList(expected));

        given(partRepository.findAll(Mockito.<Specification<Part>>any(), any(Pageable.class))).willReturn(slice);

        Slice<Part> actual = subject.getAllParts("test part", page);

        assertThat(actual.getContent().get(0)).isEqualToComparingFieldByField(expected);
        verify(partRepository).findAll(Mockito.<Specification<Part>>any(), any(Pageable.class));
    }

    @Test
    void getAllPartsBySpecification() {
        Part expected = Part.builder().name("test part").build();
        List<Part> slice = singletonList(expected);
        given(partRepository.findAll(ArgumentMatchers.<Specification<Part>>any(), any(Sort.class))).willReturn(slice);
        Specification<Part> specification = PartService.buildPartSpecification("test part");

        List<PartDto> actual = subject.getPartsBySpecification("test part");

        assertThat(actual.get(0)).isEqualToComparingFieldByField(expected);
        verify(partRepository).findAll(specification, Sort.by("name"));
    }

    @Test
    void getPartsByPage() {
        Part expected = Part.builder().name("test part").build();
        Pageable page = PageRequest.of(0, 10, Sort.by("name"));
        Page<Part> slice = new PageImpl<>(singletonList(expected));

        given(partRepository.findAll(page)).willReturn(slice);

        Slice<PartDto> actual = subject.getPartsByPage(page);

        assertThat(actual.getContent().get(0)).isEqualToComparingFieldByField(expected);
        verify(partRepository).findAll(page);
    }

    @Test
    void getPart() {
        Part expected = Part.builder().name("test part").build();
        given(partRepository.getOne(anyLong())).willReturn(expected);

        PartDto actual = subject.getPart(1L);

        assertThat(actual).isEqualToComparingFieldByField(expected);
        verify(partRepository).getOne(1L);
    }

    @Test
    void savePart() {
        Part expected = Part.builder()
            .id(1L)
            .quantity((short) 2)
            .shortDescription("short desc")
            .name("some part")
            .cost(new BigDecimal("9.28"))
            .description("some description")
            .orderDate(LocalDate.now(clock))
            .orderId("123j23")
            .url("someurl")
            .build();
        given(partRepository.save(partCaptor.capture())).willReturn(expected);

        PartDto actual = subject.save(expected);

        assertThat(actual).isEqualToComparingFieldByFieldRecursively(expected);
        assertThat(actual).isEqualToComparingFieldByFieldRecursively(partCaptor.getValue());
        verify(partRepository).save(any(Part.class));
    }

    @Test
    void saveImage() {
        Part part = Part.builder()
            .id(1L)
            .quantity((short) 2)
            .shortDescription("short desc")
            .name("some part")
            .cost(new BigDecimal("9.28"))
            .description("some description")
            .orderDate(LocalDate.now(clock))
            .orderId("123j23")
            .url("someurl")
            .build();
        Image expected = Image.builder()
            .content("something".getBytes())
            .mediaType("image/png")
            .part(part)
            .build();
        given(imageRepository.save(imageCaptor.capture())).willReturn(expected);

        Image actual = subject.save(expected);

        assertThat(actual.getMediaType()).isEqualTo("image/png");
        assertThat(new String(actual.getContent())).isEqualTo("something");
        assertThat(actual.getPart()).isEqualToComparingFieldByFieldRecursively(part);
        verify(imageRepository).save(any(Image.class));
    }
}

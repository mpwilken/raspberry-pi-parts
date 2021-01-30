package us.thirdbase.rspi.parts.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import us.thirdbase.rspi.parts.mapper.PartEntityDtoMapper;
import us.thirdbase.rspi.parts.model.Image;
import us.thirdbase.rspi.parts.model.Part;
import us.thirdbase.rspi.parts.model.PartDto;
import us.thirdbase.rspi.parts.repository.ImageRepository;
import us.thirdbase.rspi.parts.repository.PartRepository;
import us.thirdbase.rspi.parts.repository.PartSpecificationBuilder;
import us.thirdbase.rspi.parts.repository.SearchOperation;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.stream.Collectors.toList;

@Service
public class PartService {

    private final PartRepository partRepository;
    private final ImageRepository imageRepository;
    private final PartEntityDtoMapper mapper;

    PartService(PartRepository partRepository, ImageRepository imageRepository, PartEntityDtoMapper mapper) {
        this.partRepository = partRepository;
        this.imageRepository = imageRepository;
        this.mapper = mapper;
    }

    static Specification<Part> buildPartSpecification(String search) {
        PartSpecificationBuilder builder = new PartSpecificationBuilder();
        String operationSetExper = new StringJoiner("|").add(Arrays.toString(SearchOperation.SIMPLE_OPERATION_SET)).toString();
        Pattern pattern = Pattern.compile(
            "(\\w+?)(" + operationSetExper + ")(\\p{Punct}?)(\\w+?)(\\p{Punct}?),");
        Matcher matcher = pattern.matcher(search + ",");
        while (matcher.find()) {
            builder.with(
                matcher.group(1),
                matcher.group(2),
                matcher.group(4),
                matcher.group(3),
                matcher.group(5));
        }

        return builder.build();
    }

    public List<PartDto> getAllParts() {
        return partRepository.findAll(Sort.by(Sort.Order.by("name"))).stream()
            .map(mapper::partEntityToDto)
            .collect(toList());
    }

    public Slice<Part> getAllParts(String search, Pageable page) {
        return partRepository.findAll(buildPartSpecification(search), page);
    }

    public PartDto getPart(Long id) {
        return mapper.partEntityToDto(partRepository.getOne(id));
    }

    public Slice<PartDto> getPartsByPage(Pageable page) {
        return partRepository.findAll(page).map(mapper::partEntityToDto);
    }

    public Image findImage(Long id) {
        Optional<Image> image = imageRepository.findById(id);
        return image.orElse(Image.builder().content(new byte[]{}).build());
    }

    public List<Image> findImagesByPartId(Long id) {
        return imageRepository.findAllByPartId(id);
    }

    public List<PartDto> getPartsBySpecification(String search) {
        return partRepository.findAll(buildPartSpecification(search), Sort.by(Sort.Order.by("name"))).stream()
            .map(mapper::partEntityToDto)
            .collect(toList());
    }

    public PartDto save(Part part) {
        return mapper.partEntityToDto(partRepository.save(part));
    }

    public Image save(Image image) {
        return imageRepository.save(image);
    }
}

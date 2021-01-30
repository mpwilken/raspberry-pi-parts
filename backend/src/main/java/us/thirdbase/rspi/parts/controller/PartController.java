package us.thirdbase.rspi.parts.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import us.thirdbase.rspi.parts.mapper.PartEntityDtoMapper;
import us.thirdbase.rspi.parts.model.Image;
import us.thirdbase.rspi.parts.model.Part;
import us.thirdbase.rspi.parts.model.PartDto;
import us.thirdbase.rspi.parts.service.PartService;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/api")
public class PartController {

    private final PartService partService;
    private final PartEntityDtoMapper mapper;
    @Value("${info.version}")
    private String version;

    public PartController(PartService partService, PartEntityDtoMapper mapper) {
        this.partService = partService;
        this.mapper = mapper;
    }

    @GetMapping(value = "/version", produces = "application/json")
    public String version() {
        return "{\"number\": \"" + version + "\"}";
    }

    @GetMapping("/parts")
    @ResponseStatus(HttpStatus.OK)
    public List<PartDto> viewAllParts() {
        return partService.getAllParts();
    }

    @GetMapping("/parts/search")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<PartDto> findAllSpecification(@RequestParam(value = "search", required = false) String search) {
        return partService.getPartsBySpecification(search);
    }

    @GetMapping("/parts/searchByPage")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Slice<Part> findAllSpecificationByPage(@RequestParam(value = "search") String search,
                                                  @RequestParam(name = "size", required = false, defaultValue = "10") int size,
                                                  @RequestParam(name = "page", required = false, defaultValue = "0") int page) {
        return partService.getAllParts(search, PageRequest.of(page, size, Sort.by(Sort.Order.by("name"))));
    }

    @GetMapping("/parts/page")
    @ResponseStatus(HttpStatus.OK)
    public Slice<PartDto> viewPartsByPage(
        @RequestParam(name = "size", required = false, defaultValue = "10") int size,
        @RequestParam(name = "page", required = false, defaultValue = "0") int page) {
        return partService.getPartsByPage(PageRequest.of(page, size, Sort.by(Sort.Order.by("name"))));
    }

    @GetMapping("/parts/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PartDto viewPartById(@PathVariable(name = "id") Long id) {
        return partService.getPart(id);
    }

    @GetMapping("/image/{id}")
    public ResponseEntity<Image> findImageById(@PathVariable(value = "id") Long id) {
        HttpHeaders headers = new HttpHeaders();
        Image image = partService.findImage(id);
        return new ResponseEntity<>(image, headers, HttpStatus.OK);
    }

    @GetMapping("/parts/{id}/images")
    @ResponseStatus(code = HttpStatus.OK)
    public List<Image> findImagesByPartId(@PathVariable(value = "id") Long id) {
        return partService.findImagesByPartId(id);
    }

    @PostMapping("/parts")
    @ResponseStatus(code = HttpStatus.CREATED)
    public PartDto addPart(@RequestBody Part part) {
        return partService.save(part);
    }

    @PostMapping("/image/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public PartDto saveAuto(@PathVariable(value = "id") Long id,
                            @RequestParam(value = "data", required = false) List<MultipartFile> files)
        throws IOException {
        PartDto part = partService.getPart(id);
        for (MultipartFile file : files) {
            InputStream inputStream = new BufferedInputStream(file.getInputStream());
            byte[] content = inputStream.readAllBytes();
            Image image = Image.builder()
                .content(content)
                .mediaType(file.getContentType())
                .part(mapper.partDtoToEntity(part))
                .build();
            partService.save(image);
        }
        return part;
    }
}

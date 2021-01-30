package acceptance;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.LocalDate;
import java.util.Objects;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import us.thirdbase.rspi.parts.PartsApplication;
import us.thirdbase.rspi.parts.model.Part;
import us.thirdbase.rspi.parts.model.PartDto;
import us.thirdbase.rspi.parts.repository.PartRepository;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = PartsApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(properties = {
    "info.version=1",
    "app.security.jwt.name=some-token",
    "app.security.jwt.expiration=200",
    "app.security.jwt.HS256=jklkajsdf",
    "app.security.jwt.prefix=myprefix ",
    "app.security.cors.methods=HEAD, GET, POST, PUT, DELETE, PATCH, OPTIONS",
    "app.security.cors.headers=Origin, Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers, Authorization, Access-Control-Allow-Origin, APP-TOKEN-KEY, X-XSRF-TOKEN",
    "app.security.cors.exposed=X-AUTH-TOKEN, APP-TOKEN-KEY, X-XSRF-TOKEN, Content-Type, Content-Disposition",
    "app.security.cors.origins=http://localhost:4200",
    "app.security.temporary.username=joe",
    "app.security.temporary.password=password",
    "app.security.urls.unprotected=/**",
})
class PartControllerAcceptanceTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    private PartRepository partRepository;
    @Autowired
    private Clock clock;
    private ObjectMapper mapper;

    @BeforeEach
    void setUp() {
        Part sample1 = Part.builder()
            .name("Sample part 1")
            .cost(new BigDecimal("1.33"))
            .quantity((short) 2)
            .shortDescription("Sample part 1 description")
            .description("Sample part 1 description that is really long")
            .orderDate(LocalDate.now(clock))
            .url("http://someurl.com")
            .orderId("1")
            .build();
        partRepository.save(sample1);
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    @Test
    void viewAllParts() throws Exception {
        mockMvc.perform(get("/api/parts"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(greaterThan(52)))
        ;
    }

    @Test
    void viewPartsByPage() throws Exception {
        mockMvc.perform(get("/api/parts/page?number=1&size=10"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(11))
        ;
    }

    @Test
    void viewPartById() throws Exception {
        mockMvc.perform(get("/api/parts/2"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name", containsString("Solderless BreadBoard")))
        ;
    }

    @Test
    void findAllBySpecification() throws Exception {
        mockMvc.perform(get("/api/parts/search").param("search", "name:*adapter*"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(9))
            .andExpect(jsonPath("$[0].name", containsString("5V 2.5A AC Adapter")))
        ;
    }

    @Test
    void findAllBySpecificationPage() throws Exception {
        mockMvc.perform(get("/api/parts/searchByPage")
            .param("search", "name:*adapter*")
            .param("page", "1")
            .param("size", "7"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content.length()").value(2))
        ;
    }

    @Test
    void findImageById() throws Exception {
        mockMvc.perform(get("/api/image/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.mediaType").value("image/jpg"))
        ;
    }

    @Test
    void addPart() throws Exception {
        PartDto newPart = PartDto.builder()
            .quantity((short) 1)
            .shortDescription("short description")
            .name("sample part")
            .cost(new BigDecimal("9.23"))
            .description("long description")
            .orderId("123")
            .orderDate(LocalDate.now(clock).minusDays(2))
            .url("http://someurl.com")
            .build();

        mockMvc.perform(post("/api/parts/")
            .content(mapper.writeValueAsString(newPart))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
        ;
    }

    @Test
    void addPartFromJustString() throws Exception {
        String newPart = IOUtils.toString(this.getClass().getResourceAsStream("/sample-part.json"), StandardCharsets.UTF_8);

        mockMvc.perform(post("/api/parts/")
            .content(newPart.getBytes())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
        ;
    }

    @Test
    void addImage() throws Exception {
        byte[] content = new FileInputStream(new File(Objects.requireNonNull(getClass().getClassLoader().getResource("sample.png")).getFile())).readAllBytes();
        MockMultipartFile image = new MockMultipartFile("data", "sample.png", "image/png", content);

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/image/1").file(image))
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
        ;
    }
}

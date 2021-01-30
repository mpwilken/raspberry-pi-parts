package acceptance;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.time.Clock;
import java.time.Instant;
import java.util.Collection;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import us.thirdbase.rspi.parts.PartsApplication;
import us.thirdbase.rspi.parts.config.JwtTokenConfigurationProperties;
import us.thirdbase.rspi.parts.config.security.JwtAuthenticationFilter;
import us.thirdbase.rspi.parts.config.security.JwtAuthorizationFilter;
import us.thirdbase.rspi.parts.config.security.JwtProvider;
import us.thirdbase.rspi.parts.config.security.SecretService;

import static java.util.Objects.requireNonNull;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = {PartsApplication.class})
@AutoConfigureMockMvc
@Transactional
@TestPropertySource(properties = {
    "info.version=1",
    "app.security.jwt.name=some-token",
    "app.security.jwt.prefix=myprefix ",
    "app.security.jwt.expiration=200",
    "app.security.jwt.HS256=magic",
    "app.security.temporary.username=joe",
    "app.security.temporary.password=password",
})
@ActiveProfiles("test")
@Slf4j
class SecurityAcceptanceTest {

    private static final String MYPREFIX = "myprefix ";
    @Autowired
    Clock clock;
    @Autowired
    WebApplicationContext webAppContext;
    @Autowired
    SecretService secretService;
    private MockMvc mockMvc;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserDetailsManager userDetailsManager;
    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private JwtTokenConfigurationProperties jwtTokenConfigurationProperties;
    @Autowired
    private AuthenticationEntryPoint authenticationEntryPoint;
    @Value("${app.security.jwt.name}")
    private String tokenName;

    @BeforeEach
    void setup() {
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager, authenticationEntryPoint, jwtTokenConfigurationProperties, jwtProvider);
        JwtAuthorizationFilter jwtAuthorizationFilter = new JwtAuthorizationFilter(authenticationManager, jwtProvider, jwtTokenConfigurationProperties, userDetailsManager);
        mockMvc = MockMvcBuilders
            .webAppContextSetup(webAppContext)
            .apply(springSecurity())
            .addFilter(jwtAuthenticationFilter)
            .addFilter(jwtAuthorizationFilter)
            .build();
    }

    @Test
    void invalidTokenCannotAccessUserProtectedUrl() {
        try {
            mockMvc.perform(get("/api/parts")
                .header(tokenName, generateValidToken().compact().substring(1)));
        } catch (Exception e) {
            assertThat(e.getMessage()).contains("Unable to read JSON value");
        }
    }

    @Test
    void expiredTokenCannotAccessUserProtectedUrl() {
        try {
            mockMvc.perform(get("/api/parts")
                .header(tokenName, MYPREFIX + generateValidToken().setExpiration(Date.from(Instant.now(clock).minusSeconds(1))).compact()))
                .andExpect((status().isUnauthorized()))
                .andReturn();
        } catch (Exception e) {
            assertThat(e.getMessage()).isEqualTo("Expired jwt");
        }
    }

    @Test
    void authenticatedRequest() throws Exception {
        String validToken = retrieveTokenFromLogin();
        MvcResult mvcResult = mockMvc.perform(get("/login")
            .header("some-token", MYPREFIX + validToken))
            .andDo(print())
            .andExpect((status().isOk()))
            .andExpect(jsonPath("$.name").value("joe"))
            .andExpect(jsonPath("$.authorities[0].authority").value("ROLE_USER"))
            .andExpect(jsonPath("$.authenticated").value(true))
            .andExpect(jsonPath("$.principal.username").value("joe"))
            .andExpect(jsonPath("$.principal.authorities[0].authority").value("ROLE_USER"))
            .andReturn();
        assertThat(mvcResult.getResponse().getHeader("some-token")).isNotNull();
    }

    @Test
    void unauthenticatedRequest() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/login"))
            .andExpect((status().isOk()))
            .andExpect(content().string(""))
            .andReturn();
        assertThat(mvcResult.getResponse().getHeader("some-token")).isNull();
    }

    @Test
    void renewToken() throws Exception {
        String token = retrieveTokenFromLogin();
        slowToLetTokenGenerateDifferentValue();
        assertThat(token).isNotEmpty();
        MvcResult mvcResult = mockMvc.perform(get("/api/parts")
            .header("some-token", MYPREFIX + token))
            .andExpect((status().isOk()))
            .andReturn();
        assertThat(mvcResult.getResponse().getHeader("some-token")).isNotNull();
        assertThat(requireNonNull(mvcResult.getResponse().getHeader("some-token")).length()).isEqualTo(token.length());
        assertThat(mvcResult.getResponse().getHeader("some-token")).isNotEqualTo(token);
    }

    private void slowToLetTokenGenerateDifferentValue() throws InterruptedException {
        Thread.sleep(1000);
    }

    private String retrieveTokenFromLogin() throws Exception {
        MvcResult token = mockMvc.perform(get("/login").with(httpBasic("joe", "password")))
            .andExpect((status().isOk()))
            .andExpect(header().exists(tokenName))
            .andReturn();
        return token.getResponse().getHeader("some-token");
    }

    private JwtBuilder generateValidToken() {
        Collection<GrantedAuthority> simpleAuthorities = Set.of(new SimpleGrantedAuthority("ROLE_USER"));
        Set<String> authorities = simpleAuthorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
        return Jwts.builder()
            .setIssuer("thirdbase.us")
            .setSubject("joe")
            .claim("authorities", authorities)
            .setIssuedAt(Date.from(Instant.ofEpochSecond(1466796822L))) // Fri Jun 24 2016 15:33:42 GMT-0400 (EDT)
            .setExpiration(Date.from(Instant.ofEpochSecond(4622470422L))) // Sat Jun 24 2116 15:33:42 GMT-0400 (EDT)
            .signWith(SignatureAlgorithm.HS256, secretService.getHS256SecretBytes());
    }
}

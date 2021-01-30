package us.thirdbase.rspi.parts.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(initializers = ConfigFileApplicationContextInitializer.class)
@EnableConfigurationProperties(CorsConfigurationProperties.class)
@ActiveProfiles("test")
@TestPropertySource(properties = {
    "app.security.cors.exposed: X-AUTH-TOKEN",
    "app.security.cors.headers=Origin",
    "app.security.cors.methods=HEAD",
    "app.security.cors.origins=http://localhost"
})
class CorsConfigurationPropertiesTest {

    @Autowired
    private CorsConfigurationProperties subject;

    @Test
    void methods() {
        assertThat(subject.getMethods()).contains("HEAD");
    }

    @Test
    void headers() {
        assertThat(subject.getHeaders()).contains("Origin");
    }

    @Test
    void exposed() {
        assertThat(subject.getExposed()).contains("X-AUTH-TOKEN");
    }

    @Test
    void origins() {
        assertThat(subject.getOrigins()).contains("http://localhost");
    }
}

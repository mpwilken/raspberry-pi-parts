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
@EnableConfigurationProperties(UrlProtectedConfigurationProperties.class)
@ActiveProfiles("test")
@TestPropertySource(properties = {
    "app.security.urls.unprotected=something,other,else",
})
class UrlProtectedConfigurationPropertiesTest {
    @Autowired
    private UrlProtectedConfigurationProperties subject;

    @Test
    void unprotected() {
        assertThat(subject.getUnprotected().size()).isEqualTo(3);
        assertThat(subject.getUnprotected().get(0)).isEqualTo("something");
        assertThat(subject.getUnprotected().get(1)).isEqualTo("other");
        assertThat(subject.getUnprotected().get(2)).isEqualTo("else");
    }
}

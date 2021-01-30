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
@EnableConfigurationProperties(JwtTokenConfigurationProperties.class)
@ActiveProfiles("test")
@TestPropertySource(properties = {
    "app.security.jwt.issuer=megaissuer",
    "app.security.jwt.name=some-token",
    "app.security.jwt.expiration=200",
    "app.security.jwt.HS256=HS256",
    "app.security.jwt.HS384=HS384",
    "app.security.jwt.HS512=HS512",
})
class JwtTokenConfigurationPropertiesTest {

    @Autowired
    JwtTokenConfigurationProperties subject;

    @Test
    void issuer() {
        assertThat(subject.getIssuer()).isEqualTo("megaissuer");
    }

    @Test
    void name() {
        assertThat(subject.getName()).isEqualTo("some-token");
    }

    @Test
    void expiration() {
        assertThat(subject.getExpiration()).isEqualTo(200);
    }

    @Test
    void hS256() {
        assertThat(subject.getHs256()).isEqualTo("HS256");
    }

    @Test
    void hS384() {
        assertThat(subject.getHs384()).isEqualTo("HS384");
    }

    @Test
    void hS512() {
        assertThat(subject.getHs512()).isEqualTo("HS512");
    }
}

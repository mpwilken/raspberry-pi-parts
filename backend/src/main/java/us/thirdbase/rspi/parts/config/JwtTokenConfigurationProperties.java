package us.thirdbase.rspi.parts.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app.security.jwt")
@Data
@NoArgsConstructor
public class JwtTokenConfigurationProperties {
    private String issuer;
    private String name;
    private String prefix;
    private int expiration;
    private String hs256;
    private String hs384;
    private String hs512;
}

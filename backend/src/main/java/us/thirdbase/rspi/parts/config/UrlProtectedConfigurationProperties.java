package us.thirdbase.rspi.parts.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "app.security.urls")
@Data
@NoArgsConstructor
public class UrlProtectedConfigurationProperties {
    private List<String> unprotected;
}

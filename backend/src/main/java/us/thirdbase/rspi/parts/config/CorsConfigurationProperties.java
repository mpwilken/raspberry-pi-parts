package us.thirdbase.rspi.parts.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "app.security.cors")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CorsConfigurationProperties {
    private List<String> methods;
    private List<String> headers;
    private List<String> exposed;
    private List<String> origins;
    private boolean allowCredentials;
}

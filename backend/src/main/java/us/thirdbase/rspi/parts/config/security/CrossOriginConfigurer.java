package us.thirdbase.rspi.parts.config.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import us.thirdbase.rspi.parts.config.CorsConfigurationProperties;

@Configuration
@Order(1)
public class CrossOriginConfigurer implements WebMvcConfigurer {

    CorsConfigurationProperties corsConfigurationProperties;

    public CrossOriginConfigurer(CorsConfigurationProperties corsConfigurationProperties) {
        this.corsConfigurationProperties = corsConfigurationProperties;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOrigins(corsConfigurationProperties.getOrigins().toArray(new String[0]))
            .allowedMethods(corsConfigurationProperties.getMethods().toArray(new String[0]))
            .allowedHeaders(corsConfigurationProperties.getHeaders().toArray(new String[0]))
            .exposedHeaders(corsConfigurationProperties.getExposed().toArray(new String[0]))
            .maxAge(4800);
    }
}

package us.thirdbase.rspi.parts.config;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClockConfiguration {

    @Bean
    Clock clock() {
//        return Clock.fixed(Instant.parse("2019-02-17T23:01:00.00Z"), ZoneOffset.UTC);
        return Clock.fixed(Instant.parse("2018-08-06T05:01:00.00Z"), ZoneId.of("America/Chicago"));
    }
}

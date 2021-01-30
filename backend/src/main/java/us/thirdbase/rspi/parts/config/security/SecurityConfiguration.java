package us.thirdbase.rspi.parts.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import us.thirdbase.rspi.parts.config.JwtTokenConfigurationProperties;
import us.thirdbase.rspi.parts.config.UrlProtectedConfigurationProperties;

import static java.util.Collections.singletonList;

@EnableWebSecurity
public class SecurityConfiguration {

    @Value("${app.security.temporary.username}")
    private String username;
    @Value("${app.security.temporary.password}")
    private String password;

    @Bean
    public InMemoryUserDetailsManager inMemoryUserDetailsManager() {
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        User basicUser = new User(username, encoder.encode(password),
            singletonList(new SimpleGrantedAuthority("ROLE_USER")));
        return new InMemoryUserDetailsManager(singletonList(basicUser));
    }

    @Configuration
    @Order(3)
    static class JwtConfiguration extends WebSecurityConfigurerAdapter {

        @Autowired
        private JwtProvider jwtProvider;
        @Autowired
        private JwtTokenConfigurationProperties jwtTokenConfigurationProperties;
        @Autowired
        private UrlProtectedConfigurationProperties urlProtectedConfigurationProperties;
        @Autowired
        private InMemoryUserDetailsManager inMemoryUserDetailsManager;
        @Autowired
        private MyBasicAuthenticationEntryPoint authenticationEntryPoint;

        @Bean
        @Override
        public AuthenticationManager authenticationManagerBean() throws Exception {
            return super.authenticationManagerBean();
        }

        @Override
        public void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.userDetailsService(inMemoryUserDetailsManager);
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            // @formatter:off
            http
                .antMatcher("/**")
                .cors()
                .and()
                .csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/login").permitAll()
                .antMatchers(HttpMethod.OPTIONS).permitAll()
                .antMatchers(urlProtectedConfigurationProperties.getUnprotected().toArray(new String[]{})).permitAll()
                .anyRequest().authenticated()
                .and()
                .httpBasic()
                .authenticationEntryPoint(authenticationEntryPoint)
                .and()
                .headers()
                .frameOptions().sameOrigin()
                .and()
                .addFilter(new JwtAuthenticationFilter(authenticationManager(), authenticationEntryPoint,
                    jwtTokenConfigurationProperties, jwtProvider))
                .addFilter(new JwtAuthorizationFilter(authenticationManager(), jwtProvider, jwtTokenConfigurationProperties,
                    inMemoryUserDetailsManager))
            ;
            // @formatter:on
        }
    }
}

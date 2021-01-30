package us.thirdbase.rspi.parts.config.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import us.thirdbase.rspi.parts.config.JwtTokenConfigurationProperties;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class JwtAuthenticationFilter extends BasicAuthenticationFilter {

    private final JwtProvider jwtProvider;
    private final JwtTokenConfigurationProperties jwtTokenConfigurationProperties;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager,
                                   AuthenticationEntryPoint authenticationEntryPoint,
                                   JwtTokenConfigurationProperties jwtTokenConfigurationProperties,
                                   JwtProvider jwtProvider) {
        super(authenticationManager, authenticationEntryPoint);
        this.jwtProvider = jwtProvider;
        this.jwtTokenConfigurationProperties = jwtTokenConfigurationProperties;
    }

    @Override
    protected void onSuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authResult)
        throws IOException {
        String token = jwtProvider.generateJwtToken((UserDetails) authResult.getPrincipal());
        response.addHeader(jwtTokenConfigurationProperties.getName(), token);
    }
}

package us.thirdbase.rspi.parts.config.security;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import us.thirdbase.rspi.parts.config.JwtTokenConfigurationProperties;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private final JwtProvider jwtProvider;
    private final JwtTokenConfigurationProperties jwtTokenConfigurationProperties;
    private final UserDetailsManager userDetailsManager;

    public JwtAuthorizationFilter(AuthenticationManager authManager, JwtProvider jwtProvider,
                                  JwtTokenConfigurationProperties jwtTokenConfigurationProperties,
                                  UserDetailsManager userDetailsManager) {
        super(authManager);
        this.jwtProvider = jwtProvider;
        this.jwtTokenConfigurationProperties = jwtTokenConfigurationProperties;
        this.userDetailsManager = userDetailsManager;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {
        String token = request.getHeader(jwtTokenConfigurationProperties.getName());

        if (null != token && token.startsWith(jwtTokenConfigurationProperties.getPrefix())) {
            Authentication authentication = validateToken(
                token.substring(jwtTokenConfigurationProperties.getPrefix().length()), response);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        chain.doFilter(request, response);
    }

    private Authentication validateToken(String token, HttpServletResponse response) throws ServletException {
        String username;
        try {
            username = jwtProvider.getUserNameFromJwtToken(token);
        } catch (ExpiredJwtException e) {
            log.error("Jwt Exception: {}", e.getMessage());
            throw new ServletException("Expired jwt");
        }
        if (username != null) {
            UserDetails userDetails = this.userDetailsManager.loadUserByUsername(username);
            Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, "",
                userDetails.getAuthorities());
            renewToken(authentication, response);
            return authentication;
        }
        return null;
    }

    private void renewToken(Authentication authentication, HttpServletResponse response) {
        String token = jwtProvider.generateJwtToken((UserDetails) authentication.getPrincipal());
        response.addHeader(jwtTokenConfigurationProperties.getName(), token);
    }
}

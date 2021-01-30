package us.thirdbase.rspi.parts.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.impl.DefaultClaims;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import us.thirdbase.rspi.parts.config.JwtTokenConfigurationProperties;

import java.time.Clock;
import java.time.Instant;
import java.util.Collection;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JwtProvider {

    private static final String JWT_TOKEN_EXPIRED = "jwt token expired";
    private static final String JWT_TOKEN_FAILED_VALIDATION = "jwt token failed validation";
    private static final String AUTHORITIES = "authorities";
    private final SecretService secretService;
    private final JwtTokenConfigurationProperties jwtTokenConfigurationProperties;
    private final Clock notMockedClock;

    @Autowired
    public JwtProvider(SecretService secretService, JwtTokenConfigurationProperties jwtTokenConfigurationProperties) {
        this.secretService = secretService;
        this.jwtTokenConfigurationProperties = jwtTokenConfigurationProperties;
        this.notMockedClock = Clock.systemUTC();
    }

    String generateJwtToken(UserDetails user) {
        JwtBuilder jwtBuilder = Jwts.builder()
            .setIssuer(jwtTokenConfigurationProperties.getIssuer())
            .setSubject(user.getUsername())
            .addClaims(createClaimsFromAuthorities(user.getAuthorities()))
            .setIssuedAt(Date.from(Instant.now(notMockedClock)))
            .setExpiration(Date.from(Instant.now(notMockedClock).plusSeconds(jwtTokenConfigurationProperties.getExpiration())))
            .signWith(SignatureAlgorithm.HS256, secretService.getHS256SecretBytes());
        return jwtBuilder.compact();
    }

    private Claims createClaimsFromAuthorities(Collection<? extends GrantedAuthority> authorities) {
        Claims claims = new DefaultClaims();
        claims.put(AUTHORITIES, getAuthorities(authorities));
        return claims;
    }

    @NonNull
    private Set<String> getAuthorities(Collection<? extends GrantedAuthority> authorities) {
        return authorities.stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toSet());
    }

    boolean validateJwtToken(String authToken) throws JwtException {
        try {
            Jwts.parser().setSigningKeyResolver(secretService.getSigningKeyResolver()).parseClaimsJws(authToken);
            return true;
        } catch (ExpiredJwtException e) {
            log.error(JWT_TOKEN_EXPIRED);
            throw new JwtException(JWT_TOKEN_EXPIRED);
        } catch (UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
            log.error(JWT_TOKEN_FAILED_VALIDATION);
            throw new JwtException(JWT_TOKEN_FAILED_VALIDATION);
        }
    }

    String getUserNameFromJwtToken(String token) {
        return Jwts.parser()
            .setSigningKeyResolver(secretService.getSigningKeyResolver())
            .parseClaimsJws(token)
            .getBody().getSubject();
    }
}

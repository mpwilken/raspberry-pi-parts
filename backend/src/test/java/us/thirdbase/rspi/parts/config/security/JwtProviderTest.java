package us.thirdbase.rspi.parts.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.SigningKeyResolverAdapter;
import io.jsonwebtoken.impl.TextCodec;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import us.thirdbase.rspi.parts.config.JwtTokenConfigurationProperties;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@Slf4j
class JwtProviderTest {

    private static final String SECRET_KEY = "testing";
    @Mock
    Clock clock;
    @Mock
    SecretService secretService;
    @Mock
    JwtTokenConfigurationProperties jwtTokenConfigurationProperties;
    private JwtProvider subject;
    private User token;

    private SigningKeyResolverAdapter signingKeyResolverAdapter = new SigningKeyResolverAdapter() {
        @Override
        public byte[] resolveSigningKeyBytes(JwsHeader header, Claims claims) {
            return TextCodec.BASE64.decode(TextCodec.BASE64.encode(SECRET_KEY));
        }
    };

    @BeforeEach
    void setUp() {
        clock = Clock.fixed(Instant.now(), ZoneOffset.UTC);

        subject = new JwtProvider(secretService, jwtTokenConfigurationProperties);

        given(jwtTokenConfigurationProperties.getIssuer()).willReturn("coolbeans");
        given(secretService.getHS256SecretBytes()).willReturn(SECRET_KEY.getBytes());
        token = new User("someuser", "somepassword", Set.of(new SimpleGrantedAuthority("ROLE_USER")));
    }

    @AfterEach
    void tearDown() {
        verify(jwtTokenConfigurationProperties).getExpiration();
        verify(jwtTokenConfigurationProperties).getIssuer();
    }

    @Test
    void generateJwtToken() {
        given(jwtTokenConfigurationProperties.getExpiration()).willReturn(100);

        String actual = subject.generateJwtToken(token);

        assertThat(actual.length()).isEqualTo(196);
    }

    @Test
    void validTokenPasses() {
        given(jwtTokenConfigurationProperties.getExpiration()).willReturn(30000);
        given(secretService.getSigningKeyResolver()).willReturn(signingKeyResolverAdapter);

        String expected = subject.generateJwtToken(token);
        boolean actual = subject.validateJwtToken(expected);
        assertTrue(actual);
    }

    @Test
    void expiredTokenFails() {
        given(jwtTokenConfigurationProperties.getExpiration()).willReturn(0);
        given(secretService.getSigningKeyResolver()).willReturn(signingKeyResolverAdapter);

        String expected = subject.generateJwtToken(token);
        JwtException exception = assertThrows(JwtException.class, () -> subject.validateJwtToken(expected));
        assertThat(exception.getMessage()).isEqualTo(("jwt token expired"));
    }

    @Test
    void invalidTokenFails() {
        given(jwtTokenConfigurationProperties.getExpiration()).willReturn(30000);
        given(secretService.getSigningKeyResolver()).willReturn(signingKeyResolverAdapter);

        String expected = subject.generateJwtToken(token);
        JwtException exception = assertThrows(JwtException.class, () -> subject.validateJwtToken(expected.substring(1)));
        assertThat(exception.getMessage()).isEqualTo(("jwt token failed validation"));
    }
}

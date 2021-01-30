package us.thirdbase.rspi.parts.config.security;

import io.jsonwebtoken.JwtException;
import java.io.IOException;
import java.util.Set;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.UserDetailsManager;
import us.thirdbase.rspi.parts.config.JwtTokenConfigurationProperties;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@ExtendWith(MockitoExtension.class)
class JwtAuthorizationFilterTest {

    @Mock
    private AuthenticationManager authManager;
    @Mock
    private JwtProvider jwtProvider;
    @Mock
    private JwtTokenConfigurationProperties jwtTokenConfigurationProperties;
    @Mock
    private FilterChain chain;
    @Mock
    private UserDetailsManager userDetailsManager;
    @Captor
    private ArgumentCaptor<UserDetails> tokenCaptor;
    private JwtAuthorizationFilter subject;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    void setUp() {
        subject = new JwtAuthorizationFilter(authManager, jwtProvider, jwtTokenConfigurationProperties, userDetailsManager);
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        SecurityContextHolder.getContext().setAuthentication(null);
    }

    @Test
    void doFilterInternalWithoutToken() throws IOException, ServletException {
        given(jwtTokenConfigurationProperties.getName()).willReturn("token-name");
        doNothing().when(chain).doFilter(any(), any());

        subject.doFilterInternal(request, response, chain);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertThat(authentication).isNull();
        verify(chain).doFilter(any(), any());
        verifyZeroInteractions(jwtProvider);
    }

    @Test
    void doFilterInternalWithExpiredToken() throws IOException, ServletException {
        given(jwtTokenConfigurationProperties.getName()).willReturn("token-name");
        given(jwtTokenConfigurationProperties.getPrefix()).willReturn("myprefix ");
        request.addHeader("token-name", "myprefix ljalskdjf");
        given(jwtProvider.getUserNameFromJwtToken(anyString())).willThrow(new JwtException("some exception"));

        try {
            subject.doFilterInternal(request, response, chain);
            fail("Should not reach here");
        } catch (JwtException e) {
            assertThat(e.getMessage()).isEqualTo("some exception");
        } catch (ServletException e) {
            fail("Should not throw generic exception");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertThat(authentication).isNull();
        verifyZeroInteractions(chain);
        verify(jwtProvider).getUserNameFromJwtToken(anyString());
    }

    @Test
    void consecutiveCallsWithValidTokenWillRenew() throws IOException, ServletException {
        given(jwtTokenConfigurationProperties.getName()).willReturn("token-name", "token-name");
        given(jwtTokenConfigurationProperties.getPrefix()).willReturn("myprefix ", "myprefix ");
        request.addHeader("token-name", "myprefix ljalskdjf");
        doNothing().when(chain).doFilter(any(), any());
        given(jwtProvider.getUserNameFromJwtToken(anyString())).willReturn("someuser");
        given(jwtProvider.generateJwtToken(tokenCaptor.capture())).willReturn("uucuucucucucuccucu");
        given(userDetailsManager.loadUserByUsername(anyString())).willReturn(new User("someuser", "",
            Set.of(new SimpleGrantedAuthority("ROLE_USER"))));

        subject.doFilterInternal(request, response, chain);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertThat(authentication.getCredentials()).isEqualTo("");
        assertThat(tokenCaptor.getValue().getUsername()).isEqualTo("someuser");
        assertThat(authentication.getAuthorities().size()).isEqualTo(1);
        SimpleGrantedAuthority expectedAuthority = new SimpleGrantedAuthority("ROLE_USER");
        assertThat(authentication.getAuthorities().contains(expectedAuthority)).isTrue();
        assertThat(authentication.isAuthenticated()).isTrue();
        verify(jwtTokenConfigurationProperties, times(2)).getName();
        verify(chain).doFilter(any(), any());
        verify(jwtProvider).getUserNameFromJwtToken("ljalskdjf");
        verify(jwtProvider).generateJwtToken(any(UserDetails.class));
        verify(userDetailsManager).loadUserByUsername("someuser");
    }
}

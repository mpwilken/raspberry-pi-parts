package us.thirdbase.rspi.parts.config.security;

import java.io.IOException;
import java.util.ArrayList;
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
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.AuthenticationEntryPoint;
import us.thirdbase.rspi.parts.config.JwtTokenConfigurationProperties;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

    @Mock
    private AuthenticationManager authManager;
    @Mock
    private JwtProvider jwtProvider;
    @Mock
    private JwtTokenConfigurationProperties jwtTokenConfigurationProperties;
    @Mock
    private Authentication authentication;
    @Mock
    private AuthenticationEntryPoint authenticationEntryPoint;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private JwtAuthenticationFilter subject;
    @Captor
    private ArgumentCaptor<UserDetails> userDetailsCaptor;

    @BeforeEach
    void setUp() {
        subject = new JwtAuthenticationFilter(authManager, authenticationEntryPoint, jwtTokenConfigurationProperties, jwtProvider);
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @Test
    void successfulAuthentication() throws IOException {
        User user = new User("anotheruser", "anotherpassword", new ArrayList<>());
        given(authentication.getPrincipal()).willReturn(user);
        given(jwtTokenConfigurationProperties.getName()).willReturn("token-name");
        given(jwtProvider.generateJwtToken(userDetailsCaptor.capture())).willReturn("sometokenstring");

        subject.onSuccessfulAuthentication(request, response, authentication);

        assertThat(response.getHeader("token-name")).isEqualTo("sometokenstring");
        assertThat(userDetailsCaptor.getValue().getUsername()).isEqualTo("anotheruser");
        assertThat(response.getHeader("token-name")).isEqualTo("sometokenstring");
        verify(jwtTokenConfigurationProperties).getName();
        verify(jwtProvider).generateJwtToken(any(User.class));
    }
}

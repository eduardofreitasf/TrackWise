package com.trackwise.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.trackwise.common.exception.BusinessException;
import com.trackwise.common.exception.ResourceNotFoundException;
import com.trackwise.security.jwt.JwtService;
import com.trackwise.user.dto.LoginRequest;
import com.trackwise.user.dto.RegisterRequest;
import com.trackwise.user.dto.TokenResponse;
import com.trackwise.user.dto.UserProfileResponse;
import com.trackwise.user.model.User;
import com.trackwise.user.model.UserStatus;
import com.trackwise.user.repository.UserRepository;
import java.util.Collections;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserDetailsService userDetailsService;

    @InjectMocks
    private AuthService authService;

    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;
    private User user;
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        registerRequest = RegisterRequest.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .password("password123")
                .defaultCurrency("USD")
                .build();

        loginRequest = LoginRequest.builder()
                .email("john.doe@example.com")
                .password("password123")
                .build();

        user = User.builder()
                .email("john.doe@example.com")
                .passwordHash("encodedPassword")
                .firstName("John")
                .lastName("Doe")
                .defaultCurrency("USD")
                .status(UserStatus.ACTIVE)
                .build();
        user.setId(1L);

        userDetails = new org.springframework.security.core.userdetails.User(
                "john.doe@example.com",
                "encodedPassword",
                Collections.emptyList()
        );
    }

    @Test
    void register_ShouldCreateUser_WhenEmailDoesNotExist() {
        when(userRepository.existsByEmail(registerRequest.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(registerRequest.getPassword())).thenReturn("encodedPassword");
        when(userDetailsService.loadUserByUsername(registerRequest.getEmail())).thenReturn(userDetails);
        when(jwtService.generateToken(userDetails)).thenReturn("accessToken");
        when(jwtService.generateRefreshToken(userDetails)).thenReturn("refreshToken");

        TokenResponse response = authService.register(registerRequest);

        assertThat(response).isNotNull();
        assertThat(response.getAccessToken()).isEqualTo("accessToken");
        assertThat(response.getRefreshToken()).isEqualTo("refreshToken");
        assertThat(response.getTokenType()).isEqualTo("Bearer");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void register_ShouldThrowException_WhenEmailAlreadyExists() {
        when(userRepository.existsByEmail(registerRequest.getEmail())).thenReturn(true);

        assertThatThrownBy(() -> authService.register(registerRequest))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("already exists");
    }

    @Test
    void login_ShouldReturnTokens_WhenCredentialsAreValid() {
        when(userRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.of(user));
        when(userDetailsService.loadUserByUsername(loginRequest.getEmail())).thenReturn(userDetails);
        when(jwtService.generateToken(userDetails)).thenReturn("accessToken");
        when(jwtService.generateRefreshToken(userDetails)).thenReturn("refreshToken");

        TokenResponse response = authService.login(loginRequest);

        assertThat(response).isNotNull();
        assertThat(response.getAccessToken()).isEqualTo("accessToken");
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userRepository).save(user);
    }

    @Test
    void getProfile_ShouldReturnUserProfile_WhenUserExists() {
        when(userRepository.findByEmail("john.doe@example.com")).thenReturn(Optional.of(user));

        UserProfileResponse response = authService.getProfile("john.doe@example.com");

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getFirstName()).isEqualTo("John");
        assertThat(response.getLastName()).isEqualTo("Doe");
        assertThat(response.getEmail()).isEqualTo("john.doe@example.com");
    }

    @Test
    void getProfile_ShouldThrowException_WhenUserDoesNotExist() {
        when(userRepository.findByEmail("john.doe@example.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.getProfile("john.doe@example.com"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("User not found");
    }
}

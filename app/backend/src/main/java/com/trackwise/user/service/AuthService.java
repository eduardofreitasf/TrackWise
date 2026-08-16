package com.trackwise.user.service;

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
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    @Transactional
    public TokenResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException(
                    "User with email " + request.getEmail() + " already exists");
        }

        User user =
                User.builder()
                        .firstName(request.getFirstName())
                        .lastName(request.getLastName())
                        .email(request.getEmail())
                        .passwordHash(passwordEncoder.encode(request.getPassword()))
                        .defaultCurrency(request.getDefaultCurrency().toUpperCase())
                        .status(UserStatus.ACTIVE)
                        .build();

        userRepository.save(user);

        var userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        var accessToken = jwtService.generateToken(userDetails);
        var refreshToken = jwtService.generateRefreshToken(userDetails);

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .build();
    }

    @Transactional
    public TokenResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        User user =
                userRepository
                        .findByEmail(request.getEmail())
                        .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user.setLastLoginAt(LocalDateTime.now());
        userRepository.save(user);

        var userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        var accessToken = jwtService.generateToken(userDetails);
        var refreshToken = jwtService.generateRefreshToken(userDetails);

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .build();
    }

    @Transactional(readOnly = true)
    public UserProfileResponse getProfile(String email) {
        User user =
                userRepository
                        .findByEmail(email)
                        .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return UserProfileResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .defaultCurrency(user.getDefaultCurrency())
                .build();
    }
}

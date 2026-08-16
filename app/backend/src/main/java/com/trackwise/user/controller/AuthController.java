package com.trackwise.user.controller;

import com.trackwise.user.dto.LoginRequest;
import com.trackwise.user.dto.RegisterRequest;
import com.trackwise.user.dto.TokenResponse;
import com.trackwise.user.dto.UserProfileResponse;
import com.trackwise.user.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<TokenResponse> register(@Valid @RequestBody RegisterRequest request) {
        TokenResponse tokenResponse = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(tokenResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginRequest request) {
        TokenResponse tokenResponse = authService.login(request);
        return ResponseEntity.ok(tokenResponse);
    }

    @GetMapping("/me")
    public ResponseEntity<UserProfileResponse> getProfile(
            @AuthenticationPrincipal UserDetails userDetails) {
        UserProfileResponse profile = authService.getProfile(userDetails.getUsername());
        return ResponseEntity.ok(profile);
    }
}

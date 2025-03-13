package com.api.videostreaming.controllers;


import com.api.videostreaming.pojos.requests.LoginRequest;
import com.api.videostreaming.pojos.requests.RefreshTokenRequest;
import com.api.videostreaming.pojos.responses.JwtResponse;
import com.api.videostreaming.services.AuthService;
import com.api.videostreaming.utilities.URIConstants;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(URIConstants.API_VERSION + URIConstants.AUTH_BASE_URL)
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Generate JWT token", description = "Authenticates user and returns a JWT token")
    @PostMapping("/generateToken")
    public ResponseEntity<JwtResponse> generateToken(@RequestBody LoginRequest loginReq) {
        log.info("Login request received for user: {}", loginReq.getUsername());
        ResponseEntity<JwtResponse> response = authService.generateToken(loginReq);
        log.info("Returning response: {}", response.getBody());
        return response;
    }

    @Operation(
        summary = "Refresh JWT Token",
        description = "Generates a new JWT access token using a valid refresh token.",
        security = {@SecurityRequirement(name = "bearerAuth")}
    )
    @PostMapping("/refresh-token")
    public ResponseEntity<JwtResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
        log.info("Received request to refresh token for refreshToken: {}", request.getRefreshToken());
        ResponseEntity<JwtResponse> response = authService.refreshToken(request);
        log.info("Token refreshed successfully. Response: {}", response.getBody());
        return response;
    }
}

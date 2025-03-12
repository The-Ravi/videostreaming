package com.api.videostreaming.controllers;


import com.api.videostreaming.pojos.requests.LoginRequest;
import com.api.videostreaming.pojos.responses.JwtResponse;
import com.api.videostreaming.services.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
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
}

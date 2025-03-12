package com.api.videostreaming.serviceImpls;

import com.api.videostreaming.entities.Users;
import com.api.videostreaming.exceptions.AuthenticationFailedException;
import com.api.videostreaming.pojos.requests.LoginRequest;
import com.api.videostreaming.pojos.responses.JwtResponse;
import com.api.videostreaming.repositories.UsersRepository;
import com.api.videostreaming.securities.JwtUtil;
import com.api.videostreaming.services.AuthService;
import com.api.videostreaming.utilities.Constants;
import com.google.gson.Gson;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UsersRepository usersRepo; 
    private final Gson gson = new Gson();

    @Override
    public ResponseEntity<JwtResponse> generateToken(LoginRequest loginReq) {
        String userName = loginReq.getUsername();
        String password = loginReq.getPassword();

        if (StringUtils.isBlank(userName) || StringUtils.isBlank(password)) {
            log.warn("generateToken: userName/password is null/empty");
            throw new AuthenticationFailedException(Constants.INVALID_PARAMETERS);
        }

        Users user = usersRepo.findByUserName(userName);
        if (user == null) {
            log.warn("generateToken: User not found");
            throw new AuthenticationFailedException(Constants.BAD_CREDENTIALS);
        }

        // Ensure password is encoded before checking
        String encodedPassword = Base64.getEncoder().encodeToString(password.getBytes());
        if (!encodedPassword.equals(user.getPassword())) {
            log.warn("generateToken: Invalid credentials");
            throw new AuthenticationFailedException(Constants.BAD_CREDENTIALS);
        }

        log.debug("generateToken: userData: {}", gson.toJson(user));

        String jwtToken = jwtUtil.generateToken(userName, Constants.TYPE_AUTH_TOKEN);
        String refreshToken = jwtUtil.generateToken(userName, Constants.TYPE_REFRESH_TOKEN);

        // Use Builder for Response
        JwtResponse response = JwtResponse.builder()
                .token(jwtToken)
                .refreshToken(refreshToken)
                .message(Constants.TOKEN_GENERATED)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}

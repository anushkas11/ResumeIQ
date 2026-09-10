package com.resumewise.resumeiq.controller;

import com.resumewise.resumeiq.dto.request.LoginRequest;
import com.resumewise.resumeiq.dto.response.LoginResponse;
import com.resumewise.resumeiq.entity.User;
import com.resumewise.resumeiq.repository.UserRepository;
import com.resumewise.resumeiq.security.JwtService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    public AuthController(
            AuthenticationManager authenticationManager,
            JwtService jwtService,
            UserRepository userRepository
    ) {
        this.authenticationManager =
                authenticationManager;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @Valid @RequestBody LoginRequest request
    ) {

        // Checked BEFORE calling the AuthenticationManager: a Google-only
        // account has no local password hash, so let it fail with a clear
        // message here rather than a confusing 500 from the password encoder.
        User existingUser = userRepository.findByEmail(request.getEmail()).orElse(null);
        if (existingUser != null
                && existingUser.getAuthProvider() == User.AuthProvider.GOOGLE) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("This account uses Google Sign-In. Please log in with Google instead.");
        }

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getEmail(),
                                request.getPassword()
                        )
                );

        UserDetails userDetails =
                (UserDetails) authentication.getPrincipal();

        String token =
                jwtService.generateToken(userDetails);

        return ResponseEntity.ok(
                new LoginResponse(token)
        );
    }
}

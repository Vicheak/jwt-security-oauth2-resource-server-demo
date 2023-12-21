package com.vicheak.coreapp.auth.web;

import com.vicheak.coreapp.auth.AuthService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/register")
    public Map<String, String> register(@RequestBody @Valid RegisterDto registerDto) throws MessagingException {
        authService.register(registerDto);
        return Map.of("message", "Please check your email for verification code!");
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/verify")
    public Map<String, String> verify(@RequestBody @Valid VerifyDto verifyDto) {
        authService.verify(verifyDto);
        return Map.of("message", "Congratulation! Your email has been verified...!");
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/login")
    public AuthDto login(@RequestBody @Valid LoginDto loginDto){
        return authService.login(loginDto);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/token")
    public AuthDto refreshToken(@RequestBody @Valid RefreshTokenDto refreshTokenDto){
        return authService.refreshToken(refreshTokenDto);
    }

}

package com.vicheak.coreapp.auth;

import com.vicheak.coreapp.auth.web.*;
import jakarta.mail.MessagingException;

public interface AuthService {

    void register(RegisterDto registerDto) throws MessagingException;

    void verify(VerifyDto verifyDto);

    AuthDto login(LoginDto loginDto);

    AuthDto refreshToken(RefreshTokenDto refreshTokenDto);

}

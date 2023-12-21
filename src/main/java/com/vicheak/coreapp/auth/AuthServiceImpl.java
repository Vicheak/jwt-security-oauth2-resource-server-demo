package com.vicheak.coreapp.auth;

import com.vicheak.coreapp.auth.web.*;
import com.vicheak.coreapp.dto.NewUserDto;
import com.vicheak.coreapp.entity.User;
import com.vicheak.coreapp.mail.Mail;
import com.vicheak.coreapp.mail.MailService;
import com.vicheak.coreapp.service.UserService;
import com.vicheak.coreapp.util.RandomUtil;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final AuthRepository authRepository;
    private final AuthMapper authMapper;
    private final UserService userService;
    private final MailService mailService;
    private final DaoAuthenticationProvider daoAuthenticationProvider;
    private final JwtAuthenticationProvider jwtAuthenticationProvider;
    private final JwtEncoder jwtEncoder;
    private JwtEncoder jwtRefreshTokenEncoder;

    @Autowired
    public void setJwtRefreshEncoder(@Qualifier("jwtRefreshTokenEncoder") JwtEncoder jwtRefreshTokenEncoder) {
        this.jwtRefreshTokenEncoder = jwtRefreshTokenEncoder;
    }

    @Value("${spring.mail.username}")
    private String adminMail;

    @Transactional
    @Override
    public void register(RegisterDto registerDto) throws MessagingException {
        //map from register dto to new user dto
        NewUserDto newUserDto = authMapper.mapFromRegisterDtoToNewUserDto(registerDto);
        userService.createNewUser(newUserDto);

        //generate six digit verified code
        String verifiedCode = RandomUtil.getRandomNumber();

        //update verified code into the database
        authRepository.updateVerifiedCode(newUserDto.username(), verifiedCode);

        Mail<String> verifiedMail = new Mail<>();
        verifiedMail.setSender(adminMail);
        verifiedMail.setReceiver(newUserDto.email());
        verifiedMail.setSubject("ESM Service Email Verification");
        verifiedMail.setTemplate("auth/verify-mail");
        verifiedMail.setMetaData(verifiedCode);

        mailService.sendMail(verifiedMail);
    }

    @Transactional
    @Override
    public void verify(VerifyDto verifyDto) {
        //load verified user by email and valid verified code
        User verifiedUser = authRepository.findByEmailAndVerifiedCodeAndIsDeletedFalse(verifyDto.email(),
                        verifyDto.verifiedCode())
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                                "Email verification has been failed!")
                );

        //update verified status and verified code
        verifiedUser.setIsVerified(true);
        verifiedUser.setVerifiedCode(null);

        //save verified user to the database
        authRepository.save(verifiedUser);
    }

    @Override
    public AuthDto login(LoginDto loginDto) {
        //authenticate email and password
        Authentication auth = new UsernamePasswordAuthenticationToken(loginDto.email(), loginDto.password());
        //use bean dao authentication provider to authenticate with user detail service
        auth = daoAuthenticationProvider.authenticate(auth);

        Instant now = Instant.now();

        String scope = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));

        //create payload for jwt
        //access token
        JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
                .id(auth.getName())
                .issuer("public")
                .issuedAt(now)
                .expiresAt(now.plus(10, ChronoUnit.MINUTES))
                .subject("Access Token")
                .audience(List.of("public clients"))
                .claim("scope", scope)
                .build();

        //refresh token
        JwtClaimsSet refreshTokenJwtClaimsSet = JwtClaimsSet.builder()
                .id(auth.getName())
                .issuer("public")
                .issuedAt(now)
                .expiresAt(now.plus(2, ChronoUnit.DAYS))
                .subject("Refresh Token")
                .audience(List.of("public clients"))
                .claim("scope", scope)
                .build();

        //create the encoded access token
        String accessToken = jwtEncoder.encode(JwtEncoderParameters.from(jwtClaimsSet)).getTokenValue();
        String refreshToken = jwtRefreshTokenEncoder.encode(JwtEncoderParameters.from(refreshTokenJwtClaimsSet)).getTokenValue();

        return AuthDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .type("Bearer")
                .build();
    }

    @Override
    public AuthDto refreshToken(RefreshTokenDto refreshTokenDto) {
        //authenticate via bearer token authentication token
        Authentication auth = new BearerTokenAuthenticationToken(refreshTokenDto.refreshToken());
        auth = jwtAuthenticationProvider.authenticate(auth);

        log.info("Auth name : {}", auth.getName()); //name -> subject
        log.info("Auth authorities : {}", auth.getAuthorities());
        //log.info("Auth principle : {}", auth.getPrincipal());

        Jwt jwt = (Jwt) auth.getPrincipal();
        log.info("Jwt ID : {}", jwt.getId()); //jwt id -> email

        Instant now = Instant.now();

        //create payload for jwt
        //access token
        JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
                .id(jwt.getId())
                .issuer("public")
                .issuedAt(now)
                .expiresAt(now.plus(10, ChronoUnit.MINUTES))
                .subject("Access Token")
                .audience(List.of("public clients"))
                .claim("scope", jwt.getClaimAsString("scope"))
                .build();

        //refresh token
        //check duration before create another refresh token
        Duration refreshTokenDuration = Duration.between(now, jwt.getExpiresAt());

        String refreshToken = refreshTokenDto.refreshToken(); //get previous refresh token
        if(refreshTokenDuration.toDays() < 1) {
            JwtClaimsSet refreshTokenJwtClaimsSet = JwtClaimsSet.builder()
                    .id(jwt.getId())
                    .issuer("public")
                    .issuedAt(now)
                    .expiresAt(now.plus(2, ChronoUnit.DAYS))
                    .subject("Refresh Token")
                    .audience(List.of("public clients"))
                    .claim("scope", jwt.getClaimAsString("scope"))
                    .build();

            refreshToken = jwtRefreshTokenEncoder.encode(JwtEncoderParameters.from(refreshTokenJwtClaimsSet)).getTokenValue();
        }

        //create the encoded access token
        String accessToken = jwtEncoder.encode(JwtEncoderParameters.from(jwtClaimsSet)).getTokenValue();

        return AuthDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .type("Bearer")
                .build();
    }

}

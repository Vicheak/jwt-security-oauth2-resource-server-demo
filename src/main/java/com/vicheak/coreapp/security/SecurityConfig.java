package com.vicheak.coreapp.security;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.KeySourceException;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSelector;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.util.List;
import java.util.UUID;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(userDetailsService);

        return provider;
    }

    @Bean
    public JwtAuthenticationProvider jwtAuthenticationProvider() throws JOSEException {
        return new JwtAuthenticationProvider(
                jwtRefreshTokenDecoder(
                        refreshTokenRsaKey(
                                refreshTokenKeyPair())));
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        //customize security filter chain
        httpSecurity.authorizeHttpRequests(auth -> auth

                .requestMatchers("/api/v1/auth/**").permitAll()

                .requestMatchers(HttpMethod.GET,
                        "/api/v1/students/**").hasAuthority("SCOPE_student:read")
                .requestMatchers(HttpMethod.POST,
                        "/api/v1/students/**").hasAuthority("SCOPE_student:write")
                .requestMatchers(HttpMethod.PATCH,
                        "/api/v1/students/**").hasAuthority("SCOPE_student:update")
                .requestMatchers(HttpMethod.DELETE,
                        "/api/v1/students/**").hasAuthority("SCOPE_student:delete")

                .requestMatchers(HttpMethod.GET,
                        "/api/v1/courses/**").hasAuthority("SCOPE_course:read")
                .requestMatchers(HttpMethod.POST,
                        "/api/v1/courses/**").hasAuthority("SCOPE_course:write")
                .requestMatchers(HttpMethod.PATCH,
                        "/api/v1/courses/**").hasAuthority("SCOPE_course:update")
                .requestMatchers(HttpMethod.DELETE,
                        "/api/v1/courses/**").hasAuthority("SCOPE_course:delete")

                .requestMatchers(HttpMethod.GET,
                        "/api/v1/rating/**").hasAuthority("SCOPE_rating:read")
                .requestMatchers(HttpMethod.POST,
                        "/api/v1/rating/**").hasAuthority("SCOPE_rating:write")
                .requestMatchers(HttpMethod.PUT,
                        "/api/v1/rating/**").hasAuthority("SCOPE_rating:update")
                .requestMatchers(HttpMethod.DELETE,
                        "/api/v1/rating/**").hasAuthority("SCOPE_rating:delete")

                .requestMatchers(HttpMethod.POST,
                        "/api/v1/users/**").hasAuthority("SCOPE_user:write")

                .anyRequest().authenticated());

        httpSecurity.oauth2ResourceServer(oauth2 ->
                oauth2.jwt(Customizer.withDefaults()));

        //disable csrf token
        httpSecurity.csrf(AbstractHttpConfigurer::disable);

        //update API policy to stateless
        httpSecurity.sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return httpSecurity.build();
    }

    @Bean
    @Primary
    public KeyPair keyPair() {
        try {
            var keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            return keyPairGenerator.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @Bean
    @Primary
    public RSAKey rsaKey(KeyPair keyPair) {
        return new RSAKey.Builder((RSAPublicKey) keyPair.getPublic())
                .privateKey(keyPair.getPrivate())
                .keyID(UUID.randomUUID().toString())
                .build();
    }

    @Bean
    @Primary
    public JwtDecoder jwtDecoder(RSAKey rsaKey) throws JOSEException {
        return NimbusJwtDecoder.withPublicKey(rsaKey.toRSAPublicKey()).build();
    }

    @Bean
    @Primary
    public JWKSource<SecurityContext> jwkSource(RSAKey rsaKey) {
        var jwkSet = new JWKSet(rsaKey);
        return (jwkSelector, context) -> jwkSelector.select(jwkSet);
    }

    @Bean
    @Primary
    public JwtEncoder jwtEncoder(JWKSource<SecurityContext> jwkSource) {
        return new NimbusJwtEncoder(jwkSource);
    }

    @Bean(name = "refreshTokenKeyPair")
    public KeyPair refreshTokenKeyPair() {
        try {
            var keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            return keyPairGenerator.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @Bean(name = "refreshTokenRsaKey")
    public RSAKey refreshTokenRsaKey(@Qualifier("refreshTokenKeyPair") KeyPair keyPair) {
        return new RSAKey.Builder((RSAPublicKey) keyPair.getPublic())
                .privateKey(keyPair.getPrivate())
                .keyID(UUID.randomUUID().toString())
                .build();
    }

    @Bean(name = "jwtRefreshTokenDecoder")
    public JwtDecoder jwtRefreshTokenDecoder(@Qualifier("refreshTokenRsaKey") RSAKey rsaKey) throws JOSEException {
        return NimbusJwtDecoder.withPublicKey(rsaKey.toRSAPublicKey()).build();
    }

    @Bean(name = "refreshTokenJwkSource")
    public JWKSource<SecurityContext> refreshTokenJwkSource(@Qualifier("refreshTokenRsaKey") RSAKey rsaKey) {
        var jwkSet = new JWKSet(rsaKey);
        return (jwkSelector, context) -> jwkSelector.select(jwkSet);
    }

    @Bean(name = "jwtRefreshTokenEncoder")
    public JwtEncoder jwtRefreshTokenEncoder(@Qualifier("refreshTokenJwkSource") JWKSource<SecurityContext> jwkSource) {
        return new NimbusJwtEncoder(jwkSource);
    }

}

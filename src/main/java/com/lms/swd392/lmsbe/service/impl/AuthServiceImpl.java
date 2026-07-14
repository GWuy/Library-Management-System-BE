package com.lms.swd392.lmsbe.service.impl;

import com.lms.swd392.lmsbe.entity.RefreshToken;
import com.lms.swd392.lmsbe.exception.UnauthorizedException;
import com.lms.swd392.lmsbe.model.request.LoginRequest;
import com.lms.swd392.lmsbe.model.request.RefreshTokenRequest;
import com.lms.swd392.lmsbe.model.response.LoginResponse;
import com.lms.swd392.lmsbe.repository.RefreshTokenRepository;
import com.lms.swd392.lmsbe.service.AuthService;
import com.lms.swd392.lmsbe.service.JwtService;
import io.jsonwebtoken.JwtException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.HexFormat;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthServiceImpl implements AuthService {

    AuthenticationManager authenticationManager;
    UserDetailsService userDetailsService;
    JwtService jwtService;
    RefreshTokenRepository refreshTokenRepository;

    @Override
    @Transactional
    public LoginResponse login(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );
        } catch (BadCredentialsException ex) {
            throw new UnauthorizedException("Invalid username or password");
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());

        String accessToken = jwtService.generateToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);

        // Save hashed refresh token to DB
        saveRefreshToken(refreshToken, userDetails.getUsername());

        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    @Transactional
    public LoginResponse refreshToken(RefreshTokenRequest request) {
        String rawRefreshToken = request.getRefreshToken();

        // Validate JWT signature and extract username
        String username;
        try {
            username = jwtService.extractUsername(rawRefreshToken);
        } catch (JwtException ex) {
            throw new UnauthorizedException("Invalid refresh token");
        }

        if (jwtService.isTokenExpired(rawRefreshToken)) {
            throw new UnauthorizedException("Refresh token has expired");
        }

        // Find the hashed token in DB (must not be revoked)
        String tokenHash = hashToken(rawRefreshToken);
        RefreshToken storedToken = refreshTokenRepository
                .findByTokenHashAndRevokedFalse(tokenHash)
                .orElseThrow(() -> new UnauthorizedException("Refresh token has been revoked or is invalid"));

        // Verify token is not expired in DB
        if (storedToken.getExpiryDate().isBefore(Instant.now())) {
            storedToken.setRevoked(true);
            refreshTokenRepository.save(storedToken);
            throw new UnauthorizedException("Refresh token has expired");
        }

        // Revoke old token (Rotation)
        storedToken.setRevoked(true);
        refreshTokenRepository.save(storedToken);

        // Generate new tokens
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        String newAccessToken = jwtService.generateToken(userDetails);
        String newRefreshToken = jwtService.generateRefreshToken(userDetails);

        // Save new refresh token
        saveRefreshToken(newRefreshToken, username);

        return LoginResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();
    }

    @Override
    @Transactional
    public void logout(String username) {
        refreshTokenRepository.revokeAllByUsername(username);
        log.info("All refresh tokens revoked for user: {}", username);
    }

    /**
     * Hash the raw refresh token with SHA-256 and save to DB.
     */
    private void saveRefreshToken(String rawToken, String username) {
        String tokenHash = hashToken(rawToken);
        Instant expiryDate = jwtService.extractExpiration(rawToken).toInstant();

        RefreshToken refreshToken = RefreshToken.builder()
                .tokenHash(tokenHash)
                .username(username)
                .expiryDate(expiryDate)
                .revoked(false)
                .build();

        refreshTokenRepository.save(refreshToken);
    }

    /**
     * Compute SHA-256 hash of a token string.
     */
    private String hashToken(String token) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(token.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not available", e);
        }
    }
}

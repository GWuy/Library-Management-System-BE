package com.lms.swd392.lmsbe.service;

import com.lms.swd392.lmsbe.entity.User;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public interface JwtService {
    SecretKey getKey();
    String generateToken(User user);
    String generateRefreshToken(User user);
    String extractUsername(String token);
    Date extractExpiration(String token);
    boolean isTokenValid(String token, UserDetails userDetails);
    Claims extractClaims(String token);
    boolean isTokenExpired(String token);
    long getJwtExpiration();
}

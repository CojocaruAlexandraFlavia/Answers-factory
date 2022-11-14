package com.example.answersfactory.util;

import com.sun.istack.NotNull;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;

@Component
public class JwtUtils {

    private final JwtConfiguration jwtConfiguration;
    private final SecretKey secretKey;

    @Autowired
    public JwtUtils(JwtConfiguration jwtConfiguration, SecretKey secretKey) {
        this.jwtConfiguration = jwtConfiguration;
        this.secretKey = secretKey;
    }

    public String generateJwtToken(@NotNull Authentication authentication) {
        User userPrincipal = (User) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject((userPrincipal.getUsername()))
                .claim("authorities", userPrincipal.getAuthorities())
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusDays(jwtConfiguration.getTokenAvailability())))
                .signWith(secretKey)
                .compact();
    }

    public String getTokenPrefix() {
        return jwtConfiguration.getTokenPrefix();
    }

    public String getAuthorizationHeader() {
        return HttpHeaders.AUTHORIZATION;
    }

    @Bean
    public SecretKey secretKey() {
        return Keys.hmacShaKeyFor(jwtConfiguration.getSecretKey().getBytes());
    }

}
package com.lazlob.cityviewer.services.auth;

import com.lazlob.cityviewer.models.entities.Account;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class JwtTokenService {
    Integer jwtExpirationMinutes;
    SecretKey key;
    Clock clock;

    @Autowired
    public JwtTokenService(
            @Value("${jwt.signingSecret}")
            String jtwSigningSecret,
            @Value("${jwt.expirationMinutes}")
            Integer jwtExpirationMinutes,
            Clock clock) {
        this.key = Keys.hmacShaKeyFor(jtwSigningSecret.getBytes(StandardCharsets.UTF_8));
        this.jwtExpirationMinutes = jwtExpirationMinutes;
        this.clock = clock;
    }

    public String generateToken(Account account) {
        return Jwts.builder()
                .setSubject(account.getUsername())
                .setClaims(Jwts.claims(Map.of("roles", account.getRoles())))
                .setIssuedAt(new Date(clock.instant().toEpochMilli()))
                .setExpiration(new Date(clock.instant().plus(jwtExpirationMinutes, ChronoUnit.MINUTES).toEpochMilli()))
                .signWith(key)
                .compact();
    }

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Boolean validateToken(String token) {
        return !isTokenExpired(token);
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getClaimFromToken(token, Claims::getExpiration);
        return expiration.before(new Date(clock.instant().toEpochMilli()));
    }

    private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        JwtParser jwtParser = Jwts.parserBuilder().setSigningKey(key).build();
        final Claims claims = jwtParser.parseClaimsJws(token).getBody();
        return claimsResolver.apply(claims);
    }

}
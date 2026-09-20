package br.com.fiap.maelink.auth.service;

import br.com.fiap.maelink.auth.dto.AuthResponse;
import br.com.fiap.maelink.auth.dto.TokenClaimsResponse;
import br.com.fiap.maelink.common.exception.UnauthorizedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
public class JwtService {
    private final SecretKey key;
    private final long expirationMinutes;

    public JwtService(@Value("${maelink.jwt.secret}") String secret,
                      @Value("${maelink.jwt.expiration-minutes}") long expirationMinutes) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationMinutes = expirationMinutes;
    }

    public AuthResponse issue(String subject, String perfil) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expires = now.plusMinutes(expirationMinutes);
        String token = Jwts.builder()
                .subject(subject)
                .claim("perfil", perfil)
                .issuer("maelink-api")
                .issuedAt(toDate(now))
                .expiration(toDate(expires))
                .signWith(key)
                .compact();
        return new AuthResponse(token, "Bearer", expires, perfil);
    }

    public AuthResponse refresh(String authorization) {
        Claims claims = parseAuthorizationHeader(authorization);
        String perfil = claims.get("perfil", String.class);
        return issue(claims.getSubject(), perfil == null ? "DOADORA" : perfil);
    }

    public TokenClaimsResponse validate(String authorization) {
        Claims claims = parseAuthorizationHeader(authorization);
        return new TokenClaimsResponse(
                claims.getSubject(),
                claims.get("perfil", String.class),
                claims.getIssuer(),
                toLocalDateTime(claims.getIssuedAt()),
                toLocalDateTime(claims.getExpiration())
        );
    }

    private Claims parseAuthorizationHeader(String authorization) {
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            throw new UnauthorizedException("Informe um token no formato Authorization: Bearer <token>");
        }
        String token = authorization.substring(7).trim();
        if (token.isBlank()) {
            throw new UnauthorizedException("Token JWT ausente");
        }
        try {
            return Jwts.parser()
                    .verifyWith(key)
                    .requireIssuer("maelink-api")
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (JwtException | IllegalArgumentException ex) {
            throw new UnauthorizedException("Token JWT inválido ou expirado");
        }
    }

    private Date toDate(LocalDateTime value) {
        return Date.from(value.atZone(ZoneId.systemDefault()).toInstant());
    }

    private LocalDateTime toLocalDateTime(Date value) {
        return value == null ? null : LocalDateTime.ofInstant(value.toInstant(), ZoneId.systemDefault());
    }
}

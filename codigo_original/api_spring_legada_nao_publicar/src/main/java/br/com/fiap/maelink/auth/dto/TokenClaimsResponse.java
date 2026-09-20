package br.com.fiap.maelink.auth.dto;

import java.time.LocalDateTime;

public record TokenClaimsResponse(
        String subject,
        String perfil,
        String issuer,
        LocalDateTime issuedAt,
        LocalDateTime expiresAt
) {}

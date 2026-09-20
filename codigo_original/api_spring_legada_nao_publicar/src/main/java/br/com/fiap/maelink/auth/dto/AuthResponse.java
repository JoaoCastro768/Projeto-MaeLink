package br.com.fiap.maelink.auth.dto;

import java.time.LocalDateTime;

public record AuthResponse(String accessToken, String tokenType, LocalDateTime expiresAt, String perfil) {}

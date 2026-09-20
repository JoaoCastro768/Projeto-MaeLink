package br.com.fiap.maelink.auditoria.dto;

import java.time.LocalDateTime;

public record AuditoriaResponse(Long id, String usuario, String acao, LocalDateTime timestamp, String ipOrigem) {}

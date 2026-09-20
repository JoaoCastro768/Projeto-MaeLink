package br.com.fiap.maelink.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(@NotBlank String identificador, @NotBlank String senha) {}

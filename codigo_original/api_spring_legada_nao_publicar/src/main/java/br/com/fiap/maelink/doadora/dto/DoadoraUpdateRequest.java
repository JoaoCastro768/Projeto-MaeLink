package br.com.fiap.maelink.doadora.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record DoadoraUpdateRequest(
        @NotBlank @Size(max = 120) String nome,
        @NotBlank @Size(max = 160) String contato,
        @NotBlank @Size(max = 120) String cidade,
        @NotNull Boolean consentimentoLgpd
) {}

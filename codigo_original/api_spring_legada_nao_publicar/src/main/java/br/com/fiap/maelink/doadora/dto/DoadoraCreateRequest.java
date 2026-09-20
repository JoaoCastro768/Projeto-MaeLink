package br.com.fiap.maelink.doadora.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record DoadoraCreateRequest(
        @NotBlank @Size(max = 120) String nome,
        @NotBlank @Size(max = 160) String contato,
        @NotBlank @Size(max = 120) String cidade,
        @NotNull @AssertTrue(message = "O consentimento LGPD deve ser aceito para prosseguir") Boolean consentimentoLgpd
) {}

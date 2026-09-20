package br.com.fiap.maelink.triagem.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.Map;

public record TriagemCreateRequest(
        @NotNull Long doadoraId,
        @NotEmpty Map<String, String> respostas
) {}

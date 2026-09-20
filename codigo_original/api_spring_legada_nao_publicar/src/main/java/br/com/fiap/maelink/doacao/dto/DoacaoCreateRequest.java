package br.com.fiap.maelink.doacao.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

public record DoacaoCreateRequest(@NotNull Long agendamentoId, @PositiveOrZero BigDecimal volume) {}

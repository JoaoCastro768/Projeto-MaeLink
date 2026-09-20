package br.com.fiap.maelink.doacao.dto;

import br.com.fiap.maelink.doacao.model.DoacaoStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

public record DoacaoStatusRequest(@NotNull DoacaoStatus status, @PositiveOrZero BigDecimal volume) {}

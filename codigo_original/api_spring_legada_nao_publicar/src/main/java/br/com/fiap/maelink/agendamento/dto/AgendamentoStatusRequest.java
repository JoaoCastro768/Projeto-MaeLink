package br.com.fiap.maelink.agendamento.dto;

import br.com.fiap.maelink.agendamento.model.AgendamentoStatus;
import jakarta.validation.constraints.NotNull;

public record AgendamentoStatusRequest(@NotNull AgendamentoStatus status) {}

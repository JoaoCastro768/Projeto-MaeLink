package br.com.fiap.maelink.agendamento.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record ReagendamentoRequest(@NotNull @Future LocalDateTime novaData) {}

package br.com.fiap.maelink.agendamento.dto;

import br.com.fiap.maelink.agendamento.model.TipoAtendimento;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

public record AgendamentoCreateRequest(
        @NotNull Long doadoraId,
        @NotNull Long bancoId,
        @NotNull @Future LocalDateTime data,
        @NotNull TipoAtendimento tipoAtendimento,
        @Size(max = 300) String observacao
) {}

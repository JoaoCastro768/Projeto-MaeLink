package br.com.fiap.maelink.agendamento.dto;

import br.com.fiap.maelink.agendamento.model.AgendamentoStatus;
import br.com.fiap.maelink.agendamento.model.TipoAtendimento;
import java.time.LocalDateTime;

public record AgendamentoResponse(Long id, Long doadoraId, Long bancoId, String bancoNome, LocalDateTime data, AgendamentoStatus status, TipoAtendimento tipoAtendimento, String observacao) {}

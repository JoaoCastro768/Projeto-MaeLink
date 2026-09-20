package br.com.fiap.maelink.doacao.dto;

import br.com.fiap.maelink.doacao.model.DoacaoStatus;
import java.math.BigDecimal;
import java.util.List;

public record DoacaoResponse(Long id, Long agendamentoId, Long doadoraId, Long bancoId, BigDecimal volume, DoacaoStatus status, List<TimelineEntryResponse> timeline) {}

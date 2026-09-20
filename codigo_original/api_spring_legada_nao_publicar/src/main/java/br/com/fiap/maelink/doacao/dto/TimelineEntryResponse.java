package br.com.fiap.maelink.doacao.dto;

import br.com.fiap.maelink.doacao.model.DoacaoStatus;
import java.time.LocalDateTime;

public record TimelineEntryResponse(DoacaoStatus status, String descricao, LocalDateTime registradoEm) {}

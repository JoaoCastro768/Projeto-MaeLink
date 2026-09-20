package br.com.fiap.maelink.triagem.dto;

import br.com.fiap.maelink.triagem.model.TriagemResultado;
import java.time.LocalDateTime;
import java.util.Map;

public record TriagemResponse(Long id, Long doadoraId, Map<String, String> respostas, TriagemResultado resultado, LocalDateTime criadoEm) {}

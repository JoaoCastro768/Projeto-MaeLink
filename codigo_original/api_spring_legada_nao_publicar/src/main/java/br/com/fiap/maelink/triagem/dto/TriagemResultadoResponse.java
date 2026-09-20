package br.com.fiap.maelink.triagem.dto;

import br.com.fiap.maelink.triagem.model.TriagemResultado;

public record TriagemResultadoResponse(Long triagemId, TriagemResultado resultado, String orientacao) {}

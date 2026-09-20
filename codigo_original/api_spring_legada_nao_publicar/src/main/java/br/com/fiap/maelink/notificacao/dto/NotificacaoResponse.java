package br.com.fiap.maelink.notificacao.dto;

import br.com.fiap.maelink.notificacao.model.*;
import java.time.LocalDateTime;

public record NotificacaoResponse(Long id, Long usuarioId, CanalNotificacao canal, String mensagem, NotificacaoSituacao situacao, LocalDateTime criadoEm) {}

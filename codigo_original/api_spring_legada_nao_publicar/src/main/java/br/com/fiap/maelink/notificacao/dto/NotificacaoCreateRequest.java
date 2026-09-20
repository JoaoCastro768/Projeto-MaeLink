package br.com.fiap.maelink.notificacao.dto;

import br.com.fiap.maelink.notificacao.model.CanalNotificacao;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record NotificacaoCreateRequest(@NotNull Long usuarioId, @NotNull CanalNotificacao canal, @NotBlank @Size(max = 500) String mensagem) {}

package br.com.fiap.maelink.notificacao.service;

import br.com.fiap.maelink.auditoria.service.AuditoriaService;
import br.com.fiap.maelink.doadora.service.DoadoraService;
import br.com.fiap.maelink.notificacao.dto.*;
import br.com.fiap.maelink.notificacao.model.*;
import br.com.fiap.maelink.notificacao.repository.NotificacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificacaoService {
    private final NotificacaoRepository repository;
    private final DoadoraService doadoraService;
    private final AuditoriaService auditoria;

    @Transactional
    public NotificacaoResponse enviar(NotificacaoCreateRequest request, String ip) {
        doadoraService.find(request.usuarioId());
        Notificacao n = repository.save(Notificacao.builder().usuarioId(request.usuarioId()).canal(request.canal()).mensagem(request.mensagem())
                .situacao(NotificacaoSituacao.ENVIADA).criadoEm(LocalDateTime.now()).build());
        auditoria.registrar("sistema", "Notificação enviada para doadora " + request.usuarioId() + " via " + request.canal(), ip);
        return toResponse(n);
    }

    @Transactional(readOnly = true)
    public List<NotificacaoResponse> listar(Long usuarioId) { doadoraService.find(usuarioId); return repository.findByUsuarioIdOrderByCriadoEmDesc(usuarioId).stream().map(this::toResponse).toList(); }
    private NotificacaoResponse toResponse(Notificacao n) { return new NotificacaoResponse(n.getId(), n.getUsuarioId(), n.getCanal(), n.getMensagem(), n.getSituacao(), n.getCriadoEm()); }
}

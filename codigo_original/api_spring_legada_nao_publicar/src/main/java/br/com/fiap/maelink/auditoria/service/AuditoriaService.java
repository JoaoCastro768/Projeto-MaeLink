package br.com.fiap.maelink.auditoria.service;

import br.com.fiap.maelink.auditoria.dto.AuditoriaResponse;
import br.com.fiap.maelink.auditoria.model.Auditoria;
import br.com.fiap.maelink.auditoria.repository.AuditoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuditoriaService {
    private final AuditoriaRepository repository;

    @Transactional
    public void registrar(String usuario, String acao, String ip) {
        repository.save(Auditoria.builder()
                .usuario(usuario == null || usuario.isBlank() ? "sistema" : usuario)
                .acao(acao)
                .timestamp(LocalDateTime.now())
                .ipOrigem(ip == null || ip.isBlank() ? "0.0.0.0" : ip)
                .build());
    }

    @Transactional(readOnly = true)
    public Page<AuditoriaResponse> listar(Pageable pageable) {
        return repository.findAll(pageable).map(a -> new AuditoriaResponse(a.getId(), a.getUsuario(), a.getAcao(), a.getTimestamp(), a.getIpOrigem()));
    }
}

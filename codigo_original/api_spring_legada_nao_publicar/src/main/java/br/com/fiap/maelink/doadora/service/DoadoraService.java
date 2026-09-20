package br.com.fiap.maelink.doadora.service;

import br.com.fiap.maelink.auditoria.service.AuditoriaService;
import br.com.fiap.maelink.common.exception.ResourceNotFoundException;
import br.com.fiap.maelink.doadora.dto.*;
import br.com.fiap.maelink.doadora.model.Doadora;
import br.com.fiap.maelink.doadora.repository.DoadoraRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DoadoraService {
    private final DoadoraRepository repository;
    private final AuditoriaService auditoria;

    @Transactional
    public DoadoraResponse criar(DoadoraCreateRequest request, String ip) {
        Doadora entity = Doadora.builder()
                .nome(request.nome().trim())
                .contato(request.contato().trim())
                .cidade(request.cidade().trim())
                .consentimentoLgpd(request.consentimentoLgpd())
                .build();
        entity = repository.save(entity);
        auditoria.registrar("doadora:" + entity.getId(), "Cadastro de doadora criado", ip);
        return toResponse(entity);
    }

    @Transactional(readOnly = true)
    public DoadoraResponse buscar(Long id) { return toResponse(find(id)); }

    @Transactional(readOnly = true)
    public Page<DoadoraResponse> listar(Pageable pageable) { return repository.findAll(pageable).map(this::toResponse); }

    @Transactional
    public DoadoraResponse atualizar(Long id, DoadoraUpdateRequest request, String ip) {
        Doadora entity = find(id);
        entity.setNome(request.nome().trim());
        entity.setContato(request.contato().trim());
        entity.setCidade(request.cidade().trim());
        entity.setConsentimentoLgpd(request.consentimentoLgpd());
        auditoria.registrar("doadora:" + id, "Perfil da doadora atualizado", ip);
        return toResponse(entity);
    }

    public Doadora find(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Doadora não encontrada: " + id));
    }

    private DoadoraResponse toResponse(Doadora d) {
        return new DoadoraResponse(d.getId(), d.getNome(), d.getContato(), d.getCidade(), d.getConsentimentoLgpd());
    }
}

package br.com.fiap.maelink.bancoleite.service;

import br.com.fiap.maelink.auditoria.service.AuditoriaService;
import br.com.fiap.maelink.bancoleite.dto.*;
import br.com.fiap.maelink.bancoleite.model.BancoLeite;
import br.com.fiap.maelink.bancoleite.repository.BancoLeiteRepository;
import br.com.fiap.maelink.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BancoLeiteService {
    private final BancoLeiteRepository repository;
    private final AuditoriaService auditoria;

    @Transactional(readOnly = true)
    public List<BancoLeiteResponse> listar(String cidade) {
        List<BancoLeite> values = cidade == null || cidade.isBlank() ? repository.findAll() : repository.findByAreaAtendidaContainingIgnoreCase(cidade);
        return values.stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public BancoLeiteResponse buscar(Long id) { return toResponse(find(id)); }

    @Transactional
    public BancoLeiteResponse criar(BancoLeiteCreateRequest request, String ip) {
        BancoLeite b = repository.save(BancoLeite.builder().nome(request.nome()).endereco(request.endereco())
                .areaAtendida(request.areaAtendida()).capacidade(request.capacidade()).build());
        auditoria.registrar("equipe-banco", "Banco de leite cadastrado: " + b.getId(), ip);
        return toResponse(b);
    }

    public BancoLeite find(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Banco de leite não encontrado: " + id));
    }

    private BancoLeiteResponse toResponse(BancoLeite b) { return new BancoLeiteResponse(b.getId(), b.getNome(), b.getEndereco(), b.getAreaAtendida(), b.getCapacidade()); }
}

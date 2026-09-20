package br.com.fiap.maelink.agendamento.service;

import br.com.fiap.maelink.agendamento.dto.*;
import br.com.fiap.maelink.agendamento.model.*;
import br.com.fiap.maelink.agendamento.repository.AgendamentoRepository;
import br.com.fiap.maelink.auditoria.service.AuditoriaService;
import br.com.fiap.maelink.bancoleite.model.BancoLeite;
import br.com.fiap.maelink.bancoleite.service.BancoLeiteService;
import br.com.fiap.maelink.common.exception.BusinessException;
import br.com.fiap.maelink.common.exception.ResourceNotFoundException;
import br.com.fiap.maelink.doadora.model.Doadora;
import br.com.fiap.maelink.doadora.service.DoadoraService;
import br.com.fiap.maelink.triagem.service.TriagemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AgendamentoService {
    private final AgendamentoRepository repository;
    private final DoadoraService doadoraService;
    private final BancoLeiteService bancoService;
    private final TriagemService triagemService;
    private final AuditoriaService auditoria;

    @Transactional
    public AgendamentoResponse criar(AgendamentoCreateRequest request, String ip) {
        Doadora doadora = doadoraService.find(request.doadoraId());
        BancoLeite banco = bancoService.find(request.bancoId());
        triagemService.validarAptaParaAgendamento(doadora.getId());
        if (banco.getCapacidade() <= 0) throw new BusinessException("Banco de leite sem capacidade operacional para novos agendamentos");
        if (!Boolean.TRUE.equals(doadora.getConsentimentoLgpd())) throw new BusinessException("Doadora sem consentimento LGPD ativo");
        Agendamento a = repository.save(Agendamento.builder().doadora(doadora).banco(banco).data(request.data())
                .status(AgendamentoStatus.SOLICITADO).tipoAtendimento(request.tipoAtendimento()).observacao(request.observacao()).build());
        auditoria.registrar("doadora:" + doadora.getId(), "Agendamento criado: " + a.getId(), ip);
        return toResponse(a);
    }

    @Transactional(readOnly = true) public AgendamentoResponse buscar(Long id) { return toResponse(find(id)); }
    @Transactional(readOnly = true) public List<AgendamentoResponse> porDoadora(Long id) { doadoraService.find(id); return repository.findByDoadoraIdOrderByDataDesc(id).stream().map(this::toResponse).toList(); }

    @Transactional
    public AgendamentoResponse alterarStatus(Long id, AgendamentoStatusRequest request, String ip) {
        Agendamento a = find(id); a.setStatus(request.status()); auditoria.registrar("equipe-banco", "Status do agendamento " + id + " alterado para " + request.status(), ip); return toResponse(a);
    }

    @Transactional
    public AgendamentoResponse reagendar(Long id, ReagendamentoRequest request, String ip) {
        Agendamento a = find(id); if (a.getStatus() == AgendamentoStatus.CANCELADO) throw new BusinessException("Agendamento cancelado não pode ser reagendado");
        a.setData(request.novaData()); a.setStatus(AgendamentoStatus.REAGENDADO); auditoria.registrar("doadora:" + a.getDoadora().getId(), "Agendamento reagendado: " + id, ip); return toResponse(a);
    }

    @Transactional
    public void cancelar(Long id, String ip) { Agendamento a = find(id); a.setStatus(AgendamentoStatus.CANCELADO); auditoria.registrar("doadora:" + a.getDoadora().getId(), "Agendamento cancelado: " + id, ip); }

    public Agendamento find(Long id) { return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Agendamento não encontrado: " + id)); }
    private AgendamentoResponse toResponse(Agendamento a) { return new AgendamentoResponse(a.getId(), a.getDoadora().getId(), a.getBanco().getId(), a.getBanco().getNome(), a.getData(), a.getStatus(), a.getTipoAtendimento(), a.getObservacao()); }
}

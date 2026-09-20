package br.com.fiap.maelink.doacao.service;

import br.com.fiap.maelink.agendamento.model.Agendamento;
import br.com.fiap.maelink.agendamento.model.AgendamentoStatus;
import br.com.fiap.maelink.agendamento.service.AgendamentoService;
import br.com.fiap.maelink.auditoria.service.AuditoriaService;
import br.com.fiap.maelink.common.exception.BusinessException;
import br.com.fiap.maelink.common.exception.ResourceNotFoundException;
import br.com.fiap.maelink.doacao.dto.*;
import br.com.fiap.maelink.doacao.model.*;
import br.com.fiap.maelink.doacao.repository.DoacaoRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DoacaoService {
    private final DoacaoRepository repository;
    private final AgendamentoService agendamentoService;
    private final AuditoriaService auditoria;
    private final ObjectMapper objectMapper;

    @Transactional
    public DoacaoResponse criar(DoacaoCreateRequest request, String ip) {
        Agendamento agendamento = agendamentoService.find(request.agendamentoId());
        if (agendamento.getStatus() != AgendamentoStatus.CONFIRMADO && agendamento.getStatus() != AgendamentoStatus.REAGENDADO) {
            throw new BusinessException("O agendamento precisa estar confirmado antes de iniciar uma doação");
        }
        if (repository.findByAgendamentoId(agendamento.getId()).isPresent()) throw new BusinessException("Já existe uma doação para este agendamento");
        List<TimelineEntryResponse> timeline = new ArrayList<>();
        timeline.add(new TimelineEntryResponse(DoacaoStatus.SOLICITADA, "Solicitação de doação criada", LocalDateTime.now()));
        Doacao d = repository.save(Doacao.builder().agendamento(agendamento).volume(request.volume()).status(DoacaoStatus.SOLICITADA).timeline(write(timeline)).build());
        auditoria.registrar("doadora:" + agendamento.getDoadora().getId(), "Doação criada: " + d.getId(), ip);
        return toResponse(d);
    }

    @Transactional(readOnly = true) public DoacaoResponse buscar(Long id) { return toResponse(find(id)); }
    @Transactional(readOnly = true) public List<TimelineEntryResponse> timeline(Long id) { return read(find(id).getTimeline()); }

    @Transactional
    public DoacaoResponse alterarStatus(Long id, DoacaoStatusRequest request, String ip) {
        Doacao d = find(id);
        d.setStatus(request.status());
        if (request.volume() != null) d.setVolume(request.volume());
        List<TimelineEntryResponse> timeline = new ArrayList<>(read(d.getTimeline()));
        timeline.add(new TimelineEntryResponse(request.status(), descricao(request.status()), LocalDateTime.now()));
        d.setTimeline(write(timeline));
        auditoria.registrar("equipe-banco", "Status da doação " + id + " alterado para " + request.status(), ip);
        return toResponse(d);
    }

    public Doacao find(Long id) { return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Doação não encontrada: " + id)); }
    private String descricao(DoacaoStatus s) { return switch (s) {
        case SOLICITADA -> "Solicitação registrada"; case TRIAGEM_APROVADA -> "Triagem aprovada"; case AGENDADA -> "Coleta agendada";
        case COLETADA -> "Leite coletado"; case RECEBIDA -> "Leite recebido no banco"; case CONCLUIDA -> "Doação concluída"; case CANCELADA -> "Doação cancelada"; };
    }
    private String write(Object value) { try { return objectMapper.writeValueAsString(value); } catch (Exception e) { throw new IllegalStateException(e); } }
    private List<TimelineEntryResponse> read(String value) { try { return objectMapper.readValue(value, new TypeReference<>(){}); } catch (Exception e) { throw new IllegalStateException(e); } }
    private DoacaoResponse toResponse(Doacao d) { Agendamento a = d.getAgendamento(); return new DoacaoResponse(d.getId(), a.getId(), a.getDoadora().getId(), a.getBanco().getId(), d.getVolume(), d.getStatus(), read(d.getTimeline())); }
}

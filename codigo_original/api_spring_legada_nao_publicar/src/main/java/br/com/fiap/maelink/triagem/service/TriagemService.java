package br.com.fiap.maelink.triagem.service;

import br.com.fiap.maelink.auditoria.service.AuditoriaService;
import br.com.fiap.maelink.common.exception.ResourceNotFoundException;
import br.com.fiap.maelink.doadora.model.Doadora;
import br.com.fiap.maelink.doadora.service.DoadoraService;
import br.com.fiap.maelink.triagem.dto.*;
import br.com.fiap.maelink.triagem.model.*;
import br.com.fiap.maelink.triagem.repository.TriagemRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TriagemService {
    private final TriagemRepository repository;
    private final DoadoraService doadoraService;
    private final AuditoriaService auditoria;
    private final ObjectMapper objectMapper;

    @Transactional
    public TriagemResponse criar(TriagemCreateRequest request, String ip) {
        Doadora doadora = doadoraService.find(request.doadoraId());
        TriagemResultado resultado = calcularResultado(request.respostas());
        Triagem t = Triagem.builder()
                .doadora(doadora)
                .respostas(write(request.respostas()))
                .resultado(resultado)
                .criadoEm(LocalDateTime.now())
                .build();
        t = repository.save(t);
        auditoria.registrar("doadora:" + doadora.getId(), "Triagem registrada com resultado " + resultado, ip);
        return toResponse(t);
    }

    @Transactional(readOnly = true)
    public TriagemResponse buscar(Long id) { return toResponse(find(id)); }

    @Transactional(readOnly = true)
    public List<TriagemResponse> porDoadora(Long doadoraId) {
        doadoraService.find(doadoraId);
        return repository.findByDoadoraIdOrderByCriadoEmDesc(doadoraId).stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public TriagemResultadoResponse resultado(Long id) {
        Triagem t = find(id);
        String orientacao = switch (t.getResultado()) {
            case APTA_CONTATO -> "Triagem inicial compatível com encaminhamento ao banco de leite. A avaliação profissional continua necessária.";
            case NECESSITA_ORIENTACAO -> "A doadora deve receber orientação da equipe antes do agendamento.";
            case NAO_APTA -> "A triagem inicial não permite seguir automaticamente. Procure orientação profissional do banco de leite.";
        };
        return new TriagemResultadoResponse(t.getId(), t.getResultado(), orientacao);
    }


    @Transactional(readOnly = true)
    public void validarAptaParaAgendamento(Long doadoraId) {
        Triagem ultima = repository.findFirstByDoadoraIdOrderByCriadoEmDesc(doadoraId)
                .orElseThrow(() -> new br.com.fiap.maelink.common.exception.BusinessException("A doadora precisa realizar a triagem antes do agendamento"));
        if (ultima.getResultado() != TriagemResultado.APTA_CONTATO) {
            throw new br.com.fiap.maelink.common.exception.BusinessException("A triagem atual exige orientação antes de permitir o agendamento");
        }
    }

    public Triagem find(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Triagem não encontrada: " + id));
    }

    private TriagemResultado calcularResultado(Map<String, String> respostas) {
        String amamentando = normal(respostas.get("amamentando"));
        String excesso = normal(respostas.get("excessoLeite"));
        String contato = normal(respostas.get("aceitaContato"));
        if ("sim".equals(amamentando) && "sim".equals(excesso) && "sim".equals(contato)) return TriagemResultado.APTA_CONTATO;
        if ("nao".equals(amamentando)) return TriagemResultado.NAO_APTA;
        return TriagemResultado.NECESSITA_ORIENTACAO;
    }

    private String normal(String value) { return value == null ? "" : value.trim().toLowerCase().replace("ã", "a").replace("õ", "o"); }
    private String write(Object value) { try { return objectMapper.writeValueAsString(value); } catch (Exception e) { throw new IllegalStateException(e); } }
    private Map<String, String> read(String value) { try { return objectMapper.readValue(value, new TypeReference<>(){}); } catch (Exception e) { throw new IllegalStateException(e); } }
    private TriagemResponse toResponse(Triagem t) { return new TriagemResponse(t.getId(), t.getDoadora().getId(), read(t.getRespostas()), t.getResultado(), t.getCriadoEm()); }
}

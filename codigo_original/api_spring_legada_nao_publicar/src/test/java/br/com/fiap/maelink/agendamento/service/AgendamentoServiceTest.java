package br.com.fiap.maelink.agendamento.service;

import br.com.fiap.maelink.agendamento.dto.AgendamentoCreateRequest;
import br.com.fiap.maelink.agendamento.dto.AgendamentoResponse;
import br.com.fiap.maelink.agendamento.model.Agendamento;
import br.com.fiap.maelink.agendamento.model.AgendamentoStatus;
import br.com.fiap.maelink.agendamento.model.TipoAtendimento;
import br.com.fiap.maelink.agendamento.repository.AgendamentoRepository;
import br.com.fiap.maelink.auditoria.service.AuditoriaService;
import br.com.fiap.maelink.bancoleite.model.BancoLeite;
import br.com.fiap.maelink.bancoleite.service.BancoLeiteService;
import br.com.fiap.maelink.common.exception.BusinessException;
import br.com.fiap.maelink.doadora.model.Doadora;
import br.com.fiap.maelink.doadora.service.DoadoraService;
import br.com.fiap.maelink.triagem.service.TriagemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AgendamentoServiceTest {

    @Mock AgendamentoRepository repository;
    @Mock DoadoraService doadoraService;
    @Mock BancoLeiteService bancoLeiteService;
    @Mock TriagemService triagemService;
    @Mock AuditoriaService auditoriaService;

    private AgendamentoService service;

    @BeforeEach
    void setup() {
        service = new AgendamentoService(repository, doadoraService, bancoLeiteService, triagemService, auditoriaService);
    }

    @Test
    void deveCriarAgendamentoQuandoRegrasForemAtendidas() {
        Doadora doadora = Doadora.builder()
                .id(1L).nome("Ana").contato("ana@example.com").cidade("São Paulo")
                .consentimentoLgpd(true).build();
        BancoLeite banco = BancoLeite.builder()
                .id(2L).nome("Banco de Leite Central").endereco("Rua A, 100")
                .areaAtendida("São Paulo").capacidade(10).build();

        when(doadoraService.find(1L)).thenReturn(doadora);
        when(bancoLeiteService.find(2L)).thenReturn(banco);
        when(repository.save(any(Agendamento.class))).thenAnswer(invocation -> {
            Agendamento agendamento = invocation.getArgument(0);
            agendamento.setId(99L);
            return agendamento;
        });

        AgendamentoCreateRequest request = new AgendamentoCreateRequest(
                1L, 2L, LocalDateTime.now().plusDays(2), TipoAtendimento.COLETA_DOMICILIAR, "Retirada residencial"
        );

        AgendamentoResponse response = service.criar(request, "127.0.0.1");

        assertEquals(99L, response.id());
        assertEquals(AgendamentoStatus.SOLICITADO, response.status());
        assertEquals("Banco de Leite Central", response.bancoNome());
        verify(triagemService).validarAptaParaAgendamento(1L);
        verify(auditoriaService).registrar(eq("doadora:1"), contains("Agendamento criado"), eq("127.0.0.1"));
    }

    @Test
    void deveBloquearAgendamentoQuandoBancoNaoTemCapacidade() {
        Doadora doadora = Doadora.builder()
                .id(1L).nome("Ana").contato("ana@example.com").cidade("São Paulo")
                .consentimentoLgpd(true).build();
        BancoLeite banco = BancoLeite.builder()
                .id(2L).nome("Banco sem capacidade").endereco("Rua B, 200")
                .areaAtendida("São Paulo").capacidade(0).build();

        when(doadoraService.find(1L)).thenReturn(doadora);
        when(bancoLeiteService.find(2L)).thenReturn(banco);

        AgendamentoCreateRequest request = new AgendamentoCreateRequest(
                1L, 2L, LocalDateTime.now().plusDays(2), TipoAtendimento.VISITA_BANCO, null
        );

        BusinessException exception = assertThrows(BusinessException.class,
                () -> service.criar(request, "127.0.0.1"));

        assertTrue(exception.getMessage().contains("sem capacidade"));
        verify(repository, never()).save(any());
    }
}

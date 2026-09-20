package br.com.fiap.maelink.triagem.service;

import br.com.fiap.maelink.auditoria.service.AuditoriaService;
import br.com.fiap.maelink.doadora.model.Doadora;
import br.com.fiap.maelink.doadora.service.DoadoraService;
import br.com.fiap.maelink.triagem.dto.TriagemCreateRequest;
import br.com.fiap.maelink.triagem.dto.TriagemResponse;
import br.com.fiap.maelink.triagem.model.Triagem;
import br.com.fiap.maelink.triagem.model.TriagemResultado;
import br.com.fiap.maelink.triagem.repository.TriagemRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TriagemServiceTest {

    @Mock TriagemRepository repository;
    @Mock DoadoraService doadoraService;
    @Mock AuditoriaService auditoriaService;

    private TriagemService service;

    @BeforeEach
    void setup() {
        service = new TriagemService(repository, doadoraService, auditoriaService, new ObjectMapper());
    }

    @Test
    void deveClassificarComoAptaQuandoRespostasPermitemContato() {
        Doadora doadora = Doadora.builder()
                .id(1L)
                .nome("Ana Martins")
                .contato("ana@example.com")
                .cidade("São Paulo")
                .consentimentoLgpd(true)
                .build();

        when(doadoraService.find(1L)).thenReturn(doadora);
        when(repository.save(any(Triagem.class))).thenAnswer(invocation -> {
            Triagem triagem = invocation.getArgument(0);
            triagem.setId(10L);
            return triagem;
        });

        TriagemCreateRequest request = new TriagemCreateRequest(1L, Map.of(
                "amamentando", "sim",
                "excessoLeite", "sim",
                "aceitaContato", "sim"
        ));

        TriagemResponse response = service.criar(request, "127.0.0.1");

        assertEquals(10L, response.id());
        assertEquals(TriagemResultado.APTA_CONTATO, response.resultado());
        verify(auditoriaService).registrar(eq("doadora:1"), contains("APTA_CONTATO"), eq("127.0.0.1"));
    }
}

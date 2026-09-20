package br.com.fiap.maelink.conteudo.controller;

import br.com.fiap.maelink.conteudo.dto.ConteudoResponse;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/conteudos")
public class ConteudoController {
    private static final List<ConteudoResponse> CONTEUDOS = List.of(
            new ConteudoResponse(1L, "Quem pode doar?", "Orientações iniciais para nutrizes interessadas em doar leite humano.", "DOACAO"),
            new ConteudoResponse(2L, "Armazenamento", "Boas práticas gerais para armazenamento antes do contato com o banco de leite.", "ARMAZENAMENTO"),
            new ConteudoResponse(3L, "Por que doar?", "A doação de leite humano apoia o cuidado de recém-nascidos que necessitam desse recurso.", "IMPACTO")
    );

    @GetMapping
    public List<ConteudoResponse> listar() { return CONTEUDOS; }
}

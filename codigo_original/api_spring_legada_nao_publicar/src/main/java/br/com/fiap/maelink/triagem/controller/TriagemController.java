package br.com.fiap.maelink.triagem.controller;

import br.com.fiap.maelink.triagem.dto.*;
import br.com.fiap.maelink.triagem.service.TriagemService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/triagens")
@RequiredArgsConstructor
public class TriagemController {
    private final TriagemService service;

    @PostMapping @ResponseStatus(HttpStatus.CREATED)
    public TriagemResponse criar(@Valid @RequestBody TriagemCreateRequest body, HttpServletRequest request) { return service.criar(body, request.getRemoteAddr()); }

    @GetMapping("/{id}")
    public TriagemResponse buscar(@PathVariable Long id) { return service.buscar(id); }

    @GetMapping("/{id}/resultado")
    public TriagemResultadoResponse resultado(@PathVariable Long id) { return service.resultado(id); }

    @GetMapping
    public List<TriagemResponse> porDoadora(@RequestParam Long doadoraId) { return service.porDoadora(doadoraId); }
}

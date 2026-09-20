package br.com.fiap.maelink.bancoleite.controller;

import br.com.fiap.maelink.bancoleite.dto.*;
import br.com.fiap.maelink.bancoleite.service.BancoLeiteService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/bancos")
@RequiredArgsConstructor
public class BancoLeiteController {
    private final BancoLeiteService service;

    @GetMapping
    public List<BancoLeiteResponse> listar(@RequestParam(required = false) String cidade) { return service.listar(cidade); }

    @GetMapping("/{id}")
    public BancoLeiteResponse buscar(@PathVariable Long id) { return service.buscar(id); }

    @GetMapping("/{id}/capacidade")
    public BancoLeiteResponse capacidade(@PathVariable Long id) { return service.buscar(id); }

    @PostMapping @ResponseStatus(HttpStatus.CREATED)
    public BancoLeiteResponse criar(@Valid @RequestBody BancoLeiteCreateRequest body, HttpServletRequest request) {
        return service.criar(body, request.getRemoteAddr());
    }
}

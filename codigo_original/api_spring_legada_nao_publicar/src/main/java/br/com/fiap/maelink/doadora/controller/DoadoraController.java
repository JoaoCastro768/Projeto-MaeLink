package br.com.fiap.maelink.doadora.controller;

import br.com.fiap.maelink.doadora.dto.*;
import br.com.fiap.maelink.doadora.service.DoadoraService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/doadoras")
@RequiredArgsConstructor
public class DoadoraController {
    private final DoadoraService service;

    @PostMapping @ResponseStatus(HttpStatus.CREATED)
    public DoadoraResponse criar(@Valid @RequestBody DoadoraCreateRequest body, HttpServletRequest request) {
        return service.criar(body, request.getRemoteAddr());
    }

    @GetMapping("/{id}")
    public DoadoraResponse buscar(@PathVariable Long id) { return service.buscar(id); }

    @GetMapping
    public Page<DoadoraResponse> listar(Pageable pageable) { return service.listar(pageable); }

    @PutMapping("/{id}")
    public DoadoraResponse atualizar(@PathVariable Long id, @Valid @RequestBody DoadoraUpdateRequest body, HttpServletRequest request) {
        return service.atualizar(id, body, request.getRemoteAddr());
    }
}

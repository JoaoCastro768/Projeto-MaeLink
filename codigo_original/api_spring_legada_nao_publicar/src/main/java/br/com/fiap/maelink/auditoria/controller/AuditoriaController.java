package br.com.fiap.maelink.auditoria.controller;

import br.com.fiap.maelink.auditoria.dto.AuditoriaResponse;
import br.com.fiap.maelink.auditoria.service.AuditoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auditorias")
@RequiredArgsConstructor
public class AuditoriaController {
    private final AuditoriaService service;

    @GetMapping
    public Page<AuditoriaResponse> listar(Pageable pageable) {
        return service.listar(pageable);
    }
}

package br.com.fiap.maelink.doacao.controller;

import br.com.fiap.maelink.doacao.dto.*;
import br.com.fiap.maelink.doacao.service.DoacaoService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/doacoes")
@RequiredArgsConstructor
public class DoacaoController {
    private final DoacaoService service;
    @PostMapping @ResponseStatus(HttpStatus.CREATED) public DoacaoResponse criar(@Valid @RequestBody DoacaoCreateRequest body, HttpServletRequest req) { return service.criar(body, req.getRemoteAddr()); }
    @GetMapping("/{id}") public DoacaoResponse buscar(@PathVariable Long id) { return service.buscar(id); }
    @GetMapping("/{id}/timeline") public List<TimelineEntryResponse> timeline(@PathVariable Long id) { return service.timeline(id); }
    @PatchMapping("/{id}/status") public DoacaoResponse status(@PathVariable Long id, @Valid @RequestBody DoacaoStatusRequest body, HttpServletRequest req) { return service.alterarStatus(id, body, req.getRemoteAddr()); }
}

package br.com.fiap.maelink.agendamento.controller;

import br.com.fiap.maelink.agendamento.dto.*;
import br.com.fiap.maelink.agendamento.service.AgendamentoService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/agendamentos")
@RequiredArgsConstructor
public class AgendamentoController {
    private final AgendamentoService service;

    @PostMapping @ResponseStatus(HttpStatus.CREATED)
    public AgendamentoResponse criar(@Valid @RequestBody AgendamentoCreateRequest body, HttpServletRequest req) { return service.criar(body, req.getRemoteAddr()); }
    @GetMapping("/{id}") public AgendamentoResponse buscar(@PathVariable Long id) { return service.buscar(id); }
    @GetMapping public List<AgendamentoResponse> listar(@RequestParam Long doadoraId) { return service.porDoadora(doadoraId); }
    @PatchMapping("/{id}/status") public AgendamentoResponse status(@PathVariable Long id, @Valid @RequestBody AgendamentoStatusRequest body, HttpServletRequest req) { return service.alterarStatus(id, body, req.getRemoteAddr()); }
    @PatchMapping("/{id}/reagendar") public AgendamentoResponse reagendar(@PathVariable Long id, @Valid @RequestBody ReagendamentoRequest body, HttpServletRequest req) { return service.reagendar(id, body, req.getRemoteAddr()); }
    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void cancelar(@PathVariable Long id, HttpServletRequest req) { service.cancelar(id, req.getRemoteAddr()); }
}

package br.com.fiap.maelink.notificacao.controller;

import br.com.fiap.maelink.notificacao.dto.*;
import br.com.fiap.maelink.notificacao.service.NotificacaoService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/notificacoes")
@RequiredArgsConstructor
public class NotificacaoController {
    private final NotificacaoService service;
    @PostMapping @ResponseStatus(HttpStatus.CREATED) public NotificacaoResponse enviar(@Valid @RequestBody NotificacaoCreateRequest body, HttpServletRequest req) { return service.enviar(body, req.getRemoteAddr()); }
    @GetMapping public List<NotificacaoResponse> listar(@RequestParam Long usuarioId) { return service.listar(usuarioId); }
}

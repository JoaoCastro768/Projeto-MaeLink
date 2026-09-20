package br.com.fiap.maelink.auth.controller;

import br.com.fiap.maelink.auth.dto.*;
import br.com.fiap.maelink.auth.service.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final JwtService jwtService;

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest body) {
        return jwtService.issue(body.identificador(), "DOADORA");
    }

    @PostMapping("/refresh")
    public AuthResponse refresh(@RequestHeader("Authorization") String authorization) {
        return jwtService.refresh(authorization);
    }

    @GetMapping("/validate")
    public TokenClaimsResponse validate(@RequestHeader("Authorization") String authorization) {
        return jwtService.validate(authorization);
    }
}

package br.com.fiap.maelink.auth.service;

import br.com.fiap.maelink.auth.dto.AuthResponse;
import br.com.fiap.maelink.auth.dto.TokenClaimsResponse;
import br.com.fiap.maelink.common.exception.UnauthorizedException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private final JwtService service = new JwtService(
            "maelink-academic-demo-secret-key-2026-fiap-554628",
            60
    );

    @Test
    void deveEmitirValidarERenovarToken() {
        AuthResponse emitido = service.issue("joao.castro@example.com", "DOADORA");

        assertNotNull(emitido.accessToken());
        assertEquals("Bearer", emitido.tokenType());
        assertEquals("DOADORA", emitido.perfil());

        TokenClaimsResponse claims = service.validate("Bearer " + emitido.accessToken());
        assertEquals("joao.castro@example.com", claims.subject());
        assertEquals("DOADORA", claims.perfil());
        assertEquals("maelink-api", claims.issuer());

        AuthResponse renovado = service.refresh("Bearer " + emitido.accessToken());
        assertNotNull(renovado.accessToken());
        assertEquals("DOADORA", renovado.perfil());
    }

    @Test
    void deveRejeitarTokenInvalido() {
        assertThrows(UnauthorizedException.class,
                () -> service.validate("Bearer token-invalido"));
    }
}

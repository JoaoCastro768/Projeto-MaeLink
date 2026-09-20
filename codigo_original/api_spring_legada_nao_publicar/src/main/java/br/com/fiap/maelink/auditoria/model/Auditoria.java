package br.com.fiap.maelink.auditoria.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "auditorias")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Auditoria {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 160)
    private String usuario;
    @Column(nullable = false, length = 300)
    private String acao;
    @Column(nullable = false)
    private LocalDateTime timestamp;
    @Column(name = "ip_origem", nullable = false, length = 64)
    private String ipOrigem;
}

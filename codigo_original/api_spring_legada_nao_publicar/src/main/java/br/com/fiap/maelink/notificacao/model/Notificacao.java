package br.com.fiap.maelink.notificacao.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notificacoes")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Notificacao {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 30)
    private CanalNotificacao canal;
    @Column(nullable = false, length = 500)
    private String mensagem;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 30)
    private NotificacaoSituacao situacao;
    @Column(name = "criado_em", nullable = false)
    private LocalDateTime criadoEm;
}

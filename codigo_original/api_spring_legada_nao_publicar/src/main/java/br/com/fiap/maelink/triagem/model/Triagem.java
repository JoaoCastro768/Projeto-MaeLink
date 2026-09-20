package br.com.fiap.maelink.triagem.model;

import br.com.fiap.maelink.doadora.model.Doadora;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "triagens")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Triagem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "doadora_id", nullable = false)
    private Doadora doadora;
    @Column(nullable = false, columnDefinition = "TEXT")
    private String respostas;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 40)
    private TriagemResultado resultado;
    @Column(name = "criado_em", nullable = false)
    private LocalDateTime criadoEm;
}

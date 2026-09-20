package br.com.fiap.maelink.doacao.model;

import br.com.fiap.maelink.agendamento.model.Agendamento;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "doacoes")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Doacao {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "agendamento_id", nullable = false, unique = true)
    private Agendamento agendamento;
    @Column(precision = 10, scale = 2)
    private BigDecimal volume;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 40)
    private DoacaoStatus status;
    @Column(nullable = false, columnDefinition = "TEXT")
    private String timeline;
}

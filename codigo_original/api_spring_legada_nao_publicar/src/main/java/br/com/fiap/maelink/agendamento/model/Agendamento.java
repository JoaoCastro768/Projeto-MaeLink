package br.com.fiap.maelink.agendamento.model;

import br.com.fiap.maelink.bancoleite.model.BancoLeite;
import br.com.fiap.maelink.doadora.model.Doadora;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "agendamentos")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Agendamento {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(optional = false, fetch = FetchType.LAZY) @JoinColumn(name = "doadora_id", nullable = false)
    private Doadora doadora;
    @ManyToOne(optional = false, fetch = FetchType.LAZY) @JoinColumn(name = "banco_id", nullable = false)
    private BancoLeite banco;
    @Column(nullable = false)
    private LocalDateTime data;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 40)
    private AgendamentoStatus status;
    @Enumerated(EnumType.STRING) @Column(name = "tipo_atendimento", nullable = false, length = 40)
    private TipoAtendimento tipoAtendimento;
    @Column(length = 300)
    private String observacao;
}

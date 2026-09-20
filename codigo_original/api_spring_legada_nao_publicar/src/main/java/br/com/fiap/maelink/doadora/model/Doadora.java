package br.com.fiap.maelink.doadora.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "doadoras")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Doadora {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 120)
    private String nome;
    @Column(nullable = false, length = 160)
    private String contato;
    @Column(nullable = false, length = 120)
    private String cidade;
    @Column(name = "consentimento_lgpd", nullable = false)
    private Boolean consentimentoLgpd;
}

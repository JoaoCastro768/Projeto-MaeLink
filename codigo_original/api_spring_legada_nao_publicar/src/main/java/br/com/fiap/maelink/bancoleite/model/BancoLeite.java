package br.com.fiap.maelink.bancoleite.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "bancos_leite")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class BancoLeite {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 160)
    private String nome;
    @Column(nullable = false, length = 220)
    private String endereco;
    @Column(name = "area_atendida", nullable = false, length = 180)
    private String areaAtendida;
    @Column(nullable = false)
    private Integer capacidade;
}

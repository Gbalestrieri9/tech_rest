package com.techchallenge.produto.domain.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;

@Entity
@Table(name = "produtos")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(length = 255)
    private String descricao;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal preco;

    @Column(length = 50)
    private String categoria;

    @Column(nullable = false)
    private Boolean ativo = true;

    @Column(nullable = false, unique = true, length = 36)
    private String sku;

    @PrePersist
    public void prePersist() {
        if (sku == null || sku.isBlank()) {
            sku = java.util.UUID.randomUUID().toString();
        }
        if (ativo == null) ativo = true;
    }
}

package com.techchallenge.estoque.domain.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "estoque")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Estoque {
    @Id
    private Long produtoId;

    @Column(nullable = false)
    private Integer quantidade;
}

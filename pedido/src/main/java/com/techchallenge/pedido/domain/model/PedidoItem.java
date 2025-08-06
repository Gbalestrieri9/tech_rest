package com.techchallenge.pedido.domain.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity @Table(name="pedido_items")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PedidoItem {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @ManyToOne @JoinColumn(name="pedido_id")
    @JsonBackReference
    private Pedido pedido;

    private Long produtoId;
    private Integer quantidade;
    private BigDecimal precoUnitario;
}
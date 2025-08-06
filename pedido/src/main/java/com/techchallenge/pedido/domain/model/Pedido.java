package com.techchallenge.pedido.domain.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Entity @Table(name="pedidos")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Pedido {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    private Long clienteId;
    private BigDecimal total;
    private Instant dataCriacao;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "pedido", fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<PedidoItem> itens;
}
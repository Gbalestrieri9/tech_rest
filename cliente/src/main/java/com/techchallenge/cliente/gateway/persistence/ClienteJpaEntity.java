package com.techchallenge.cliente.gateway.persistence;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="clientes", uniqueConstraints = @UniqueConstraint(name="uk_cliente_cpf", columnNames = "cpf"))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ClienteJpaEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, length=120)
    private String nome;

    @Column(nullable=false, length=11)
    private String cpf;

    @Column(name="data_nascimento", nullable=false)
    private LocalDate dataNascimento;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name="cliente_enderecos", joinColumns = @JoinColumn(name="cliente_id"))
    private List<EnderecoEmbeddable> enderecos = new ArrayList<>();
}

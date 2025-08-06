package com.techchallenge.cliente.gateway.persistence;

import jakarta.persistence.Embeddable;
import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class EnderecoEmbeddable {
    private String logradouro;
    private String numero;
    private String complemento;
    private String cep;
    private String cidade;
    private String uf;
}

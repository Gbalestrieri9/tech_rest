package com.techchallenge.cliente.domain.validation;

import com.techchallenge.cliente.domain.entity.Endereco;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
public class EnderecoTest {

    @Test
    void deveCriarEnderecoComBuilder() {
        Endereco endereco = Endereco.builder()
                .logradouro("Rua A")
                .numero("123")
                .complemento("Ap 1")
                .cep("12345000")
                .cidade("SP")
                .uf("SP")
                .build();

        assertThat(endereco.getLogradouro()).isEqualTo("Rua A");
        assertThat(endereco.getNumero()).isEqualTo("123");
        assertThat(endereco.getComplemento()).isEqualTo("Ap 1");
        assertThat(endereco.getCep()).isEqualTo("12345000");
        assertThat(endereco.getCidade()).isEqualTo("SP");
        assertThat(endereco.getUf()).isEqualTo("SP");
    }
}
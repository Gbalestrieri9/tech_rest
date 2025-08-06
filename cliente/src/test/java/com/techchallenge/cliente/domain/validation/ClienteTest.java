package com.techchallenge.cliente.domain.validation;

import com.techchallenge.cliente.domain.entity.Cliente;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
public class ClienteTest {

    @Test
    void deveCriarClienteComBuilder() {
        Cliente cliente = Cliente.builder()
                .id(1L)
                .nome("João")
                .cpf("12345678901")
                .dataNascimento(LocalDate.of(1990, 1, 1))
                .enderecos(List.of())
                .build();

        assertThat(cliente.getId()).isEqualTo(1L);
        assertThat(cliente.getNome()).isEqualTo("João");
        assertThat(cliente.getCpf()).isEqualTo("12345678901");
        assertThat(cliente.getDataNascimento()).isEqualTo(LocalDate.of(1990, 1, 1));
        assertThat(cliente.getEnderecos()).isEmpty();
    }
}

package com.techchallenge.cliente.gateway.persistence;

import com.techchallenge.cliente.gateway.persistence.ClienteJpaEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.Collections;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ClienteRepositoryTest {

    @Autowired
    private ClienteJpaRepository repo;

    @Test
    void deveSalvarERecuperarCliente() {
        ClienteJpaEntity entidade = ClienteJpaEntity.builder()
                .nome("Ana")
                .cpf("98765432100")
                .dataNascimento(LocalDate.of(1990, 1, 1))
                .enderecos(Collections.emptyList())
                .build();

        ClienteJpaEntity saved = repo.save(entidade);
        assertThat(saved.getId()).isNotNull();

        ClienteJpaEntity found = repo.findById(saved.getId()).orElseThrow();
        assertThat(found.getCpf()).isEqualTo("98765432100");
    }
}

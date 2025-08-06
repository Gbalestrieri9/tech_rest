package com.techchallenge.cliente.gateway.persistence;

import com.techchallenge.cliente.domain.entity.Cliente;
import com.techchallenge.cliente.domain.entity.Endereco;
import com.techchallenge.cliente.exception.ClienteNaoEncontradoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
class ClienteRepositoryAdapterTest {

    @Autowired
    private ClienteRepositoryAdapter adapter;

    @Autowired
    private ClienteJpaRepository jpa;

    @BeforeEach
    void setup() {
        jpa.deleteAll();
    }

    @Test
    void deveSalvarCliente() {
        Cliente cliente = getCliente();
        Cliente saved = adapter.save(cliente);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getNome()).isEqualTo("Maria");
    }

    @Test
    void deveBuscarClientePorId() {
        Cliente saved = adapter.save(getCliente());
        Cliente found = adapter.findById(saved.getId());

        assertThat(found.getId()).isEqualTo(saved.getId());
    }

    @Test
    void deveLancarExcecaoQuandoNaoEncontrarCliente() {
        assertThatThrownBy(() -> adapter.findById(999L))
                .isInstanceOf(ClienteNaoEncontradoException.class)
                .hasMessageContaining("999");
    }

    @Test
    void deveListarTodosClientes() {
        Cliente c1 = getCliente("12345678901");
        Cliente c2 = getCliente("98765432100");

        adapter.save(c1);
        adapter.save(c2);

        List<Cliente> list = adapter.findAll();

        assertThat(list).hasSize(2);
    }

    @Test
    void deveAtualizarCliente() {
        Cliente cliente = adapter.save(getCliente());
        Cliente atualizado = Cliente.builder()
                .id(cliente.getId())
                .nome("Novo Nome")
                .cpf(cliente.getCpf())
                .dataNascimento(cliente.getDataNascimento())
                .enderecos(cliente.getEnderecos())
                .build();

        Cliente updated = adapter.update(atualizado);

        assertThat(updated.getNome()).isEqualTo("Novo Nome");
    }

    @Test
    void deveDeletarCliente() {
        Cliente cliente = adapter.save(getCliente());
        adapter.delete(cliente.getId());

        assertThatThrownBy(() -> adapter.findById(cliente.getId()))
                .isInstanceOf(ClienteNaoEncontradoException.class);
    }

    @Test
    void deveVerificarExistenciaPorCpf() {
        Cliente cliente = adapter.save(getCliente());
        boolean exists = adapter.existsByCpf(cliente.getCpf());

        assertThat(exists).isTrue();
    }

    private Cliente getCliente(String cpf) {
        Endereco endereco = Endereco.builder()
                .logradouro("Rua A")
                .numero("123")
                .complemento("Apt 1")
                .cep("12345000")
                .cidade("SP")
                .uf("SP")
                .build();

        return Cliente.builder()
                .nome("Maria")
                .cpf(cpf)
                .dataNascimento(LocalDate.of(2000, 1, 1))
                .enderecos(List.of(endereco))
                .build();
    }

    private Cliente getCliente() {
        return getCliente("12345678901");
    }
}


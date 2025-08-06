package com.techchallenge.cliente.usecase;

import com.techchallenge.cliente.domain.entity.Cliente;
import com.techchallenge.cliente.domain.repository.ClienteRepositoryPort;
import com.techchallenge.cliente.exception.ClienteNaoEncontradoException;
import com.techchallenge.cliente.util.ClienteFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
class AtualizarClienteUseCaseTest {

    @Mock
    ClienteRepositoryPort repo;

    @InjectMocks
    AtualizarClienteUseCase useCase;

    @Test
    void deveAtualizarMantendoCpfOriginal(){
        Cliente existente = ClienteFactory.clienteValidoSemId().withId(10L);
        when(repo.findById(10L)).thenReturn(existente);
        when(repo.update(any())).thenAnswer(inv -> inv.getArgument(0));

        Cliente dados = Cliente.builder()
                .id(null)
                .nome("Novo Nome")
                .cpf("000.000.000-00")
                .dataNascimento(LocalDate.of(2000,1,1))
                .enderecos(existente.getEnderecos())
                .build();

        Cliente atualizado = useCase.atualizar(10L, dados);

        assertEquals("Novo Nome", atualizado.getNome());
        assertEquals(existente.getCpf(), atualizado.getCpf());
        verify(repo).update(any());
    }

    @Test
    void deveFalharSeNaoExiste(){
        when(repo.findById(99L)).thenThrow(new ClienteNaoEncontradoException(99L));
        Cliente dados = ClienteFactory.clienteValidoSemId();

        assertThrows(ClienteNaoEncontradoException.class, () -> useCase.atualizar(99L, dados));
    }
}

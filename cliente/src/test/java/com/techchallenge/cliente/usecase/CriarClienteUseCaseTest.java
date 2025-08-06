package com.techchallenge.cliente.usecase;

import com.techchallenge.cliente.domain.entity.Cliente;
import com.techchallenge.cliente.domain.repository.ClienteRepositoryPort;
import com.techchallenge.cliente.exception.ClienteJaExisteException;
import com.techchallenge.cliente.exception.CpfInvalidoException;
import com.techchallenge.cliente.util.ClienteFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
class CriarClienteUseCaseTest {

    @Mock
    ClienteRepositoryPort repo;

    @InjectMocks
    CriarClienteUseCase useCase;

    @Test
    void deveCriarClienteQuandoCpfValidoENaoDuplicado(){
        Cliente cliente = ClienteFactory.clienteValidoSemId();

        when(repo.existsByCpf("39053344705")).thenReturn(false);
        when(repo.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Cliente salvo = useCase.execute(cliente);

        assertNotNull(salvo);
        assertEquals("39053344705", salvo.getCpf());
        verify(repo).save(any());
    }

    @Test
    void deveLancarExcecaoQuandoCpfInvalido(){
        Cliente invalido = Cliente.builder()
                .id(null)
                .nome("Teste")
                .cpf("111.111.111-11")
                .dataNascimento(clienteValido().getDataNascimento())
                .enderecos(clienteValido().getEnderecos())
                .build();

        assertThrows(CpfInvalidoException.class, () -> useCase.execute(invalido));
        verify(repo, never()).save(any());
    }

    @Test
    void deveLancarExcecaoQuandoCpfDuplicado(){
        Cliente cliente = ClienteFactory.clienteValidoSemId();
        when(repo.existsByCpf("39053344705")).thenReturn(true);

        assertThrows(ClienteJaExisteException.class, () -> useCase.execute(cliente));
        verify(repo, never()).save(any());
    }

    private Cliente clienteValido(){
        return ClienteFactory.clienteValidoSemId();
    }
}

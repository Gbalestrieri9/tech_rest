package com.techchallenge.cliente.usecase;

import com.techchallenge.cliente.domain.repository.ClienteRepositoryPort;
import com.techchallenge.cliente.exception.ClienteNaoEncontradoException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
class RemoverClienteUseCaseTest {

    @Mock
    ClienteRepositoryPort repo;

    @InjectMocks
    RemoverClienteUseCase useCase;

    @Test
    void deveRemoverClienteExistente(){
        doNothing().when(repo).delete(5L);

        useCase.remover(5L);

        verify(repo).delete(5L);
    }

    @Test
    void devePropagarExcecaoQuandoNaoExiste(){
        doThrow(new ClienteNaoEncontradoException(77L)).when(repo).delete(77L);

        assertThrows(ClienteNaoEncontradoException.class, () -> useCase.remover(77L));
        verify(repo).delete(77L);
    }
}

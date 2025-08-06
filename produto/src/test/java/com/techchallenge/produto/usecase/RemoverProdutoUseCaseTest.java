package com.techchallenge.produto.usecase;

import com.techchallenge.produto.domain.model.repository.ProdutoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class RemoverProdutoUseCaseTest {

    @Mock
    private ProdutoRepository repository;

    @InjectMocks
    private RemoverProdutoUseCase useCase;

    @Test
    void executar_DeveRemoverProdutoComSucesso() {
        when(repository.existsById(1L)).thenReturn(true);

        assertDoesNotThrow(() -> useCase.executar(1L));

        verify(repository).existsById(1L);
        verify(repository).deleteById(1L);
    }

    @Test
    void executar_DeveLancarExcecaoQuandoProdutoNaoExiste() {
        when(repository.existsById(1L)).thenReturn(false);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.executar(1L)
        );

        assertEquals("Produto id 1 não encontrado", exception.getMessage());
        verify(repository).existsById(1L);
        verify(repository, never()).deleteById(any());
    }
}
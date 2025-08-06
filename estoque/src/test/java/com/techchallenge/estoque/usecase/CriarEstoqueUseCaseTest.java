package com.techchallenge.estoque.usecase;

import com.techchallenge.estoque.domain.model.Estoque;
import com.techchallenge.estoque.domain.repository.EstoqueRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CriarEstoqueUseCaseTest {

    @Mock
    private EstoqueRepository repository;

    private CriarEstoqueUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new CriarEstoqueUseCase(repository);
    }

    @Test
    void deveCriarEstoqueComSucesso() {
        Long produtoId = 1L;
        Integer quantidade = 100;
        Estoque estoqueEsperado = Estoque.builder()
                .produtoId(produtoId)
                .quantidade(quantidade)
                .build();

        when(repository.existsById(produtoId)).thenReturn(false);
        when(repository.save(any(Estoque.class))).thenReturn(estoqueEsperado);

        Estoque resultado = useCase.executar(produtoId, quantidade);

        assertNotNull(resultado);
        assertEquals(produtoId, resultado.getProdutoId());
        assertEquals(quantidade, resultado.getQuantidade());
        verify(repository).existsById(produtoId);
        verify(repository).save(any(Estoque.class));
    }

    @Test
    void deveCriarEstoqueComQuantidadeZeroQuandoQuantidadeForNula() {
        Long produtoId = 1L;
        Integer quantidade = null;
        Estoque estoqueEsperado = Estoque.builder()
                .produtoId(produtoId)
                .quantidade(0)
                .build();

        when(repository.existsById(produtoId)).thenReturn(false);
        when(repository.save(any(Estoque.class))).thenReturn(estoqueEsperado);

        Estoque resultado = useCase.executar(produtoId, quantidade);

        assertNotNull(resultado);
        assertEquals(produtoId, resultado.getProdutoId());
        assertEquals(0, resultado.getQuantidade());
        verify(repository).existsById(produtoId);
        verify(repository).save(any(Estoque.class));
    }

    @Test
    void deveLancarExcecaoQuandoEstoqueJaExiste() {
        Long produtoId = 1L;
        Integer quantidade = 100;

        when(repository.existsById(produtoId)).thenReturn(true);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.executar(produtoId, quantidade)
        );

        assertEquals("Estoque para produto 1 já existe", exception.getMessage());
        verify(repository).existsById(produtoId);
        verify(repository, never()).save(any(Estoque.class));
    }

    @Test
    void deveBuscarEstoqueComSucesso() {
        Long produtoId = 1L;
        Estoque estoqueEsperado = Estoque.builder()
                .produtoId(produtoId)
                .quantidade(100)
                .build();

        when(repository.findById(produtoId)).thenReturn(Optional.of(estoqueEsperado));

        Estoque resultado = useCase.buscar(produtoId);

        assertNotNull(resultado);
        assertEquals(produtoId, resultado.getProdutoId());
        assertEquals(100, resultado.getQuantidade());
        verify(repository).findById(produtoId);
    }

    @Test
    void deveLancarExcecaoQuandoEstoqueNaoExisteParaBusca() {
        Long produtoId = 1L;

        when(repository.findById(produtoId)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.buscar(produtoId)
        );

        assertEquals("Estoque para produto 1 não existe", exception.getMessage());
        verify(repository).findById(produtoId);
    }
}
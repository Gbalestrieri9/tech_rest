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
class AtualizarEstoqueUseCaseTest {

    @Mock
    private EstoqueRepository repository;

    private AtualizarEstoqueUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new AtualizarEstoqueUseCase(repository);
    }

    @Test
    void deveAtualizarEstoqueComSucesso() {
        Long produtoId = 1L;
        Integer novaQuantidade = 150;
        Estoque estoqueExistente = Estoque.builder()
                .produtoId(produtoId)
                .quantidade(100)
                .build();

        Estoque estoqueAtualizado = Estoque.builder()
                .produtoId(produtoId)
                .quantidade(novaQuantidade)
                .build();

        when(repository.findById(produtoId)).thenReturn(Optional.of(estoqueExistente));
        when(repository.save(any(Estoque.class))).thenReturn(estoqueAtualizado);

        Estoque resultado = useCase.executar(produtoId, novaQuantidade);

        assertNotNull(resultado);
        assertEquals(produtoId, resultado.getProdutoId());
        assertEquals(novaQuantidade, resultado.getQuantidade());
        verify(repository).findById(produtoId);
        verify(repository).save(estoqueExistente);
        assertEquals(novaQuantidade, estoqueExistente.getQuantidade());
    }

    @Test
    void deveAtualizarEstoqueComQuantidadeZeroQuandoQuantidadeForNula() {
        Long produtoId = 1L;
        Integer novaQuantidade = null;
        Estoque estoqueExistente = Estoque.builder()
                .produtoId(produtoId)
                .quantidade(100)
                .build();

        Estoque estoqueAtualizado = Estoque.builder()
                .produtoId(produtoId)
                .quantidade(0)
                .build();

        when(repository.findById(produtoId)).thenReturn(Optional.of(estoqueExistente));
        when(repository.save(any(Estoque.class))).thenReturn(estoqueAtualizado);

        Estoque resultado = useCase.executar(produtoId, novaQuantidade);

        assertNotNull(resultado);
        assertEquals(produtoId, resultado.getProdutoId());
        assertEquals(0, resultado.getQuantidade());
        verify(repository).findById(produtoId);
        verify(repository).save(estoqueExistente);
        assertEquals(0, estoqueExistente.getQuantidade());
    }

    @Test
    void deveLancarExcecaoQuandoEstoqueNaoExiste() {
        Long produtoId = 1L;
        Integer novaQuantidade = 150;

        when(repository.findById(produtoId)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.executar(produtoId, novaQuantidade)
        );

        assertEquals("Estoque para produto 1 não existe", exception.getMessage());
        verify(repository).findById(produtoId);
        verify(repository, never()).save(any(Estoque.class));
    }

    @Test
    void deveAtualizarEstoqueComQuantidadeZero() {
        Long produtoId = 1L;
        Integer novaQuantidade = 0;
        Estoque estoqueExistente = Estoque.builder()
                .produtoId(produtoId)
                .quantidade(100)
                .build();

        Estoque estoqueAtualizado = Estoque.builder()
                .produtoId(produtoId)
                .quantidade(0)
                .build();

        when(repository.findById(produtoId)).thenReturn(Optional.of(estoqueExistente));
        when(repository.save(any(Estoque.class))).thenReturn(estoqueAtualizado);

        Estoque resultado = useCase.executar(produtoId, novaQuantidade);

        assertNotNull(resultado);
        assertEquals(produtoId, resultado.getProdutoId());
        assertEquals(0, resultado.getQuantidade());
        verify(repository).findById(produtoId);
        verify(repository).save(estoqueExistente);
        assertEquals(0, estoqueExistente.getQuantidade());
    }
}
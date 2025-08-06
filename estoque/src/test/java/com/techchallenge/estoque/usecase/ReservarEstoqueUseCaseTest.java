package com.techchallenge.estoque.usecase;

import com.techchallenge.estoque.domain.model.Estoque;
import com.techchallenge.estoque.domain.repository.EstoqueRepository;
import com.techchallenge.estoque.exception.InsufficientStockException;
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
class ReservarEstoqueUseCaseTest {

    @Mock
    private EstoqueRepository repository;

    private ReservarEstoqueUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new ReservarEstoqueUseCase(repository);
    }

    @Test
    void deveReservarEstoqueComSucesso() {
        Long produtoId = 1L;
        int quantidadeReserva = 20;
        Estoque estoqueExistente = Estoque.builder()
                .produtoId(produtoId)
                .quantidade(100)
                .build();

        Estoque estoqueAtualizado = Estoque.builder()
                .produtoId(produtoId)
                .quantidade(80)
                .build();

        when(repository.findById(produtoId)).thenReturn(Optional.of(estoqueExistente));
        when(repository.save(any(Estoque.class))).thenReturn(estoqueAtualizado);

        Estoque resultado = useCase.executar(produtoId, quantidadeReserva);

        assertNotNull(resultado);
        assertEquals(produtoId, resultado.getProdutoId());
        assertEquals(80, resultado.getQuantidade());
        verify(repository).findById(produtoId);
        verify(repository).save(estoqueExistente);
        assertEquals(80, estoqueExistente.getQuantidade());
    }

    @Test
    void deveReservarTodoEstoqueDisponivel() {
        Long produtoId = 1L;
        int quantidadeReserva = 100;
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

        Estoque resultado = useCase.executar(produtoId, quantidadeReserva);

        assertNotNull(resultado);
        assertEquals(produtoId, resultado.getProdutoId());
        assertEquals(0, resultado.getQuantidade());
        verify(repository).findById(produtoId);
        verify(repository).save(estoqueExistente);
        assertEquals(0, estoqueExistente.getQuantidade());
    }

    @Test
    void deveLancarExcecaoQuandoEstoqueInsuficiente() {
        Long produtoId = 1L;
        int quantidadeReserva = 150;
        Estoque estoqueExistente = Estoque.builder()
                .produtoId(produtoId)
                .quantidade(100)
                .build();

        when(repository.findById(produtoId)).thenReturn(Optional.of(estoqueExistente));

        InsufficientStockException exception = assertThrows(
                InsufficientStockException.class,
                () -> useCase.executar(produtoId, quantidadeReserva)
        );

        assertEquals("Estoque insuficiente para produto 1", exception.getMessage());
        verify(repository).findById(produtoId);
        verify(repository, never()).save(any(Estoque.class));
    }

    @Test
    void deveLancarExcecaoQuandoEstoqueZeroETentaReservar() {
        Long produtoId = 1L;
        int quantidadeReserva = 1;
        Estoque estoqueExistente = Estoque.builder()
                .produtoId(produtoId)
                .quantidade(0)
                .build();

        when(repository.findById(produtoId)).thenReturn(Optional.of(estoqueExistente));

        InsufficientStockException exception = assertThrows(
                InsufficientStockException.class,
                () -> useCase.executar(produtoId, quantidadeReserva)
        );

        assertEquals("Estoque insuficiente para produto 1", exception.getMessage());
        verify(repository).findById(produtoId);
        verify(repository, never()).save(any(Estoque.class));
    }

    @Test
    void deveLancarExcecaoQuandoEstoqueNaoExiste() {
        Long produtoId = 1L;
        int quantidadeReserva = 10;

        when(repository.findById(produtoId)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.executar(produtoId, quantidadeReserva)
        );

        assertEquals("Estoque para produto 1 não existe", exception.getMessage());
        verify(repository).findById(produtoId);
        verify(repository, never()).save(any(Estoque.class));
    }
}
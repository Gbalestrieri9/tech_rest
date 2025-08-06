package com.techchallenge.estoque.usecase;

import com.techchallenge.estoque.domain.model.Estoque;
import com.techchallenge.estoque.domain.repository.EstoqueRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListarEstoqueUseCaseTest {

    @Mock
    private EstoqueRepository repository;

    private ListarEstoqueUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new ListarEstoqueUseCase(repository);
    }

    @Test
    void deveListarTodosEstoquesComSucesso() {
        List<Estoque> estoquesEsperados = Arrays.asList(
                Estoque.builder().produtoId(1L).quantidade(100).build(),
                Estoque.builder().produtoId(2L).quantidade(50).build(),
                Estoque.builder().produtoId(3L).quantidade(0).build()
        );

        when(repository.findAll()).thenReturn(estoquesEsperados);

        List<Estoque> resultado = useCase.todos();

        assertNotNull(resultado);
        assertEquals(3, resultado.size());
        assertEquals(1L, resultado.get(0).getProdutoId());
        assertEquals(100, resultado.get(0).getQuantidade());
        assertEquals(2L, resultado.get(1).getProdutoId());
        assertEquals(50, resultado.get(1).getQuantidade());
        assertEquals(3L, resultado.get(2).getProdutoId());
        assertEquals(0, resultado.get(2).getQuantidade());
        verify(repository).findAll();
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoHouverEstoques() {
        when(repository.findAll()).thenReturn(Collections.emptyList());

        List<Estoque> resultado = useCase.todos();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(repository).findAll();
    }

    @Test
    void deveListarUmUnicoEstoque() {
        List<Estoque> estoquesEsperados = Arrays.asList(
                Estoque.builder().produtoId(1L).quantidade(75).build()
        );

        when(repository.findAll()).thenReturn(estoquesEsperados);

        List<Estoque> resultado = useCase.todos();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(1L, resultado.get(0).getProdutoId());
        assertEquals(75, resultado.get(0).getQuantidade());
        verify(repository).findAll();
    }
}
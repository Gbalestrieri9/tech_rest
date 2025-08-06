package com.techchallenge.produto.usecase;

import com.techchallenge.produto.domain.model.Produto;
import com.techchallenge.produto.domain.model.repository.ProdutoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListarProdutosUseCaseTest {

    @Mock
    private ProdutoRepository repository;

    @InjectMocks
    private ListarProdutosUseCase useCase;

    @Test
    void todos_DeveRetornarListaDeProdutos() {
        Produto produto1 = Produto.builder()
                .id(1L)
                .nome("Produto 1")
                .preco(new BigDecimal("10.00"))
                .sku("SKU1")
                .build();

        Produto produto2 = Produto.builder()
                .id(2L)
                .nome("Produto 2")
                .preco(new BigDecimal("20.00"))
                .sku("SKU2")
                .build();

        List<Produto> produtos = Arrays.asList(produto1, produto2);
        when(repository.findAll()).thenReturn(produtos);

        List<Produto> resultado = useCase.todos();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Produto 1", resultado.get(0).getNome());
        assertEquals("Produto 2", resultado.get(1).getNome());
        verify(repository).findAll();
    }

    @Test
    void todos_DeveRetornarListaVazia() {
        when(repository.findAll()).thenReturn(List.of());

        List<Produto> resultado = useCase.todos();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(repository).findAll();
    }
}
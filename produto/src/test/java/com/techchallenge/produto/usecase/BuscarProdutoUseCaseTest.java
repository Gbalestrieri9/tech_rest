package com.techchallenge.produto.usecase;

import com.techchallenge.produto.domain.model.Produto;
import com.techchallenge.produto.domain.model.repository.ProdutoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BuscarProdutoUseCaseTest {

    @Mock
    private ProdutoRepository repository;

    @InjectMocks
    private BuscarProdutoUseCase useCase;

    private Produto produto;

    @BeforeEach
    void setUp() {
        produto = Produto.builder()
                .id(1L)
                .nome("Produto Teste")
                .descricao("Descrição do produto")
                .preco(new BigDecimal("99.99"))
                .categoria("Categoria1")
                .ativo(true)
                .sku("SKU123")
                .build();
    }

    @Test
    void porId_DeveRetornarProdutoQuandoEncontrado() {
        when(repository.findById(1L)).thenReturn(Optional.of(produto));

        Produto resultado = useCase.porId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Produto Teste", resultado.getNome());
        verify(repository).findById(1L);
    }

    @Test
    void porId_DeveLancarExcecaoQuandoNaoEncontrado() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.porId(1L)
        );

        assertEquals("Produto id 1 não encontrado", exception.getMessage());
        verify(repository).findById(1L);
    }

    @Test
    void porSku_DeveRetornarProdutoQuandoEncontrado() {
        when(repository.findBySku("SKU123")).thenReturn(Optional.of(produto));

        Produto resultado = useCase.porSku("SKU123");

        assertNotNull(resultado);
        assertEquals("SKU123", resultado.getSku());
        assertEquals("Produto Teste", resultado.getNome());
        verify(repository).findBySku("SKU123");
    }

    @Test
    void porSku_DeveLancarExcecaoQuandoNaoEncontrado() {
        when(repository.findBySku("SKU123")).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.porSku("SKU123")
        );

        assertEquals("Produto sku SKU123 não encontrado", exception.getMessage());
        verify(repository).findBySku("SKU123");
    }
}
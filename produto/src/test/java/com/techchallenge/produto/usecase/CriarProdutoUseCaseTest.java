package com.techchallenge.produto.usecase;

import com.techchallenge.produto.controller.dto.ProdutoRequest;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CriarProdutoUseCaseTest {

    @Mock
    private ProdutoRepository repository;

    @InjectMocks
    private CriarProdutoUseCase useCase;

    private ProdutoRequest request;
    private Produto produto;

    @BeforeEach
    void setUp() {
        request = new ProdutoRequest(
                "Produto Teste",
                "Descrição do produto",
                new BigDecimal("99.99"),
                "Categoria1",
                true,
                "SKU123"
        );

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
    void executar_DeveCriarProdutoComSucesso() {
        when(repository.existsBySku("SKU123")).thenReturn(false);
        when(repository.save(any(Produto.class))).thenReturn(produto);

        Produto resultado = useCase.executar(request);

        assertNotNull(resultado);
        assertEquals("Produto Teste", resultado.getNome());
        assertEquals("SKU123", resultado.getSku());
        verify(repository).existsBySku("SKU123");
        verify(repository).save(any(Produto.class));
    }

    @Test
    void executar_DeveCriarProdutoSemSku() {
        ProdutoRequest requestSemSku = new ProdutoRequest(
                "Produto Teste",
                "Descrição",
                new BigDecimal("50.00"),
                "Categoria1",
                true,
                null
        );

        when(repository.save(any(Produto.class))).thenReturn(produto);

        Produto resultado = useCase.executar(requestSemSku);

        assertNotNull(resultado);
        verify(repository, never()).existsBySku(any());
        verify(repository).save(any(Produto.class));
    }

    @Test
    void executar_DeveCriarProdutoComAtivoNull() {
        ProdutoRequest requestAtivoNull = new ProdutoRequest(
                "Produto Teste",
                "Descrição",
                new BigDecimal("50.00"),
                "Categoria1",
                null,
                "SKU456"
        );

        when(repository.existsBySku("SKU456")).thenReturn(false);
        when(repository.save(any(Produto.class))).thenReturn(produto);

        Produto resultado = useCase.executar(requestAtivoNull);

        assertNotNull(resultado);
        verify(repository).save(any(Produto.class));
    }

    @Test
    void executar_DeveLancarExcecaoQuandoSkuJaExiste() {
        when(repository.existsBySku("SKU123")).thenReturn(true);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.executar(request)
        );

        assertEquals("SKU já existente", exception.getMessage());
        verify(repository).existsBySku("SKU123");
        verify(repository, never()).save(any(Produto.class));
    }
}
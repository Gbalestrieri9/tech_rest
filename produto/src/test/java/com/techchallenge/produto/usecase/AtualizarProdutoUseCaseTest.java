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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AtualizarProdutoUseCaseTest {

    @Mock
    private ProdutoRepository repository;

    @InjectMocks
    private AtualizarProdutoUseCase useCase;

    private Produto produto;
    private ProdutoRequest request;

    @BeforeEach
    void setUp() {
        produto = Produto.builder()
                .id(1L)
                .nome("Produto Original")
                .descricao("Descrição Original")
                .preco(new BigDecimal("50.00"))
                .categoria("Categoria Original")
                .ativo(true)
                .sku("SKU_ORIGINAL")
                .build();

        request = new ProdutoRequest(
                "Produto Atualizado",
                "Descrição Atualizada",
                new BigDecimal("75.00"),
                "Categoria Atualizada",
                false,
                "SKU_NOVO"
        );
    }

    @Test
    void executar_DeveAtualizarProdutoComSucesso() {
        when(repository.findById(1L)).thenReturn(Optional.of(produto));
        when(repository.existsBySku("SKU_NOVO")).thenReturn(false);
        when(repository.save(any(Produto.class))).thenReturn(produto);

        Produto resultado = useCase.executar(1L, request);

        assertNotNull(resultado);
        assertEquals("Produto Atualizado", produto.getNome());
        assertEquals("Descrição Atualizada", produto.getDescricao());
        assertEquals(new BigDecimal("75.00"), produto.getPreco());
        assertEquals("Categoria Atualizada", produto.getCategoria());
        assertEquals(false, produto.getAtivo());
        assertEquals("SKU_NOVO", produto.getSku());

        verify(repository).findById(1L);
        verify(repository).existsBySku("SKU_NOVO");
        verify(repository).save(produto);
    }

    @Test
    void executar_DeveAtualizarSemAlterarSku() {
        ProdutoRequest requestMesmoSku = new ProdutoRequest(
                "Produto Atualizado",
                "Descrição Atualizada",
                new BigDecimal("75.00"),
                "Categoria Atualizada",
                true,
                "SKU_ORIGINAL"
        );

        when(repository.findById(1L)).thenReturn(Optional.of(produto));
        when(repository.save(any(Produto.class))).thenReturn(produto);

        Produto resultado = useCase.executar(1L, requestMesmoSku);

        assertNotNull(resultado);
        verify(repository).findById(1L);
        verify(repository, never()).existsBySku(any());
        verify(repository).save(produto);
    }

    @Test
    void executar_DeveAtualizarComAtivoNull() {
        ProdutoRequest requestAtivoNull = new ProdutoRequest(
                "Produto Atualizado",
                "Descrição Atualizada",
                new BigDecimal("75.00"),
                "Categoria Atualizada",
                null,
                "SKU_ORIGINAL"
        );

        when(repository.findById(1L)).thenReturn(Optional.of(produto));
        when(repository.save(any(Produto.class))).thenReturn(produto);

        Produto resultado = useCase.executar(1L, requestAtivoNull);

        assertNotNull(resultado);
        assertEquals(true, produto.getAtivo());
        verify(repository).save(produto);
    }

    @Test
    void executar_DeveAtualizarComSkuNull() {
        ProdutoRequest requestSkuNull = new ProdutoRequest(
                "Produto Atualizado",
                "Descrição Atualizada",
                new BigDecimal("75.00"),
                "Categoria Atualizada",
                true,
                null
        );

        when(repository.findById(1L)).thenReturn(Optional.of(produto));
        when(repository.save(any(Produto.class))).thenReturn(produto);

        Produto resultado = useCase.executar(1L, requestSkuNull);

        assertNotNull(resultado);
        assertEquals("SKU_ORIGINAL", produto.getSku());
        verify(repository, never()).existsBySku(any());
        verify(repository).save(produto);
    }

    @Test
    void executar_DeveLancarExcecaoQuandoProdutoNaoEncontrado() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.executar(1L, request)
        );

        assertEquals("Produto id 1 não encontrado", exception.getMessage());
        verify(repository).findById(1L);
        verify(repository, never()).save(any());
    }

    @Test
    void executar_DeveLancarExcecaoQuandoNovoSkuJaExiste() {
        when(repository.findById(1L)).thenReturn(Optional.of(produto));
        when(repository.existsBySku("SKU_NOVO")).thenReturn(true);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.executar(1L, request)
        );

        assertEquals("SKU já existente", exception.getMessage());
        verify(repository).findById(1L);
        verify(repository).existsBySku("SKU_NOVO");
        verify(repository, never()).save(any());
    }
}
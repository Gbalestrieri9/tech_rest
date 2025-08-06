package com.techchallenge.produto.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techchallenge.produto.controller.dto.ProdutoRequest;
import com.techchallenge.produto.domain.model.Produto;
import com.techchallenge.produto.usecase.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProdutoController.class)
class ProdutoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CriarProdutoUseCase criarProdutoUseCase;

    @MockBean
    private AtualizarProdutoUseCase atualizarProdutoUseCase;

    @MockBean
    private BuscarProdutoUseCase buscarProdutoUseCase;

    @MockBean
    private ListarProdutosUseCase listarProdutosUseCase;

    @MockBean
    private RemoverProdutoUseCase removerProdutoUseCase;

    private Produto produto;
    private ProdutoRequest produtoRequest;

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

        produtoRequest = new ProdutoRequest(
                "Produto Teste",
                "Descrição do produto",
                new BigDecimal("99.99"),
                "Categoria1",
                true,
                "SKU123"
        );
    }

    @Test
    void criar_DeveRetornarStatusCreatedComProdutoCriado() throws Exception {
        when(criarProdutoUseCase.executar(any(ProdutoRequest.class))).thenReturn(produto);

        mockMvc.perform(post("/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(produtoRequest)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/produtos/1"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Produto Teste"))
                .andExpect(jsonPath("$.preco").value(99.99))
                .andExpect(jsonPath("$.sku").value("SKU123"));
    }

    @Test
    void criar_DeveRetornarBadRequestQuandoNomeVazio() throws Exception {
        ProdutoRequest requestInvalido = new ProdutoRequest(
                "",
                "Descrição",
                new BigDecimal("50.00"),
                "Categoria",
                true,
                "SKU123"
        );

        mockMvc.perform(post("/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestInvalido)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void criar_DeveRetornarBadRequestQuandoPrecoNulo() throws Exception {
        ProdutoRequest requestInvalido = new ProdutoRequest(
                "Produto",
                "Descrição",
                null,
                "Categoria",
                true,
                "SKU123"
        );

        mockMvc.perform(post("/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestInvalido)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void listar_DeveRetornarListaDeProdutos() throws Exception {
        Produto produto2 = Produto.builder()
                .id(2L)
                .nome("Produto 2")
                .preco(new BigDecimal("50.00"))
                .sku("SKU456")
                .build();

        List<Produto> produtos = Arrays.asList(produto, produto2);
        when(listarProdutosUseCase.todos()).thenReturn(produtos);

        mockMvc.perform(get("/produtos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nome").value("Produto Teste"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].nome").value("Produto 2"));
    }

    @Test
    void buscarPorId_DeveRetornarProduto() throws Exception {
        when(buscarProdutoUseCase.porId(1L)).thenReturn(produto);

        mockMvc.perform(get("/produtos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Produto Teste"))
                .andExpect(jsonPath("$.sku").value("SKU123"));
    }

    @Test
    void buscarPorSku_DeveRetornarProduto() throws Exception {
        when(buscarProdutoUseCase.porSku("SKU123")).thenReturn(produto);

        mockMvc.perform(get("/produtos/sku/SKU123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Produto Teste"))
                .andExpect(jsonPath("$.sku").value("SKU123"));
    }

    @Test
    void atualizar_DeveRetornarProdutoAtualizado() throws Exception {
        Produto produtoAtualizado = Produto.builder()
                .id(1L)
                .nome("Produto Atualizado")
                .descricao("Nova descrição")
                .preco(new BigDecimal("120.00"))
                .categoria("Nova Categoria")
                .ativo(true)
                .sku("SKU123")
                .build();

        when(atualizarProdutoUseCase.executar(eq(1L), any(ProdutoRequest.class)))
                .thenReturn(produtoAtualizado);

        ProdutoRequest requestAtualizado = new ProdutoRequest(
                "Produto Atualizado",
                "Nova descrição",
                new BigDecimal("120.00"),
                "Nova Categoria",
                true,
                "SKU123"
        );

        mockMvc.perform(put("/produtos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestAtualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Produto Atualizado"))
                .andExpect(jsonPath("$.descricao").value("Nova descrição"))
                .andExpect(jsonPath("$.preco").value(120.00));
    }

    @Test
    void remover_DeveRetornarNoContent() throws Exception {
        mockMvc.perform(delete("/produtos/1"))
                .andExpect(status().isNoContent());
    }
}
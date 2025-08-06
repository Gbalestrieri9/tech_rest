package com.techchallenge.produto.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techchallenge.produto.controller.dto.ProdutoRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class ProdutoControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void fluxoCompleto_criarListarBuscarAtualizarRemoverProduto() throws Exception {
        ProdutoRequest request = new ProdutoRequest(
                "Camiseta",
                "Camiseta de algodão",
                new BigDecimal("59.90"),
                "Roupas",
                true,
                "CAMISETA-001"
        );

        String responseBody = mockMvc.perform(post("/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.nome").value("Camiseta"))
                .andExpect(jsonPath("$.preco").value(59.90))
                .andExpect(jsonPath("$.sku").value("CAMISETA-001"))
                .andReturn().getResponse().getContentAsString();

        Long produtoId = objectMapper.readTree(responseBody).get("id").asLong();

        mockMvc.perform(get("/produtos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[?(@.nome == 'Camiseta')]").exists());

        mockMvc.perform(get("/produtos/" + produtoId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Camiseta"))
                .andExpect(jsonPath("$.sku").value("CAMISETA-001"));

        mockMvc.perform(get("/produtos/sku/CAMISETA-001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Camiseta"));

        ProdutoRequest requestAtualizado = new ProdutoRequest(
                "Camiseta Premium",
                "Camiseta de algodão premium",
                new BigDecimal("79.90"),
                "Roupas Premium",
                true,
                "CAMISETA-001"
        );

        mockMvc.perform(put("/produtos/" + produtoId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestAtualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Camiseta Premium"))
                .andExpect(jsonPath("$.preco").value(79.90));

        mockMvc.perform(delete("/produtos/" + produtoId))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/produtos/" + produtoId))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornar400ParaDadosInvalidos() throws Exception {
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
    void deveRetornar400ParaPrecoNegativo() throws Exception {
        ProdutoRequest requestInvalido = new ProdutoRequest(
                "Produto",
                "Descrição",
                new BigDecimal("-10.00"),
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
    void deveRetornar400AoBuscarProdutoInexistente() throws Exception {
        mockMvc.perform(get("/produtos/99999"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornar400AoBuscarSkuInexistente() throws Exception {
        mockMvc.perform(get("/produtos/sku/SKU_INEXISTENTE"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornar400AoAtualizarProdutoInexistente() throws Exception {
        ProdutoRequest request = new ProdutoRequest(
                "Produto",
                "Descrição",
                new BigDecimal("50.00"),
                "Categoria",
                true,
                "SKU123"
        );

        mockMvc.perform(put("/produtos/99999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornar400AoRemoverProdutoInexistente() throws Exception {
        mockMvc.perform(delete("/produtos/99999"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveTratarSkuDuplicado() throws Exception {
        ProdutoRequest request1 = new ProdutoRequest(
                "Produto 1",
                "Descrição 1",
                new BigDecimal("50.00"),
                "Categoria",
                true,
                "SKU_DUPLICADO"
        );

        mockMvc.perform(post("/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request1)))
                .andExpect(status().isCreated());

        ProdutoRequest request2 = new ProdutoRequest(
                "Produto 2",
                "Descrição 2",
                new BigDecimal("60.00"),
                "Categoria",
                true,
                "SKU_DUPLICADO"
        );

        mockMvc.perform(post("/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request2)))
                .andExpect(status().isBadRequest());
    }
}
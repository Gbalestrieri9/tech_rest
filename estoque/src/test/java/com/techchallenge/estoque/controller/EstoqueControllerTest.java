package com.techchallenge.estoque.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techchallenge.estoque.controller.dto.EstoqueRequest;
import com.techchallenge.estoque.controller.dto.ReservaRequest;
import com.techchallenge.estoque.domain.model.Estoque;
import com.techchallenge.estoque.exception.InsufficientStockException;
import com.techchallenge.estoque.usecase.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EstoqueController.class)
class EstoqueControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CriarEstoqueUseCase criarEstoqueUseCase;

    @MockBean
    private ListarEstoqueUseCase listarEstoqueUseCase;

    @MockBean
    private AtualizarEstoqueUseCase atualizarEstoqueUseCase;

    @MockBean
    private ReservarEstoqueUseCase reservarEstoqueUseCase;

    @Test
    void deveCriarEstoqueComSucesso() throws Exception {
        EstoqueRequest request = new EstoqueRequest(1L, 100);
        Estoque estoque = Estoque.builder()
                .produtoId(1L)
                .quantidade(100)
                .build();

        when(criarEstoqueUseCase.executar(1L, 100)).thenReturn(estoque);

        mockMvc.perform(post("/estoque")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/estoque/1"))
                .andExpect(jsonPath("$.produtoId").value(1L))
                .andExpect(jsonPath("$.quantidade").value(100));
    }

    @Test
    void deveRetornarBadRequestQuandoProdutoIdForNulo() throws Exception {
        EstoqueRequest request = new EstoqueRequest(null, 100);

        mockMvc.perform(post("/estoque")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornarBadRequestQuandoQuantidadeForNegativa() throws Exception {
        EstoqueRequest request = new EstoqueRequest(1L, -1);

        mockMvc.perform(post("/estoque")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveListarEstoquesComSucesso() throws Exception {
        List<Estoque> estoques = Arrays.asList(
                Estoque.builder().produtoId(1L).quantidade(100).build(),
                Estoque.builder().produtoId(2L).quantidade(50).build()
        );

        when(listarEstoqueUseCase.todos()).thenReturn(estoques);

        mockMvc.perform(get("/estoque"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].produtoId").value(1L))
                .andExpect(jsonPath("$[0].quantidade").value(100))
                .andExpect(jsonPath("$[1].produtoId").value(2L))
                .andExpect(jsonPath("$[1].quantidade").value(50));
    }

    @Test
    void deveBuscarEstoquePorIdComSucesso() throws Exception {
        Estoque estoque = Estoque.builder()
                .produtoId(1L)
                .quantidade(100)
                .build();

        when(criarEstoqueUseCase.buscar(1L)).thenReturn(estoque);

        mockMvc.perform(get("/estoque/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.produtoId").value(1L))
                .andExpect(jsonPath("$.quantidade").value(100));
    }

    @Test
    void deveAtualizarEstoqueComSucesso() throws Exception {
        EstoqueRequest request = new EstoqueRequest(1L, 200);
        Estoque estoque = Estoque.builder()
                .produtoId(1L)
                .quantidade(200)
                .build();

        when(atualizarEstoqueUseCase.executar(1L, 200)).thenReturn(estoque);

        mockMvc.perform(put("/estoque/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.produtoId").value(1L))
                .andExpect(jsonPath("$.quantidade").value(200));
    }

    @Test
    void deveReservarEstoqueComSucesso() throws Exception {
        ReservaRequest request = new ReservaRequest(10);
        Estoque estoque = Estoque.builder()
                .produtoId(1L)
                .quantidade(90)
                .build();

        when(reservarEstoqueUseCase.executar(1L, 10)).thenReturn(estoque);

        mockMvc.perform(post("/estoque/1/reservar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.produtoId").value(1L))
                .andExpect(jsonPath("$.quantidade").value(90));
    }

    @Test
    void deveRetornarBadRequestQuandoQuantidadeReservaForZero() throws Exception {
        ReservaRequest request = new ReservaRequest(0);

        mockMvc.perform(post("/estoque/1/reservar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornarBadRequestQuandoQuantidadeReservaNula() throws Exception {
        ReservaRequest request = new ReservaRequest(null);

        mockMvc.perform(post("/estoque/1/reservar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
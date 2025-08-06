package com.techchallenge.pedido.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techchallenge.pedido.controller.dto.*;
import com.techchallenge.pedido.domain.model.*;
import com.techchallenge.pedido.usecase.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PedidoController.class)
class PedidoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CriarPedidoUseCase criarPedidoUseCase;

    @MockBean
    private ListarPedidosUseCase listarPedidosUseCase;

    @MockBean
    private BuscarPedidoUseCase buscarPedidoUseCase;

    @Autowired
    private ObjectMapper objectMapper;

    private Pedido pedido;
    private PedidoRequest pedidoRequest;
    private PedidoItem pedidoItem;

    @BeforeEach
    void setUp() {
        pedidoItem = PedidoItem.builder()
                .id(1L)
                .produtoId(1L)
                .quantidade(2)
                .precoUnitario(new BigDecimal("10.00"))
                .build();

        pedido = Pedido.builder()
                .id(1L)
                .clienteId(1L)
                .total(new BigDecimal("20.00"))
                .dataCriacao(Instant.parse("2024-01-01T10:00:00Z"))
                .itens(List.of(pedidoItem))
                .build();

        pedidoItem.setPedido(pedido);

        pedidoRequest = new PedidoRequest(1L, List.of(
                new ItemPedidoRequest(1L, 2)
        ));
    }

    @Test
    void criar_DeveRetornarPedidoCriado() throws Exception {
        when(criarPedidoUseCase.executar(any(PedidoRequest.class))).thenReturn(pedido);

        mockMvc.perform(post("/pedidos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pedidoRequest)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/pedidos/1"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.clienteId").value(1))
                .andExpect(jsonPath("$.total").value(20.00))
                .andExpect(jsonPath("$.itens[0].produtoId").value(1))
                .andExpect(jsonPath("$.itens[0].quantidade").value(2));

        verify(criarPedidoUseCase).executar(any(PedidoRequest.class));
    }

    @Test
    void criar_DeveRetornarErroQuandoRequestInvalido() throws Exception {
        PedidoRequest requestInvalido = new PedidoRequest(null, null);

        mockMvc.perform(post("/pedidos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestInvalido)))
                .andExpect(status().isBadRequest());

        verify(criarPedidoUseCase, never()).executar(any());
    }

    @Test
    void listar_DeveRetornarListaDePedidos() throws Exception {
        List<Pedido> pedidos = Arrays.asList(pedido);
        when(listarPedidosUseCase.todos()).thenReturn(pedidos);

        mockMvc.perform(get("/pedidos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].clienteId").value(1))
                .andExpect(jsonPath("$[0].total").value(20.00));

        verify(listarPedidosUseCase).todos();
    }

    @Test
    void buscar_DeveRetornarPedidoPorId() throws Exception {
        when(buscarPedidoUseCase.porId(1L)).thenReturn(pedido);

        mockMvc.perform(get("/pedidos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.clienteId").value(1))
                .andExpect(jsonPath("$.total").value(20.00));

        verify(buscarPedidoUseCase).porId(1L);
    }
}
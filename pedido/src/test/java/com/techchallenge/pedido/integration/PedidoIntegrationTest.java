package com.techchallenge.pedido.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techchallenge.pedido.controller.dto.*;
import com.techchallenge.pedido.domain.model.Pedido;
import com.techchallenge.pedido.domain.repository.PedidoRepository;
import com.techchallenge.pedido.usecase.CriarPedidoUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureWebMvc
@ActiveProfiles("test")
class PedidoIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RestTemplate restTemplate;

    @MockBean
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private CriarPedidoUseCase criarPedidoUseCase;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        pedidoRepository.deleteAll();

        try {
            Field rtField = CriarPedidoUseCase.class.getDeclaredField("rt");
            rtField.setAccessible(true);
            rtField.set(criarPedidoUseCase, restTemplate);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao injetar RestTemplate mockado", e);
        }
    }

    @Test
    void criarPedido_FluxoCompletoComSucesso() throws Exception {
        PedidoRequest request = new PedidoRequest(1L, List.of(
                new ItemPedidoRequest(1L, 2)
        ));

        ProdutoResponse produtoResponse = new ProdutoResponse(
                1L, "Produto Teste", "Descrição", new BigDecimal("50.00"),
                "Categoria", true, "SKU123"
        );

        when(restTemplate.getForObject(anyString(), eq(ProdutoResponse.class), Optional.ofNullable(any())))
                .thenReturn(produtoResponse);
        when(restTemplate.postForEntity(anyString(), any(), eq(Void.class), Optional.ofNullable(any())))
                .thenReturn(null);

        mockMvc.perform(post("/pedidos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.clienteId").value(1))
                .andExpect(jsonPath("$.total").value(100.00))
                .andExpect(jsonPath("$.itens[0].produtoId").value(1))
                .andExpect(jsonPath("$.itens[0].quantidade").value(2))
                .andExpect(jsonPath("$.id").exists());

        List<Pedido> pedidos = pedidoRepository.findAll();
        assertEquals(1, pedidos.size());
        assertEquals(new BigDecimal("100.00"), pedidos.get(0).getTotal());

        verify(rabbitTemplate).convertAndSend(anyString(), anyString(), Optional.ofNullable(any()));

        verify(restTemplate).getForObject(anyString(), eq(ProdutoResponse.class), Optional.ofNullable(any()));
        verify(restTemplate).postForEntity(anyString(), any(), eq(Void.class), Optional.ofNullable(any()));
    }

    @Test
    void listarPedidos_DeveRetornarPedidosExistentes() throws Exception {
        PedidoRequest request = new PedidoRequest(1L, List.of(
                new ItemPedidoRequest(1L, 1)
        ));

        ProdutoResponse produtoResponse = new ProdutoResponse(
                1L, "Produto", "Desc", new BigDecimal("25.00"),
                "Cat", true, "SKU"
        );

        when(restTemplate.getForObject(anyString(), eq(ProdutoResponse.class), Optional.ofNullable(any())))
                .thenReturn(produtoResponse);
        when(restTemplate.postForEntity(anyString(), any(), eq(Void.class), Optional.ofNullable(any())))
                .thenReturn(null);

        mockMvc.perform(post("/pedidos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/pedidos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].clienteId").value(1))
                .andExpect(jsonPath("$[0].total").value(25.00));
    }

    @Test
    void buscarPedido_DeveRetornarPedidoExistente() throws Exception {
        PedidoRequest request = new PedidoRequest(1L, List.of(
                new ItemPedidoRequest(1L, 1)
        ));

        ProdutoResponse produtoResponse = new ProdutoResponse(
                1L, "Produto", "Desc", new BigDecimal("30.00"),
                "Cat", true, "SKU"
        );

        when(restTemplate.getForObject(anyString(), eq(ProdutoResponse.class), Optional.ofNullable(any())))
                .thenReturn(produtoResponse);
        when(restTemplate.postForEntity(anyString(), any(), eq(Void.class), Optional.ofNullable(any())))
                .thenReturn(null);

        mockMvc.perform(post("/pedidos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        List<Pedido> pedidos = pedidoRepository.findAll();
        assertEquals(1, pedidos.size());
        Long pedidoId = pedidos.get(0).getId();
        assertNotNull(pedidoId);

        mockMvc.perform(get("/pedidos/" + pedidoId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(pedidoId))
                .andExpect(jsonPath("$.clienteId").value(1))
                .andExpect(jsonPath("$.total").value(30.00))
                .andExpect(jsonPath("$.itens").isArray())
                .andExpect(jsonPath("$.itens[0].produtoId").value(1))
                .andExpect(jsonPath("$.itens[0].quantidade").value(1))
                .andExpect(jsonPath("$.itens[0].precoUnitario").value(30.00));
    }

    @Test
    void criarPedido_DeveRetornarErroQuandoValidacaoFalha() throws Exception {
        PedidoRequest requestInvalido = new PedidoRequest(null, List.of(
                new ItemPedidoRequest(1L, 0)
        ));

        mockMvc.perform(post("/pedidos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestInvalido)))
                .andExpect(status().isBadRequest());

        assertEquals(0, pedidoRepository.count());
    }
}
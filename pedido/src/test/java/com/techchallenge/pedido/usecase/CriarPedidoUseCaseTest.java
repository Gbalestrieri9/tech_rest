package com.techchallenge.pedido.usecase;

import com.techchallenge.pedido.controller.dto.*;
import com.techchallenge.pedido.domain.model.*;
import com.techchallenge.pedido.domain.repository.PedidoRepository;
import com.techchallenge.pedido.event.PedidoCreatedEvent;
import com.techchallenge.pedido.exception.FalhaIntegracaoException;
import com.techchallenge.pedido.exception.RecursoNaoEncontradoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CriarPedidoUseCaseTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private CriarPedidoUseCase criarPedidoUseCase;

    @Mock
    private RestTemplate restTemplate;

    private PedidoRequest pedidoRequest;
    private ProdutoResponse produtoResponse;
    private Pedido pedidoSalvo;

    @BeforeEach
    void setUp() {
        try {
            var field = CriarPedidoUseCase.class.getDeclaredField("rt");
            field.setAccessible(true);
            field.set(criarPedidoUseCase, restTemplate);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        pedidoRequest = new PedidoRequest(1L, List.of(
                new ItemPedidoRequest(1L, 2)
        ));

        produtoResponse = new ProdutoResponse(1L, "Produto Teste", "Descrição",
                new BigDecimal("10.00"), "Categoria", true, "SKU123");

        pedidoSalvo = Pedido.builder()
                .id(1L)
                .clienteId(1L)
                .total(new BigDecimal("20.00"))
                .dataCriacao(Instant.now())
                .build();
    }

    @Test
    void executar_DevecriarPedidoComSucesso() {
        when(restTemplate.getForObject(anyString(), eq(ProdutoResponse.class), Optional.ofNullable(any())))
                .thenReturn(produtoResponse);

        when(restTemplate.postForEntity(anyString(), any(), eq(Void.class), Optional.ofNullable(any())))
                .thenReturn(null);

        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedidoSalvo);

        Pedido resultado = criarPedidoUseCase.executar(pedidoRequest);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verify(pedidoRepository).save(any(Pedido.class));
        verify(rabbitTemplate).convertAndSend(anyString(), anyString(), any(PedidoCreatedEvent.class));
    }

    @Test
    void executar_DeveLancarExcecaoQuandoProdutoNaoEncontrado() {
        when(restTemplate.getForObject(anyString(), eq(ProdutoResponse.class), any(Long.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        FalhaIntegracaoException exception = assertThrows(
                FalhaIntegracaoException.class,
                () -> criarPedidoUseCase.executar(pedidoRequest)
        );

        assertTrue(exception.getMessage().contains("Erro ao chamar produto-service"));
        verify(pedidoRepository, never()).save(any());
    }

    @Test
    void executar_DeveLancarExcecaoQuandoFalhaIntegracaoProduto() {
        when(restTemplate.getForObject(anyString(), eq(ProdutoResponse.class), Optional.ofNullable(any())))
                .thenThrow(new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR));

        FalhaIntegracaoException exception = assertThrows(
                FalhaIntegracaoException.class,
                () -> criarPedidoUseCase.executar(pedidoRequest)
        );

        assertTrue(exception.getMessage().contains("Erro ao chamar produto-service"));
        verify(pedidoRepository, never()).save(any());
    }

    @Test
    void executar_DeveLancarExcecaoQuandoEstoqueInsuficiente() {
        when(restTemplate.getForObject(anyString(), eq(ProdutoResponse.class), any(Long.class)))
                .thenReturn(produtoResponse);

        when(restTemplate.postForEntity(anyString(), any(), eq(Void.class), any(Long.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

        FalhaIntegracaoException exception = assertThrows(
                FalhaIntegracaoException.class,
                () -> criarPedidoUseCase.executar(pedidoRequest)
        );

        assertTrue(exception.getMessage().contains("Erro ao chamar estoque-service"));
        verify(pedidoRepository, never()).save(any());
    }

    @Test
    void executar_DeveLancarExcecaoQuandoFalhaIntegracaoEstoque() {
        when(restTemplate.getForObject(anyString(), eq(ProdutoResponse.class), Optional.ofNullable(any())))
                .thenReturn(produtoResponse);

        when(restTemplate.postForEntity(anyString(), any(), eq(Void.class), Optional.ofNullable(any())))
                .thenThrow(new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR));

        FalhaIntegracaoException exception = assertThrows(
                FalhaIntegracaoException.class,
                () -> criarPedidoUseCase.executar(pedidoRequest)
        );

        assertTrue(exception.getMessage().contains("Erro ao chamar estoque-service"));
        verify(pedidoRepository, never()).save(any());
    }

    @Test
    void executar_DeveCalcularTotalCorretamente() {
        PedidoRequest requestMultiplosItens = new PedidoRequest(1L, List.of(
                new ItemPedidoRequest(1L, 2),
                new ItemPedidoRequest(2L, 3)
        ));

        ProdutoResponse produto2 = new ProdutoResponse(2L, "Produto 2", "Desc",
                new BigDecimal("15.00"), "Cat", true, "SKU456");

        reset(restTemplate);

        when(restTemplate.getForObject(anyString(), eq(ProdutoResponse.class), any(Long.class)))
                .thenAnswer(invocation -> {
                    Long produtoId = invocation.getArgument(2);
                    if (produtoId.equals(1L)) {
                        return produtoResponse;
                    } else if (produtoId.equals(2L)) {
                        return produto2;
                    }
                    return produtoResponse;
                });

        when(restTemplate.postForEntity(anyString(), any(), eq(Void.class), any(Long.class)))
                .thenReturn(null);

        ArgumentCaptor<Pedido> pedidoCaptor = ArgumentCaptor.forClass(Pedido.class);
        when(pedidoRepository.save(pedidoCaptor.capture())).thenReturn(pedidoSalvo);

        criarPedidoUseCase.executar(requestMultiplosItens);

        Pedido pedidoCapturado = pedidoCaptor.getValue();
        assertEquals(new BigDecimal("65.00"), pedidoCapturado.getTotal());
        assertEquals(2, pedidoCapturado.getItens().size());
    }
}
package com.techchallenge.pedido.usecase;

import com.techchallenge.pedido.domain.model.Pedido;
import com.techchallenge.pedido.domain.repository.PedidoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListarPedidosUseCaseTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @InjectMocks
    private ListarPedidosUseCase listarPedidosUseCase;

    private List<Pedido> pedidos;

    @BeforeEach
    void setUp() {
        Pedido pedido1 = Pedido.builder()
                .id(1L)
                .clienteId(1L)
                .total(new BigDecimal("100.00"))
                .dataCriacao(Instant.now())
                .build();

        Pedido pedido2 = Pedido.builder()
                .id(2L)
                .clienteId(2L)
                .total(new BigDecimal("200.00"))
                .dataCriacao(Instant.now())
                .build();

        pedidos = Arrays.asList(pedido1, pedido2);
    }

    @Test
    void todos_DeveRetornarListaDePedidos() {
        when(pedidoRepository.findAll()).thenReturn(pedidos);

        List<Pedido> resultado = listarPedidosUseCase.todos();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals(1L, resultado.get(0).getId());
        assertEquals(2L, resultado.get(1).getId());
        verify(pedidoRepository).findAll();
    }

    @Test
    void todos_DeveRetornarListaVaziaQuandoNaoHaPedidos() {
        when(pedidoRepository.findAll()).thenReturn(List.of());

        List<Pedido> resultado = listarPedidosUseCase.todos();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(pedidoRepository).findAll();
    }
}
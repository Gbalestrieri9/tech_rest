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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BuscarPedidoUseCaseTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @InjectMocks
    private BuscarPedidoUseCase buscarPedidoUseCase;

    private Pedido pedido;

    @BeforeEach
    void setUp() {
        pedido = Pedido.builder()
                .id(1L)
                .clienteId(1L)
                .total(new BigDecimal("100.00"))
                .dataCriacao(Instant.now())
                .build();
    }

    @Test
    void porId_DeveRetornarPedidoQuandoExiste() {
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));

        Pedido resultado = buscarPedidoUseCase.porId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals(1L, resultado.getClienteId());
        verify(pedidoRepository).findById(1L);
    }

    @Test
    void porId_DeveLancarExcecaoQuandoPedidoNaoExiste() {
        when(pedidoRepository.findById(1L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> buscarPedidoUseCase.porId(1L)
        );

        assertEquals("Pedido id 1 não encontrado", exception.getMessage());
        verify(pedidoRepository).findById(1L);
    }
}
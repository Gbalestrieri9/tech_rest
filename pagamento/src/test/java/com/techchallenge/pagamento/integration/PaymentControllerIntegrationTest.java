package com.techchallenge.pagamento.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techchallenge.pagamento.domain.model.Payment;
import com.techchallenge.pagamento.domain.repository.PaymentRepository;
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
import org.springframework.web.context.WebApplicationContext;

import java.time.Instant;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebMvc
@ActiveProfiles("test")
class PaymentControllerIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private PaymentRepository paymentRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void getPaymentStatus_WhenPaymentExists_ShouldReturnOk() throws Exception {
        Long pedidoId = 1L;
        Payment payment = Payment.builder()
                .id(1L)
                .pedidoId(pedidoId)
                .status(Payment.Status.APPROVED)
                .processedAt(Instant.parse("2023-01-01T00:00:00Z"))
                .build();

        when(paymentRepository.findByPedidoId(pedidoId)).thenReturn(Optional.of(payment));

        mockMvc.perform(get("/pagamentos/{pedidoId}", pedidoId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.pedidoId").value(1))
                .andExpect(jsonPath("$.status").value("APPROVED"))
                .andExpect(jsonPath("$.processedAt").value("2023-01-01T00:00:00Z"));
    }

    @Test
    void getPaymentStatus_WhenPaymentNotFound_ShouldReturnNotFound() throws Exception {
        Long pedidoId = 999L;
        when(paymentRepository.findByPedidoId(pedidoId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/pagamentos/{pedidoId}", pedidoId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Pagamento para pedido " + pedidoId + " não encontrado"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void getPaymentStatus_WithRejectedPayment_ShouldReturnRejectedStatus() throws Exception {
        Long pedidoId = 2L;
        Payment payment = Payment.builder()
                .id(2L)
                .pedidoId(pedidoId)
                .status(Payment.Status.REJECTED)
                .processedAt(Instant.parse("2023-01-01T12:00:00Z"))
                .build();

        when(paymentRepository.findByPedidoId(pedidoId)).thenReturn(Optional.of(payment));

        mockMvc.perform(get("/pagamentos/{pedidoId}", pedidoId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.pedidoId").value(2))
                .andExpect(jsonPath("$.status").value("REJECTED"))
                .andExpect(jsonPath("$.processedAt").value("2023-01-01T12:00:00Z"));
    }
}
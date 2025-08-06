package com.techchallenge.estoque.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techchallenge.estoque.controller.dto.EstoqueRequest;
import com.techchallenge.estoque.controller.dto.ReservaRequest;
import com.techchallenge.estoque.domain.model.Estoque;
import com.techchallenge.estoque.domain.repository.EstoqueRepository;
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

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureWebMvc
class EstoqueIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EstoqueRepository estoqueRepository;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void deveExecutarFluxoCompletoDeEstoque() throws Exception {
        EstoqueRequest criarRequest = new EstoqueRequest(1L, 100);
        Estoque estoqueNovo = Estoque.builder().produtoId(1L).quantidade(100).build();

        when(estoqueRepository.existsById(1L)).thenReturn(false);
        when(estoqueRepository.save(any(Estoque.class))).thenReturn(estoqueNovo);

        mockMvc.perform(post("/estoque")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(criarRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.produtoId").value(1L))
                .andExpect(jsonPath("$.quantidade").value(100));

        when(estoqueRepository.findAll()).thenReturn(Arrays.asList(estoqueNovo));

        mockMvc.perform(get("/estoque"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].produtoId").value(1L))
                .andExpect(jsonPath("$[0].quantidade").value(100));

        when(estoqueRepository.findById(1L)).thenReturn(Optional.of(estoqueNovo));

        mockMvc.perform(get("/estoque/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.produtoId").value(1L))
                .andExpect(jsonPath("$.quantidade").value(100));

        EstoqueRequest atualizarRequest = new EstoqueRequest(1L, 150);
        Estoque estoqueAtualizado = Estoque.builder().produtoId(1L).quantidade(150).build();

        when(estoqueRepository.save(any(Estoque.class))).thenReturn(estoqueAtualizado);

        mockMvc.perform(put("/estoque/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(atualizarRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.produtoId").value(1L))
                .andExpect(jsonPath("$.quantidade").value(150));

        ReservaRequest reservaRequest = new ReservaRequest(20);
        Estoque estoqueReservado = Estoque.builder().produtoId(1L).quantidade(130).build();

        Estoque estoqueParaReserva = Estoque.builder().produtoId(1L).quantidade(150).build();
        when(estoqueRepository.findById(1L)).thenReturn(Optional.of(estoqueParaReserva));
        when(estoqueRepository.save(any(Estoque.class))).thenReturn(estoqueReservado);

        mockMvc.perform(post("/estoque/1/reservar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reservaRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.produtoId").value(1L))
                .andExpect(jsonPath("$.quantidade").value(130));
    }

    @Test
    void deveRetornarErroQuandoTentarCriarEstoqueDuplicado() throws Exception {
        EstoqueRequest request = new EstoqueRequest(1L, 100);
        when(estoqueRepository.existsById(1L)).thenReturn(true);

        mockMvc.perform(post("/estoque")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornarErroQuandoTentarBuscarEstoqueInexistente() throws Exception {
        when(estoqueRepository.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/estoque/99"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornarErroQuandoTentarReservarMaisQueDisponivel() throws Exception {
        ReservaRequest request = new ReservaRequest(200);
        Estoque estoque = Estoque.builder().produtoId(1L).quantidade(100).build();

        when(estoqueRepository.findById(1L)).thenReturn(Optional.of(estoque));

        mockMvc.perform(post("/estoque/1/reservar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }
}
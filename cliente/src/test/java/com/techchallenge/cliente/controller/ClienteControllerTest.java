package com.techchallenge.cliente.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techchallenge.cliente.controller.dto.ClienteRequest;
import com.techchallenge.cliente.controller.dto.EnderecoRequest;
import com.techchallenge.cliente.domain.entity.Cliente;
import com.techchallenge.cliente.domain.entity.Endereco;
import com.techchallenge.cliente.exception.GlobalExceptionHandler;
import com.techchallenge.cliente.exception.ClienteNaoEncontradoException;
import com.techchallenge.cliente.usecase.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClienteController.class)
@Import(GlobalExceptionHandler.class)
@ActiveProfiles("test")
class ClienteControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean CriarClienteUseCase criarUC;
    @MockBean BuscarClienteUseCase buscarUC;
    @MockBean ListarClientesUseCase listarUC;
    @MockBean AtualizarClienteUseCase atualizarUC;
    @MockBean RemoverClienteUseCase removerUC;

    private Cliente domain(Long id) {
        Endereco e = Endereco.builder()
                .logradouro("Rua A")
                .numero("100")
                .complemento("Apto 1")
                .cep("12345000")
                .cidade("Cidade")
                .uf("SP")
                .build();
        return Cliente.builder()
                .id(id)
                .nome("Maria")
                .cpf("12345678909")
                .dataNascimento(LocalDate.of(1990,1,1))
                .enderecos(List.of(e))
                .build();
    }

    private ClienteRequest request() {
        EnderecoRequest er = new EnderecoRequest(
                "Rua A","100","Apto 1","12345000","Cidade","SP"
        );
        return new ClienteRequest(
                "Maria","12345678909",
                LocalDate.of(1990,1,1),
                List.of(er)
        );
    }

    @Test
    @DisplayName("POST /clientes → 201 e body correto")
    void POST_deveCriarCliente_comSucesso() throws Exception {
        given(criarUC.execute(any())).willReturn(domain(42L));

        mvc.perform(post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(42))
                .andExpect(jsonPath("$.nome").value("Maria"))
                .andExpect(jsonPath("$.cpf").value("12345678909"));

        ArgumentCaptor<Cliente> cap = ArgumentCaptor.forClass(Cliente.class);
        then(criarUC).should().execute(cap.capture());
        assertThat(cap.getValue().getId()).isNull();
        assertThat(cap.getValue().getNome()).isEqualTo("Maria");
    }

    @Test
    @DisplayName("GET /clientes/{id} → 200 e body correto")
    void GET_porId_deveRetornarCliente() throws Exception {
        given(buscarUC.porId(1L)).willReturn(domain(1L));

        mvc.perform(get("/clientes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Maria"));
    }

    @Test
    @DisplayName("GET /clientes/{id} inexistente → 404")
    void GET_porId_naoEncontrado_deveRetornar404() throws Exception {
        given(buscarUC.porId(99L))
                .willThrow(new ClienteNaoEncontradoException(99L));

        mvc.perform(get("/clientes/99")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error")
                        .value("Cliente id 99 não encontrado"));
    }

    @Test
    @DisplayName("GET /clientes → 200 e lista de clientes")
    void GET_listar_deveRetornarLista() throws Exception {
        given(listarUC.todos()).willReturn(List.of(domain(1L), domain(2L)));

        mvc.perform(get("/clientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));
    }

    @Test
    @DisplayName("PUT /clientes/{id} → 200 e body atualizado")
    void PUT_deveAtualizarCliente_comSucesso() throws Exception {
        given(atualizarUC.atualizar(eq(5L), any()))
                .willReturn(domain(5L));

        mvc.perform(put("/clientes/5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.cpf").value("12345678909"));

        ArgumentCaptor<Cliente> cap = ArgumentCaptor.forClass(Cliente.class);
        then(atualizarUC).should().atualizar(eq(5L), cap.capture());
        assertThat(cap.getValue().getNome()).isEqualTo("Maria");
    }

    @Test
    @DisplayName("DELETE /clientes/{id} → 204")
    void DELETE_deveRemoverCliente_comSucesso() throws Exception {
        mvc.perform(delete("/clientes/7"))
                .andExpect(status().isNoContent());

        then(removerUC).should().remover(7L);
    }
}

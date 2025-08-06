package com.techchallenge.cliente.controller;

import com.techchallenge.cliente.ClienteApplication;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ClienteControllerIT {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16")
            .withDatabaseName("cliente_db")
            .withUsername("cliente")
            .withPassword("cliente");

    @DynamicPropertySource
    static void props(DynamicPropertyRegistry r){
        r.add("spring.datasource.url", postgres::getJdbcUrl);
        r.add("spring.datasource.username", postgres::getUsername);
        r.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    MockMvc mockMvc;

    @Test
    void fluxoBasicoCriarBuscarListar() throws Exception {
        String body = """
        {
          "nome":"Fulano",
          "cpf":"390.533.447-05",
          "dataNascimento":"1990-01-01",
          "enderecos":[
            {
              "logradouro":"Rua A",
              "numero":"10",
              "complemento":"Ap",
              "cep":"01001000",
              "cidade":"São Paulo",
              "uf":"SP"
            }
          ]
        }
        """;

        String location = mockMvc.perform(post("/clientes")
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.cpf").value("39053344705"))
                .andReturn().getResponse().getHeader("Location");

        mockMvc.perform(get("/clientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Fulano"));

    }

    @Test
    void deveRetornar422ParaCpfDuplicado() throws Exception {
        String body = """
        {
          "nome":"Fulano",
          "cpf":"390.533.447-05",
          "dataNascimento":"1990-01-01",
          "enderecos":[{"logradouro":"Rua","numero":"1","complemento":null,"cep":"01001000","cidade":"SP","uf":"SP"}]
        }
        """;
        mockMvc.perform(post("/clientes").contentType(String.valueOf(MediaType.APPLICATION_JSON)).content(body))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/clientes").contentType(String.valueOf(MediaType.APPLICATION_JSON)).content(body))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    void deveRetornar422ParaCpfInvalido() throws Exception {
        String body = """
        {
          "nome":"Fulano",
          "cpf":"111.111.111-11",
          "dataNascimento":"1990-01-01",
          "enderecos":[{"logradouro":"Rua","numero":"1","complemento":null,"cep":"01001000","cidade":"SP","uf":"SP"}]
        }
        """;
        mockMvc.perform(post("/clientes").contentType(String.valueOf(MediaType.APPLICATION_JSON)).content(body))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.error").value("CPF inválido: 111.111.111-11"));
    }

    @Test
    void deveRetornar404QuandoNaoEncontrado() throws Exception {
        mockMvc.perform(get("/clientes/99999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").exists());
    }


}

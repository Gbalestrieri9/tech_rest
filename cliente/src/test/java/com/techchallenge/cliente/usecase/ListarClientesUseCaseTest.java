package com.techchallenge.cliente.usecase;

import com.techchallenge.cliente.domain.entity.Cliente;
import com.techchallenge.cliente.domain.repository.ClienteRepositoryPort;
import com.techchallenge.cliente.util.ClienteFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
class ListarClientesUseCaseTest {

    @Mock
    ClienteRepositoryPort repo;
    @InjectMocks
    ListarClientesUseCase useCase;

    @Test
    void deveListar(){
        List<Cliente> lista = List.of(
                ClienteFactory.clienteValidoSemId().withId(1L),
                ClienteFactory.clienteValidoSemId().withId(2L)
        );
        when(repo.findAll()).thenReturn(lista);

        List<Cliente> result = useCase.todos();

        assertEquals(2, result.size());
    }
}

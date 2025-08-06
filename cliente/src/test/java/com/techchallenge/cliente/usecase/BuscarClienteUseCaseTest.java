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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
class BuscarClienteUseCaseTest {

    @Mock
    ClienteRepositoryPort repo;
    @InjectMocks
    BuscarClienteUseCase useCase;

    @Test
    void deveBuscarPorId(){
        Cliente cliente = ClienteFactory.clienteValidoSemId().withId(1L);
        when(repo.findById(1L)).thenReturn(cliente);

        Cliente retorno = useCase.porId(1L);

        assertEquals(1L, retorno.getId());
    }
}

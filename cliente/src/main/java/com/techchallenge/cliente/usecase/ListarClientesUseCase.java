package com.techchallenge.cliente.usecase;

import com.techchallenge.cliente.domain.entity.Cliente;
import com.techchallenge.cliente.domain.repository.ClienteRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ListarClientesUseCase {
    private final ClienteRepositoryPort repo;
    public java.util.List<Cliente> todos(){ return repo.findAll(); }
}

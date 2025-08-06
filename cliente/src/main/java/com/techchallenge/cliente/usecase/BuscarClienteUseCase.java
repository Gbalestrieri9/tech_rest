package com.techchallenge.cliente.usecase;

import com.techchallenge.cliente.domain.entity.Cliente;
import com.techchallenge.cliente.domain.repository.ClienteRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BuscarClienteUseCase {
    private final ClienteRepositoryPort repo;
    public Cliente porId(Long id){ return repo.findById(id); }
}

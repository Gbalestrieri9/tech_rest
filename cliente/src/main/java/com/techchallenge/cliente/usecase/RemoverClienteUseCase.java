package com.techchallenge.cliente.usecase;

import com.techchallenge.cliente.domain.repository.ClienteRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RemoverClienteUseCase {
    private final ClienteRepositoryPort repo;
    public void remover(Long id){ repo.delete(id); }
}

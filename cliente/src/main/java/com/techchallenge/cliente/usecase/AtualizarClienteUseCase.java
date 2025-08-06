package com.techchallenge.cliente.usecase;

import com.techchallenge.cliente.domain.entity.Cliente;
import com.techchallenge.cliente.domain.repository.ClienteRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AtualizarClienteUseCase {
    private final ClienteRepositoryPort repo;
    public Cliente atualizar(Long id, Cliente dados){
        Cliente existente = repo.findById(id);

        Cliente atualizado = Cliente.builder()
                .id(id)
                .nome(dados.getNome())
                .cpf(existente.getCpf())
                .dataNascimento(dados.getDataNascimento())
                .enderecos(dados.getEnderecos())
                .build();
        return repo.update(atualizado);
    }
}

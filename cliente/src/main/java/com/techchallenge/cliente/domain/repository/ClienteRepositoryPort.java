package com.techchallenge.cliente.domain.repository;

import com.techchallenge.cliente.domain.entity.Cliente;
import java.util.List;

public interface ClienteRepositoryPort {
    boolean existsByCpf(String cpf);
    Cliente save(Cliente cliente);
    Cliente findById(Long id);
    List<Cliente> findAll();
    Cliente update(Cliente cliente);
    void delete(Long id);
}

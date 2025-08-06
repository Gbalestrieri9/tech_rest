package com.techchallenge.cliente.gateway.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteJpaRepository extends JpaRepository<ClienteJpaEntity, Long> {
    boolean existsByCpf(String cpf);
}
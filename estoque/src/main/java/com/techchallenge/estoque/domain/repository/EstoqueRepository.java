package com.techchallenge.estoque.domain.repository;

import com.techchallenge.estoque.domain.model.Estoque;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstoqueRepository extends JpaRepository<Estoque, Long> {
}

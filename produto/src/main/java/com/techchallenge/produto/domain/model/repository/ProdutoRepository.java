package com.techchallenge.produto.domain.model.repository;

import com.techchallenge.produto.domain.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    Optional<Produto> findBySku(String sku);
    boolean existsBySku(String sku);
}

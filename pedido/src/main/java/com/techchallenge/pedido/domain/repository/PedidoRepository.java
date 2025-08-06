package com.techchallenge.pedido.domain.repository;

import com.techchallenge.pedido.domain.model.Pedido;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    @Override
    @EntityGraph(attributePaths = {"itens"})
    List<Pedido> findAll();
}

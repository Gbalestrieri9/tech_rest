package com.techchallenge.pedido.domain.repository;

import com.techchallenge.pedido.domain.model.PedidoItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoItemRepository extends JpaRepository<PedidoItem,Long> {}

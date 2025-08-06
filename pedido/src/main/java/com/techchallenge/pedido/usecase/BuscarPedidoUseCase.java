package com.techchallenge.pedido.usecase;

import com.techchallenge.pedido.domain.model.Pedido;
import com.techchallenge.pedido.domain.repository.PedidoRepository;
import org.springframework.stereotype.Service;

@Service
public class BuscarPedidoUseCase {
    private final PedidoRepository repo;
    public BuscarPedidoUseCase(PedidoRepository repo){this.repo=repo;}
    public Pedido porId(Long id){
        return repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pedido id "+id+" não encontrado"));
    }
}

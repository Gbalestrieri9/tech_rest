package com.techchallenge.pedido.usecase;
import com.techchallenge.pedido.domain.model.Pedido;
import com.techchallenge.pedido.domain.repository.PedidoRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ListarPedidosUseCase {
    private final PedidoRepository repo;
    public ListarPedidosUseCase(PedidoRepository repo){this.repo=repo;}
    public List<Pedido> todos(){return repo.findAll();}
}

package com.techchallenge.pedido.controller;

import com.techchallenge.pedido.controller.dto.*;
import com.techchallenge.pedido.domain.model.Pedido;
import com.techchallenge.pedido.usecase.*;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import static com.techchallenge.pedido.controller.dto.PedidoMapper.*;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    private final CriarPedidoUseCase criar;
    private final ListarPedidosUseCase listar;
    private final BuscarPedidoUseCase buscar;

    public PedidoController(CriarPedidoUseCase criar,
                            ListarPedidosUseCase listar,
                            BuscarPedidoUseCase buscar) {
        this.criar = criar;
        this.listar = listar;
        this.buscar = buscar;
    }

    @PostMapping
    public ResponseEntity<PedidoResponse> criar(@RequestBody @Valid PedidoRequest req) {
        var p = criar.executar(req);
        return ResponseEntity.created(URI.create("/pedidos/" + p.getId()))
                .body(toResponse(p));
    }

    @GetMapping
    public List<PedidoResponse> listar() {
        return listar.todos().stream().map(PedidoMapper::toResponse).toList();
    }

    @GetMapping("/{id}")
    public PedidoResponse buscar(@PathVariable Long id) {
        return toResponse(buscar.porId(id));
    }
}

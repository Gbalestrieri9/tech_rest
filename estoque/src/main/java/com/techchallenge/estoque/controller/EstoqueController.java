package com.techchallenge.estoque.controller;

import com.techchallenge.estoque.controller.dto.*;
import com.techchallenge.estoque.domain.model.Estoque;
import com.techchallenge.estoque.usecase.*;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/estoque")
public class EstoqueController {

    private final CriarEstoqueUseCase criar;
    private final ListarEstoqueUseCase listar;
    private final AtualizarEstoqueUseCase atualizar;
    private final ReservarEstoqueUseCase reservar;

    public EstoqueController(CriarEstoqueUseCase criar,
                             ListarEstoqueUseCase listar,
                             AtualizarEstoqueUseCase atualizar,
                             ReservarEstoqueUseCase reservar) {
        this.criar = criar;
        this.listar = listar;
        this.atualizar = atualizar;
        this.reservar = reservar;
    }

    @PostMapping
    public ResponseEntity<EstoqueResponse> criar(@RequestBody @Valid EstoqueRequest req) {
        Estoque e = criar.executar(req.produtoId(), req.quantidade());
        return ResponseEntity.created(URI.create("/estoque/" + e.getProdutoId()))
                .body(new EstoqueResponse(e.getProdutoId(), e.getQuantidade()));
    }

    @GetMapping
    public List<EstoqueResponse> listar() {
        return listar.todos().stream()
                .map(e -> new EstoqueResponse(e.getProdutoId(), e.getQuantidade()))
                .toList();
    }

    @GetMapping("/{produtoId}")
    public EstoqueResponse buscar(@PathVariable Long produtoId) {
        var e = criar.buscar(produtoId);
        return new EstoqueResponse(e.getProdutoId(), e.getQuantidade());
    }

    @PutMapping("/{produtoId}")
    public EstoqueResponse atualizar(@PathVariable Long produtoId,
                                     @RequestBody @Valid EstoqueRequest req) {
        var e = atualizar.executar(produtoId, req.quantidade());
        return new EstoqueResponse(e.getProdutoId(), e.getQuantidade());
    }

    @PostMapping("/{produtoId}/reservar")
    public EstoqueResponse reservar(@PathVariable Long produtoId,
                                    @RequestBody @Valid ReservaRequest req) {
        var e = reservar.executar(produtoId, req.quantidade());
        return new EstoqueResponse(e.getProdutoId(), e.getQuantidade());
    }
}

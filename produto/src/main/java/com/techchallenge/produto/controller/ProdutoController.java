package com.techchallenge.produto.controller;

import com.techchallenge.produto.controller.dto.*;
import com.techchallenge.produto.usecase.*;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

import static com.techchallenge.produto.controller.dto.ProdutoMapper.toResponse;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    private final CriarProdutoUseCase criar;
    private final AtualizarProdutoUseCase atualizar;
    private final BuscarProdutoUseCase buscar;
    private final ListarProdutosUseCase listar;
    private final RemoverProdutoUseCase remover;

    public ProdutoController(CriarProdutoUseCase criar,
                             AtualizarProdutoUseCase atualizar,
                             BuscarProdutoUseCase buscar,
                             ListarProdutosUseCase listar,
                             RemoverProdutoUseCase remover) {
        this.criar = criar;
        this.atualizar = atualizar;
        this.buscar = buscar;
        this.listar = listar;
        this.remover = remover;
    }

    @PostMapping
    public ResponseEntity<ProdutoResponse> criar(@RequestBody @Valid ProdutoRequest req) {
        var prod = criar.executar(req);
        return ResponseEntity
                .created(URI.create("/produtos/" + prod.getId()))
                .body(toResponse(prod));
    }

    @GetMapping
    public List<ProdutoResponse> listar() {
        return listar.todos().stream().map(ProdutoMapper::toResponse).toList();
    }

    @GetMapping("/{id}")
    public ProdutoResponse buscarPorId(@PathVariable Long id) {
        return toResponse(buscar.porId(id));
    }

    @GetMapping("/sku/{sku}")
    public ProdutoResponse buscarPorSku(@PathVariable String sku) {
        return toResponse(buscar.porSku(sku));
    }

    @PutMapping("/{id}")
    public ProdutoResponse atualizar(@PathVariable Long id, @RequestBody @Valid ProdutoRequest req) {
        return toResponse(atualizar.executar(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        remover.executar(id);
        return ResponseEntity.noContent().build();
    }
}

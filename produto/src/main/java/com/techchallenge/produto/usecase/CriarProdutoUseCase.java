package com.techchallenge.produto.usecase;

import com.techchallenge.produto.controller.dto.ProdutoRequest;
import com.techchallenge.produto.domain.model.Produto;
import com.techchallenge.produto.domain.model.repository.ProdutoRepository;
import org.springframework.stereotype.Service;

@Service
public class CriarProdutoUseCase {
    private final ProdutoRepository repo;

    public CriarProdutoUseCase(ProdutoRepository repo) { this.repo = repo; }

    public Produto executar(ProdutoRequest req) {
        if (req.sku() != null && repo.existsBySku(req.sku()))
            throw new IllegalArgumentException("SKU já existente");
        Produto p = Produto.builder()
                .nome(req.nome())
                .descricao(req.descricao())
                .preco(req.preco())
                .categoria(req.categoria())
                .ativo(req.ativo() == null ? true : req.ativo())
                .sku(req.sku())
                .build();
        return repo.save(p);
    }
}
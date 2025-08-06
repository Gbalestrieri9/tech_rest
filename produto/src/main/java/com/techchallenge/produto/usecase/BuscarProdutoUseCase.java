package com.techchallenge.produto.usecase;

import com.techchallenge.produto.domain.model.Produto;
import com.techchallenge.produto.domain.model.repository.ProdutoRepository;
import org.springframework.stereotype.Service;

@Service
public class BuscarProdutoUseCase {
    private final ProdutoRepository repo;
    public BuscarProdutoUseCase(ProdutoRepository repo) { this.repo = repo; }

    public Produto porId(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Produto id %d não encontrado".formatted(id)));
    }

    public Produto porSku(String sku) {
        return repo.findBySku(sku)
                .orElseThrow(() -> new IllegalArgumentException("Produto sku %s não encontrado".formatted(sku)));
    }
}

package com.techchallenge.produto.usecase;

import com.techchallenge.produto.controller.dto.ProdutoRequest;
import com.techchallenge.produto.domain.model.Produto;
import com.techchallenge.produto.domain.model.repository.ProdutoRepository;
import org.springframework.stereotype.Service;

@Service
public class AtualizarProdutoUseCase {
    private final ProdutoRepository repo;

    public AtualizarProdutoUseCase(ProdutoRepository repo) { this.repo = repo; }

    public Produto executar(Long id, ProdutoRequest req) {
        Produto p = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Produto id %d não encontrado".formatted(id)));

        p.setNome(req.nome());
        p.setDescricao(req.descricao());
        p.setPreco(req.preco());
        p.setCategoria(req.categoria());
        if (req.ativo() != null) p.setAtivo(req.ativo());
        if (req.sku() != null && !req.sku().equals(p.getSku())) {
            if (repo.existsBySku(req.sku())) throw new IllegalArgumentException("SKU já existente");
            p.setSku(req.sku());
        }
        return repo.save(p);
    }
}

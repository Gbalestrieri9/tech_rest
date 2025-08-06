package com.techchallenge.produto.usecase;

import com.techchallenge.produto.domain.model.repository.ProdutoRepository;
import org.springframework.stereotype.Service;

@Service
public class RemoverProdutoUseCase {
    private final ProdutoRepository repo;

    public RemoverProdutoUseCase(ProdutoRepository repo) { this.repo = repo; }

    public void executar(Long id) {
        if (!repo.existsById(id))
            throw new IllegalArgumentException("Produto id %d não encontrado".formatted(id));
        repo.deleteById(id);
    }
}
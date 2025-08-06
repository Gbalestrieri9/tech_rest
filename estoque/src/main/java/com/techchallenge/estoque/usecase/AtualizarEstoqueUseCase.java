package com.techchallenge.estoque.usecase;

import com.techchallenge.estoque.domain.model.Estoque;
import com.techchallenge.estoque.domain.repository.EstoqueRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AtualizarEstoqueUseCase {
    private final EstoqueRepository repo;

    public AtualizarEstoqueUseCase(EstoqueRepository repo) { this.repo = repo; }

    @Transactional
    public Estoque executar(Long produtoId, Integer quantidade) {
        Estoque e = repo.findById(produtoId)
                .orElseThrow(() -> new IllegalArgumentException("Estoque para produto " + produtoId + " não existe"));
        e.setQuantidade(quantidade == null ? 0 : quantidade);
        return repo.save(e);
    }
}

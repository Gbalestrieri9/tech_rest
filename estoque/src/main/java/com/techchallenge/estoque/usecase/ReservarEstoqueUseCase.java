package com.techchallenge.estoque.usecase;

import com.techchallenge.estoque.domain.model.Estoque;
import com.techchallenge.estoque.domain.repository.EstoqueRepository;
import com.techchallenge.estoque.exception.InsufficientStockException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReservarEstoqueUseCase {
    private final EstoqueRepository repo;

    public ReservarEstoqueUseCase(EstoqueRepository repo) { this.repo = repo; }

    @Transactional
    public Estoque executar(Long produtoId, int quantidade) {
        Estoque e = repo.findById(produtoId)
                .orElseThrow(() -> new IllegalArgumentException("Estoque para produto " + produtoId + " não existe"));
        if (e.getQuantidade() < quantidade) {
            throw new InsufficientStockException("Estoque insuficiente para produto " + produtoId);
        }
        e.setQuantidade(e.getQuantidade() - quantidade);
        return repo.save(e);
    }
}

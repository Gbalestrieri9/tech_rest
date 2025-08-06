package com.techchallenge.estoque.usecase;

import com.techchallenge.estoque.domain.model.Estoque;
import com.techchallenge.estoque.domain.repository.EstoqueRepository;
import org.springframework.stereotype.Service;

@Service
public class CriarEstoqueUseCase {
    private final EstoqueRepository repo;

    public CriarEstoqueUseCase(EstoqueRepository repo) { this.repo = repo; }

    public Estoque executar(Long produtoId, Integer quantidade) {
        if (repo.existsById(produtoId)) {
            throw new IllegalArgumentException("Estoque para produto " + produtoId + " já existe");
        }
        Estoque e = Estoque.builder()
                .produtoId(produtoId)
                .quantidade(quantidade == null ? 0 : quantidade)
                .build();
        return repo.save(e);
    }

    public Estoque buscar(Long produtoId) {
        return repo.findById(produtoId)
                .orElseThrow(() -> new IllegalArgumentException("Estoque para produto " + produtoId + " não existe"));
    }
}

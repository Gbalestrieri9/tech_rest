package com.techchallenge.estoque.usecase;

import com.techchallenge.estoque.domain.model.Estoque;
import com.techchallenge.estoque.domain.repository.EstoqueRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListarEstoqueUseCase {
    private final EstoqueRepository repo;

    public ListarEstoqueUseCase(EstoqueRepository repo) { this.repo = repo; }

    public List<Estoque> todos() {
        return repo.findAll();
    }
}

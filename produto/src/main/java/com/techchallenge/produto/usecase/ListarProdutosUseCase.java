package com.techchallenge.produto.usecase;

import com.techchallenge.produto.domain.model.Produto;
import com.techchallenge.produto.domain.model.repository.ProdutoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListarProdutosUseCase {
    private final ProdutoRepository repo;
    public ListarProdutosUseCase(ProdutoRepository repo) { this.repo = repo; }

    public List<Produto> todos() { return repo.findAll(); }
}
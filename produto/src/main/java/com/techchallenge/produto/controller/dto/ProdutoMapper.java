package com.techchallenge.produto.controller.dto;

import com.techchallenge.produto.domain.model.Produto;

public class ProdutoMapper {
    public static ProdutoResponse toResponse(Produto p) {
        return new ProdutoResponse(
                p.getId(), p.getNome(), p.getDescricao(),
                p.getPreco(), p.getCategoria(), p.getAtivo(), p.getSku()
        );
    }
}

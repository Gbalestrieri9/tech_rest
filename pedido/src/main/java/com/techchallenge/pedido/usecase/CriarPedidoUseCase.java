package com.techchallenge.pedido.usecase;

import com.techchallenge.pedido.controller.dto.*;
import com.techchallenge.pedido.domain.model.*;
import com.techchallenge.pedido.domain.repository.PedidoRepository;
import com.techchallenge.pedido.event.PedidoCreatedEvent;
import com.techchallenge.pedido.controller.dto.ProdutoResponse;

import java.util.Map;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import com.techchallenge.pedido.exception.RecursoNaoEncontradoException;
import com.techchallenge.pedido.exception.FalhaIntegracaoException;
import org.springframework.web.client.HttpClientErrorException;


import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import static com.techchallenge.pedido.config.RabbitConfig.EXCHANGE;
import static com.techchallenge.pedido.config.RabbitConfig.ROUTING_KEY;

@Service
public class CriarPedidoUseCase {
    private final PedidoRepository repo;
    private final RestTemplate rt;
    private final RabbitTemplate rabbit;

    public CriarPedidoUseCase(PedidoRepository repo,
                              RestTemplate restTemplate,
                              RabbitTemplate rabbit) {
        this.repo = repo;
        this.rt = restTemplate;
        this.rabbit = rabbit;
    }

    @Transactional
    public Pedido executar(PedidoRequest req) {
        List<PedidoItem> items = req.itens().stream().map(i -> {
            ProdutoResponse p;
            try {
                p = rt.getForObject(
                        "http://produto-service:8082/produtos/{id}",
                        ProdutoResponse.class,
                        i.produtoId()
                );
            } catch(HttpClientErrorException.NotFound e) {
                throw new RecursoNaoEncontradoException("Produto id " + i.produtoId() + " não encontrado");
            } catch(HttpClientErrorException e) {
                throw new FalhaIntegracaoException("Erro ao chamar produto-service: " + e.getStatusCode());
            }

            try {
                rt.postForEntity(
                        "http://estoque-service:8083/estoque/{id}/reservar",
                        Map.of("quantidade", i.quantidade()),
                        Void.class,
                        i.produtoId()
                );
            } catch (HttpClientErrorException.BadRequest e) {
                throw new RecursoNaoEncontradoException("Não foi possível reservar estoque para produto " + i.produtoId());
            } catch (HttpClientErrorException e) {
                throw new FalhaIntegracaoException("Erro ao chamar estoque-service: " + e.getStatusCode());
            }

            return PedidoItem.builder()
                    .produtoId(i.produtoId())
                    .quantidade(i.quantidade())
                    .precoUnitario(p.preco())  // aqui já encontra p
                    .build();
        }).toList();

        BigDecimal total = items.stream()
                .map(it -> it.getPrecoUnitario().multiply(BigDecimal.valueOf(it.getQuantidade())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Pedido pedido = Pedido.builder()
                .clienteId(req.clienteId())
                .total(total)
                .dataCriacao(Instant.now())
                .itens(items)
                .build();

        items.forEach(item -> item.setPedido(pedido));
        Pedido salvo = repo.save(pedido);

        rabbit.convertAndSend(EXCHANGE, ROUTING_KEY,
                new PedidoCreatedEvent(salvo.getId(), salvo.getClienteId(), salvo.getTotal()));

        return salvo;
    }
}

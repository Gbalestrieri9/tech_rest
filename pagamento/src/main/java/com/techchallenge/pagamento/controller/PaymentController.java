package com.techchallenge.pagamento.controller;

import com.techchallenge.pagamento.controller.dto.PaymentResponse;
import com.techchallenge.pagamento.domain.repository.PaymentRepository;
import com.techchallenge.pagamento.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pagamentos")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentRepository repo;

    @GetMapping("/{pedidoId}")
    public ResponseEntity<PaymentResponse> status(@PathVariable Long pedidoId) {
        return repo.findByPedidoId(pedidoId)
                .map(p -> new PaymentResponse(p.getId(), p.getPedidoId(), p.getStatus().name(), p.getProcessedAt()))
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Pagamento para pedido " + pedidoId + " não encontrado"));
    }
}

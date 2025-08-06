package com.techchallenge.cliente.controller;

import com.techchallenge.cliente.controller.dto.ClienteRequest;
import com.techchallenge.cliente.controller.dto.ClienteResponse;
import com.techchallenge.cliente.controller.dto.EnderecoResponse;
import com.techchallenge.cliente.domain.entity.Cliente;
import com.techchallenge.cliente.domain.entity.Endereco;
import com.techchallenge.cliente.usecase.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final CriarClienteUseCase criar;
    private final BuscarClienteUseCase buscar;
    private final ListarClientesUseCase listar;
    private final AtualizarClienteUseCase atualizar;
    private final RemoverClienteUseCase remover;

    @PostMapping
    public ResponseEntity<ClienteResponse> criar(@Valid @RequestBody ClienteRequest req){
        Cliente cliente = criar.execute(toDomain(null, req));
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(cliente));
    }

    @GetMapping("/{id}")
    public ClienteResponse buscar(@PathVariable Long id){
        return toResponse(buscar.porId(id));
    }

    @GetMapping
    public List<ClienteResponse> listar(){
        return listar.todos().stream().map(this::toResponse).toList();
    }

    @PutMapping("/{id}")
    public ClienteResponse atualizar(@PathVariable Long id, @Valid @RequestBody ClienteRequest req){
        Cliente updated = atualizar.atualizar(id, toDomain(id, req));
        return toResponse(updated);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long id){
        remover.remover(id);
    }

    private Cliente toDomain(Long id, ClienteRequest r){
        return Cliente.builder()
                .id(id)
                .nome(r.nome())
                .cpf(r.cpf())
                .dataNascimento(r.dataNascimento())
                .enderecos(r.enderecos().stream()
                        .map(e -> Endereco.builder()
                                .logradouro(e.logradouro())
                                .numero(e.numero())
                                .complemento(e.complemento())
                                .cep(e.cep())
                                .cidade(e.cidade())
                                .uf(e.uf())
                                .build())
                        .toList())
                .build();
    }

    private ClienteResponse toResponse(Cliente c){
        return new ClienteResponse(
                c.getId(), c.getNome(), c.getCpf(), c.getDataNascimento(),
                c.getEnderecos().stream()
                        .map(e -> new EnderecoResponse(e.getLogradouro(), e.getNumero(), e.getComplemento(),
                                e.getCep(), e.getCidade(), e.getUf()))
                        .toList()
        );
    }
}

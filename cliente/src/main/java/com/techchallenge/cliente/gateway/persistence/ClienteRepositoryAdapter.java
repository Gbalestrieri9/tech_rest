package com.techchallenge.cliente.gateway.persistence;

import com.techchallenge.cliente.domain.entity.Cliente;
import com.techchallenge.cliente.domain.entity.Endereco;
import com.techchallenge.cliente.domain.repository.ClienteRepositoryPort;
import com.techchallenge.cliente.exception.ClienteNaoEncontradoException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ClienteRepositoryAdapter implements ClienteRepositoryPort {

    private final ClienteJpaRepository jpa;

    @Override
    public boolean existsByCpf(String cpf) {
        return jpa.existsByCpf(cpf);
    }

    @Override
    public Cliente save(Cliente cliente) {
        ClienteJpaEntity entity = toJpa(cliente);
        entity.setId(null);
        return toDomain(jpa.save(entity));
    }

    @Override
    public Cliente findById(Long id) {
        return jpa.findById(id).map(this::toDomain)
                .orElseThrow(() -> new ClienteNaoEncontradoException(id));
    }

    @Override
    public List<Cliente> findAll() {
        return jpa.findAll().stream().map(this::toDomain).toList();
    }

    @Override
    public Cliente update(Cliente cliente) {
        if(!jpa.existsById(cliente.getId())){
            throw new ClienteNaoEncontradoException(cliente.getId());
        }
        return toDomain(jpa.save(toJpa(cliente)));
    }

    @Override
    public void delete(Long id) {
        if(!jpa.existsById(id)) throw new ClienteNaoEncontradoException(id);
        jpa.deleteById(id);
    }

    private ClienteJpaEntity toJpa(Cliente c){
        return ClienteJpaEntity.builder()
                .id(c.getId())
                .nome(c.getNome())
                .cpf(c.getCpf())
                .dataNascimento(c.getDataNascimento())
                .enderecos(c.getEnderecos().stream()
                        .map(e -> new EnderecoEmbeddable(
                                e.getLogradouro(), e.getNumero(), e.getComplemento(),
                                e.getCep(), e.getCidade(), e.getUf()))
                        .toList())
                .build();
    }

    private Cliente toDomain(ClienteJpaEntity e){
        return Cliente.builder()
                .id(e.getId())
                .nome(e.getNome())
                .cpf(e.getCpf())
                .dataNascimento(e.getDataNascimento())
                .enderecos(e.getEnderecos().stream()
                        .map(x -> Endereco.builder()
                                .logradouro(x.getLogradouro())
                                .numero(x.getNumero())
                                .complemento(x.getComplemento())
                                .cep(x.getCep())
                                .cidade(x.getCidade())
                                .uf(x.getUf())
                                .build())
                        .toList())
                .build();
    }
}

package com.techchallenge.cliente.usecase;

import com.techchallenge.cliente.domain.entity.Cliente;
import com.techchallenge.cliente.domain.repository.ClienteRepositoryPort;
import com.techchallenge.cliente.domain.validation.CpfValidatorUtil;
import com.techchallenge.cliente.exception.ClienteJaExisteException;
import com.techchallenge.cliente.exception.CpfInvalidoException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CriarClienteUseCase {

    private final ClienteRepositoryPort repo;

    public Cliente execute(Cliente cliente){
        String cpfNorm = CpfValidatorUtil.normalize(cliente.getCpf());
        if(!CpfValidatorUtil.isValid(cpfNorm)) throw new CpfInvalidoException(cliente.getCpf());
        if(repo.existsByCpf(cpfNorm)) throw new ClienteJaExisteException(cpfNorm);

        Cliente novo = Cliente.builder()
                .id(null)
                .nome(cliente.getNome())
                .cpf(cpfNorm)
                .dataNascimento(cliente.getDataNascimento())
                .enderecos(cliente.getEnderecos())
                .build();
        return repo.save(novo);
    }
}

package com.example.distAPI.service;

import com.example.distAPI.exception.RegraNegocioException;
import com.example.distAPI.model.entity.Endereco;
import com.example.distAPI.model.repository.EnderecoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class EnderecoService {

    private final EnderecoRepository repository;

    public EnderecoService(EnderecoRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Endereco salvar(Endereco endereco) {
        validar(endereco);
        return repository.save(endereco);
    }

    public Optional<Endereco> obterPorId(Long id) {
        return repository.findById(id);
    }

    public List<Endereco> listar() {
        return repository.findAll();
    }

    @Transactional
    public void excluir(Endereco endereco) {
        if (endereco == null || endereco.getId() == null) {
            throw new RegraNegocioException("Endereço não encontrado para exclusão.");
        }
        repository.delete(endereco);
    }

    public void validar(Endereco endereco) {
        if (endereco.getCep() == null || endereco.getCep().trim().isEmpty()) {
            throw new RegraNegocioException("O CEP é obrigatório.");
        }
        if (endereco.getLogradouro() == null || endereco.getLogradouro().trim().isEmpty()) {
            throw new RegraNegocioException("O logradouro é obrigatório.");
        }
        if (endereco.getNumero() == null || endereco.getNumero().trim().isEmpty()) {
            throw new RegraNegocioException("O número é obrigatório.");
        }
    }
}
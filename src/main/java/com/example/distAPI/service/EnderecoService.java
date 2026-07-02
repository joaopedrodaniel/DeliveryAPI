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
    private final EnderecoLookupService lookupService;

    public EnderecoService(EnderecoRepository repository, EnderecoLookupService lookupService) {
        this.repository = repository;
        this.lookupService = lookupService;
    }

    @Transactional
    public Endereco atualizar(Endereco endereco) {
        if (endereco.getId() == null) {
            throw new RegraNegocioException("Endereço não encontrado para atualização.");
        }
        prepararEndereco(endereco);
        return repository.save(endereco);
    }

    @Transactional
    public Endereco salvar(Endereco endereco) {
        prepararEndereco(endereco);
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
        endereco.setCep(validarCep(endereco));
        validarCamposObrigatorios(endereco);
    }

    private void prepararEndereco(Endereco endereco) {
        endereco.setCep(validarCep(endereco));
        lookupService.preencherPorCep(endereco.getCep(), endereco);
        validarCamposObrigatorios(endereco);
    }

    private String validarCep(Endereco endereco) {
        if (endereco == null) {
            throw new RegraNegocioException("Endereço é obrigatório.");
        }
        if (endereco.getCep() == null || endereco.getCep().trim().isEmpty()) {
            throw new RegraNegocioException("O CEP é obrigatório.");
        }

        String cep = endereco.getCep().replaceAll("\\D", "");
        if (cep.length() != 8) {
            throw new RegraNegocioException("O CEP deve conter 8 dígitos.");
        }

        return cep;
    }

    private void validarCamposObrigatorios(Endereco endereco) {
        if (endereco.getLogradouro() == null || endereco.getLogradouro().trim().isEmpty()) {
            throw new RegraNegocioException("O logradouro é obrigatório.");
        }
        if (endereco.getNumero() == null || endereco.getNumero().trim().isEmpty()) {
            throw new RegraNegocioException("O número é obrigatório.");
        }
    }
}

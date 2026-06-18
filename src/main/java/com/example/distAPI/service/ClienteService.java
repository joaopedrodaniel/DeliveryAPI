package com.example.distAPI.service;

import com.example.distAPI.exception.RegraNegocioException;
import com.example.distAPI.model.entity.Cliente;
import com.example.distAPI.model.repository.ClienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    private final ClienteRepository repository;

    public ClienteService(ClienteRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Cliente salvar(Cliente cliente) {
        validar(cliente);
        return repository.save(cliente);
    }

    public Optional<Cliente> obterPorId(Long id) {
        return repository.findById(id);
    }

    public List<Cliente> listar() {
        return repository.findAll();
    }

    @Transactional
    public void excluir(Cliente cliente) {
        if (cliente == null || cliente.getId() == null) {
            throw new RegraNegocioException("Cliente não encontrado para exclusão.");
        }
        repository.delete(cliente);
    }

    public void validar(Cliente cliente) {
        // Exemplo de validação, assumindo que as propriedades vêm da classe Usuario
        if (cliente.getNome() == null || cliente.getNome().trim().isEmpty()) {
            throw new RegraNegocioException("Informe um Nome válido.");
        }
        if (cliente.getEmail() == null || cliente.getEmail().trim().isEmpty()) {
            throw new RegraNegocioException("Informe um Email válido.");
        }
        if (cliente.getSenha() == null || cliente.getSenha().trim().isEmpty()) {
            throw new RegraNegocioException("A senha é obrigatória.");
        }
    }
}
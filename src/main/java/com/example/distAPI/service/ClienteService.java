package com.example.distAPI.service;

import com.example.distAPI.exception.RegraNegocioException;
import com.example.distAPI.model.entity.Cliente;
import com.example.distAPI.model.entity.PerfilUsuario;
import com.example.distAPI.model.repository.ClienteRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    private final ClienteRepository repository;
    private final PasswordEncoder passwordEncoder;

    public ClienteService(ClienteRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Cliente salvar(Cliente cliente) {
        validar(cliente);
        cliente.setSenha(passwordEncoder.encode(cliente.getSenha()));
        cliente.setPerfil(PerfilUsuario.CLIENTE);
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

        if (cliente.getNome() == null || cliente.getNome().trim().isEmpty()) {
            throw new RegraNegocioException("Informe um Nome válido.");
        }

        if (cliente.getEmail() == null || cliente.getEmail().trim().isEmpty()) {
            throw new RegraNegocioException("Informe um Email válido.");
        }

        repository.findByEmail(cliente.getEmail())
                .ifPresent(c -> {
                    if (!c.getId().equals(cliente.getId())) {
                        throw new RegraNegocioException("Já existe um cliente cadastrado com este e-mail.");
                    }
                });

        if (cliente.getSenha() == null || cliente.getSenha().trim().isEmpty()) {
            throw new RegraNegocioException("A senha é obrigatória.");
        }
    }
}
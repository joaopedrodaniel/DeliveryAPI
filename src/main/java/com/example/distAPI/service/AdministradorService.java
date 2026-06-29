package com.example.distAPI.service;

import com.example.distAPI.exception.RegraNegocioException;
import com.example.distAPI.model.entity.Administrador;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.example.distAPI.model.repository.AdministradorRepository;
import org.springframework.stereotype.Service;
import com.example.distAPI.model.entity.PerfilUsuario;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AdministradorService {

    private final AdministradorRepository repository;
    private final PasswordEncoder passwordEncoder;

    public AdministradorService(AdministradorRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Administrador salvar(Administrador administrador) {
        validar(administrador);
        administrador.setSenha(passwordEncoder.encode(administrador.getSenha()));
        administrador.setPerfil(PerfilUsuario.ADMIN);
        return repository.save(administrador);
    }

    public Optional<Administrador> obterPorId(Long id) {
        return repository.findById(id);
    }

    public List<Administrador> listar() {
        return repository.findAll();
    }

    @Transactional
    public void excluir(Administrador administrador) {
        if (administrador == null || administrador.getId() == null) {
            throw new RegraNegocioException("Administrador não encontrado para exclusão.");
        }
        repository.delete(administrador);
    }

    public void validar(Administrador administrador) {

        if (administrador.getNome() == null || administrador.getNome().trim().isEmpty()) {
            throw new RegraNegocioException("Informe um Nome válido.");
        }

        if (administrador.getEmail() == null || administrador.getEmail().trim().isEmpty()) {
            throw new RegraNegocioException("Informe um Email válido.");
        }

        repository.findByEmail(administrador.getEmail())
                .ifPresent(c -> {
                    if (!c.getId().equals(administrador.getId())) {
                        throw new RegraNegocioException("Já existe um administrador cadastrado com este e-mail.");
                    }
                });

        if (administrador.getSenha() == null || administrador.getSenha().trim().isEmpty()) {
            throw new RegraNegocioException("A senha é obrigatória.");
        }
    }
}
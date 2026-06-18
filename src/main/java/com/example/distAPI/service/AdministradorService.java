package com.example.distAPI.service;

import com.example.distAPI.exception.RegraNegocioException;
import com.example.distAPI.model.entity.Administrador;
import com.example.distAPI.model.repository.AdministradorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AdministradorService {

    private final AdministradorRepository repository;

    public AdministradorService(AdministradorRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Administrador salvar(Administrador administrador) {
        validar(administrador);
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
        if (administrador.getSenha() == null || administrador.getSenha().trim().isEmpty()) {
            throw new RegraNegocioException("A senha é obrigatória.");
        }
    }
}
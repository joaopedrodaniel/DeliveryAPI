package com.example.distAPI.service;

import com.example.distAPI.exception.RegraNegocioException;
import com.example.distAPI.model.entity.PerfilUsuario;
import com.example.distAPI.model.entity.Vendedor;
import com.example.distAPI.model.repository.VendedorRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class VendedorService {

    private final VendedorRepository repository;
    private final PasswordEncoder passwordEncoder;

    public VendedorService(VendedorRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Vendedor salvar(Vendedor vendedor) {
        validar(vendedor);

        vendedor.setSenha(passwordEncoder.encode(vendedor.getSenha()));

        vendedor.setPerfil(PerfilUsuario.VENDEDOR);
        return repository.save(vendedor);
    }

    public Optional<Vendedor> obterPorId(Long id) {
        return repository.findById(id);
    }

    public List<Vendedor> listar() {
        return repository.findAll();
    }

    @Transactional
    public void excluir(Vendedor vendedor) {
        if (vendedor == null || vendedor.getId() == null) {
            throw new RegraNegocioException("Vendedor não encontrado para exclusão.");
        }
        repository.delete(vendedor);
    }

    @Transactional
    public void atualizar(Vendedor vendedor) {
        if (vendedor == null || vendedor.getId() == null) {
            throw new RegraNegocioException("Vendedor não encontrado para atualização.");
        }
        validar(vendedor);
        repository.save(vendedor);
    }

    public void validar(Vendedor vendedor) {

        if (vendedor.getNome() == null || vendedor.getNome().trim().isEmpty()) {
            throw new RegraNegocioException("Informe um Nome válido.");
        }

        if (vendedor.getEmail() == null || vendedor.getEmail().trim().isEmpty()) {
            throw new RegraNegocioException("Informe um Email válido.");
        }

        repository.findByEmail(vendedor.getEmail())
                .ifPresent(c -> {
                    if (!c.getId().equals(vendedor.getId())) {
                        throw new RegraNegocioException("Já existe um vendedor cadastrado com este e-mail.");
                    }
                });

        if (vendedor.getSenha() == null || vendedor.getSenha().trim().isEmpty()) {
            throw new RegraNegocioException("A senha é obrigatória.");
        }
    }
}
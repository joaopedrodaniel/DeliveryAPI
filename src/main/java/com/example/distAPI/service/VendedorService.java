package com.example.distAPI.service;

import com.example.distAPI.exception.RegraNegocioException;
import com.example.distAPI.model.entity.Vendedor;
import com.example.distAPI.model.repository.VendedorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class VendedorService {

    private final VendedorRepository repository;

    public VendedorService(VendedorRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Vendedor salvar(Vendedor vendedor) {
        validar(vendedor);
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

    public void validar(Vendedor vendedor) {
        // Assumindo propriedades herdadas da classe Usuario
        if (vendedor.getNome() == null || vendedor.getNome().trim().isEmpty()) {
            throw new RegraNegocioException("Informe um Nome válido.");
        }
        if (vendedor.getEmail() == null || vendedor.getEmail().trim().isEmpty()) {
            throw new RegraNegocioException("Informe um Email válido.");
        }
        if (vendedor.getSenha() == null || vendedor.getSenha().trim().isEmpty()) {
            throw new RegraNegocioException("A senha é obrigatória.");
        }
        if (vendedor.getMatricula() == null || vendedor.getMatricula().trim().isEmpty()) {
            throw new RegraNegocioException("A matrícula do vendedor é obrigatória.");
        }
    }
}
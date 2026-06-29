package com.example.distAPI.service;

import com.example.distAPI.exception.RegraNegocioException;
import com.example.distAPI.model.entity.Produto;
import com.example.distAPI.model.entity.StatusProduto;
import com.example.distAPI.model.repository.ProdutoRepository;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProdutoService {

    private final ProdutoRepository repository;

    public ProdutoService(ProdutoRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Produto salvar(Produto produto) {
        validar(produto);
        return repository.save(produto);
    }

    public Optional<Produto> obterPorId(Long id) {
        return repository.findById(id);
    }

    public List<Produto> listar() {
        return repository.findAll();
    }

    public List<Produto> buscar(String nome, StatusProduto status) {
        Produto filtro = new Produto();
        filtro.setNome(nome);
        filtro.setStatus(status);

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);

        Example<Produto> example = Example.of(filtro, matcher);
        
        return repository.findAll(example); 
    }

    @Transactional
    public void excluir(Produto produto) {
        if (produto == null || produto.getId() == null) {
            throw new RegraNegocioException("Produto não encontrado para exclusão.");
        }
        repository.delete(produto);
    }

    public void validar(Produto produto) {
        if (produto.getNome() == null || produto.getNome().trim().isEmpty()) {
            throw new RegraNegocioException("Informe o nome do produto.");
        }
        if (produto.getPreco() == null || produto.getPreco() <= 0) {
            throw new RegraNegocioException("Informe um preço válido e maior que zero.");
        }
        if (produto.getEstoque() == null || produto.getEstoque() < 0) {
            throw new RegraNegocioException("O estoque não pode ser negativo.");
        }
        if (produto.getStatus() == null) {
            throw new RegraNegocioException("O status do produto é obrigatório.");
        }
    }
}
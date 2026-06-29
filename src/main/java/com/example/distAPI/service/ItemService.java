package com.example.distAPI.service;

import com.example.distAPI.exception.RegraNegocioException;
import com.example.distAPI.model.entity.Item;
import com.example.distAPI.model.repository.ItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ItemService {

    private final ItemRepository repository;

    public ItemService(ItemRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Item atualizar(Item item) {
        if (item.getId() == null) {
            throw new RegraNegocioException("Item não encontrado para atualização.");
        }
        validar(item);
        return repository.save(item);
    }

    @Transactional
    public Item salvar(Item item) {
        validar(item);
        return repository.save(item);
    }

    public Optional<Item> obterPorId(Long id) {
        return repository.findById(id);
    }

    public List<Item> listar() {
        return repository.findAll();
    }

    @Transactional
    public void excluir(Item item) {
        if (item == null || item.getId() == null) {
            throw new RegraNegocioException("Item não encontrado para exclusão.");
        }
        repository.delete(item);
    }

    public void validar(Item item) {
        if (item.getProduto() == null) {
            throw new RegraNegocioException("O item deve estar vinculado a um produto.");
        }
        if (item.getQuantidade() == null || item.getQuantidade() <= 0) {
            throw new RegraNegocioException("A quantidade do item deve ser maior que zero.");
        }
        if (item.getPrecoUnitario() == null || item.getPrecoUnitario() <= 0) {
            throw new RegraNegocioException("O preço unitário do item deve ser maior que zero.");
        }
    }
}
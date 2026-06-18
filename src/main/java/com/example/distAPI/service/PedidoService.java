package com.example.distAPI.service;

import com.example.distAPI.exception.RegraNegocioException;
import com.example.distAPI.model.entity.Pedido;
import com.example.distAPI.model.repository.PedidoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PedidoService {

    private final PedidoRepository repository;

    public PedidoService(PedidoRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Pedido salvar(Pedido pedido) {
        validar(pedido);
        return repository.save(pedido);
    }

    public Optional<Pedido> obterPorId(Long id) {
        return repository.findById(id);
    }

    public List<Pedido> listar() {
        return repository.findAll();
    }

    @Transactional
    public void excluir(Pedido pedido) {
        if (pedido == null || pedido.getId() == null) {
            throw new RegraNegocioException("Pedido não encontrado para exclusão.");
        }
        repository.delete(pedido);
    }

    public void validar(Pedido pedido) {
        if (pedido.getCliente() == null) {
            throw new RegraNegocioException("O pedido deve estar vinculado a um cliente.");
        }
        if (pedido.getStatus() == null) {
            throw new RegraNegocioException("O status do pedido é obrigatório.");
        }
        if (pedido.getEndereco() == null) {
            throw new RegraNegocioException("O endereço de entrega do pedido é obrigatório.");
        }
    }
}
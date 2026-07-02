package com.example.distAPI.service;

import com.example.distAPI.exception.RegraNegocioException;
import com.example.distAPI.model.entity.Item;
import com.example.distAPI.model.entity.OrderStatus;
import com.example.distAPI.model.entity.Pedido;
import com.example.distAPI.model.entity.Produto;
import com.example.distAPI.model.repository.PedidoRepository;
import com.example.distAPI.model.repository.ProdutoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PedidoService {

    private final PedidoRepository repository;
    private final ProdutoRepository produtoRepository;
    private final DeliveryFeeService deliveryFeeService;

    public PedidoService(PedidoRepository repository,
                         ProdutoRepository produtoRepository,
                         DeliveryFeeService deliveryFeeService) {
        this.repository = repository;
        this.produtoRepository = produtoRepository;
        this.deliveryFeeService = deliveryFeeService;
    }

    @Transactional
    public Pedido salvar(Pedido pedido) {

        validar(pedido);

        pedido.setStatus(OrderStatus.PENDENTE);

        pedido.setDataPedido(LocalDateTime.now());

        Double total = 0.0;

        for (Item item : pedido.getItens()) {

            Produto produto = produtoRepository.findById(
                    item.getProduto().getId())
                    .orElseThrow(() ->
                            new RegraNegocioException(
                                    "Produto não encontrado."));

            if (produto.getEstoque() < item.getQuantidade()) {
                throw new RegraNegocioException(
                        "Estoque insuficiente para o produto: "
                                + produto.getNome());
            }

            produto.setEstoque(
                    produto.getEstoque() - item.getQuantidade());

            produtoRepository.save(produto);

            item.setPedido(pedido);

            total += item.getQuantidade()
                    * item.getPrecoUnitario();
        }

        pedido.setValorTotal(total);

        // calculate delivery fee and persist
        Double taxa = deliveryFeeService.calcularTaxa(pedido.getEndereco());
        pedido.setTaxaEntrega(taxa);

        return repository.save(pedido);
    }

    public Optional<Pedido> obterPorId(Long id) {
        return repository.findById(id);
    }

    public List<Pedido> listar() {
        return repository.findAll();
    }

    public List<Pedido> listarPorPeriodo(java.time.LocalDateTime inicio, java.time.LocalDateTime fim) {
        return repository.findByDataPedidoBetween(inicio, fim);
    }

    public List<Pedido> listarPorCliente(Long idCliente) {
        return repository.findByClienteId(idCliente);
    }

    public List<Pedido> listarPorVendedor(Long idVendedor) {
        return repository.findByVendedorId(idVendedor);
    }

    @Transactional
    public Pedido atualizarStatus(Long id,
                                  OrderStatus status) {

        Pedido pedido = repository.findById(id)
                .orElseThrow(() ->
                        new RegraNegocioException(
                                "Pedido não encontrado."));

        pedido.setStatus(status);

        return repository.save(pedido);
    }

    @Transactional
    public Pedido cancelar(Long id) {

        Pedido pedido = repository.findById(id)
                .orElseThrow(() ->
                        new RegraNegocioException(
                                "Pedido não encontrado."));

        if (pedido.getStatus() != OrderStatus.PENDENTE) {
            throw new RegraNegocioException(
                    "Somente pedidos pendentes podem ser cancelados.");
        }

        pedido.setStatus(OrderStatus.CANCELADO);

        // Devolve estoque
        for (Item item : pedido.getItens()) {

            Produto produto = item.getProduto();

            produto.setEstoque(
                    produto.getEstoque() + item.getQuantidade());

            produtoRepository.save(produto);
        }

        return repository.save(pedido);
    }

    @Transactional
    public void excluir(Pedido pedido) {

        if (pedido == null || pedido.getId() == null) {
            throw new RegraNegocioException(
                    "Pedido não encontrado para exclusão.");
        }

        repository.delete(pedido);
    }

    public void validar(Pedido pedido) {

        if (pedido.getCliente() == null) {
            throw new RegraNegocioException(
                    "O pedido deve estar vinculado a um cliente.");
        }

        if (pedido.getEndereco() == null) {
            throw new RegraNegocioException(
                    "O endereço de entrega é obrigatório.");
        }

        if (pedido.getItens() == null ||
                pedido.getItens().isEmpty()) {

            throw new RegraNegocioException(
                    "O pedido deve possuir pelo menos um item.");
        }
    }
}
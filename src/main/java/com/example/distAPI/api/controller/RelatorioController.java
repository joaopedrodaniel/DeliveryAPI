package com.example.distAPI.api.controller;

import com.example.distAPI.model.entity.OrderStatus;
import com.example.distAPI.model.entity.Pedido;
import com.example.distAPI.model.entity.Produto;
import com.example.distAPI.service.PedidoService;
import com.example.distAPI.service.ProdutoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/relatorios")
@RequiredArgsConstructor
public class RelatorioController {

    private final PedidoService pedidoService;
    private final ProdutoService produtoService;

    @GetMapping("/dashboard")
    public Map<String, Object> dashboard() {

        List<Pedido> pedidos = pedidoService.listar();
        List<Produto> produtos = produtoService.listar();

        long pedidosEntregues = pedidos.stream()
                .filter(p -> p.getStatus() == OrderStatus.ENTREGUE)
                .count();

        long pedidosCancelados = pedidos.stream()
                .filter(p -> p.getStatus() == OrderStatus.CANCELADO)
                .count();

        long produtosSemEstoque = produtos.stream()
                .filter(p -> p.getEstoque() == 0)
                .count();

        Double valorEstoque = produtos.stream()
                .mapToDouble(p -> p.getPreco() * p.getEstoque())
                .sum();

        Map<String, Object> relatorio = new HashMap<>();

        relatorio.put("totalPedidos", pedidos.size());
        relatorio.put("pedidosEntregues", pedidosEntregues);
        relatorio.put("pedidosCancelados", pedidosCancelados);
        relatorio.put("totalProdutos", produtos.size());
        relatorio.put("produtosSemEstoque", produtosSemEstoque);
        relatorio.put("valorTotalEstoque", valorEstoque);

        return relatorio;
    }
}
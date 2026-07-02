package com.example.distAPI.api.controller;

import com.example.distAPI.api.dto.TopProdutoDTO;
import com.example.distAPI.model.entity.OrderStatus;
import com.example.distAPI.model.entity.Pedido;
import com.example.distAPI.model.entity.Produto;
import com.example.distAPI.service.ItemService;
import com.example.distAPI.service.PedidoService;
import com.example.distAPI.service.ProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/relatorios")
@Tag(name = "Relatórios", description = "Endpoints para consulta de métricas e relatórios da aplicação")
@RequiredArgsConstructor
public class RelatorioController {

    private final PedidoService pedidoService;
    private final ProdutoService produtoService;
    private final ItemService itemService;

    @Operation(summary = "Dashboard geral", description = "Retorna métricas resumidas de pedidos, produtos e valor total em estoque.")
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

    @Operation(summary = "Vendas por período", description = "Retorna total de pedidos, pedidos entregues e receita total para o período especificado.")
    @GetMapping("/vendas")
    public Map<String, Object> vendasPorPeriodo(
            @RequestParam("dataInicio") String dataInicio,
            @RequestParam("dataFim") String dataFim) {

        LocalDate inicio = LocalDate.parse(dataInicio);
        LocalDate fim = LocalDate.parse(dataFim);

        LocalDateTime inicioDateTime = inicio.atStartOfDay();
        LocalDateTime fimDateTime = fim.atTime(23, 59, 59);

        List<Pedido> pedidos = pedidoService.listarPorPeriodo(inicioDateTime, fimDateTime);

        double receitaTotal = pedidos.stream()
                .mapToDouble(p -> (p.getValorTotal() != null ? p.getValorTotal() : 0.0) +
                        (p.getTaxaEntrega() != null ? p.getTaxaEntrega() : 0.0))
                .sum();

        long pedidosEntregues = pedidos.stream()
                .filter(p -> p.getStatus() == OrderStatus.ENTREGUE)
                .count();

        Map<String, Object> relatorio = new HashMap<>();
        relatorio.put("dataInicio", dataInicio);
        relatorio.put("dataFim", dataFim);
        relatorio.put("totalPedidos", pedidos.size());
        relatorio.put("pedidosEntregues", pedidosEntregues);
        relatorio.put("receitaTotal", receitaTotal);

        return relatorio;
    }

    @Operation(summary = "Produtos mais vendidos", description = "Lista os produtos com maior quantidade vendida, ordenados do mais vendido para o menos vendido.")
    @GetMapping("/produtos-mais-vendidos")
    public List<TopProdutoDTO> produtosMaisVendidos(
            @RequestParam(value = "top", defaultValue = "10") int top) {
        return itemService.produtosMaisVendidos(top);
    }
}

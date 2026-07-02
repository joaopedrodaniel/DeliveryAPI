package com.example.distAPI.scheduler;

import com.example.distAPI.exception.RegraNegocioException;
import com.example.distAPI.model.entity.OrderStatus;
import com.example.distAPI.model.entity.Pedido;
import com.example.distAPI.model.repository.PedidoRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class DeliveryScheduler {

    private final PedidoRepository pedidoRepository;

    public DeliveryScheduler(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    @Scheduled(fixedDelayString = "PT30M") // every 30 minutes
    @Transactional
    public void marcarEntregueAutomaticamente() {
        LocalDateTime limite = LocalDateTime.now().minusHours(4);
        List<Pedido> pedidos = pedidoRepository.findAll();
        for (Pedido p : pedidos) {
            if (p.getStatus() == OrderStatus.ENVIADO && p.getDataPedido() != null) {
                // assume dataPedido stores order time; if sent time available, use it
                if (p.getDataPedido().isBefore(limite)) {
                    p.setStatus(OrderStatus.ENTREGUE);
                    pedidoRepository.save(p);
                }
            }
        }
    }
}

package com.example.distAPI.model.repository;

import com.example.distAPI.model.entity.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    List<Pedido> findByClienteId(Long id);

    List<Pedido> findByVendedorId(Long id);

}

package com.example.distAPI.api.dto;

import com.example.distAPI.model.entity.OrderStatus;
import com.example.distAPI.model.entity.Pedido;
import org.modelmapper.ModelMapper;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class PedidoDTO {
    private Long id;
    private OrderStatus status;
    private Long idCliente;
    private String nomeCliente;
    private Long idVendedor;
    private String nomeVendedor;
    private Long idEndereco;

    public static PedidoDTO create(Pedido pedido){
        ModelMapper modelMapper = new ModelMapper();
        PedidoDTO dto = modelMapper.map(pedido, PedidoDTO.class);

        dto.idCliente = pedido.getCliente().getId();
        dto.nomeCliente = pedido.getCliente().getNome();

        dto.idVendedor = pedido.getVendedor().getId();
        dto.nomeVendedor = pedido.getVendedor().getNome();

        dto.idEndereco = pedido.getEndereco().getId();

        return dto;
    }
}

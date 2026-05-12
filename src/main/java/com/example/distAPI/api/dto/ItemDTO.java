package com.example.distAPI.api.dto;

import com.example.distAPI.model.entity.Item;
import org.modelmapper.ModelMapper;

public class ItemDTO {

    private Long id;
    private Integer quantidade;
    private Double precoUnitario;
    private Long idPedido;
    private Long idProduto;
    private String nomeProduto;

    public static ItemDTO create(Item item){
        ModelMapper modelMapper = new ModelMapper();
        ItemDTO dto = modelMapper.map(item, ItemDTO.class);

        dto.idPedido = item.getPedido().getId();
        dto.idProduto = item.getProduto().getId();
        dto.nomeProduto = item.getProduto().getNome();

        return dto;
    }
}

package com.example.distAPI.api.dto;

import com.example.distAPI.model.entity.Item;
import org.modelmapper.ModelMapper;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemDTO {

    private Long id;
    private Integer quantidade;
    private Double precoUnitario;
    private Long idPedido;
    private Long idProduto;
    private String nomeProduto;
    private ProdutoDTO produto;

    public static ItemDTO create(Item item){
        ModelMapper modelMapper = new ModelMapper();
        ItemDTO dto = modelMapper.map(item, ItemDTO.class);

        if (item.getPedido() != null) {
            dto.idPedido = item.getPedido().getId();
        }
        if (item.getProduto() != null) {
            dto.idProduto = item.getProduto().getId();
            dto.nomeProduto = item.getProduto().getNome();
            dto.produto = ProdutoDTO.create(item.getProduto());
        }

        return dto;
    }
}

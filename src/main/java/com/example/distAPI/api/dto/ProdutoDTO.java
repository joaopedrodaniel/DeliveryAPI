package com.example.distAPI.api.dto;

import com.example.distAPI.model.entity.Produto;
import com.example.distAPI.model.entity.StatusProduto;
import org.modelmapper.ModelMapper;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor


public class ProdutoDTO {
    private Long id;
    private String nome;
    private Double preco;
    private Integer estoque;
    private StatusProduto idStatus;

    public static ProdutoDTO create(Produto produto){
        ModelMapper modelMapper = new ModelMapper();
        ProdutoDTO dto = modelMapper.map(produto, ProdutoDTO.class);
        return dto;
    }
}

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
    private String descricao;
    private String foto;
    private String categoria;
    private StatusProduto status;

    public static ProdutoDTO create(Produto produto){
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(produto, ProdutoDTO.class);
    }
}

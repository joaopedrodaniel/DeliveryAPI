package com.example.distAPI.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TopProdutoDTO {
    private Long produtoId;
    private String nome;
    private Long quantidadeVendida;
}

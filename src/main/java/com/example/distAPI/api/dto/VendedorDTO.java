package com.example.distAPI.api.dto;

import com.example.distAPI.model.entity.Vendedor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class VendedorDTO {
    private Long id;
    private String nome;
    private String email;
    private String senha;
    private String matricula;

    public static VendedorDTO create(Vendedor vendedor){
        ModelMapper modelMapper = new ModelMapper();
        VendedorDTO dto = modelMapper.map(vendedor, VendedorDTO.class);
        return dto;
    }
}
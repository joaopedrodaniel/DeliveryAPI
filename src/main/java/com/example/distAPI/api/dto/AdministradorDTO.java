package com.example.distAPI.api.dto;

import com.example.distAPI.model.entity.Administrador;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class AdministradorDTO {
    private Long id;
    private String nome;
    private String email;
    private String senha;
    private boolean temAcesso;

    public static AdministradorDTO create(Administrador adm){
        ModelMapper modelMapper = new ModelMapper();
        AdministradorDTO dto = modelMapper.map(adm, AdministradorDTO.class);
        return dto;
    }
}
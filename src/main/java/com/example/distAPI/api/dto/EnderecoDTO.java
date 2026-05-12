package com.example.distAPI.api.dto;

import com.example.distAPI.model.entity.Cliente;
import com.example.distAPI.model.entity.Endereco;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class EnderecoDTO {
    private Long id;
    private String cep;
    private String logradouro;
    private String numero;
    private String complemento;
    private Long idCliente;
    private String nomeCliente;


    public static EnderecoDTO create(Endereco endereco){
        ModelMapper modelMapper = new ModelMapper();
        EnderecoDTO dto = modelMapper.map(endereco, EnderecoDTO.class);

        dto.nomeCliente = endereco.getCliente().getNome();
        dto.nomeCliente = endereco.getCliente().getNome();

        return dto;
    }
}

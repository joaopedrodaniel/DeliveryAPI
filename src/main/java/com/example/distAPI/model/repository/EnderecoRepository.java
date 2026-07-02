package com.example.distAPI.model.repository;

import com.example.distAPI.model.entity.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EnderecoRepository extends JpaRepository <Endereco, Long> {

    List<Endereco> findByClienteId(Long idCliente);
}

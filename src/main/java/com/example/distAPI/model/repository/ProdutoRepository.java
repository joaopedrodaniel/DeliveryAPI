package com.example.distAPI.model.repository;

import com.example.distAPI.model.entity.Produto;
import com.example.distAPI.model.entity.StatusProduto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    Optional<Produto> findByNomeIgnoreCase(String nome);

    List<Produto> findByNomeContainingIgnoreCase(String nome);

    List<Produto> findByStatus(StatusProduto status);

    List<Produto> findByPrecoBetween(Double precoMin, Double precoMax);

    List<Produto> findByCategoriaContainingIgnoreCase(String categoria);
}

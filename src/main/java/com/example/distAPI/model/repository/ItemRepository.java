package com.example.distAPI.model.repository;

import com.example.distAPI.api.dto.TopProdutoDTO;
import com.example.distAPI.model.entity.Item;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query("SELECT new com.example.distAPI.api.dto.TopProdutoDTO(i.produto.id, i.produto.nome, SUM(i.quantidade)) " +
            "FROM Item i GROUP BY i.produto.id, i.produto.nome ORDER BY SUM(i.quantidade) DESC")
    List<TopProdutoDTO> findTopProdutosMaisVendidos(Pageable pageable);
}

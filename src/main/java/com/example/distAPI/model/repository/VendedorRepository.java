package com.example.distAPI.model.repository;

import com.example.distAPI.model.entity.Vendedor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VendedorRepository extends JpaRepository <Vendedor, Long> {
}

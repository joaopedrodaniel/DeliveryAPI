package com.example.distAPI.model.repository;

import com.example.distAPI.model.entity.Administrador;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdministradorRepository extends JpaRepository <Administrador, Long> {
}

package com.example.distAPI.model.entity;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)


public class Administrador extends Usuario {
    private boolean temAcesso;
}

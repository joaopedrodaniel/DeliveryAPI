package com.example.distAPI.model.entity;

import jakarta.persistence.Entity;

@Entity

public class Administrador extends Usuario {
    private boolean temAcesso;
}

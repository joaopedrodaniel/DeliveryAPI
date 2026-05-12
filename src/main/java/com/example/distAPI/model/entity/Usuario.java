package com.example.distAPI.model.entity;
import jakarta.persistence.*;

@MappedSuperclass //atenção aqui
//@Inheritance(strategy = InheritanceType.JOINED)

public abstract class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String email;
    private String senha;

}

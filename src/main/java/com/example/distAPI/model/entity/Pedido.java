package com.example.distAPI.model.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @ManyToOne
    private Cliente cliente;

    @ManyToOne
    private Vendedor vendedor;

    @OneToOne
    private Endereco endereco;

    @OneToMany(mappedBy = "pedido",
           cascade = CascadeType.ALL)
    private List<Item> itens;

    private Double valorTotal;

    private Double taxaEntrega;

    private LocalDateTime dataPedido;

    private Integer volumeMl;

    private String marca;
    
    private boolean alcoolico;
}

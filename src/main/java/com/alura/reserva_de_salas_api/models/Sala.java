package com.alura.reserva_de_salas_api.models;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "salas")
@Getter
@Setter
public class Sala {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String nome;

    @Column(nullable = false)
    private int capacidade;

    @Column(nullable = false)
    private String localizacao;

    @OneToMany(mappedBy = "sala")
    private List<Reserva> reservas = new ArrayList<>();

    public Sala(String nome, int capacidade) {
        this.nome = nome;
        this.capacidade = capacidade;
    }

    public Sala() {}
}

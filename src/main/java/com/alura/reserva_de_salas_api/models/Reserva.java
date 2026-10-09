package com.alura.reserva_de_salas_api.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "reservas")
@Getter
@Setter
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sala_id", nullable = false)
    private Sala sala;

    @Column(name = "data_inicio",nullable = false)
    private LocalDateTime dataInicio;

    @Column(name = "data_fim",nullable = false)
    private LocalDateTime dataFim;

    @Column(name = "quantidade_pessoas",nullable = false)
    private Integer quantidadePessoas;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_reserva",nullable = false)
    private StatusReserva statusReserva;

    public Reserva(Usuario usuario, Sala sala, LocalDateTime dataInicio, LocalDateTime dataFim, Integer quantidadePessoas) {
        this.usuario = usuario;
        this.sala = sala;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.quantidadePessoas = quantidadePessoas;
        this.statusReserva = StatusReserva.ATIVA;
    }

    public Reserva() {}
}

package com.alura.reserva_de_salas_api.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.AnyDiscriminatorImplicitValues;

import java.time.LocalDate;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalDate dataNascimento;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(unique = true)
    private String cpf;

    public Usuario(String name, LocalDate dataNascimento, String email, String cpf) {
        this.name = name;
        this.dataNascimento = dataNascimento;
        this.email = email;
        this.cpf = cpf;
    }

    public Usuario() {}
}

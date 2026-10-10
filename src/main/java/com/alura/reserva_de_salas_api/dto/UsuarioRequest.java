package com.alura.reserva_de_salas_api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

public record UsuarioRequest(
        @NotBlank @Size(max = 100) String nome,
        @NotBlank @Past LocalDate dataNascimento,
        @Email @NotBlank String email,
        @CPF String cpf
) {
}

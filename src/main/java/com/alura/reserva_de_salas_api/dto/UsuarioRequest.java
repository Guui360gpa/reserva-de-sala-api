package com.alura.reserva_de_salas_api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

public record UsuarioRequest(
        @NotBlank String nome,
        @NotBlank @Past LocalDate dataNascimento,
        @Email @NotBlank String email,
        @CPF String cpf
) {
}

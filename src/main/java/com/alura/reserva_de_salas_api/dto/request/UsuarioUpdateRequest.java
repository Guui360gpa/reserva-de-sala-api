package com.alura.reserva_de_salas_api.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

public record UsuarioUpdateRequest(
        @Size(max = 100) String nome,
        @Past LocalDate dataNascimento,
        @Email String email,
        @CPF String cpf
        ) {
}

package com.alura.reserva_de_salas_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record SalaRequest(
        @NotBlank @Size(max = 100) String nome,
        @NotNull @Positive Integer capacidade,
        @NotBlank @Size(max = 150) String localizacao
) {
}

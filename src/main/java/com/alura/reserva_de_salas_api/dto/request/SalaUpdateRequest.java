package com.alura.reserva_de_salas_api.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record SalaUpdateRequest(
        @Size(max = 100) String nome,
        @Positive @Max(500) Integer capacidade,
        @Size(max = 150) String localizacao
) {
}

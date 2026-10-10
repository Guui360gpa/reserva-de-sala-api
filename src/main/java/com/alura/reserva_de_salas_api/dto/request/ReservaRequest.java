package com.alura.reserva_de_salas_api.dto.request;

import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

public record ReservaRequest(
        @NotNull Long usuarioId,
        @NotNull Long salaId,
        @NotBlank @Future LocalDateTime dataInicio,
        @NotBlank @Future LocalDateTime dataFim,
        @NotNull @Positive Integer quantidadePessoas
        ) {
}

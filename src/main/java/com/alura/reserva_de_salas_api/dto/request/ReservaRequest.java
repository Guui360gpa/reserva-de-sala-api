package com.alura.reserva_de_salas_api.dto.request;

import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

public record ReservaRequest(
        @NotBlank String usuarioEmail,
        @NotBlank String salaNome,
        @NotBlank @Future LocalDateTime dataInicio,
        @NotBlank @Future LocalDateTime dataFim,
        @NotNull @Positive Integer quantidadePessoas
        ) {
}

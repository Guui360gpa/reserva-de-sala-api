package com.alura.reserva_de_salas_api.dto.response;

import com.alura.reserva_de_salas_api.models.Reserva;
import com.alura.reserva_de_salas_api.models.StatusReserva;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public record ReservaResponse(
        Long id,
        UsuarioResponse usuario,
        SalaResponse sala,
        LocalDateTime dataInicio,
        LocalDateTime dataFim,
        Integer quantidadePessoas,
        StatusReserva statusReserva

) {

    public static ReservaResponse from(Reserva reserva){
        return new ReservaResponse(
                reserva.getId(),
                UsuarioResponse.from(reserva.getUsuario()),
                SalaResponse.from(reserva.getSala()),
                reserva.getDataInicio(),
                reserva.getDataFim(),
                reserva.getQuantidadePessoas(),
                reserva.getStatusReserva()
        );
    }

    public static record ReservaUpdateRequest(
            @NotNull @Future LocalDateTime dataInicio,
            @NotNull @Future LocalDateTime dataFim,
            @NotNull @Positive Integer quantidadePessoas
            ) {
    }
}

package com.alura.reserva_de_salas_api.dto;

import com.alura.reserva_de_salas_api.models.Sala;
import com.alura.reserva_de_salas_api.models.StatusSala;

public record SalaResponse(
        Long id,
        String nome,
        Integer capacidade,
        String localizacao,
        StatusSala status
) {

    public static SalaResponse from(Sala sala){
        return new SalaResponse(
                sala.getId(),
                sala.getNome(),
                sala.getCapacidade(),
                sala.getLocalizacao(),
                sala.getStatus()
        );
    }
}

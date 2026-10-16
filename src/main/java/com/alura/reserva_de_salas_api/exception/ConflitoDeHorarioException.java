package com.alura.reserva_de_salas_api.exception;

public class ConflitoDeHorarioException extends RuntimeException {
    public ConflitoDeHorarioException(String message) {
        super(message);
    }
}

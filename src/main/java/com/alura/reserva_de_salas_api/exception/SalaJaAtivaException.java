package com.alura.reserva_de_salas_api.exception;

public class SalaJaAtivaException extends RuntimeException {
    public SalaJaAtivaException(String message) {
        super(message);
    }
}

package com.alura.reserva_de_salas_api.exception;

public class ReservaJaCanceladaException extends RuntimeException {
    public ReservaJaCanceladaException(String message) {
        super(message);
    }
}

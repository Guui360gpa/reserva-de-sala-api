package com.alura.reserva_de_salas_api.exception;

public class SalaExistenteException extends RuntimeException {
    public SalaExistenteException(String message) {
        super(message);
    }
}

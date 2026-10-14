package com.alura.reserva_de_salas_api.repository;

public class SalaJaInativaException extends RuntimeException {
    public SalaJaInativaException(String message) {
        super(message);
    }
}

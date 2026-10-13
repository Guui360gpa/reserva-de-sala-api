package com.alura.reserva_de_salas_api.exception;

public class SalaNaoEncontradaException extends RuntimeException {
    public SalaNaoEncontradaException(String message) {
        super(message);
    }
}

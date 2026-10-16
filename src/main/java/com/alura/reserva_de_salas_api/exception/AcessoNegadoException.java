package com.alura.reserva_de_salas_api.exception;

public class AcessoNegadoException extends RuntimeException {
    public AcessoNegadoException(String message) {
        super(message);
    }
}

package com.alura.reserva_de_salas_api.exception;

public class OcupacaoMinimaNaoAtendidaException extends RuntimeException {
    public OcupacaoMinimaNaoAtendidaException(String message) {
        super(message);
    }
}

package com.alura.reserva_de_salas_api.exception;

public class CapacidadeExcedidaException extends RuntimeException {
    public CapacidadeExcedidaException(String message) {
        super(message);
    }
}

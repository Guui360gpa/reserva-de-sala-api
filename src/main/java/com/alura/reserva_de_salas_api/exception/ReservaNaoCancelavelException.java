package com.alura.reserva_de_salas_api.exception;

public class ReservaNaoCancelavelException extends RuntimeException {
    public ReservaNaoCancelavelException(String message) {
        super(message);
    }
}

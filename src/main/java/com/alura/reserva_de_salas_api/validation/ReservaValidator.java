package com.alura.reserva_de_salas_api.validation;

import com.alura.reserva_de_salas_api.exception.*;
import com.alura.reserva_de_salas_api.models.Sala;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class ReservaValidator {

    private final Clock clock;


    public void validarPeriodo(LocalDateTime inicio, LocalDateTime fim){
        if (!fim.isAfter(inicio)){
            throw new PeriodoInvalidoException("A data de fim deve ser posterior à data de inicio");
        }
        Duration duration = Duration.between(inicio,fim);
        if (duration.compareTo(Duration.ofMinutes(30)) < 0){
            throw new DuracaoMinimaException("A reserva deve ter no mínimo 30 min");
        }
        if (duration.compareTo(Duration.ofHours(4)) > 0){
            throw new DuracaoMaximaException("A reserva deve ter no máximo 4 horas");
        }
    }

    public void validarAntecedencia(LocalDateTime inicio) {
        LocalDateTime now = LocalDateTime.now(clock);
        if (inicio.isBefore(now.plusMinutes(30))){
            throw new AntecedenciaInvalidaException("A reserva exige antecedência mínima de 30 min.");
        }
        if (inicio.isAfter(now.plusDays(90))){
            throw new AntecedenciaInvalidaException("A reserva só pode ser feita com até 90 dias de antecedência.");
        }
    }

    public void validarOcupacao (Sala sala, int pessoas) {
        if (pessoas > sala.getCapacidade()) {
            throw new CapacidadeExcedidaException("A quantidade de pessoas excede a capacidade da sala.");
        }
        int minimo = sala.capacidadeMinima();
        if (pessoas < minimo) {
            throw new OcupacaoMinimaNaoAtendidaException("A sala exige no mínimo " + minimo + " pessoas.");
        }
    }
}

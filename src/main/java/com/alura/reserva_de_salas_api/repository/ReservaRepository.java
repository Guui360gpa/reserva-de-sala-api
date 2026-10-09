package com.alura.reserva_de_salas_api.repository;

import com.alura.reserva_de_salas_api.models.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {
}

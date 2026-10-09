package com.alura.reserva_de_salas_api.repository;

import com.alura.reserva_de_salas_api.models.Sala;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SalaRepository extends JpaRepository<Sala, Long> {
}

package com.alura.reserva_de_salas_api.repository;

import com.alura.reserva_de_salas_api.models.Sala;
import com.alura.reserva_de_salas_api.models.StatusSala;
import com.alura.reserva_de_salas_api.models.StatusUsuario;
import com.alura.reserva_de_salas_api.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SalaRepository extends JpaRepository<Sala, Long> {

    @Query("""
        SELECT s FROM Sala s
        WHERE s.status = com.alura.reserva_de_salas_api.models.StatusSala.ATIVA
          AND s.capacidade >= :capacidade
          AND NOT EXISTS (
              SELECT 1 FROM Reserva r
              WHERE r.sala = s
                AND r.statusReserva = com.alura.reserva_de_salas_api.models.StatusReserva.ATIVA
                AND r.dataInicio < :fim
                AND r.dataFim > :inicio
          )
        ORDER BY s.capacidade ASC
        """)
    List<Sala> buscarDisponiveis(@Param("capacidade") Integer capacidade,
                                 @Param("inicio") LocalDateTime inicio,
                                 @Param("fim") LocalDateTime fim);

    boolean existsByNomeIgnoreCaseAndIdNot(String nomeSala);

    boolean existsByNomeIgnoreCase(String nomeSala);

    List<Sala> findByStatus(StatusSala status);

    Optional<Sala> findByNomeIgnoreCaseAndStatus(String nome, StatusSala statusSala);

    Optional<Sala> findByIdIgnoreCaseAndStatus(Long id, StatusSala statusSala);

    boolean existsByIdAndStatusSala(Long id, StatusSala statusSala);
}

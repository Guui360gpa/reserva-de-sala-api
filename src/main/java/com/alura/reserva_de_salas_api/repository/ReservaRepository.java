package com.alura.reserva_de_salas_api.repository;

import com.alura.reserva_de_salas_api.models.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    List<Reserva> findByUsuarioId(Long usuarioId);

    List<Reserva> findBySalaId(Long salaId);

    @Query("""
        SELECT COUNT(r) > 0 FROM Reserva r
        WHERE r.sala.id = :salaId
          AND r.statusReserva = com.alura.reserva_de_salas_api.models.StatusReserva.ATIVA
          AND r.dataFim > :agora
          AND r.quantidadePessoas > :novaCapacidade
        """)
    boolean existeReservaFuturaAcimaDe(@Param("salaId") Long salaId,
                                       @Param("novaCapacidade") Integer novaCapacidade,
                                       @Param("agora") LocalDateTime agora);

}

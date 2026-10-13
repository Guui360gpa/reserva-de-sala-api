package com.alura.reserva_de_salas_api.repository;

import com.alura.reserva_de_salas_api.models.Sala;
import com.alura.reserva_de_salas_api.models.StatusSala;
import com.alura.reserva_de_salas_api.models.StatusUsuario;
import com.alura.reserva_de_salas_api.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SalaRepository extends JpaRepository<Sala, Long> {

    boolean existsByNomeIgnoreCase(String nomeSala);

    List<Sala> findByStatus(StatusSala status);

    Optional<Sala> findByNome(String nome);
}

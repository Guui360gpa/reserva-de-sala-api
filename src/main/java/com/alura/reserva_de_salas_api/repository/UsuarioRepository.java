package com.alura.reserva_de_salas_api.repository;

import com.alura.reserva_de_salas_api.models.StatusUsuario;
import com.alura.reserva_de_salas_api.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario,Long> {
    boolean existsByEmail(String email);

    boolean existsByCpf(String cpf);

    List<Usuario> findByStatus(StatusUsuario status);

    Optional<Usuario> findByEmail(String email);
}

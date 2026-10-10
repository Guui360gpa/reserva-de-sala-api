package com.alura.reserva_de_salas_api.dto.response;

import com.alura.reserva_de_salas_api.models.Usuario;

import java.time.LocalDate;

public record UsuarioResponse(
        Long id,
        String nome,
        LocalDate dataNascimento,
        String email,
        String cpf
) {
    public static UsuarioResponse from(Usuario usuario){
        return new UsuarioResponse(
                usuario.getId(),
                usuario.getName(),
                usuario.getDataNascimento(),
                usuario.getEmail(),
                usuario.getCpf()
        );
    }
}

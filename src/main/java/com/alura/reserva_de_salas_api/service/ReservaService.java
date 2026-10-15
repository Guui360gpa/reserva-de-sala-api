package com.alura.reserva_de_salas_api.service;

import com.alura.reserva_de_salas_api.dto.request.ReservaRequest;
import com.alura.reserva_de_salas_api.dto.response.ReservaResponse;
import com.alura.reserva_de_salas_api.exception.PeriodoInvalidoException;
import com.alura.reserva_de_salas_api.exception.SalaNaoEncontradaException;
import com.alura.reserva_de_salas_api.exception.UsuarioNaoEncontradoException;
import com.alura.reserva_de_salas_api.models.*;
import com.alura.reserva_de_salas_api.repository.ReservaRepository;
import com.alura.reserva_de_salas_api.repository.SalaRepository;
import com.alura.reserva_de_salas_api.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final UsuarioRepository usuarioRepository;
    private final SalaRepository salaRepository;

    @Transactional
    public ReservaResponse criar(ReservaRequest dto){
        Usuario usuario = usuarioRepository.findByEmailIgnoreCaseAndStatus(dto.usuarioEmail(), StatusUsuario.ATIVO)
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuario não encontrado."));

        Sala sala = salaRepository.findByNomeIgnoreCaseAndStatus(dto.salaNome(), StatusSala.ATIVA)
                .orElseThrow(() -> new SalaNaoEncontradaException("Sala não encontrada."));

        if (!dto.dataFim().isAfter(dto.dataInicio())){
            throw new PeriodoInvalidoException("A data de fim deve ser posterior á data de início");
        }

        Reserva reserva = new Reserva(
                usuario,
                sala,
                dto.dataInicio(),
                dto.dataFim(),
                dto.quantidadePessoas()
        );

        return ReservaResponse.from(reserva);
    }
}

package com.alura.reserva_de_salas_api.service;

import com.alura.reserva_de_salas_api.dto.request.ReservaRequest;
import com.alura.reserva_de_salas_api.dto.response.ReservaResponse;
import com.alura.reserva_de_salas_api.exception.*;
import com.alura.reserva_de_salas_api.models.*;
import com.alura.reserva_de_salas_api.repository.ReservaRepository;
import com.alura.reserva_de_salas_api.repository.SalaRepository;
import com.alura.reserva_de_salas_api.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final UsuarioRepository usuarioRepository;
    private final SalaRepository salaRepository;
    private final Clock clock = clock();


    @Bean
    public Clock clock() {
        return Clock.system(ZoneId.of("America/Sao_Paulo"));
    }

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

        return ReservaResponse.from(reservaRepository.save(reserva));
    }

    @Transactional(readOnly = true)
    public List<ReservaResponse> listarReservasDoDia(){
        LocalDate hoje = LocalDate.now(clock);
        LocalDateTime inicioDia = hoje.atStartOfDay();
        LocalDateTime fimDia = hoje.plusDays(1).atStartOfDay();

        return reservaRepository.listarDoDia(List.of(StatusReserva.ATIVA, StatusReserva.EM_ANDAMENTO), inicioDia, fimDia).stream()
                .map(ReservaResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ReservaResponse> listarReservasEmAndamento(){
        return reservaRepository.listarEmAndamento(LocalDateTime.now(clock))
                .stream()
                .map(ReservaResponse::from)
                .toList();
    }

    @Transactional
    public ReservaResponse buscar(Long id){
        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() -> new ReservaNaoEncontradaException("Reserva não encontrada"));

        return ReservaResponse.from(reserva);
    }

    public ReservaResponse cancelar(Long id){
        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() -> new ReservaNaoEncontradaException("Reserva não encontrada"));

        if (reserva.getStatusReserva() == StatusReserva.CANCELADA){
            throw new ReservaJaCanceladaException("Reserva já cancelada.");
        }

        reserva.setStatusReserva(StatusReserva.CANCELADA);

        return ReservaResponse.from(reservaRepository.save(reserva));
    }
}

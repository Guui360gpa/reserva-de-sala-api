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

import java.time.*;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final UsuarioRepository usuarioRepository;
    private final SalaRepository salaRepository;
    private final Clock clock;


    @Transactional
    public ReservaResponse criar(ReservaRequest dto){
        Usuario usuario = usuarioRepository.findByEmailIgnoreCaseAndStatus(dto.usuarioEmail(), StatusUsuario.ATIVO)
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuario não encontrado."));

        Sala sala = salaRepository.findByNomeIgnoreCaseAndStatus(dto.salaNome(), StatusSala.ATIVA)
                .orElseThrow(() -> new SalaNaoEncontradaException("Sala não encontrada."));

        validarPeriodo(dto.dataInicio(),dto.dataFim());
        validarAntecedencia(dto.dataInicio());
        validarOcupacao(sala,dto.quantidadePessoas());

        if (reservaRepository.existeConflito(sala.getId(), dto.dataInicio(), dto.dataFim())){
            throw new ConflitoDeHorarioException("A sala já está reservada neste horário.");
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

    private void validarPeriodo(LocalDateTime inicio, LocalDateTime fim){
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

    private void validarAntecedencia(LocalDateTime inicio) {
        LocalDateTime now = LocalDateTime.now(clock);
        if (inicio.isBefore(now.plusMinutes(30))){
            throw new AntecedenciaInvalidaException("A reserva exige antecedência mínima de 30 min.");
        }
        if (inicio.isAfter(now.plusDays(90))){
            throw new AntecedenciaInvalidaException("A reserva só pode ser feita com até 90 dias de antecedência.");
        }
    }

    private void validarOcupacao (Sala sala, int pessoas) {
        if (pessoas > sala.getCapacidade()) {
            throw new CapacidadeExcedidaException("A quantidade de pessoas excede a capacidade da sala.");
        }
        int minimo = sala.capacidadeMinima();
        if (pessoas < minimo) {
            throw new OcupacaoMinimaNaoAtendidaException("A sala exige no mínimo " + minimo + " pessoas.");
        }
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

    @Transactional(readOnly = true)
    public ReservaResponse buscar(Long id){
        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() -> new ReservaNaoEncontradaException("Reserva não encontrada"));

        return ReservaResponse.from(reserva);
    }

    @Transactional
    public ReservaResponse cancelar(Long id, Long solicitanteId){
        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() -> new ReservaNaoEncontradaException("Reserva não encontrada"));
        Usuario usuarioSolicitante = usuarioRepository.findById(solicitanteId)
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado."));

        if (reserva.getStatusReserva() == StatusReserva.CANCELADA){
            throw new ReservaJaCanceladaException("Reserva já cancelada.");
        }

        reserva.cancelar(usuarioSolicitante,LocalDateTime.now());
        return ReservaResponse.from(reserva);
    }
}

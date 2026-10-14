package com.alura.reserva_de_salas_api.service;

import com.alura.reserva_de_salas_api.dto.request.SalaRequest;
import com.alura.reserva_de_salas_api.dto.request.SalaUpdateRequest;
import com.alura.reserva_de_salas_api.dto.response.ReservaResponse;
import com.alura.reserva_de_salas_api.dto.response.SalaResponse;
import com.alura.reserva_de_salas_api.exception.*;
import com.alura.reserva_de_salas_api.models.*;
import com.alura.reserva_de_salas_api.repository.ReservaRepository;
import com.alura.reserva_de_salas_api.repository.SalaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class SalaService {

    private final SalaRepository salaRepository;
    private final ReservaRepository reservaRepository;

    public SalaResponse cadastrar(SalaRequest dto){
        if (salaRepository.existsByNomeIgnoreCase(dto.nome().trim())){
            throw new SalaExistenteException("A sala " + dto.nome()  + " já existe.");
        }

        Sala salaSalva = salaRepository.save(new Sala(
                dto.nome(),
                dto.capacidade(),
                dto.localizacao()
        ));

        return SalaResponse.from(salaSalva);
    }

    public List<SalaResponse> listar(){
        List<Sala> salas = salaRepository.findByStatus(StatusSala.ATIVA);

        return salas.stream()
                .map(SalaResponse::from)
                .toList();
    }

    public SalaResponse buscar(String nome){
        Sala salaEncontrada = salaRepository.findByNomeIgnoreCase(nome.trim())
                .orElseThrow(() -> new SalaNaoEncontradaException("Sala não encontrada."));

        return SalaResponse.from(salaEncontrada);
    }

    public SalaResponse atualizar(Long id, SalaUpdateRequest dto){
        Sala salaEncontrada = salaRepository.findByIdIgnoreCaseAndStatus(id,StatusSala.ATIVA)
                .orElseThrow(() -> new SalaNaoEncontradaException("Sala não encontrada."));

        if (dto.nome() != null){
            if (salaRepository.existsByNomeIgnoreCase(dto.nome())){
                throw new SalaExistenteException("A sala " + dto.nome()  + " já existe.");
            }
            salaEncontrada.setNome(dto.nome());
        }

        if (dto.capacidade() != null){
            salaEncontrada.setCapacidade(dto.capacidade());
        }

        if (dto.localizacao() != null){
            salaEncontrada.setLocalizacao(dto.localizacao());
        }

        return SalaResponse.from(salaRepository.save(salaEncontrada));
    }

    public SalaResponse desativar(Long id) {
        Sala sala = salaRepository.findById(id)
                .orElseThrow(() -> new SalaNaoEncontradaException("Sala não encontrada."));

        if (sala.getStatus() == StatusSala.INATIVA) {
            throw new SalaJaInativaException("A sala " + sala.getNome() + " já está inativa.");
        }

        sala.setStatus(StatusSala.INATIVA);
        return SalaResponse.from(salaRepository.save(sala));
    }

    public SalaResponse ativar(Long id) {
        Sala sala = salaRepository.findById(id)
                .orElseThrow(() -> new SalaNaoEncontradaException("Sala não encontrada."));

        if (sala.getStatus() == StatusSala.ATIVA) {
            throw new SalaJaAtivaException("A sala " + sala.getNome() + " já está ativa.");
        }

        sala.setStatus(StatusSala.ATIVA);
        return SalaResponse.from(salaRepository.save(sala));
    }

    public List<SalaResponse> listarDisponiveis(){
        List<Sala> salas = salaRepository.findByStatus(StatusSala.ATIVA);
        List<Sala> salasDisponiveis = new ArrayList<>();

        for (Sala s : salas ){
            Reserva reserva = s.getReservas().getLast();
            if (reserva.getStatusReserva() != StatusReserva.EM_ANDAMENTO){
                salasDisponiveis.add(s);
            }
        }

        return salasDisponiveis.stream()
                .map(SalaResponse::from).toList();
    }


    @Transactional(readOnly = true)
    public List<ReservaResponse> listarReservas(Long id){
        if (!salaRepository.existsByIdAndStatusSala(id, StatusSala.ATIVA)){
            throw new SalaNaoEncontradaException("Sala não encontrada.");
        }

        List<Reserva> reservasSala = reservaRepository.findBySalaId(id);

        return reservasSala.stream()
                .map(ReservaResponse::from)
                .toList();
    }

}

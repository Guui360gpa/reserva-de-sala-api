package com.alura.reserva_de_salas_api.service;

import com.alura.reserva_de_salas_api.dto.request.SalaRequest;
import com.alura.reserva_de_salas_api.dto.request.SalaUpdateRequest;
import com.alura.reserva_de_salas_api.dto.response.SalaResponse;
import com.alura.reserva_de_salas_api.exception.SalaExistenteException;
import com.alura.reserva_de_salas_api.exception.SalaNaoEncontradaException;
import com.alura.reserva_de_salas_api.models.Sala;
import com.alura.reserva_de_salas_api.models.StatusSala;
import com.alura.reserva_de_salas_api.repository.SalaJaInativaException;
import com.alura.reserva_de_salas_api.repository.SalaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SalaService {

    private final SalaRepository salaRepository;

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

}

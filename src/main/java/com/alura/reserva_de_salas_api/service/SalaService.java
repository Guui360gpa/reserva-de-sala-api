package com.alura.reserva_de_salas_api.service;

import com.alura.reserva_de_salas_api.dto.request.SalaRequest;
import com.alura.reserva_de_salas_api.dto.response.SalaResponse;
import com.alura.reserva_de_salas_api.exception.SalaExistenteException;
import com.alura.reserva_de_salas_api.models.Sala;
import com.alura.reserva_de_salas_api.repository.SalaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

}

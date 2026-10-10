package com.alura.reserva_de_salas_api.controller;

import com.alura.reserva_de_salas_api.dto.response.ReservaResponse;
import com.alura.reserva_de_salas_api.dto.request.SalaRequest;
import com.alura.reserva_de_salas_api.dto.response.SalaResponse;
import com.alura.reserva_de_salas_api.dto.request.SalaUpdateRequest;
import com.alura.reserva_de_salas_api.service.SalaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/salas")
@RequiredArgsConstructor
public class SalaController {

    private final SalaService salaService;

    @PostMapping
    public ResponseEntity<SalaResponse> cadastro(@RequestBody @Valid SalaRequest salaRequest){
        return null;
    }

    @GetMapping
    public ResponseEntity<List<SalaResponse>> lista(){
        return null;
    }

    @GetMapping("/{id}")
    public ResponseEntity<SalaResponse> buscaPorId(@PathVariable Long id){
        return null;
    }

    @PatchMapping("/{id}")
    public ResponseEntity<SalaResponse> atualizaPorId(@PathVariable Long id, @RequestBody @Valid SalaUpdateRequest salaUpdateRequest){
        return null;
    }

    @PatchMapping("/{id}/desativar")
    public ResponseEntity<SalaResponse> desativaPorId(@PathVariable Long id){
        return null;
    }

    @PatchMapping("/{id}/ativar")
    public ResponseEntity<SalaResponse> ativaPorId(@PathVariable Long id){
        return null;
    }

    @GetMapping("/disponiveis")
    public ResponseEntity<List<SalaResponse>> listarDisponiveis(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim,
            @RequestParam(required = false, defaultValue = "1") Integer capacidade) {

        return null;
    }

    @GetMapping("{id}/reservas")
    public ResponseEntity<List<ReservaResponse>> listaReservasPorId(@PathVariable Long id){
        return null;
    }


}

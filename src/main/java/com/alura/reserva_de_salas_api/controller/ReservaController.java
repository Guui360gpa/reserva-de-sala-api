package com.alura.reserva_de_salas_api.controller;

import com.alura.reserva_de_salas_api.dto.request.ReservaRequest;
import com.alura.reserva_de_salas_api.dto.response.ReservaResponse;
import com.alura.reserva_de_salas_api.service.ReservaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservas")
@RequiredArgsConstructor
public class ReservaController {

    private final ReservaService reservaService;

    @PostMapping
    public ResponseEntity<ReservaResponse> cria(@RequestBody @Valid ReservaRequest reservaRequest){
        ReservaResponse reservaResponse = reservaService.criar(reservaRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(reservaResponse);
    }

    @GetMapping
    public ResponseEntity<List<ReservaResponse>> lista(){
        return null;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservaResponse> buscaPorId(@PathVariable Long id){
        return null;
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReservaResponse> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid ReservaResponse.ReservaUpdateRequest reservaUpdateRequest) {
        return null;
    }

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<Void> cancelar(@PathVariable Long id) {
        return null;
    }



}

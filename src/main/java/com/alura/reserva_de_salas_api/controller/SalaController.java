package com.alura.reserva_de_salas_api.controller;

import com.alura.reserva_de_salas_api.dto.response.ReservaResponse;
import com.alura.reserva_de_salas_api.dto.request.SalaRequest;
import com.alura.reserva_de_salas_api.dto.response.SalaResponse;
import com.alura.reserva_de_salas_api.dto.request.SalaUpdateRequest;
import com.alura.reserva_de_salas_api.service.SalaService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
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
        SalaResponse salaResponse = salaService.cadastrar(salaRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(salaResponse);
    }

    @GetMapping
    public ResponseEntity<List<SalaResponse>> lista(){
        return null;
    }

    @GetMapping("?nome=")
    public ResponseEntity<SalaResponse> buscaPorNome(@RequestParam String nome){
        SalaResponse salaResponse = salaService.buscar(nome);
        return ResponseEntity.ok(salaResponse);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<SalaResponse> atualizaPorId(@PathVariable Long id, @RequestBody @Valid SalaUpdateRequest salaUpdateRequest){
        SalaResponse salaResponse = salaService.atualizar(id,salaUpdateRequest);
        return ResponseEntity.ok(salaResponse);
    }

    @PatchMapping("/{id}/desativar")
    public ResponseEntity<SalaResponse> desativaPorId(@PathVariable Long id){
        SalaResponse salaResponse = salaService.desativar(id);
        return ResponseEntity.ok(salaResponse);
    }

    @PatchMapping("/{id}/ativar")
    public ResponseEntity<SalaResponse> ativaPorId(@PathVariable Long id){
        SalaResponse salaResponse = salaService.ativar(id);
        return ResponseEntity.ok(salaResponse);
    }

    @GetMapping("/disponiveis")
    public ResponseEntity<List<SalaResponse>> listarDisponiveis(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim,
            @RequestParam(defaultValue = "1") @Min(1) Integer capacidade) {

        return ResponseEntity.ok(salaService.listarDisponiveis(inicio, fim, capacidade));
    }

    @GetMapping("{id}/reservas")
    public ResponseEntity<List<ReservaResponse>> listaReservasPorId(@PathVariable Long id){
        List<ReservaResponse> reservasResponse = salaService.listarReservas(id);
        return ResponseEntity.ok(reservasResponse);
    }


}

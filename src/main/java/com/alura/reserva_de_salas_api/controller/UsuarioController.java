package com.alura.reserva_de_salas_api.controller;


import com.alura.reserva_de_salas_api.dto.ReservaResponse;
import com.alura.reserva_de_salas_api.dto.UsuarioRequest;
import com.alura.reserva_de_salas_api.dto.UsuarioResponse;
import com.alura.reserva_de_salas_api.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<UsuarioResponse> cadastro(@RequestBody @Valid UsuarioRequest usuarioRequest){

    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponse>> lista(){

    }

    @GetMapping("?email=")
    public ResponseEntity<UsuarioResponse> buscaPorEmail(@RequestParam(required = false) String email){

    }

    @PatchMapping("/{id}")
    public ResponseEntity<UsuarioResponse> atualizaPorId(@PathVariable Long id, @RequestBody @Valid ){

    }

    @PatchMapping("/{id}/desativar")
    public ResponseEntity<UsuarioResponse> desativaPorId(@PathVariable Long id){

    }

    @GetMapping("/{id}/reservas")
    public ResponseEntity<List<ReservaResponse>> listaReservaPorId(@PathVariable Long id){

    }
}

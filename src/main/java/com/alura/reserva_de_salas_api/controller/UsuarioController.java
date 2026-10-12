package com.alura.reserva_de_salas_api.controller;


import com.alura.reserva_de_salas_api.dto.response.ReservaResponse;
import com.alura.reserva_de_salas_api.dto.request.UsuarioRequest;
import com.alura.reserva_de_salas_api.dto.response.UsuarioResponse;
import com.alura.reserva_de_salas_api.dto.request.UsuarioUpdateRequest;
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
        return null;
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponse>> lista(){
        return null;
    }

    @GetMapping("?email=")
    public ResponseEntity<UsuarioResponse> buscaPorEmail(@RequestParam(required = false) String email){
        return null;
    }

    @PatchMapping("?email=")
    public ResponseEntity<UsuarioResponse> atualizaPorEmail(@RequestParam(required = false) String email , @RequestBody @Valid UsuarioUpdateRequest usuarioUpdateRequest){
        return null;
    }

    @PatchMapping("/{id}/desativar")
    public ResponseEntity<UsuarioResponse> desativaPorId(@PathVariable Long id){
        return null;
    }

    @GetMapping("/{id}/reservas")
    public ResponseEntity<List<ReservaResponse>> listaReservaPorId(@PathVariable Long id){
        return null;
    }
}

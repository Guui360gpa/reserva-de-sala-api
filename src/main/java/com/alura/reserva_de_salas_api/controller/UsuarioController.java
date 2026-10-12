package com.alura.reserva_de_salas_api.controller;


import com.alura.reserva_de_salas_api.dto.response.ReservaResponse;
import com.alura.reserva_de_salas_api.dto.request.UsuarioRequest;
import com.alura.reserva_de_salas_api.dto.response.UsuarioResponse;
import com.alura.reserva_de_salas_api.dto.request.UsuarioUpdateRequest;
import com.alura.reserva_de_salas_api.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
        UsuarioResponse usuarioResponse = usuarioService.cadastrar(usuarioRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioResponse);
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponse>> lista(){
        List<UsuarioResponse> usuariosResponse = usuarioService.listar();
        return ResponseEntity.ok(usuariosResponse);
    }

    @GetMapping("?email=")
    public ResponseEntity<UsuarioResponse> buscaPorEmail(@RequestParam(required = false) String email){
        UsuarioResponse usuarioResponse = usuarioService.buscar(email);
        return ResponseEntity.ok(usuarioResponse);
    }

    @PatchMapping("?email=")
    public ResponseEntity<UsuarioResponse> atualizaPorEmail(@RequestParam(required = false) String email , @RequestBody @Valid UsuarioUpdateRequest usuarioUpdateRequest){
        UsuarioResponse usuarioResponse = usuarioService.atualizar(email,usuarioUpdateRequest);
        return ResponseEntity.ok(usuarioResponse);
    }

    @PatchMapping("/{id}/desativar")
    public ResponseEntity<UsuarioResponse> desativaPorId(@PathVariable Long id){
        UsuarioResponse usuarioResponse = usuarioService.desativar(id);
        return ResponseEntity.ok(usuarioResponse);
    }

    @GetMapping("/{id}/reservas")
    public ResponseEntity<List<ReservaResponse>> listaReservaPorId(@PathVariable Long id){
        List<ReservaResponse> reservasResponse = usuarioService.listarReservas(id);
        return ResponseEntity.ok(reservasResponse);
    }
}

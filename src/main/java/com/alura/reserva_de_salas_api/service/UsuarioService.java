package com.alura.reserva_de_salas_api.service;

import com.alura.reserva_de_salas_api.dto.request.UsuarioRequest;
import com.alura.reserva_de_salas_api.dto.response.UsuarioResponse;
import com.alura.reserva_de_salas_api.exception.UsuarioExistenteException;
import com.alura.reserva_de_salas_api.exception.UsuarioNaoEncontradoException;
import com.alura.reserva_de_salas_api.models.StatusUsuario;
import com.alura.reserva_de_salas_api.models.Usuario;
import com.alura.reserva_de_salas_api.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioResponse cadastrar(UsuarioRequest dto){

        String cpf = normalizarCpf(dto.cpf());
        String email = dto.email().trim().toLowerCase();

        //Verifica se email não cadastrado
        if (usuarioRepository.existsByEmail(dto.email())){
            throw new UsuarioExistenteException("Email inválido");
        }

        //Verifica se o cpf foi preenchido
        if (dto.cpf() != null && !dto.cpf().isBlank()){

            //Verifica se CPF não cadastrado
            if (usuarioRepository.existsByCpf(dto.cpf())){
                throw new UsuarioExistenteException("CPF inválido");
            }
        }

        Usuario usuario = new Usuario(
                dto.nome(),
                dto.dataNascimento(),
                email,
                cpf
        );

        return UsuarioResponse.from(usuarioRepository.save(usuario));
    }

    private String normalizarCpf(String cpf) {
        if (cpf == null || cpf.isBlank()) {
            return null;
        }
        return cpf.replaceAll("\\D", "");   // remove pontos e traço
    }

    public List<UsuarioResponse> listar(){
        List<Usuario> usuarios = usuarioRepository.findByStatus(StatusUsuario.ATIVO);

        return usuarios.stream()
                .map(u -> UsuarioResponse.from(u))
                .toList();
    }

    @Transactional(readOnly = true)
    public UsuarioResponse buscar(String email){
        Usuario usuarioEncontrado = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado."));

        return UsuarioResponse.from(usuarioEncontrado);
    }
}

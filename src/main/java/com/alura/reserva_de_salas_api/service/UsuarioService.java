package com.alura.reserva_de_salas_api.service;

import com.alura.reserva_de_salas_api.dto.request.UsuarioRequest;
import com.alura.reserva_de_salas_api.dto.request.UsuarioUpdateRequest;
import com.alura.reserva_de_salas_api.dto.response.ReservaResponse;
import com.alura.reserva_de_salas_api.dto.response.UsuarioResponse;
import com.alura.reserva_de_salas_api.exception.UsuarioExistenteException;
import com.alura.reserva_de_salas_api.exception.UsuarioNaoEncontradoException;
import com.alura.reserva_de_salas_api.models.Reserva;
import com.alura.reserva_de_salas_api.models.StatusUsuario;
import com.alura.reserva_de_salas_api.models.Usuario;
import com.alura.reserva_de_salas_api.repository.ReservaRepository;
import com.alura.reserva_de_salas_api.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final ReservaRepository reservaRepository;

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
        Usuario usuarioEncontrado = usuarioRepository.findByEmail(email.trim())
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado."));

        return UsuarioResponse.from(usuarioEncontrado);
    }

    public UsuarioResponse atualizar(String email, UsuarioUpdateRequest dto){
        Usuario usuario = usuarioRepository.findByEmailIgnoreCaseAndStatus(email.trim(), StatusUsuario.ATIVO)
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado."));

        if (dto.nome() != null){
            usuario.setName(dto.nome());
        }

        if (dto.dataNascimento() != null){
            usuario.setDataNascimento(dto.dataNascimento());
        }

        if (dto.email() != null){
            String novoEmail = dto.email().trim().toLowerCase();
            if (usuarioRepository.existsByEmailIgnoreCaseAndIdNot(novoEmail,usuario.getId())){
                throw new UsuarioExistenteException("Email já cadastrado.");
            }
            usuario.setEmail(dto.email());
        }

        if (dto.cpf() != null){
            String novoCpf = normalizarCpf(dto.cpf());
            if (novoCpf != null && usuarioRepository.existsByCpfAndIdNot(novoCpf, usuario.getId())){
                throw new UsuarioExistenteException("CPF já cadastrado");
            }
            usuario.setCpf(dto.cpf());
        }

        return UsuarioResponse.from(usuarioRepository.save(usuario));
    }

    @Transactional
    public UsuarioResponse desativar(Long id){
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado"));

        usuario.setStatusUsuario(StatusUsuario.INATIVO);

        return UsuarioResponse.from(usuario);
    }

    @Transactional(readOnly = true)
    public List<ReservaResponse> listarReservas(Long id){
        if (!usuarioRepository.existsByIdAndStatusUsuario(id,StatusUsuario.ATIVO)){
            throw new UsuarioNaoEncontradoException("Usuário não encontrado.");
        }

        List<Reserva> reservasUsuario = reservaRepository.findByUsuarioId(id);

        return reservasUsuario.stream()
                .map(ReservaResponse::from)
                .toList();
    }
}

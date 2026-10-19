package br.com.aguaalerta.controller;

import br.com.aguaalerta.dto.UsuarioRequest;
import br.com.aguaalerta.dto.LoginRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.aguaalerta.agua_alerta.model.Usuario;
import com.aguaalerta.agua_alerta.repository.UsuarioRepository;
import java.util.List;
import java.util.Objects;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin("*")
public class UsuarioController {
    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UsuarioController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    @GetMapping("/{id}")
    public Usuario buscarUsuario(@PathVariable Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado."));
    }

  @PostMapping
public Usuario cadastrarUsuario(@RequestBody UsuarioRequest usuarioRequest) {

    Usuario usuario = new Usuario(
            usuarioRequest.getNome(),
            usuarioRequest.getEmail(),
            usuarioRequest.getSenha(),
            usuarioRequest.getCpf(),
            usuarioRequest.getTelefone()
    );

        usuario.setSenha(passwordEncoder.encode(usuarioRequest.getSenha()));
        return usuarioRepository.save(usuario);
    }

    @PostMapping("/login")
    public Usuario login(@RequestBody LoginRequest dados) {
        if (dados.getEmail() == null || dados.getSenha() == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "E-mail ou senha inválidos.");
        }

        Usuario usuario = usuarioRepository.findByEmailIgnoreCase(dados.getEmail().trim())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED, "E-mail ou senha inválidos."));

        String senhaArmazenada = usuario.getSenha();
        if (isBcryptHash(senhaArmazenada)) {
            if (!passwordEncoder.matches(dados.getSenha(), senhaArmazenada)) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "E-mail ou senha inválidos.");
            }
        } else if (Objects.equals(senhaArmazenada, dados.getSenha())) {
            usuario.setSenha(passwordEncoder.encode(dados.getSenha()));
            usuarioRepository.save(usuario);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "E-mail ou senha inválidos.");
        }

        return usuario;
    }

  @PutMapping("/{id}")
public Usuario atualizarUsuario(@PathVariable Long id, @RequestBody UsuarioRequest dados) {

    Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado."));

    usuario.setNome(dados.getNome());
    usuario.setEmail(dados.getEmail());
    if (dados.getSenha() != null && !dados.getSenha().isBlank()) {
        usuario.setSenha(passwordEncoder.encode(dados.getSenha()));
    }
    usuario.setCpf(dados.getCpf());
    usuario.setTelefone(dados.getTelefone());

    return usuarioRepository.save(usuario);
}

   @DeleteMapping("/{id}")
public String excluirUsuario(@PathVariable Long id) {

    usuarioRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado."));

    usuarioRepository.deleteById(id);

    return "Usuário " + id + " excluído com sucesso!";
}

    private boolean isBcryptHash(String senha) {
        return senha != null
                && (senha.startsWith("$2a$") || senha.startsWith("$2b$") || senha.startsWith("$2y$"));
    }
}
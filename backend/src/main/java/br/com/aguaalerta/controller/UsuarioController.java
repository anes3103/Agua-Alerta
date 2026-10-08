package br.com.aguaalerta.controller;

import br.com.aguaalerta.dto.UsuarioRequest;
import org.springframework.web.bind.annotation.*;
import com.aguaalerta.agua_alerta.model.Usuario;
import com.aguaalerta.agua_alerta.repository.UsuarioRepository;
import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin("*")
public class UsuarioController {
    private final UsuarioRepository usuarioRepository;

public UsuarioController(UsuarioRepository usuarioRepository) {
    this.usuarioRepository = usuarioRepository;
}

    @GetMapping
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    @GetMapping("/{id}")
    public Usuario buscarUsuario(@PathVariable Long id) {
    return usuarioRepository.findById(id).orElse(null);
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

    return usuarioRepository.save(usuario);
}

    

  @PutMapping("/{id}")
public Usuario atualizarUsuario(@PathVariable Long id, @RequestBody UsuarioRequest dados) {

    Usuario usuario = usuarioRepository.findById(id).orElse(null);

    if (usuario == null) {
        return null;
    }

    usuario.setNome(dados.getNome());
    usuario.setEmail(dados.getEmail());
    usuario.setSenha(dados.getSenha());
    usuario.setCpf(dados.getCpf());
    usuario.setTelefone(dados.getTelefone());

    return usuarioRepository.save(usuario);
}

   @DeleteMapping("/{id}")
public String excluirUsuario(@PathVariable Long id) {

    Usuario usuario = usuarioRepository.findById(id).orElse(null);

    if (usuario == null) {
        return "Usuário não encontrado!";
    }

    usuarioRepository.deleteById(id);

    return "Usuário " + id + " excluído com sucesso!";
}
}
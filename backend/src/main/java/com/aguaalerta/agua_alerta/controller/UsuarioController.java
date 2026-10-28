package com.aguaalerta.agua_alerta.controller;


import com.aguaalerta.agua_alerta.model.Usuario;
import com.aguaalerta.agua_alerta.repository.UsuarioRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.Map;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {
    private final UsuarioRepository repository;
    public UsuarioController(UsuarioRepository repository) { this.repository = repository; }

    @PostMapping
    public ResponseEntity<?> cadastrar(@RequestBody Usuario usuario) {
        if (usuario.getEmail() == null || usuario.getEmail().isBlank() || usuario.getSenha() == null || usuario.getSenha().isBlank())
            return ResponseEntity.badRequest().body(Map.of("message", "E-mail e senha são obrigatórios."));
        if (repository.existsByEmail(usuario.getEmail()))
            return ResponseEntity.status(409).body(Map.of("message", "Já existe uma conta com esse e-mail."));
        if (usuario.getCpf() != null && !usuario.getCpf().isBlank() && repository.existsByCpf(usuario.getCpf()))
            return ResponseEntity.status(409).body(Map.of("message", "Já existe uma conta com esse CPF."));
        return ResponseEntity.ok(repository.save(usuario));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String,String> dados) {
        String email = dados.get("email");
        String senha = dados.get("senha");
        if (email == null || senha == null)
            return ResponseEntity.status(401).body(Map.of("message", "E-mail ou senha inválidos."));
        return repository.findAllByEmail(email).stream()
            .filter(usuario -> senha.equals(usuario.getSenha()))
            .findFirst()
            .<ResponseEntity<?>>map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.status(401).body(Map.of("message", "E-mail ou senha inválidos.")));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscar(@PathVariable Long id) {
        return repository.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody Usuario dados) {
        return repository.findById(id).map(u -> {
            if (dados.getEmail() != null && !dados.getEmail().equals(u.getEmail()) && repository.existsByEmail(dados.getEmail()))
                return ResponseEntity.status(409).body(Map.of("message", "Já existe uma conta com esse e-mail."));
            if (dados.getCpf() != null && !dados.getCpf().isBlank() && !dados.getCpf().equals(u.getCpf()) && repository.existsByCpf(dados.getCpf()))
                return ResponseEntity.status(409).body(Map.of("message", "Já existe uma conta com esse CPF."));
            u.setNome(dados.getNome()); u.setEmail(dados.getEmail()); u.setCpf(dados.getCpf()); u.setTelefone(dados.getTelefone());
            if (dados.getSenha() != null && !dados.getSenha().isBlank()) u.setSenha(dados.getSenha());
            return ResponseEntity.ok().body(repository.save(u));
        }).orElse(ResponseEntity.notFound().build());
    }
}

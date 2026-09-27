package br.com.aguaalerta.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @GetMapping
    public String listarUsuarios() {
        return "Lista de usuários";
    }

    @GetMapping("/{id}")
    public String buscarUsuario(@PathVariable Long id) {
        return "Usuário de ID: " + id;
    }

    @PostMapping
    public String cadastrarUsuario(@RequestBody String dados) {
        return "Usuário cadastrado com sucesso!";
    }

    @PutMapping("/{id}")
    public String atualizarUsuario(@PathVariable Long id, @RequestBody String dados) {
        return "Usuário " + id + " atualizado com sucesso!";
    }

    @DeleteMapping("/{id}")
    public String excluirUsuario(@PathVariable Long id) {
        return "Usuário " + id + " excluído com sucesso!";
    }
}
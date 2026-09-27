package br.com.aguaalerta.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/denuncias")
public class DenunciaController {

    @GetMapping
    public String listarDenuncias() {
        return "Lista de denúncias";
    }

    @GetMapping("/{id}")
    public String buscarDenuncia(@PathVariable Long id) {
        return "Denúncia de ID: " + id;
    }

    @PostMapping
    public String cadastrarDenuncia(@RequestBody String dados) {
        return "Denúncia cadastrada com sucesso!";
    }

    @PutMapping("/{id}")
    public String atualizarDenuncia(@PathVariable Long id, @RequestBody String dados) {
        return "Denúncia " + id + " atualizada com sucesso!";
    }

    @DeleteMapping("/{id}")
    public String excluirDenuncia(@PathVariable Long id) {
        return "Denúncia " + id + " excluída com sucesso!";
    }
}
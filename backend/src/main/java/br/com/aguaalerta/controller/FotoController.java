package br.com.aguaalerta.controller;

import br.com.aguaalerta.dto.FotoRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/fotos")
public class FotoController {

    @GetMapping
    public String listarFotos() {
        return "Lista de fotos";
    }

    @GetMapping("/{id}")
    public String buscarFoto(@PathVariable Long id) {
        return "Foto de ID: " + id;
    }

    @PostMapping
    public String cadastrarFoto(@RequestBody FotoRequest foto) {
    return "Foto recebida: "
            + foto.getNomeArquivo()
            + " | URL: " + foto.getUrl()
            + " | Denúncia: " + foto.getDenunciaId();
    }

    @PutMapping("/{id}")
    public String atualizarFoto(@PathVariable Long id, @RequestBody String dados) {
        return "Foto " + id + " atualizada com sucesso!";
    }

    @DeleteMapping("/{id}")
    public String excluirFoto(@PathVariable Long id) {
        return "Foto " + id + " excluída com sucesso!";
    }
}
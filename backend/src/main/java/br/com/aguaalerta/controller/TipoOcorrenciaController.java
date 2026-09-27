package br.com.aguaalerta.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tipos-ocorrencia")
public class TipoOcorrenciaController {

    @GetMapping
    public String listarTiposOcorrencia() {
        return "Lista de tipos de ocorrência";
    }

    @GetMapping("/{id}")
    public String buscarTipoOcorrencia(@PathVariable Long id) {
        return "Tipo de ocorrência de ID: " + id;
    }

    @PostMapping
    public String cadastrarTipoOcorrencia(@RequestBody String dados) {
        return "Tipo de ocorrência cadastrado com sucesso!";
    }

    @PutMapping("/{id}")
    public String atualizarTipoOcorrencia(@PathVariable Long id, @RequestBody String dados) {
        return "Tipo de ocorrência " + id + " atualizado com sucesso!";
    }

    @DeleteMapping("/{id}")
    public String excluirTipoOcorrencia(@PathVariable Long id) {
        return "Tipo de ocorrência " + id + " excluído com sucesso!";
    }
}
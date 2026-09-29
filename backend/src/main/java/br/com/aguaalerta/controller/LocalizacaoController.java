package br.com.aguaalerta.controller;

import br.com.aguaalerta.dto.LocalizacaoRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/localizacoes")
public class LocalizacaoController {

    @GetMapping
    public String listarLocalizacoes() {
        return "Lista de localizações";
    }

    @GetMapping("/{id}")
    public String buscarLocalizacao(@PathVariable Long id) {
        return "Localização de ID: " + id;
    }

    @PostMapping
    public String cadastrarLocalizacao(@RequestBody LocalizacaoRequest localizacao) {
    return "Localização recebida: "
            + localizacao.getEndereco()
            + " | Cidade: " + localizacao.getCidade()
            + " | Estado: " + localizacao.getEstado();

    }

    @PutMapping("/{id}")
    public String atualizarLocalizacao(@PathVariable Long id, @RequestBody String dados) {
        return "Localização " + id + " atualizada com sucesso!";
    }

    @DeleteMapping("/{id}")
    public String excluirLocalizacao(@PathVariable Long id) {
        return "Localização " + id + " excluída com sucesso!";
    }
}
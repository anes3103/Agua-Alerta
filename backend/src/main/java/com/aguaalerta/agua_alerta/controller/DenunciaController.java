package com.aguaalerta.agua_alerta.controller;

import com.aguaalerta.agua_alerta.model.Denuncia;
import com.aguaalerta.agua_alerta.repository.DenunciaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/denuncias")
@CrossOrigin(origins = "*")
public class DenunciaController {

    @Autowired
    private DenunciaRepository denunciaRepository;

    @GetMapping
    public List<Denuncia> listar() {
        return denunciaRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Denuncia> buscar(@PathVariable Long id) {
        return denunciaRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Denuncia criar(@RequestBody Denuncia denuncia) {
        return denunciaRepository.save(denuncia);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Denuncia> atualizar(
            @PathVariable Long id,
            @RequestBody Denuncia dados) {

        return denunciaRepository.findById(id)
                .map(denuncia -> {
                    denuncia.setTitulo(dados.getTitulo());
                    denuncia.setDescricao(dados.getDescricao());
                    denuncia.setStatus(dados.consultarStatus());
                    denuncia.setUsuario(dados.getUsuario());
                    denuncia.setFotos(dados.getFotos());
                    denuncia.setTipoOcorrencia(dados.getTipoOcorrencia());
                    denuncia.setLocalizacao(dados.getLocalizacao());

                    return ResponseEntity.ok(denunciaRepository.save(denuncia));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {

        if (!denunciaRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        denunciaRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
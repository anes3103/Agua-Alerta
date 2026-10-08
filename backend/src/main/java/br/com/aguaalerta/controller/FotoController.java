package br.com.aguaalerta.controller;

import br.com.aguaalerta.dto.FotoRequest;
import org.springframework.web.bind.annotation.*;
import com.aguaalerta.agua_alerta.model.Foto;
import com.aguaalerta.agua_alerta.model.Denuncia;
import com.aguaalerta.agua_alerta.repository.FotoRepository;
import com.aguaalerta.agua_alerta.repository.DenunciaRepository;
import java.util.List;

@RestController
@RequestMapping("/api/fotos")
@CrossOrigin("*")
public class FotoController { 
    private final FotoRepository fotoRepository;
private final DenunciaRepository denunciaRepository;

public FotoController(FotoRepository fotoRepository, DenunciaRepository denunciaRepository) {
    this.fotoRepository = fotoRepository;
    this.denunciaRepository = denunciaRepository;
    }

     @GetMapping
public List<Foto> listarFotos() {
    return fotoRepository.findAll();
    }

    @GetMapping("/{id}")
    public Foto buscarFoto(@PathVariable Long id) {
    return fotoRepository.findById(id).orElse(null);
    }

   @PostMapping
public Foto cadastrarFoto(@RequestBody FotoRequest dados) {

    Denuncia denuncia = denunciaRepository
            .findById(dados.getDenunciaId())
            .orElse(null);

    if (denuncia == null) {
        return null;
    }

    Foto foto = new Foto(
            dados.getNomeArquivo(),
            dados.getUrl(),
            denuncia
    );

    return fotoRepository.save(foto);
}

    @PutMapping("/{id}")
public Foto atualizarFoto(@PathVariable Long id, @RequestBody FotoRequest dados) {

    Foto foto = fotoRepository.findById(id).orElse(null);

    if (foto == null) {
        return null;
    }

    Denuncia denuncia = denunciaRepository
            .findById(dados.getDenunciaId())
            .orElse(null);

    if (denuncia == null) {
        return null;
    }

    foto.setNomeArquivo(dados.getNomeArquivo());
    foto.setUrl(dados.getUrl());
    foto.setDenuncia(denuncia);

    return fotoRepository.save(foto);
    }

    @DeleteMapping("/{id}")
public String excluirFoto(@PathVariable Long id) {

    Foto foto = fotoRepository.findById(id).orElse(null);

    if (foto == null) {
        return "Foto não encontrada!";
    }

    fotoRepository.deleteById(id);

    return "Foto " + id + " excluída com sucesso!";
    }
}
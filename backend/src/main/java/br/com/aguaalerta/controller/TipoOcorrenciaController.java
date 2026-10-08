package br.com.aguaalerta.controller;

import br.com.aguaalerta.dto.TipoOcorrenciaRequest;
import org.springframework.web.bind.annotation.*;
import com.aguaalerta.agua_alerta.model.TipoOcorrencia;
import com.aguaalerta.agua_alerta.repository.TipoOcorrenciaRepository;
import java.util.List;

@RestController
@RequestMapping("/api/tipos-ocorrencia")
@CrossOrigin("*")
public class TipoOcorrenciaController {
    private final TipoOcorrenciaRepository tipoOcorrenciaRepository;

public TipoOcorrenciaController(TipoOcorrenciaRepository tipoOcorrenciaRepository) {
    this.tipoOcorrenciaRepository = tipoOcorrenciaRepository;
}

   @GetMapping
public List<TipoOcorrencia> listarTiposOcorrencia() {
    return tipoOcorrenciaRepository.findAll();
}

 @GetMapping("/{id}")
public TipoOcorrencia buscarTipoOcorrencia(@PathVariable Long id) {
    return tipoOcorrenciaRepository.findById(id).orElse(null);
}

  @PostMapping
public TipoOcorrencia cadastrarTipoOcorrencia(@RequestBody TipoOcorrenciaRequest dados) {

    TipoOcorrencia tipo = new TipoOcorrencia(dados.getNome());

    return tipoOcorrenciaRepository.save(tipo);
}

   @PutMapping("/{id}")
public TipoOcorrencia atualizarTipoOcorrencia(
        @PathVariable Long id,
        @RequestBody TipoOcorrenciaRequest dados) {

    TipoOcorrencia tipo = tipoOcorrenciaRepository.findById(id).orElse(null);

    if (tipo == null) {
        return null;
    }

    tipo.setNome(dados.getNome());

    return tipoOcorrenciaRepository.save(tipo);
}
 @DeleteMapping("/{id}")
public String excluirTipoOcorrencia(@PathVariable Long id) {

    TipoOcorrencia tipo = tipoOcorrenciaRepository.findById(id).orElse(null);

    if (tipo == null) {
        return "Tipo de ocorrência não encontrado!";
    }

    tipoOcorrenciaRepository.deleteById(id);

    return "Tipo de ocorrência " + id + " excluído com sucesso!";
}

}
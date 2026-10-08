package br.com.aguaalerta.controller;

import br.com.aguaalerta.dto.LocalizacaoRequest;
import org.springframework.web.bind.annotation.*;
import com.aguaalerta.agua_alerta.model.Localizacao;
import com.aguaalerta.agua_alerta.repository.LocalizacaoRepository;
import java.util.List;

@RestController
@RequestMapping("/api/localizacoes")
@CrossOrigin("*")
public class LocalizacaoController {

    private final LocalizacaoRepository localizacaoRepository;

    public LocalizacaoController(LocalizacaoRepository localizacaoRepository) {
        this.localizacaoRepository = localizacaoRepository;
    }

    @GetMapping
    public List<Localizacao> listarLocalizacoes() {
        return localizacaoRepository.findAll();
    }

   @GetMapping("/{id}")
    public Localizacao buscarLocalizacao(@PathVariable Long id) {
        return localizacaoRepository.findById(id).orElse(null);
    }
    


    @PostMapping
public Localizacao cadastrarLocalizacao(@RequestBody LocalizacaoRequest dados) {

    Localizacao localizacao = new Localizacao(
            dados.getEndereco(),
            dados.getBairro(),
            dados.getCidade(),
            dados.getEstado(),
            dados.getCep(),
            dados.getLatitude(),
            dados.getLongitude()
    );

    return localizacaoRepository.save(localizacao);
}

  @PutMapping("/{id}")
public Localizacao atualizarLocalizacao(
        @PathVariable Long id,
        @RequestBody LocalizacaoRequest dados) {

    Localizacao localizacao = localizacaoRepository.findById(id).orElse(null);

    if (localizacao == null) {
        return null;
    }

    localizacao.setEndereco(dados.getEndereco());
    localizacao.setBairro(dados.getBairro());
    localizacao.setCidade(dados.getCidade());
    localizacao.setEstado(dados.getEstado());
    localizacao.setCep(dados.getCep());
    localizacao.setLatitude(dados.getLatitude());
    localizacao.setLongitude(dados.getLongitude());

    return localizacaoRepository.save(localizacao);
}
   @DeleteMapping("/{id}")
public String excluirLocalizacao(@PathVariable Long id) {

    Localizacao localizacao = localizacaoRepository.findById(id).orElse(null);

    if (localizacao == null) {
        return "Localização não encontrada!";
    }

    localizacaoRepository.deleteById(id);

    return "Localização " + id + " excluída com sucesso!";
}
}
package br.com.aguaalerta.controller;

import br.com.aguaalerta.dto.DenunciaRequest;
import org.springframework.web.bind.annotation.*;
import com.aguaalerta.agua_alerta.model.Denuncia;
import com.aguaalerta.agua_alerta.repository.DenunciaRepository;
import com.aguaalerta.agua_alerta.repository.UsuarioRepository;
import com.aguaalerta.agua_alerta.repository.LocalizacaoRepository;
import com.aguaalerta.agua_alerta.repository.TipoOcorrenciaRepository;
import java.util.List;
import com.aguaalerta.agua_alerta.model.Usuario;
import com.aguaalerta.agua_alerta.model.Localizacao;
import com.aguaalerta.agua_alerta.model.TipoOcorrencia;

@RestController
@RequestMapping("/api/denuncias")
public class DenunciaController { private final DenunciaRepository denunciaRepository;
private final UsuarioRepository usuarioRepository;
private final LocalizacaoRepository localizacaoRepository;
private final TipoOcorrenciaRepository tipoOcorrenciaRepository;

public DenunciaController(
        DenunciaRepository denunciaRepository,
        UsuarioRepository usuarioRepository,
        LocalizacaoRepository localizacaoRepository,
        TipoOcorrenciaRepository tipoOcorrenciaRepository) {

    this.denunciaRepository = denunciaRepository;
    this.usuarioRepository = usuarioRepository;
    this.localizacaoRepository = localizacaoRepository;
    this.tipoOcorrenciaRepository = tipoOcorrenciaRepository;
    }

    @GetMapping
    public List<Denuncia> listarDenuncias() {
    return denunciaRepository.findAll();
    }

    @GetMapping("/{id}")
    public Denuncia buscarDenuncia(@PathVariable Long id) {
    return denunciaRepository.findById(id).orElse(null);
    }

   @PostMapping
public Denuncia cadastrarDenuncia(@RequestBody DenunciaRequest dados) {

    TipoOcorrencia tipoOcorrencia = tipoOcorrenciaRepository
            .findById(dados.getTipoOcorrenciaId())
            .orElse(null);

    Usuario usuario = usuarioRepository
            .findById(dados.getUsuarioId())
            .orElse(null);

    Localizacao localizacao = localizacaoRepository
            .findById(dados.getLocalizacaoId())
            .orElse(null);

    if (tipoOcorrencia == null || usuario == null || localizacao == null) {
        return null;
    }

    Denuncia denuncia = new Denuncia(
            dados.getTitulo(),
            dados.getDescricao(),
            tipoOcorrencia,
            usuario,
            localizacao
    );

    return denunciaRepository.save(denuncia);
}
   @PutMapping("/{id}")
public Denuncia atualizarDenuncia(@PathVariable Long id, @RequestBody DenunciaRequest dados) {

    Denuncia denuncia = denunciaRepository.findById(id).orElse(null);

    if (denuncia == null) {
        return null;
    }

    TipoOcorrencia tipoOcorrencia = tipoOcorrenciaRepository
            .findById(dados.getTipoOcorrenciaId())
            .orElse(null);

    Usuario usuario = usuarioRepository
            .findById(dados.getUsuarioId())
            .orElse(null);

    Localizacao localizacao = localizacaoRepository
            .findById(dados.getLocalizacaoId())
            .orElse(null);

    if (tipoOcorrencia == null || usuario == null || localizacao == null) {
        return null;
    }

    denuncia.setTitulo(dados.getTitulo());
    denuncia.setDescricao(dados.getDescricao());
    denuncia.setTipoOcorrencia(tipoOcorrencia);
    denuncia.setUsuario(usuario);
    denuncia.setLocalizacao(localizacao);

    return denunciaRepository.save(denuncia);
    }

   @DeleteMapping("/{id}")
public String excluirDenuncia(@PathVariable Long id) {

    Denuncia denuncia = denunciaRepository.findById(id).orElse(null);

    if (denuncia == null) {
        return "Denúncia não encontrada!";
    }

    denunciaRepository.deleteById(id);

    return "Denúncia " + id + " excluída com sucesso!";
    }
}
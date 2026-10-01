package br.com.aguaalerta.service;

import com.aguaalerta.agua_alerta.model.Denuncia;
import com.aguaalerta.agua_alerta.model.Localizacao;
import com.aguaalerta.agua_alerta.model.TipoOcorrencia;
import com.aguaalerta.agua_alerta.model.Usuario;
import com.aguaalerta.agua_alerta.repository.DenunciaRepository;
import com.aguaalerta.agua_alerta.repository.LocalizacaoRepository;
import com.aguaalerta.agua_alerta.repository.TipoOcorrenciaRepository;
import com.aguaalerta.agua_alerta.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DenunciaService {

    private final DenunciaRepository denunciaRepository;
    private final UsuarioRepository usuarioRepository;
    private final LocalizacaoRepository localizacaoRepository;
    private final TipoOcorrenciaRepository tipoOcorrenciaRepository;

    public DenunciaService(
            DenunciaRepository denunciaRepository,
            UsuarioRepository usuarioRepository,
            LocalizacaoRepository localizacaoRepository,
            TipoOcorrenciaRepository tipoOcorrenciaRepository) {

        this.denunciaRepository = denunciaRepository;
        this.usuarioRepository = usuarioRepository;
        this.localizacaoRepository = localizacaoRepository;
        this.tipoOcorrenciaRepository = tipoOcorrenciaRepository;
    }

    public List<Denuncia> listar() {
        return denunciaRepository.findAll();
    }

    public Denuncia buscarPorId(Long id) {
        return denunciaRepository.findById(id).orElse(null);
    }

    public Denuncia criar(
            String titulo,
            String descricao,
            Long tipoOcorrenciaId,
            Long usuarioId,
            Long localizacaoId) {

        TipoOcorrencia tipoOcorrencia = tipoOcorrenciaRepository
                .findById(tipoOcorrenciaId)
                .orElse(null);

        Usuario usuario = usuarioRepository
                .findById(usuarioId)
                .orElse(null);

        Localizacao localizacao = localizacaoRepository
                .findById(localizacaoId)
                .orElse(null);

        if (tipoOcorrencia == null || usuario == null || localizacao == null) {
            return null;
        }

        Denuncia denuncia = new Denuncia(
                titulo,
                descricao,
                tipoOcorrencia,
                usuario,
                localizacao
        );

        return denunciaRepository.save(denuncia);
    }

    public Denuncia atualizar(
            Long id,
            String titulo,
            String descricao,
            Long tipoOcorrenciaId,
            Long usuarioId,
            Long localizacaoId) {

        Denuncia denuncia = denunciaRepository
                .findById(id)
                .orElse(null);

        if (denuncia == null) {
            return null;
        }

        TipoOcorrencia tipoOcorrencia = tipoOcorrenciaRepository
                .findById(tipoOcorrenciaId)
                .orElse(null);

        Usuario usuario = usuarioRepository
                .findById(usuarioId)
                .orElse(null);

        Localizacao localizacao = localizacaoRepository
                .findById(localizacaoId)
                .orElse(null);

        if (tipoOcorrencia == null || usuario == null || localizacao == null) {
            return null;
        }

        denuncia.setTitulo(titulo);
        denuncia.setDescricao(descricao);
        denuncia.setTipoOcorrencia(tipoOcorrencia);
        denuncia.setUsuario(usuario);
        denuncia.setLocalizacao(localizacao);

        return denunciaRepository.save(denuncia);
    }

    public boolean excluir(Long id) {

        if (!denunciaRepository.existsById(id)) {
            return false;
        }

        denunciaRepository.deleteById(id);
        return true;
    }
}
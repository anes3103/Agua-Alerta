package br.com.aguaalerta.controller;

import br.com.aguaalerta.dto.DenunciaRequest;
import br.com.aguaalerta.service.DenunciaService;
import com.aguaalerta.agua_alerta.model.Denuncia;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/denuncias")
public class DenunciaController {

    private final DenunciaService denunciaService;

    public DenunciaController(DenunciaService denunciaService) {
        this.denunciaService = denunciaService;
    }

    @GetMapping
    public List<Denuncia> listarDenuncias() {
        return denunciaService.listar();
    }

    @GetMapping("/{id}")
    public Denuncia buscarDenuncia(@PathVariable Long id) {
        return denunciaService.buscarPorId(id);
    }

    @PostMapping
    public Denuncia cadastrarDenuncia(@RequestBody DenunciaRequest dados) {

        return denunciaService.criar(
                dados.getTitulo(),
                dados.getDescricao(),
                dados.getTipoOcorrenciaId(),
                dados.getUsuarioId(),
                dados.getLocalizacaoId()
        );
    }

    @PutMapping("/{id}")
    public Denuncia atualizarDenuncia(
            @PathVariable Long id,
            @RequestBody DenunciaRequest dados) {

        return denunciaService.atualizar(
                id,
                dados.getTitulo(),
                dados.getDescricao(),
                dados.getTipoOcorrenciaId(),
                dados.getUsuarioId(),
                dados.getLocalizacaoId()
        );
    }

    @DeleteMapping("/{id}")
    public String excluirDenuncia(@PathVariable Long id) {

        boolean excluida = denunciaService.excluir(id);

        if (!excluida) {
            return "Denúncia não encontrada!";
        }

        return "Denúncia " + id + " excluída com sucesso!";
    }
}
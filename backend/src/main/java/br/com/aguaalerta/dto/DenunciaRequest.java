package br.com.aguaalerta.dto;

public class DenunciaRequest {

    private String titulo;
    private String descricao;
    private Long tipoOcorrenciaId;
    private Long usuarioId;
    private Long localizacaoId;

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Long getTipoOcorrenciaId() {
        return tipoOcorrenciaId;
    }

    public void setTipoOcorrenciaId(Long tipoOcorrenciaId) {
        this.tipoOcorrenciaId = tipoOcorrenciaId;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Long getLocalizacaoId() {
        return localizacaoId;
    }

    public void setLocalizacaoId(Long localizacaoId) {
        this.localizacaoId = localizacaoId;
    }
}
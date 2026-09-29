package br.com.aguaalerta.dto;

public class FotoRequest {

    private String nomeArquivo;
    private String url;
    private Long denunciaId;

    public String getNomeArquivo() {
        return nomeArquivo;
    }

    public void setNomeArquivo(String nomeArquivo) {
        this.nomeArquivo = nomeArquivo;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getDenunciaId() {
        return denunciaId;
    }

    public void setDenunciaId(Long denunciaId) {
        this.denunciaId = denunciaId;
    }
}
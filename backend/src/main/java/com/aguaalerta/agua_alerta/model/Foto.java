package com.aguaalerta.agua_alerta.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
public class Foto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nomeArquivo;
    private String url;
    private Date dataUpload;

    // Construtor vazio, exigido pelo JPA
    public Foto() {
    }

    // Seu construtor de sempre, só sem o id
    public Foto(String nomeArquivo, String url) {
        this.nomeArquivo = nomeArquivo;
        this.url = url;
        this.dataUpload = new Date();
    }

    public void enviar() {
        System.out.println("Foto \"" + nomeArquivo + "\" enviada com sucesso.");
    }

    public void excluir() {
        System.out.println("Foto \"" + nomeArquivo + "\" excluída.");
    }

    public void visualizar() {
        System.out.println("Visualizando foto: " + url);
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNomeArquivo() { return nomeArquivo; }
    public void setNomeArquivo(String nomeArquivo) { this.nomeArquivo = nomeArquivo; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public Date getDataUpload() { return dataUpload; }
}
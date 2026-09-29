package com.aguaalerta.agua_alerta.model;

import jakarta.persistence.*;

@Entity
public class Localizacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String endereco;
    private String bairro;
    private String cidade;
    private String estado;
    private String cep;
    private double latitude;
    private double longitude;

    public Localizacao() {
    }

    public Localizacao(String endereco, String bairro, String cidade, String estado, String cep, double latitude, double longitude) {
        this.endereco = endereco;
        this.bairro = bairro;
        this.cidade = cidade;
        this.estado = estado;
        this.cep = cep;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void cadastrarLocalizacao() {
        System.out.println("Localização cadastrada: " + endereco + ", " + bairro + " - " + cidade + "/" + estado);
    }

    public void atualizarLocalizacao() {
        System.out.println("Localização atualizada.");
    }

    public String obterCoordenadas() {
        return latitude + ", " + longitude;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }

    public String getBairro() { return bairro; }
    public void setBairro(String bairro) { this.bairro = bairro; }

    public String getCidade() { return cidade; }
    public void setCidade(String cidade) { this.cidade = cidade; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getCep() { return cep; }
    public void setCep(String cep) { this.cep = cep; }

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }
}
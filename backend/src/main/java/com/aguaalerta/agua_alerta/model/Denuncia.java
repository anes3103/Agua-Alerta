package com.aguaalerta.agua_alerta.model;

import jakarta.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

@Entity
public class Denuncia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private String descricao;
    private Date data;

    @Enumerated(EnumType.STRING)
    private StatusDenuncia status;

    @ManyToOne
    @JoinColumn(name = "tipo_ocorrencia_id")
    private TipoOcorrencia tipoOcorrencia;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @OneToOne
    @JoinColumn(name = "localizacao_id")
    private Localizacao localizacao;

    @OneToMany(mappedBy = "denuncia", cascade = CascadeType.ALL)
    private List<Foto> fotos;

    public Denuncia() {
    }

    public Denuncia(String titulo, String descricao, TipoOcorrencia tipoOcorrencia,
                    Usuario usuario, Localizacao localizacao) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.tipoOcorrencia = tipoOcorrencia;
        this.usuario = usuario;
        this.localizacao = localizacao;
        this.data = new Date();
        this.status = StatusDenuncia.PENDENTE;
        this.fotos = new ArrayList<>();
    }

    public void registrar() {
        System.out.println("Denúncia \"" + titulo + "\" registrada (" + tipoOcorrencia.getNome() + ").");
    }

    public void editar() {
        System.out.println("Denúncia \"" + titulo + "\" editada.");
    }

    public void alterarStatus(StatusDenuncia novoStatus) {
        this.status = novoStatus;
        System.out.println("Status da denúncia \"" + titulo + "\" alterado para " + novoStatus + ".");
    }

    public void adicionarFoto(Foto foto) {
        fotos.add(foto);
        System.out.println("Foto adicionada. Total de fotos: " + fotos.size());
    }

    public StatusDenuncia consultarStatus() {
        return status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Date getData() {
        return data;
    }

    public TipoOcorrencia getTipoOcorrencia() {
        return tipoOcorrencia;
    }

    public void setTipoOcorrencia(TipoOcorrencia tipoOcorrencia) {
        this.tipoOcorrencia = tipoOcorrencia;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Localizacao getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(Localizacao localizacao) {
        this.localizacao = localizacao;
    }

    public List<Foto> getFotos() {
        return fotos;
    }
}
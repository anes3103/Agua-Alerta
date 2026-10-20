package classes;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

public class Denuncia {
    
    private int id;
    private String titulo;
    private String descricao;
    private Date data;
    private StatusDenuncia status;
    private TipoProblema tipoProblema;

    private Localizaçao localizacao;
    private List<Foto> fotos;

    public Denuncia(int id, String titulo, String descricao, TipoProblema tipoProblema, Localizaçao localizacao) {
        this.id= id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.tipoProblema = tipoProblema;
        this.localizacao = localizacao;
        this.data = new Date();
        this.status = StatusDenuncia.PENDENTE;
        this.fotos = new ArrayList<>();
    }

    public void registrar() {
        System.out.println("Denúncia \"" + titulo + "\" registrada ("+ tipoProblema +"),");
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

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) {this.descricao = descricao;}

    public Date getData() { return data; }

    public TipoProblema getTipoProblema() {return tipoProblema; }
    public void setTipoProblema(TipoProblema tipoProblema) {this.tipoProblema = tipoProblema;}

    public Localizaçao getLocalizaçao() {return localizacao; }
    public void setLocalizaçao(Localizaçao localizacao) {this.localizacao = localizacao;}

    public List<Foto> geFotos() { return fotos; }
}

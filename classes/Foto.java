package classes;

import java.util.Date;

public class Foto {
    
    // Atributos: os dados que cada objeto Foto vai guardar
    private int id;
    private String nomeArquivo;
    private String url;
    private Date dataUpload;

    // Construtor: roda quando da 'new Foto(...)'
    public Foto(int id, String nomeArquivo, String url) {
        this.id = id;
        this.nomeArquivo = nomeArquivo;
        this.url = url;
        this.dataUpload = new Date(); // guarda o momento exato da criação
    }

    //Métodos: as ações que uma Foto sabe fazer
    public void enviar() {
        System.out.println("Foto \"" + nomeArquivo + "\" enviada com sucesso.");
    }

    public void excluir() {
        System.out.println("Foto \"" + nomeArquivo + "\" excluída.");
    }

    public void visualizar(){
        System.out.println("Visualizando foto: " + url);
    }

    // Getters e setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public Date getDataUpload() {
        return dataUpload;
    }
} 

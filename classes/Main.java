package classes;

public class Main {
    public static void main(String[] args) {
        Foto foto1 = new Foto(1, "vazamento_rua.jpg", "https://exemplo.com/fotos/vazamento_rua.jpg");
        
        foto1.enviar();
        foto1.visualizar();

        System.out.println("ID da foto: " + foto1.getId());

        Localizaçao local1 = new Localizaçao(1, "Rua das Flores, 123", "Centro", "Aiuaba", "CE", "63570-000", -6.5556, -40.1111);
    local1.cadastrarLocalizacao();
    System.out.println("Coordenadas: " + local1.obterCoordenadas());

        Usuario user1 = new Usuario(1, " Felipe ferreira", "felipe@gmail.com", "", "12365478965", "1199999999");

        user1.cadastrar();
        user1.login();


        Administrador admin1 = new Administrador(2, " livia", "livia@gmail.com", "12345678", "83843979285", "1199999999");

        admin1.cadastrar();
        admin1.visualizarDenuncia();
        admin1.alterarStatus();
        admin1.editarDenuncia();
        admin1.removerDenuncia();

        Denuncia denuncia1 = new Denuncia(1, "Vazamento na Rua das Flores", "Vazamento constante perto do poste 45", TipoProblema.VAZAMENTO, local1);
denuncia1.registrar();
denuncia1.adicionarFoto(foto1);
denuncia1.alterarStatus(StatusDenuncia.EM_ANALISE);
System.out.println("Status atual: " + denuncia1.consultarStatus());
Denuncia denuncia2 = user1.criarDenuncia(2, "Esgoto a céu aberto na Rua B", "Esgoto vazando na calçada há 3 dias", TipoProblema.ESGOTO_A_CEU_ABERTO, local1);
denuncia2.registrar();
System.out.println("Status da nova denúncia: " + denuncia2.consultarStatus());
    }


}
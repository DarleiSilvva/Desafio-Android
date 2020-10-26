package caatsoft.studio.com.desafioandroidmobills;
/**
 Darlei Silva 26/10/2020 whatsapp: +55 (74) 981050807  Instagram darlei._.s email: darlei.p.d.silva@gmail.com
 **/
public class Usuario {

    private String uuid;
    private String nomeDoUsuario;

    public Usuario() {
    }

    public Usuario(String uuid, String nomeDoUsuario) {
        this.uuid = uuid;
        this.nomeDoUsuario = nomeDoUsuario;
    }

    public String getUuid() {
        return uuid;
    }

    public String getNomeDoUsuario() {
        return nomeDoUsuario;
    }
}

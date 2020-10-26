package caatsoft.studio.com.desafioandroidmobills;

import java.sql.Timestamp;
/**
 Darlei Silva 26/10/2020 whatsapp: +55 (74) 981050807  Instagram darlei._.s email: darlei.p.d.silva@gmail.com
 **/
public class GettersAndSetters implements Comparable <GettersAndSetters> {

    private String id;
    private int valor;
    private String descricao;
    private String data;
    private boolean booleana;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getValor() {
        return valor;
    }

    public void setValor(int valor) {
        this.valor = valor;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public boolean isBooleana() {
        return booleana;
    }

    public void setBooleana(boolean booleana) {
        this.booleana = booleana;
    }

    @Override
    public int compareTo(GettersAndSetters o) {
        return o.valor - this.valor;
    }
}

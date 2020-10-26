package caatsoft.studio.com.desafioandroidmobills;

import java.sql.Timestamp;
import java.util.UUID;
/**
 Darlei Silva 26/10/2020 whatsapp: +55 (74) 981050807  Instagram darlei._.s email: darlei.p.d.silva@gmail.com
 **/
public class Receita implements Comparable <Receita> {

    private String id;
    private int valor;
    private String descricao;
    private Timestamp data;
    private boolean recebido;

    public Receita() {
    }

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

    public Timestamp getData() {
        return data;
    }

    public void setData(Timestamp data) {
        this.data = data;
    }

    public boolean isRecebido() {
        return recebido;
    }

    public void setRecebido(boolean recebido) {
        this.recebido = recebido;
    }

    @Override
    public int compareTo(Receita o) {
        return o.valor - this.valor;
    }
}

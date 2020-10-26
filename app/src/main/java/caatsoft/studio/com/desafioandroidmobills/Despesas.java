package caatsoft.studio.com.desafioandroidmobills;

import java.sql.Timestamp;
import java.util.UUID;
/**
 Darlei Silva 26/10/2020 whatsapp: +55 (74) 981050807  Instagram darlei._.s email: darlei.p.d.silva@gmail.com
 **/
public class Despesas implements Comparable <Despesas> {

    private String id;
    private int valor;
    private String descricao;
    private Timestamp data;
    private boolean pago;

    public Despesas() {
    }

    /*public Despesas (String id, int valor, String descricao, Timestamp data, boolean pago) {
        this.id = id;
        this.valor = valor;
        this.descricao = descricao;
        this.data = data;
        this.pago = pago;
    }*/


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

    public boolean isPago() {
        return pago;
    }

    public void setPago(boolean pago) {
        this.pago = pago;
    }

    @Override
    public int compareTo(Despesas o) {
        return o.valor - this.valor;
    }


}

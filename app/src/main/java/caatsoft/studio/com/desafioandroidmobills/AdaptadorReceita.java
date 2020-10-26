package caatsoft.studio.com.desafioandroidmobills;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

/**
 Darlei Silva 26/10/2020 whatsapp: +55 (74) 981050807  Instagram darlei._.s email: darlei.p.d.silva@gmail.com
 **/

public class AdaptadorReceita extends RecyclerView.Adapter<AdaptadorReceita.ViewHolder> {

    private ArrayList<GettersAndSetters> receitas;
    private Context context;
    TextView excluir;
    TextView editar;
    EditText editValor_text, editDescricao_text;
    TextView editData_text;
    ImageView imageViewBoolean;
    String millisInString;
    boolean aBoolean;
    SimpleDateFormat dateFormat;
    Timestamp dataTimestamp;
    String valor, descricao, data;
    String id;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    public AdaptadorReceita(ArrayList<GettersAndSetters> receitas, Context context) {
        this.receitas = receitas;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.modelo, parent, false);
        excluir = view.findViewById(R.id.excluir_nome);
        editar = view.findViewById(R.id.editar_nome);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.fundo.setBackgroundResource(R.drawable.fundo_receita);
        holder.valor_text.setText(String.valueOf(receitas.get(position).getValor()) + " R$");
        holder.descricao_text.setText(receitas.get(position).getDescricao());
        holder.data_text.setText( receitas.get(position).getData());
        holder.pago_nome.setText("Recebido");
        holder.pago_nome.setTextSize(13.5F);
        if (receitas.get(position).isBooleana()){
            holder.pago_boolean.setImageResource(R.drawable.ic_check_correto);
        } else {
            holder.pago_boolean.setImageResource(R.drawable.ic_check_errado);
        }

        excluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = receitas.get(position).getId();
                apagar ();
            }
        });

        editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                valor = String.valueOf(receitas.get(position).getValor());
                descricao = receitas.get(position).getDescricao();
                data = receitas.get(position).getData();
                aBoolean = receitas.get(position).isBooleana();
                id = receitas.get(position).getId();
                alterar();
            }
        });

    }

    @Override
    public int getItemCount() {
        return receitas.size();
    }

    public void apagar () {
        TextView txtclose, apagar_text;
        RelativeLayout backgroundFundo;

        Dialog dialog = new Dialog(context);

        dialog.setContentView(R.layout.apagar_dados_popup);
        backgroundFundo = (RelativeLayout) dialog.findViewById(R.id.backgroundFundo);
        txtclose = (TextView) dialog.findViewById(R.id.txtclose);
        apagar_text = (TextView) dialog.findViewById(R.id.apagar_text);
        backgroundFundo.setBackgroundResource(R.drawable.fundo_receita);
        apagar_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseApp.initializeApp(context);
                firebaseDatabase = FirebaseDatabase.getInstance();
                databaseReference = firebaseDatabase.getReference();
                Receita receitas = new Receita();
                receitas.setId(id);
                String uid = FirebaseAuth.getInstance().getUid();
                databaseReference.child("Receita"+uid).child(String.valueOf(receitas.getId())).removeValue();
                Toast.makeText(context, "Excluído", Toast.LENGTH_LONG).show();
            }
        });
        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }


    public void alterar () {
        Dialog dialog = new Dialog(context);
        TextView txtclose, excluir, editar, pago_nome;
        RelativeLayout backgroundFundo;
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        millisInString  = dateFormat.format(new Date());
        dataTimestamp = Timestamp.valueOf(millisInString);

        dialog.setContentView(R.layout.dados_popup);
        pago_nome = (TextView) dialog.findViewById(R.id.pago_nome);
        txtclose = (TextView) dialog.findViewById(R.id.txtclose);
        backgroundFundo = (RelativeLayout) dialog.findViewById(R.id.backgroundFundo);
        editValor_text = (EditText) dialog.findViewById(R.id.valor_text);
        editDescricao_text = (EditText) dialog.findViewById(R.id.descricao_text);
        editData_text = (TextView) dialog.findViewById(R.id.data_text);
        imageViewBoolean = (ImageView) dialog.findViewById(R.id.pago_boolean);
        excluir = (TextView) dialog.findViewById(R.id.excluir_nome2);
        editar = (TextView) dialog.findViewById(R.id.editar_nome2);
        editValor_text.setText(valor);
        editDescricao_text.setText(descricao);
        editData_text.setText(data);
        pago_nome.setText("Recebido:");
        if (aBoolean){
            imageViewBoolean.setImageResource(R.drawable.ic_check_correto);
        } else {
            imageViewBoolean.setImageResource(R.drawable.ic_check_errado);
        }
        editData_text.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                editData_text.setText(millisInString);
                return true;
            }
        });
        excluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseApp.initializeApp(context);
                firebaseDatabase = FirebaseDatabase.getInstance();
                databaseReference = firebaseDatabase.getReference();
                Receita receitas = new Receita();
                receitas.setId(id);
                String uid = FirebaseAuth.getInstance().getUid();
                databaseReference.child("Receita"+uid).child(String.valueOf(receitas.getId())).removeValue();
                Toast.makeText(context, "Excluído", Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        });
        imageViewBoolean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (aBoolean){
                    aBoolean = false;
                    imageViewBoolean.setImageResource(R.drawable.ic_check_errado);
                } else {
                    aBoolean = true;
                    imageViewBoolean.setImageResource(R.drawable.ic_check_correto);
                }
            }
        });
        backgroundFundo.setBackgroundResource(R.drawable.fundo_receita);
        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseApp.initializeApp(context);
                firebaseDatabase = FirebaseDatabase.getInstance();
                databaseReference = firebaseDatabase.getReference();
                Receita receitas = new Receita();
                receitas.setId(id);
                receitas.setValor(Integer.parseInt(String.valueOf(editValor_text.getText())));
                receitas.setDescricao(String.valueOf(editDescricao_text.getText()));
                receitas.setData(dataTimestamp);
                receitas.setRecebido(aBoolean);
                String uid = FirebaseAuth.getInstance().getUid();
                databaseReference.child("Receita"+uid).child(String.valueOf(receitas.getId())).setValue(receitas);
                Toast.makeText(context, "Atualizado", Toast.LENGTH_LONG).show();
                //limpar dados
                editValor_text.setText("");
                editDescricao_text.setText("");
                editData_text.setText("");
                aBoolean = false;
                imageViewBoolean.setImageResource(R.drawable.ic_check_errado);
                dialog.dismiss();
            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView valor_text;
        public TextView descricao_text;
        public TextView data_text, pago_nome;
        public ImageView pago_boolean;
        public ConstraintLayout fundo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            valor_text = itemView.findViewById(R.id.valor_text);
            descricao_text = itemView.findViewById(R.id.descricao_text);
            data_text = itemView.findViewById(R.id.data_text);
            pago_nome = itemView.findViewById(R.id.pago_nome);
            pago_boolean = itemView.findViewById(R.id.pago_boolean);
            fundo = itemView.findViewById(R.id.fundo);
        }


    }
}

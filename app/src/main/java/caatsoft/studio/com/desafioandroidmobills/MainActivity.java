package caatsoft.studio.com.desafioandroidmobills;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
/**
 Darlei Silva 26/10/2020 whatsapp: +55 (74) 981050807  Instagram darlei._.s email: darlei.p.d.silva@gmail.com
 **/
public class MainActivity extends AppCompatActivity {

    FloatingActionButton floatingActionButton;
    Dialog dialog;
    EditText editValor_text, editDescricao_text;
    TextView editData_text;
    ImageView imageViewBoolean;
    //HolderDespesa holderDespesa = null;
    AdaptadorDespesa adaptadorDespesa;
    AdaptadorReceita adaptadorReceita;
    int valorInt;
    String descricaoString;
    Timestamp dataTimestamp;
    boolean pagoString;
    String millisInString;
    boolean aBoolean;
    SimpleDateFormat dateFormat;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    RecyclerView recyclerView;
    boolean identificarTipo;
    ArrayList<GettersAndSetters> arrayAdapterDespesa = new ArrayList<GettersAndSetters>();
    ArrayList<GettersAndSetters> arrayAdapterReceita = new ArrayList<GettersAndSetters>();
    Button b1, b2;
    Button button;
    int x = 0;
    String uid;
    int parafixarCadastro;
    SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("   D. A. Mobills ");
        toolbar.setTitleTextColor(Color.parseColor("#FF000000"));
        toolbar.setLogo(R.drawable.ic_logo);
        toolbar.setBackgroundResource(R.color.verde2);
        setSupportActionBar(toolbar);


        int fixarCadastro = getIntent().getIntExtra("FIXAR_CADASTRO", 0);

        settings = getSharedPreferences("fixar", Context.MODE_PRIVATE);

        parafixarCadastro = settings.getInt("fixarCadastro", 0);
        parafixarCadastro += fixarCadastro;

        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("fixarCadastro", parafixarCadastro);
        editor.commit();

        if (parafixarCadastro == 0){
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            finish();
            startActivity(intent);
        }

        dialog = new Dialog(this);
        recyclerView = (RecyclerView) findViewById(R.id.recycleView);

        floatingActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        millisInString  = dateFormat.format(new Date());
        dataTimestamp = Timestamp.valueOf(millisInString);
        uid = FirebaseAuth.getInstance().getUid();
        button = (Button) findViewById(R.id.button_Despesas);
        button.setTextColor(getResources().getColor(R.color.white));
        button.setBackgroundResource(R.color.verde3);
        b2 = button;
        identificarTipo = true;
        adaptadorDespesaClasse();
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (identificarTipo){
                    cadastro(true);
                } else {
                    cadastro(false);
                }
            }
        });

    }

    public void tabClick (View v){

        button = (Button) findViewById(v.getId());

        if (v.getId() == R.id.button_Despesas) {
            tabClickVerificacao(v);
            button.setTextColor(getResources().getColor(R.color.white));
            button.setBackgroundResource(R.color.verde3);
            identificarTipo = true;
            adaptadorDespesaClasse();
        }

        if (v.getId() == R.id.button_Receitas) {
            tabClickVerificacao(v);
            button.setTextColor(getResources().getColor(R.color.white));
            button.setBackgroundResource(R.color.verde3);
            identificarTipo = false;
            adaptadorReceitaClasse();
        }
    }
    public void tabClickVerificacao (View v){
        if (x % 2 == 0){
            x = 1;
            b1  = (Button) findViewById(v.getId());
            if (b2 != null){
                b2.setTextColor(getResources().getColor(R.color.black));
                b2.setBackgroundResource(R.color.verde2);
            }
        } else{
            x = 2 ;
            b2 = (Button) findViewById(v.getId());
            if (b1 != null) {
                b1.setTextColor(getResources().getColor(R.color.black));
                b1.setBackgroundResource(R.color.verde2);
            }

            if (b1 != null && b2 != null ){
                if (b1.getId() == b2.getId()){
                    v.setBackgroundResource(R.color.verde2);
                }
            }
        }
    }

    public void adaptadorDespesaClasse() {
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        databaseReference.child("Despesa"+uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayAdapterDespesa.clear();
                GettersAndSetters gettersAndSetters;
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    gettersAndSetters = new GettersAndSetters();
                    String id = (String) data.child("id").getValue().toString();
                    String valor = (String) data.child("valor").getValue().toString();
                    String descricao = (String) data.child("descricao").getValue();
                    String datah = (String) data.child("data").child("date").getValue().toString();
                    String mes = (String) data.child("data").child("month").getValue().toString();
                    String horas = (String) data.child("data").child("hours").getValue().toString();
                    String minutos = (String) data.child("data").child("minutes").getValue().toString();
                    boolean boolena = (boolean) data.child("pago").getValue();
                    gettersAndSetters.setId(id);
                    gettersAndSetters.setValor(Integer.parseInt(valor));
                    gettersAndSetters.setDescricao(descricao);
                    gettersAndSetters.setData(datah + "/" + mes + " | " + horas+ ":" + minutos);
                    gettersAndSetters.setBooleana(boolena);
                    arrayAdapterDespesa.add(gettersAndSetters);
                }
                adaptadorDespesa = new AdaptadorDespesa(arrayAdapterDespesa, MainActivity.this);
                recyclerView.setAdapter(adaptadorDespesa);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });
    }

    public void adaptadorReceitaClasse() {
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        databaseReference.child("Receita"+uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayAdapterReceita.clear();
                GettersAndSetters gettersAndSetters;
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    gettersAndSetters = new GettersAndSetters();
                    String id = (String) data.child("id").getValue().toString();
                    String valor = (String) data.child("valor").getValue().toString();
                    String descricao = (String) data.child("descricao").getValue();
                    String datah = (String) data.child("data").child("date").getValue().toString();
                    String mes = (String) data.child("data").child("month").getValue().toString();
                    String horas = (String) data.child("data").child("hours").getValue().toString();
                    String minutos = (String) data.child("data").child("minutes").getValue().toString();
                    boolean boolena = (boolean) data.child("recebido").getValue();
                    gettersAndSetters.setId(id);
                    gettersAndSetters.setValor(Integer.parseInt(valor));
                    gettersAndSetters.setDescricao(descricao);
                    gettersAndSetters.setData(datah + "/" + mes + " | " + horas+ ":" + minutos);
                    gettersAndSetters.setBooleana(boolena);
                    arrayAdapterReceita.add(gettersAndSetters);
                }
                adaptadorReceita = new AdaptadorReceita(arrayAdapterReceita, MainActivity.this);
                recyclerView.setAdapter(adaptadorReceita);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });
    }

    public void cadastro (boolean tipo) {
        TextView txtclose, pago_nome;
        RelativeLayout backgroundFundo;

        dialog.setContentView(R.layout.inserir_dados_popup);
        txtclose = (TextView) dialog.findViewById(R.id.txtclose);
        pago_nome = (TextView) dialog.findViewById(R.id.pago_nome);
        backgroundFundo = (RelativeLayout) dialog.findViewById(R.id.backgroundFundo);
        editValor_text = (EditText) dialog.findViewById(R.id.valor_text);
        editDescricao_text = (EditText) dialog.findViewById(R.id.descricao_text);
        editData_text = (TextView) dialog.findViewById(R.id.data_text);
        imageViewBoolean = (ImageView) dialog.findViewById(R.id.pago_boolean);
        editData_text.setText(millisInString);
        if (!identificarTipo){
            pago_nome.setText("Recebido:");
        }
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
        if (tipo){
            backgroundFundo.setBackgroundResource(R.drawable.fundo_despesa);
        } else {
            backgroundFundo.setBackgroundResource(R.drawable.fundo_receita);
        }
        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    public void opcaoDeSelecaoClick (View v){
        String valorS = String.valueOf(editValor_text.getText());
        String descricaoS = String.valueOf(editDescricao_text.getText());

        if (valorS.isEmpty() || valorS == null || descricaoS.isEmpty() || descricaoS == null) {
            Toast.makeText(getBaseContext(), "Insira os valores", Toast.LENGTH_LONG).show();
        } else {
            valorInt = Integer.parseInt(valorS);
            descricaoString = descricaoS;
            dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            millisInString  = dateFormat.format(new Date());
            dataTimestamp = Timestamp.valueOf(millisInString);
            pagoString = aBoolean;
            if (validacao()){
                if (identificarTipo){
                    Despesas despesas = new Despesas();
                    despesas.setId(String.valueOf(UUID.randomUUID()));
                    despesas.setValor(valorInt);
                    despesas.setDescricao(descricaoString);
                    despesas.setData(dataTimestamp);
                    despesas.setPago(pagoString);
                    databaseReference.child("Despesa"+uid).child(String.valueOf(despesas.getId())).setValue(despesas);
                    adaptadorDespesaClasse();
                } else {
                    Receita receita = new Receita();
                    receita.setId(String.valueOf(UUID.randomUUID()));
                    receita.setValor(valorInt);
                    receita.setDescricao(descricaoString);
                    receita.setData(dataTimestamp);
                    receita.setRecebido(pagoString);
                    databaseReference.child("Receita"+uid).child(String.valueOf(receita.getId())).setValue(receita);
                    adaptadorReceitaClasse();
                }
                Toast.makeText(this, "Confirmado", Toast.LENGTH_LONG).show();
                limparDados();
                dialog.dismiss();
            }
        }

    }

    private boolean validacao() {
        boolean x = true;
        if (valorInt <= 0){
            editValor_text.setError("Insira um valor acima de 0");
            x = false;
        }

        if (descricaoString.isEmpty()){
            editDescricao_text.setError("Insira pelo menos um caractere");
            x = false;
        }
        return x;
    }

    public void limparDados (){
        editValor_text.setText("");
        editDescricao_text.setText("");
        editData_text.setText("");
        aBoolean = false;
        imageViewBoolean.setImageResource(R.drawable.ic_check_errado);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.grafico_gerais) {
            estatistica();
            return true;
        } else if (id == R.id.logout) {
            logout();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void estatistica () {
        TextView txtclose;

        dialog.setContentView(R.layout.grafico_dados_popup);
        txtclose = (TextView) dialog.findViewById(R.id.txtclose);

        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }
    public void estatisticaClick (View view) {
        if (view.getId() == R.id.Saldocomtodososvalores){
            Intent intent = new Intent(getApplicationContext(), EstatisticaActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            dialog.dismiss();
            startActivity(intent);
        } else if (view.getId() == R.id.Saldocomosvaloresnaopagos){
            Intent intent = new Intent(getApplicationContext(), EstatisticaNaoPagoActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            dialog.dismiss();
            startActivity(intent);
        } else if (view.getId() == R.id.Saldocomosvaloresdomesatual){
            Intent intent = new Intent(getApplicationContext(), EstatisticaMensalActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            dialog.dismiss();
            startActivity(intent);
        }

    }
    public void logout () {
        TextView txtclose, logout_text;

        dialog.setContentView(R.layout.log_out_dados_popup);
        txtclose = (TextView) dialog.findViewById(R.id.txtclose);
        logout_text = (TextView) dialog.findViewById(R.id.logout_text);
        logout_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parafixarCadastro = settings.getInt("fixarCadastro", 0);
                parafixarCadastro = 0;

                SharedPreferences.Editor editor = settings.edit();
                editor.putInt("fixarCadastro", parafixarCadastro);
                editor.commit();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                finish();
                startActivity(intent);
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
}
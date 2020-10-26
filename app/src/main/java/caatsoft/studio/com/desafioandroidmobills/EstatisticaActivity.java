package caatsoft.studio.com.desafioandroidmobills;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 Darlei Silva 26/10/2020 whatsapp: +55 (74) 981050807  Instagram darlei._.s email: darlei.p.d.silva@gmail.com
 **/
public class EstatisticaActivity extends AppCompatActivity {

    PieChart pieChart;
    int[] colorClassArray = new int[] {Color.parseColor("#9F3C35"),
            Color.parseColor("#429A46")};

    int valorDes, valorRes;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    TextView txtDespesa, txtReceita;
    ArrayList<PieEntry> dataVals = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estatistica);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("   Gráficos");
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        toolbar.setLogo(R.drawable.ic_logo);
        toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.verde3));
        setSupportActionBar(toolbar);

        txtDespesa = (TextView) findViewById(R.id.txtDespesa);
        txtReceita = (TextView) findViewById(R.id.txtReceita);
        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText("Estatística com todos os valores");
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        despesaClasse();
        receitaClasse();
    }
    public void pieChartFun (){
        pieChart = findViewById(R.id.pie_chart);

        PieDataSet pieDataSet = new PieDataSet(dataVals, "");
        pieDataSet.setColors(colorClassArray);

        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.invalidate();
        pieChart.getDescription().setEnabled(false);
        pieChart.setBackgroundColor(ContextCompat.getColor(this, R.color.verde3));
        pieChart.setTransparentCircleRadius(35f);
        pieChart.setHoleRadius(30f);
        pieChart.setHoleColor(ContextCompat.getColor(this, R.color.verde3));

        pieDataSet.setValueTextColor(ContextCompat.getColor(this, R.color.white));
        pieDataSet.setValueTextSize(12f);

        Legend legendPie = pieChart.getLegend();
        legendPie.setTextSize(20f);
        legendPie.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legendPie.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        legendPie.setOrientation(Legend.LegendOrientation.VERTICAL);
        legendPie.setDrawInside(false);
        legendPie.setTextColor(ContextCompat.getColor(this, R.color.white));
    }

    public void despesaClasse() {
        String uid = FirebaseAuth.getInstance().getUid();
        databaseReference.child("Despesa"+uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int x = 0;
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    String valor = (String) data.child("valor").getValue().toString();
                    x += Integer.parseInt(valor);
                }
                txtDespesa.setText(String.valueOf(x)+ " R$");
                valorDes = x;
                dataVals.add(new PieEntry(valorDes, "Despesas"));
                pieChartFun();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });
    }

    public void receitaClasse() {
        String uid = FirebaseAuth.getInstance().getUid();
        databaseReference.child("Receita"+uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int x = 0;
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    String valor = (String) data.child("valor").getValue().toString();
                    x += Integer.parseInt(valor);
                }
                txtReceita.setText(String.valueOf(x)+ " R$");
                valorRes = x;
                dataVals.add(new PieEntry(valorRes, "Receitas"));
                pieChartFun();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.grafico_gerais) {
            estatistica();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void estatistica () {
        TextView txtclose;
        Dialog dialog = new Dialog(this);

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
            startActivity(intent);
        } else if (view.getId() == R.id.Saldocomosvaloresnaopagos){
            Intent intent = new Intent(getApplicationContext(), EstatisticaNaoPagoActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (view.getId() == R.id.Saldocomosvaloresdomesatual){
            Intent intent = new Intent(getApplicationContext(), EstatisticaMensalActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }

    }

}

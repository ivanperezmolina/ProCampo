package com.ivan.procampo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.ivan.procampo.adaptadores.AdaptadorCarroCompras;
import com.ivan.procampo.adaptadores.AdaptadorProductos;
import com.ivan.procampo.modelos.Fertilizantes;
import com.ivan.procampo.modelos.Producto;

import java.util.List;

public class CarroCompra extends AppCompatActivity {
    List<Fertilizantes> carroCompras;

    AdaptadorCarroCompras adaptador;

    RecyclerView rvListaCarro;
    TextView tvTotal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carro_compra);

        carroCompras = (List<Fertilizantes>) getIntent().getSerializableExtra("CarroCompras");

        rvListaCarro = findViewById(R.id.rvListaCarro);
        rvListaCarro.setLayoutManager(new LinearLayoutManager(CarroCompra.this));
        tvTotal = findViewById(R.id.tvTotal);

        adaptador = new AdaptadorCarroCompras(CarroCompra.this,carroCompras,tvTotal);
        rvListaCarro.setAdapter(adaptador);
    }
}

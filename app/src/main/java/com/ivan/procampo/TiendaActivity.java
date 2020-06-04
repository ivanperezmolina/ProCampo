package com.ivan.procampo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ivan.procampo.adaptadores.AdaptadorProductos;
import com.ivan.procampo.adaptadores.FertilizanteAdapter;
import com.ivan.procampo.adaptadores.SulfatosAdapter;
import com.ivan.procampo.modelos.Fertilizantes;
import com.ivan.procampo.modelos.Producto;
import com.ivan.procampo.modelos.Sulfatos;

import java.util.ArrayList;
import java.util.List;

public class TiendaActivity extends AppCompatActivity {

    DatabaseReference reference;
    RecyclerView recyclerView;

    ArrayList<Fertilizantes> list;
    ArrayList<Fertilizantes> carroCompras = new ArrayList<>();


    FertilizanteAdapter adapter;

    SearchView searchView;


    TextView tvCantProductos;

    Button btnVerCarro;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tienda);

        recyclerView = findViewById(R.id.myRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        searchView = findViewById(R.id.buscamelo);

        tvCantProductos = findViewById(R.id.tvCantProductos);

        btnVerCarro = findViewById(R.id.btnVerCarro);

       /* cbCarroFer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (cbCarroFer.isChecked() == true) {
                    numeroArticulos.setText("333");
                    //carroCompra.add(listaProductos.get(position));
                }else if (cbCarroFer.isChecked() == false){
                    numeroArticulos.setText(""+ (Integer.parseInt(numeroArticulos.getText().toString().trim())-1));
                }


        }
        });*/

        list = new ArrayList<Fertilizantes>();

        reference = FirebaseDatabase.getInstance().getReference().child("FERTILIZANTES");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){

                    Fertilizantes f = dataSnapshot1.getValue(Fertilizantes.class);

                    list.add(f);
                }

                Context contexto = TiendaActivity.this;
                adapter = new FertilizanteAdapter(contexto,list,carroCompras,tvCantProductos,btnVerCarro);

                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(TiendaActivity.this,"No",Toast.LENGTH_SHORT).show();
            }
        });


        if(reference != null){
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        if(searchView != null){
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    search(s);
                    return true;
                }
            });

        }
    }

    private void search(String str) {
        ArrayList<Fertilizantes> myList = new ArrayList<>();

        for(Fertilizantes object : list){
            if (object.getNombre().toLowerCase().contains(str.toLowerCase())){

                myList.add(object);

            }

        }

        FertilizanteAdapter adapterClass = new FertilizanteAdapter(TiendaActivity.this,myList,carroCompras,tvCantProductos,btnVerCarro);
        recyclerView.setAdapter(adapterClass);
    }

}

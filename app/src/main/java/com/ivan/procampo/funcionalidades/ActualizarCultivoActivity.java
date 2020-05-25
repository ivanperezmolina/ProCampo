package com.ivan.procampo.funcionalidades;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ivan.procampo.R;
import com.ivan.procampo.modelos.TiposAceitunas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActualizarCultivoActivity extends AppCompatActivity {

    //Base de datos
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    //Botones y elementos
    private TextView codigo,TextoPrueba;

    private EditText nombre;
    private EditText hectareas;
    private Spinner tipoAceituna;
    private EditText localizacion;

    private Button botonActualizarCultivo, botonCancelarActualizarCultivo;

    private FirebaseAuth mAuth;

    String miTipo = "";

    private String tipoAceitunaSeleccionado = "Sin especificar";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_cultivo);

        //Inicializamos Firebase
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        mAuth = FirebaseAuth.getInstance();




        //Relacionamos variables
        codigo = findViewById(R.id.codigoRegistroCultivo);
        nombre = findViewById(R.id.nombreCultivoEditar);
        hectareas = findViewById(R.id.hectareasCultivoEditar);
        tipoAceituna = findViewById(R.id.tipoAceitunaEditar);
        localizacion = findViewById(R.id.localizacionCultivoEditar);

        botonActualizarCultivo = findViewById(R.id.botonEditarCultivo);
        botonCancelarActualizarCultivo = findViewById(R.id.botonCancelarEditarCultivo);

        //Cogemos los extras que nos viene del Fragments
        Bundle extrasDelCultivo = getIntent().getExtras();

        final String elCodigo = extrasDelCultivo.getString("codigoCultivo");
        String elNombre = extrasDelCultivo.getString("nombreCultivo");
        String laHectarea = extrasDelCultivo.getString("hectareasCultivos");
        String elTipo = extrasDelCultivo.getString("tipoDeAceituna");
        String laLocalizacion = extrasDelCultivo.getString("localizacionCultivo");

        miTipo =extrasDelCultivo.getString("tipoDeAceituna");

        tipoAceitunaSeleccionado = extrasDelCultivo.getString("tipoDeAceituna");

        codigo.setText(elCodigo);
        nombre.setText(elNombre);
        hectareas.setText(laHectarea);
        //tipoAceituna.set
        localizacion.setText(laLocalizacion);

        Toast.makeText(ActualizarCultivoActivity.this,"Llega :"+tipoAceitunaSeleccionado,Toast.LENGTH_SHORT).show();

        obtenerDatosTipoAceitunas();

        ///////////////////////////////////////////////
        botonCancelarActualizarCultivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        botonActualizarCultivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String,Object> cultivoMap = new HashMap<>();
                final String actCodigoCultivo = codigo.getText().toString().trim();
                final String actNombreCultivo = nombre.getText().toString().trim();
                final String actHectareaCultivo = hectareas.getText().toString().trim();
                final String actTipoCultivo = tipoAceitunaSeleccionado;
                final String actLocalizacionCultivo = localizacion.getText().toString().trim();

                cultivoMap.put("codigoCultivo",actCodigoCultivo);
                cultivoMap.put("nombreCultivo",actNombreCultivo);
                cultivoMap.put("hectareasCultivo",actHectareaCultivo);
                cultivoMap.put("tipoDeAceituna",actTipoCultivo);
                cultivoMap.put("localizacionCultivo",actLocalizacionCultivo);

                Toast.makeText(ActualizarCultivoActivity.this,elCodigo,Toast.LENGTH_SHORT).show();

                databaseReference.child("CULTIVOS").child(mAuth.getCurrentUser().getUid()).child(elCodigo).updateChildren(cultivoMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(ActualizarCultivoActivity.this,"Cultivo actualizado con exito",Toast.LENGTH_SHORT).show();
                        finish();



                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ActualizarCultivoActivity.this, "Hubo un error al actualizar el cultivo", Toast.LENGTH_SHORT).show();

                    }
                });


            }
        });
    }

    public void obtenerDatosTipoAceitunas(){
        final List<TiposAceitunas> tiposAceitunas = new ArrayList<>();


        tiposAceitunas.add(new TiposAceitunas("666",miTipo));

        databaseReference.child("tiposAceitunas").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot ds: dataSnapshot.getChildren()){
                        String id = ds.getKey();
                        String nombre = ds.child("tipo").getValue().toString();
                        tiposAceitunas.add(new TiposAceitunas(id,nombre));
                    }

                    ArrayAdapter<TiposAceitunas> arrayAdapter = new ArrayAdapter<>(ActualizarCultivoActivity.this, R.layout.support_simple_spinner_dropdown_item,tiposAceitunas);
                    tipoAceituna.setAdapter(arrayAdapter);
                    tipoAceituna.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            tipoAceitunaSeleccionado = parent.getItemAtPosition(position).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    /**
     * Método para traerme el tipo de aceituna guardado en firebase
     * Obtengo el valor y luego cargo los valores que haya tengo en firebase
     * Buscar como poner primero el seleccionado
     */

 /*   private void traerTipoAceituna(){

        Bundle extrasDelCultivo = getIntent().getExtras();

        final String elCodigo = extrasDelCultivo.getString("codigoCultivo");


        databaseReference.child("CULTIVOS").child(mAuth.getCurrentUser().getUid()).child(elCodigo).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for (DataSnapshot ds : dataSnapshot.getChildren()){
                        String tipoAceituna = ds.child("tipoDeAceituna").getValue().toString();

                        TextoPrueba.setText(tipoAceituna);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }*/
}

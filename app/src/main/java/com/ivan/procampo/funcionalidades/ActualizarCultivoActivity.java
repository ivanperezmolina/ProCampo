package com.ivan.procampo.funcionalidades;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ivan.procampo.R;

import java.util.HashMap;
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
        codigo = findViewById(R.id.actCodigoCultivo);
        nombre = findViewById(R.id.actNombreCultivo);
        hectareas = findViewById(R.id.actHectareasCultivo);
        tipoAceituna = findViewById(R.id.actTipoAceituna);
        localizacion = findViewById(R.id.actLocalizacionCultivo);

        TextoPrueba = findViewById(R.id.textoPrueba);

        botonActualizarCultivo = findViewById(R.id.botonActualizarCultivo);
        botonCancelarActualizarCultivo = findViewById(R.id.botonCancelarActualizarCultivo);

        //Cogemos los extras que nos viene del Fragments
        Bundle extrasDelCultivo = getIntent().getExtras();

        final String elCodigo = extrasDelCultivo.getString("codigoCultivo");
        String elNombre = extrasDelCultivo.getString("nombreCultivo");
        String laHectarea = extrasDelCultivo.getString("hectareasCultivos");
        String elTipo = extrasDelCultivo.getString("tipoDeAceituna");
        String laLocalizacion = extrasDelCultivo.getString("localizacionCultivo");

        codigo.setText(elCodigo);
        nombre.setText(elNombre);
        hectareas.setText(laHectarea);
        localizacion.setText(laLocalizacion);


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

package com.ivan.procampo.funcionalidades;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ivan.procampo.R;
import com.ivan.procampo.modelos.Cultivos;
import com.ivan.procampo.modelos.Recolectas;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

public class AnnadirRecolectaActivity extends AppCompatActivity implements View.OnClickListener {

    //Variable para la fecha
    private Button bfecha;

    private EditText efecha;

    private int dia, mes, ano;

    //Mis variables
    private Button botonSubirNuevaRecolecta;

    private Button botonCancelar;

    private EditText fechaRecolecta;

    private EditText kilosRecolecta;

    private Spinner cultivoRecolecta;

    String cultivoRecolectaSeleccionado = "";

    //Firebase
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private StorageReference mStorageReference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_annadir_recolecta);

        //Referencias de fechas

        bfecha = findViewById(R.id.bFecha);
        efecha = findViewById(R.id.efecha);

        bfecha.setOnClickListener(this);

        //Inicializamos Firebase
        FirebaseApp.initializeApp(this);
        mStorageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        //Relacionamos variables

        fechaRecolecta = findViewById(R.id.efecha);
        kilosRecolecta = findViewById(R.id.kilosRecolecta);
        cultivoRecolecta = findViewById(R.id.cultivoRecolecta);

        botonSubirNuevaRecolecta = findViewById(R.id.botonSubirNuevaRecolecta);
        botonCancelar = findViewById(R.id.botonCancelarAnnadirRecolecta);

        //Cogemos los cultivos que tiene el usuario
        obtenerCultivosDelUsuario();

        //Boton cancelar
        botonCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                terminar();



            }
        });

        botonSubirNuevaRecolecta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validacionYSubida();
            }
        });


    }

    private void terminar() {
        Toast.makeText(this, "Adios", Toast.LENGTH_SHORT).show();
        finish();
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    /**
     * Método para traer los cultivos del usuario que quiere añadir una recolecta
     */
    private void obtenerCultivosDelUsuario() {
        final List<Cultivos> cultivosUsuario = new ArrayList<>();
        databaseReference.child("CULTIVOS").child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot ds : dataSnapshot.getChildren()){
                        String id = ds.getKey();
                        String nombre = ds.child("nombreCultivo").getValue().toString();
                        cultivosUsuario.add(new Cultivos(id,nombre));
                    }

                    ArrayAdapter<Cultivos> arrayAdapter = new ArrayAdapter<>(AnnadirRecolectaActivity.this,R.layout.support_simple_spinner_dropdown_item,cultivosUsuario);
                    cultivoRecolecta.setAdapter(arrayAdapter);
                    cultivoRecolecta.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            cultivoRecolectaSeleccionado = parent.getItemAtPosition(position).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }else {
                    finish();
                    //Muestro un mensaje de que no puede añadir recolecta sin cultivos
                    Context context = getApplicationContext();
                    int duration = Toast.LENGTH_LONG;
                    Toast toast = Toast.makeText(context, "No tiene cultivos para añadir una recolecta ", duration);
                    toast.show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    /**
     * Método para poder mostrar el dialogo de la fecha
     * @param v
     */
    @Override
    public void onClick(View v) {
        if (v==bfecha){
            final Calendar c = Calendar.getInstance();
            dia = c.get(Calendar.DAY_OF_MONTH);
            mes = c.get(Calendar.MONTH);
            ano = c.get(Calendar.YEAR);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    efecha.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                }
            },ano,mes,dia);
            datePickerDialog.show();
        }

    }

    /**
     * Método que nos valida to y sube el contenodo a firebase
     *
     */
    private void validacionYSubida() {
        final String fecha = fechaRecolecta.getText().toString();
        final String kilos = kilosRecolecta.getText().toString();
        final String cultivo = cultivoRecolectaSeleccionado;
        final String fotoValeRecolecta = "https://firebasestorage.googleapis.com/v0/b/procampo-ce4e3.appspot.com/o/nohayfoto.png?alt=media&token=42533563-5c60-46d1-8cae-a1db00228186";
        final String fotoDATRecolecta = "https://firebasestorage.googleapis.com/v0/b/procampo-ce4e3.appspot.com/o/nohayfoto.png?alt=media&token=42533563-5c60-46d1-8cae-a1db00228186";

        Recolectas recolecta = new Recolectas();
        recolecta.setCodigoRecolecta(UUID.randomUUID().toString());

        if (fecha.equals("")){
            fechaRecolecta.setError("Campo Obligatorio");
        }

        if (kilos.equals("")){
            kilosRecolecta.setError("Campo Obligatorio");
        }

        if (fecha.equals("") || kilos.equals("")){
            Toast.makeText(AnnadirRecolectaActivity.this,R.string.validacionCampos,Toast.LENGTH_SHORT).show();
        }else{
            recolecta.setFechaRecolecta(fecha);
            recolecta.setKilosRecolecta(kilos);
            recolecta.setCultivoRecolecta(cultivo);
            recolecta.setFotoValeRecolecta(fotoValeRecolecta);
            recolecta.setFotoDATRecolecta(fotoDATRecolecta);

            databaseReference.child("RECOLECTAS").child(mAuth.getCurrentUser().getUid()).child(recolecta.getCodigoRecolecta()).setValue(recolecta);

            Toast.makeText(AnnadirRecolectaActivity.this, "AÑADIDO", Toast.LENGTH_SHORT).show();

            finish();
        }
    }
}

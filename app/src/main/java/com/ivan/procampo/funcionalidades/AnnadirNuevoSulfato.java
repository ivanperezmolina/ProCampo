package com.ivan.procampo.funcionalidades;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
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
import com.ivan.procampo.modelos.Sulfatos;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

public class AnnadirNuevoSulfato extends AppCompatActivity implements View.OnClickListener {
    //Variables
    private Button bfecha;

    private EditText efechaSulfato;

    private int dia, mes, ano;

    //Mis variables
    private Button botonSubirNuevoSulfato;

    private Button botonCancelar;

    private EditText fechaSulfato;

    private EditText tratamientoSulfato;

    private Spinner cultivoSulfato;

    String cultivoSulfatoSeleccionado = "";

    //Firebase
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private StorageReference mStorageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_annadir_nuevo_sulfato);

        //Referencias de fechas

        bfecha = findViewById(R.id.bFecha);
        efechaSulfato = findViewById(R.id.efechaSulfato);

        bfecha.setOnClickListener(this);

        //Inicializamos Firebase
        FirebaseApp.initializeApp(this);
        mStorageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        //Relacionamos variables

        fechaSulfato = findViewById(R.id.efechaSulfato);
        cultivoSulfato = findViewById(R.id.cultivoSulfato);
        tratamientoSulfato = findViewById(R.id.tratamientoSulfato);

        botonSubirNuevoSulfato = findViewById(R.id.botonSubirNuevoSulfato);
        botonCancelar = findViewById(R.id.botonCancelarAnnadirSulfato);

        //Cogemos los cultivos que tiene el usuario
        obtenerCultivosDelUsuario();

        //Boton cancelar
        botonCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        botonSubirNuevoSulfato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validacionYSubida();
            }
        });

    }

    /**
     * Obtengo los datos de los cultivos del usuario
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

                    ArrayAdapter<Cultivos> arrayAdapter = new ArrayAdapter<>(AnnadirNuevoSulfato.this,R.layout.support_simple_spinner_dropdown_item,cultivosUsuario);
                    cultivoSulfato.setAdapter(arrayAdapter);
                    cultivoSulfato.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            cultivoSulfatoSeleccionado = parent.getItemAtPosition(position).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }else {
                    finish();
                    //Muestro un mensaje de que no puede añadir sulfatos sin cultivos
                    Context context = getApplicationContext();
                    int duration = Toast.LENGTH_LONG;
                    Toast toast = Toast.makeText(context, "No tiene cultivos para añadir un sulfato ", duration);
                    toast.show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /**
     * Valido que este all bien y subo a firebase
     *
     */
    private void validacionYSubida() {
        final String fecha = fechaSulfato.getText().toString();
        final String cultivo = cultivoSulfatoSeleccionado;
        final String tratamiento =tratamientoSulfato.getText().toString();

        Sulfatos sulfato = new Sulfatos();
        sulfato.setCodigoSulfato(UUID.randomUUID().toString());


        if (fecha.equals("")){
            fechaSulfato.setError("Campo Obligatorio");
        }

        if (tratamiento.equals("")){
            tratamientoSulfato.setError("Campo Obligatorio");
        }

        if (fecha.equals("") || tratamiento.equals("")){
            Toast.makeText(AnnadirNuevoSulfato.this,R.string.validacionCampos,Toast.LENGTH_SHORT).show();
        }else{
            sulfato.setFechaSulfato(fecha);
            sulfato.setCultivoSulfato(cultivo);
            sulfato.setTratamientoSulfato(tratamiento);

            databaseReference.child("SULFATOS").child(mAuth.getCurrentUser().getUid()).child(sulfato.getCodigoSulfato()).setValue(sulfato);

            Toast.makeText(AnnadirNuevoSulfato.this, "AÑADIDO", Toast.LENGTH_SHORT).show();

            finish();

        }


    }

    /**
     * Metodo de la fecha
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
                    efechaSulfato.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                }
            },ano,mes,dia);
            datePickerDialog.show();
        }
    }
}

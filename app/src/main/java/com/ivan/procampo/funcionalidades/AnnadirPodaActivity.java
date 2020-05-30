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
import com.ivan.procampo.modelos.Podas;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

public class AnnadirPodaActivity extends AppCompatActivity implements View.OnClickListener {
    //Variables para la fecha
    private Button bfechaPoda;

    private EditText efechaPoda;

    private int dia, mes, ano;

    //Mis variables
    private Button botonSubirNuevaPoda, botonCancelar;

    private EditText fechaPoda;

    private Spinner cultivoPoda;

    String cultivoPodaSeleccionado = "";

    //Firebase
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private StorageReference mStorageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_annadir_poda);

        //Fechas
        bfechaPoda = findViewById(R.id.bFechaPoda);
        efechaPoda = findViewById(R.id.efechaPoda);

        bfechaPoda.setOnClickListener(this);

        //Inicializamos Firebase
        FirebaseApp.initializeApp(this);
        mStorageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        //Relacionamos variables
        fechaPoda = findViewById(R.id.efechaPoda);
        cultivoPoda = findViewById(R.id.cultivoPoda);

        botonSubirNuevaPoda = findViewById(R.id.botonSubirNuevaPoda);
        botonCancelar = findViewById(R.id.botonCancelarAnnadirPoda);

        //Cogemos los cultivos que tiene el usuario
        obtenerCultivosDelUsuario();

        //Boton cancelar
        botonCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        botonSubirNuevaPoda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validacionYSubida();
            }
        });

    }//fin onCreate!

    /**
     * Método para subir a firebase bajo comprobaciones
     *
     */
    private void validacionYSubida() {
        final String fecha = fechaPoda.getText().toString();
        final String cultivo = cultivoPodaSeleccionado;

        Podas poda = new Podas();
        poda.setCodigoPoda(UUID.randomUUID().toString());

        if (fecha.equals("")){
            fechaPoda.setError("Campo Obligatorio");
        }

        if (fecha.equals("")){
            Toast.makeText(AnnadirPodaActivity.this,R.string.validacionCampos,Toast.LENGTH_SHORT).show();
        }else {
            poda.setFechaPoda(fecha);
            poda.setCultivoPoda(cultivo);

            databaseReference.child("PODAS").child(mAuth.getCurrentUser().getUid()).child(poda.getCodigoPoda()).setValue(poda);

            Toast.makeText(AnnadirPodaActivity.this, "AÑADIDO", Toast.LENGTH_SHORT).show();

            finish();
        }
    }

    /**
     * Metodo para coger los cultivos del usuario
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

                    ArrayAdapter<Cultivos> arrayAdapter = new ArrayAdapter<>(AnnadirPodaActivity.this,R.layout.support_simple_spinner_dropdown_item,cultivosUsuario);
                    cultivoPoda.setAdapter(arrayAdapter);
                    cultivoPoda.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            cultivoPodaSeleccionado = parent.getItemAtPosition(position).toString();
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
                    Toast toast = Toast.makeText(context, "No tiene cultivos para añadir una poda ", duration);
                    toast.show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    /**
     * Metodo de la fecha en un dialogo
     * @param v
     */
    @Override
    public void onClick(View v) {
        if (v==bfechaPoda){
            final Calendar c = Calendar.getInstance();
            dia = c.get(Calendar.DAY_OF_MONTH);
            mes = c.get(Calendar.MONTH);
            ano = c.get(Calendar.YEAR);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    efechaPoda.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                }
            },ano,mes,dia);
            datePickerDialog.show();
        }
    }
}

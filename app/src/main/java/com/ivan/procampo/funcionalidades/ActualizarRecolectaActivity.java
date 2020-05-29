package com.ivan.procampo.funcionalidades;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
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
import com.ivan.procampo.modelos.Cultivos;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActualizarRecolectaActivity extends AppCompatActivity implements View.OnClickListener {

    //VARIABLES
    //Firebase
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    //Botones y elementos
    private TextView codigoRegistroRecolecta;

    private EditText fechaRecolectaEditar;
    private EditText kilosRecolectaEditar;
    private Spinner cultivoRecolectaEditar;

    private Button botonEditarRecolecta, botonCancelarEditarRecolecta,bFecha;

    private FirebaseAuth mAuth;

    String miCultivo = "";

    private String cultivoSeleccionado = "Ninguno";

    private int dia, mes, ano;

    EditText efecha;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_recolecta);

        //Inicializar todo Firebase
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        mAuth = FirebaseAuth.getInstance();

        efecha= findViewById(R.id.fechaRecolectaEditar);

        //Relacionamos las variables
        codigoRegistroRecolecta = findViewById(R.id.codigoRegistroRecolecta);
        fechaRecolectaEditar = findViewById(R.id.fechaRecolectaEditar);
        kilosRecolectaEditar = findViewById(R.id.kilosRecolectaEditar);
        cultivoRecolectaEditar = findViewById(R.id.cultivoRecolectaEditar);

        botonEditarRecolecta = findViewById(R.id.botonEditarRecolecta);
        botonCancelarEditarRecolecta = findViewById(R.id.botonCancelarEditarRecolecta);
        bFecha = findViewById(R.id.bFechaAc);

        //Cogemos los extras que nos vienen del fragmets
        Bundle extrasDeLaRecolecta = getIntent().getExtras();

        final String elCodigo = extrasDeLaRecolecta.getString("codigoRecolecta");
        String laFecha = extrasDeLaRecolecta.getString("fechaRecolecta");
        String losKilos = extrasDeLaRecolecta.getString("kilosRecolecta");
        String elCultivo = extrasDeLaRecolecta.getString("cultivoRecolecta");

        miCultivo = extrasDeLaRecolecta.getString("cultivoRecolecta");

        cultivoSeleccionado = extrasDeLaRecolecta.getString("cultivoRecolecta");

        //Asignos a los extras las variables

        codigoRegistroRecolecta.setText(elCodigo);
        fechaRecolectaEditar.setText(laFecha);
        kilosRecolectaEditar.setText(losKilos);

        obtenerCultivosDelUsuario();

        /////////////////////////////////////////////
        botonCancelarEditarRecolecta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        bFecha.setOnClickListener(this);

        botonEditarRecolecta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String,Object> recolectaMap = new HashMap<>();

                final String actCodigoRecolecta = codigoRegistroRecolecta.getText().toString().trim();
                final String actFechaRecolecta = fechaRecolectaEditar.getText().toString().trim();
                final String actKilosRecolecta = kilosRecolectaEditar.getText().toString().trim();
                final String actCultivoRecolecta = cultivoSeleccionado;

                recolectaMap.put("codigoRecolecta",actCodigoRecolecta);
                recolectaMap.put("fechaRecolecta",actFechaRecolecta);
                recolectaMap.put("kilosRecolecta",actKilosRecolecta);
                recolectaMap.put("cultivoRecolecta",actCultivoRecolecta);

                databaseReference.child("RECOLECTAS").child(mAuth.getCurrentUser().getUid()).child(elCodigo).updateChildren(recolectaMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(ActualizarRecolectaActivity.this,"Recolecta actualizada con exito",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ActualizarRecolectaActivity.this, "Hubo un error al actualizar la recolecta", Toast.LENGTH_SHORT).show();
                    }
                });



            }
        });


    }

    private void obtenerCultivosDelUsuario() {
        final List<Cultivos> cultivosUsuario = new ArrayList<>();

        cultivosUsuario.add(new Cultivos("609",miCultivo));

        databaseReference.child("CULTIVOS").child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot ds : dataSnapshot.getChildren()){
                        String id = ds.getKey();
                        String nombre = ds.child("nombreCultivo").getValue().toString();

                        cultivosUsuario.add(new Cultivos(id,nombre));
                    }
                    ArrayAdapter<Cultivos> arrayAdapter = new ArrayAdapter<>(ActualizarRecolectaActivity.this,R.layout.support_simple_spinner_dropdown_item,cultivosUsuario);
                    cultivoRecolectaEditar.setAdapter(arrayAdapter);
                    cultivoRecolectaEditar.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            cultivoSeleccionado = parent.getItemAtPosition(position).toString();
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


    @Override
    public void onClick(View v) {
        if (v==bFecha){
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
}

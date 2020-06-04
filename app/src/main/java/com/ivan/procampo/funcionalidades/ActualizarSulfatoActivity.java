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

public class ActualizarSulfatoActivity extends AppCompatActivity {
    //VARIABLES
    //Firebase
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    //Botones y elementos
    private TextView codigoRegistroSulfato;

    private EditText fechaSulfatoEditar;

    private Spinner cultivoSulfatoEditar;

    private EditText tratamientoSulfatoEditar;

    private Button botonEditarSulfato, botonCancelarEditarSulfato,bFechaSulfato;

    private FirebaseAuth mAuth;

    String miCultivo = "";

    private String cultivoSeleccionado = "Ninguno";

    private int dia, mes, ano;

    EditText efechaSulfatoEditar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_sulfato);

        //Inicializar todo Firebase
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        mAuth = FirebaseAuth.getInstance();

        efechaSulfatoEditar= findViewById(R.id.efechaSulfatoEditar);

        //Relacionamos las variables
        codigoRegistroSulfato = findViewById(R.id.codigoRegistroSulfato);
        fechaSulfatoEditar = findViewById(R.id.efechaSulfatoEditar);
        cultivoSulfatoEditar = findViewById(R.id.cultivoSulfatoEditar);
        tratamientoSulfatoEditar = findViewById(R.id.tratamientoSulfatoEditar);

        botonEditarSulfato = findViewById(R.id.botonEditarSulfato);
        botonCancelarEditarSulfato = findViewById(R.id.botonCancelarEditarSulfato);
        bFechaSulfato = findViewById(R.id.bFechaSulfato);

        //Cogemos los extras que nos vienen del fragmets
        Bundle extrasDelSulfato = getIntent().getExtras();

        final String elCodigo = extrasDelSulfato.getString("codigoSulfato");
        String laFecha = extrasDelSulfato.getString("fechaSulfato");
        String elCultivo = extrasDelSulfato.getString("cultivoSulfato");
        String elTratamiento = extrasDelSulfato.getString("tratamientoSulfato");

        miCultivo = extrasDelSulfato.getString("cultivoSulfato");

        cultivoSeleccionado = extrasDelSulfato.getString("cultivoSulfato");

        //Asignos a los extras las variables

        codigoRegistroSulfato.setText(elCodigo);
        fechaSulfatoEditar.setText(laFecha);
        tratamientoSulfatoEditar.setText(elTratamiento);

        obtenerCultivosDelUsuario();

        botonCancelarEditarSulfato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        bFechaSulfato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v==bFechaSulfato){
                    final Calendar c = Calendar.getInstance();
                    dia = c.get(Calendar.DAY_OF_MONTH);
                    mes = c.get(Calendar.MONTH);
                    ano = c.get(Calendar.YEAR);

                    DatePickerDialog datePickerDialog = new DatePickerDialog(ActualizarSulfatoActivity.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            efechaSulfatoEditar.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                        }
                    },ano,mes,dia);
                    datePickerDialog.show();
                }
            }
        });

        botonEditarSulfato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String,Object> sulfatoMap = new HashMap<>();

                final String actCodigoSulfato = codigoRegistroSulfato.getText().toString().trim();
                final String actFechaSulfato = fechaSulfatoEditar.getText().toString().trim();
                final String actCultivoSulfato = cultivoSeleccionado;
                final String actTratamientoSulfato = tratamientoSulfatoEditar.getText().toString().trim();

                sulfatoMap.put("codigoSulfato",actCodigoSulfato);
                sulfatoMap.put("fechaSulfato",actFechaSulfato);
                sulfatoMap.put("cultivoSulfato",actCultivoSulfato);
                sulfatoMap.put("tratamientoSulfato",actTratamientoSulfato);

                databaseReference.child("SULFATOS").child(mAuth.getCurrentUser().getUid()).child(elCodigo).updateChildren(sulfatoMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(ActualizarSulfatoActivity.this,"Sulfato actualizado con exito",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ActualizarSulfatoActivity.this, "Hubo un error al actualizar", Toast.LENGTH_SHORT).show();
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
                    ArrayAdapter<Cultivos> arrayAdapter = new ArrayAdapter<>(ActualizarSulfatoActivity.this,R.layout.support_simple_spinner_dropdown_item,cultivosUsuario);
                    cultivoSulfatoEditar.setAdapter(arrayAdapter);
                    cultivoSulfatoEditar.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
}

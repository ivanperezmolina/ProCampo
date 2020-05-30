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

public class ActualizarPodaActivity extends AppCompatActivity {
    //VARIABLES
    //Firebase
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    //Botones y elementos
    private TextView codigoRegistroPoda;

    private EditText fechaPodaEditar;

    private Spinner cultivoPodaEditar;

    private Button botonEditarPoda, botonCancelarEditarPoda,bFechaPoda;

    private FirebaseAuth mAuth;

    String miCultivo = "";

    private String cultivoSeleccionado = "Ninguno";

    private int dia, mes, ano;

    EditText efecha;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_poda);


        //Inicializar todo Firebase
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        mAuth = FirebaseAuth.getInstance();

        efecha= findViewById(R.id.fechaPodaEditar);

        //Relacionamos las variables
        codigoRegistroPoda = findViewById(R.id.codigoRegistroPoda);
        fechaPodaEditar = findViewById(R.id.fechaPodaEditar);
        cultivoPodaEditar = findViewById(R.id.cultivoPodaEditar);

        botonEditarPoda = findViewById(R.id.botonEditarPoda);
        botonCancelarEditarPoda = findViewById(R.id.botonCancelarEditarPoda);
        bFechaPoda = findViewById(R.id.bFechaPodaActualizar);

        //Cogemos los extras que nos vienen del fragmets
        Bundle extrasDeLaPoda = getIntent().getExtras();

        final String elCodigo = extrasDeLaPoda.getString("codigoPoda");
        String laFecha = extrasDeLaPoda.getString("fechaPoda");
        String elCultivo = extrasDeLaPoda.getString("cultivoPoda");

        miCultivo = extrasDeLaPoda.getString("cultivoPoda");

        cultivoSeleccionado = extrasDeLaPoda.getString("cultivoPoda");

        //Asignos a los extras las variables

        codigoRegistroPoda.setText(elCodigo);
        fechaPodaEditar.setText(laFecha);

        obtenerCultivosDelUsuario();

        botonCancelarEditarPoda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        bFechaPoda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v==bFechaPoda){
                    final Calendar c = Calendar.getInstance();
                    dia = c.get(Calendar.DAY_OF_MONTH);
                    mes = c.get(Calendar.MONTH);
                    ano = c.get(Calendar.YEAR);

                    DatePickerDialog datePickerDialog = new DatePickerDialog(ActualizarPodaActivity.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            efecha.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                        }
                    },ano,mes,dia);
                    datePickerDialog.show();
                }
            }
        });

        botonEditarPoda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String,Object> podaMap = new HashMap<>();

                final String actCodigoPoda = codigoRegistroPoda.getText().toString().trim();
                final String actFechaPoda = fechaPodaEditar.getText().toString().trim();
                final String actCultivoPoda = cultivoSeleccionado;

                podaMap.put("codigoPoda",actCodigoPoda);
                podaMap.put("fechaPoda",actFechaPoda);
                podaMap.put("cultivoPoda",actCultivoPoda);

                databaseReference.child("PODAS").child(mAuth.getCurrentUser().getUid()).child(elCodigo).updateChildren(podaMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(ActualizarPodaActivity.this,"Poda actualizada con exito",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ActualizarPodaActivity.this, "Hubo un error al actualizar la poda", Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });

    }

    /**
     * obtengo cultivos usuario
     */
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
                    ArrayAdapter<Cultivos> arrayAdapter = new ArrayAdapter<>(ActualizarPodaActivity.this,R.layout.support_simple_spinner_dropdown_item,cultivosUsuario);
                    cultivoPodaEditar.setAdapter(arrayAdapter);
                    cultivoPodaEditar.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

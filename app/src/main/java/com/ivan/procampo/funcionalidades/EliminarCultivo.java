package com.ivan.procampo.funcionalidades;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ivan.procampo.R;

public class EliminarCultivo extends AppCompatActivity {

    Button btnBorrar;
    Button btnCancelar;

    private DatabaseReference mDatabase;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eliminar_cultivo);

        btnBorrar = findViewById(R.id.botonEliminarCultivo);
        btnCancelar = findViewById(R.id.botonCancelarEliminarCultivo);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        //Cogemos los extras que nos viene del Fragments
        Bundle extrasDelCultivo = getIntent().getExtras();

        final String elCodigo = extrasDelCultivo.getString("codigoCultivo");


        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase.child("CULTIVOS").child(mAuth.getCurrentUser().getUid()).child(elCodigo).removeValue();

                finish();
            }
        });
    }
}
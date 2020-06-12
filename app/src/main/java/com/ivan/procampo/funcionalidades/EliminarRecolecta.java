package com.ivan.procampo.funcionalidades;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ivan.procampo.R;

public class EliminarRecolecta extends AppCompatActivity {

    Button btnBorrar;
    Button btnCancelar;

    private DatabaseReference mDatabase;

    private FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eliminar_recolecta);

        btnBorrar = findViewById(R.id.botonEliminarRecolecta);
        btnCancelar = findViewById(R.id.botonCancelarEliminarRecolecta);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        //Cogemos los extras que nos viene del Fragments
        Bundle extrasDeLaRecolecta = getIntent().getExtras();

        final String elCodigo = extrasDeLaRecolecta.getString("codigoRecolecta");

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase.child("RECOLECTAS").child(mAuth.getCurrentUser().getUid()).child(elCodigo).removeValue();
                finish();
            }
        });


    }
}
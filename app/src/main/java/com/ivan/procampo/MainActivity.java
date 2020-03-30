package com.ivan.procampo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    //Definimos los botones
    private Button btnLog,btnReg;
    private final int CODIGO_REGISTRO = 1;
    private final int CODIGO_LOGIN = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLog = findViewById(R.id.boton_entrar);
        btnReg = findViewById(R.id.boton_registrarse);

        //Definimos un escuchador para el botón LOGIN
        btnLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pasarALogin = new Intent(MainActivity.this,LoginActivity.class);
                startActivityForResult(pasarALogin,CODIGO_LOGIN);


            }
        });

        //Definimos un escuchador para el botón REGISTRO
        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pasarARegistro = new Intent(MainActivity.this,RegisterActivity.class);
                startActivityForResult(pasarARegistro,CODIGO_REGISTRO);

            }
        });
    }
}

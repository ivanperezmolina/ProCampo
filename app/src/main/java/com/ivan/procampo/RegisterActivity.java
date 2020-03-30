package com.ivan.procampo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ivan.procampo.modelos.Usuario;

public class RegisterActivity extends AppCompatActivity {

    //Declaración de variables
    private EditText nombreRegistro;
    private EditText apellidosRegistro;
    private EditText telefonoRegistro;
    private EditText correoRegistro;
    private EditText contraseñaregistro;
    private Button botonRegistro;
    private Button botonCancelar;

    //Declaración de un objeto firebaseAuth
    private FirebaseAuth mAuth;
    private FirebaseDatabase fbdatabase ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Inicializamos Firebaese Auth
        mAuth = FirebaseAuth.getInstance();
        fbdatabase =  FirebaseDatabase.getInstance() ;

        //Referenciar los campos y botones
        //Se puede hacer con la herramienta @BindView
        nombreRegistro = findViewById(R.id.nombreRegistro);
        apellidosRegistro = findViewById(R.id.apellidosRegistro);
        telefonoRegistro = findViewById(R.id.telefonoRegistro);
        correoRegistro = findViewById(R.id.correoRegistro);
        contraseñaregistro = findViewById(R.id.contraseñaRegistro);

        botonRegistro = findViewById(R.id.botonRegistro);
        botonCancelar = findViewById(R.id.botonCancelar);


        //Añadimos listener al botón
        botonRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                //Obtener el valor de los campos
                final String nombre = nombreRegistro.getText().toString().trim();
                final String apellidos = apellidosRegistro.getText().toString().trim();
                final String telefono = telefonoRegistro.getText().toString().trim();
                final String correo = correoRegistro.getText().toString().trim();
                final String contraseña = contraseñaregistro.getText().toString().trim();


                //Comprobamos que los campos tienen informacion
                if (nombre.isEmpty() || apellidos.isEmpty() || telefono.isEmpty() ||
                        correo.isEmpty() || contraseña.isEmpty())
                {
                    Context context = getApplicationContext();
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, R.string.campo_vacio, duration);
                    toast.show();

                    return ;
                }

                //Procedemos con el sistema de autenticación
                mAuth.createUserWithEmailAndPassword(correo,contraseña).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){//Registrado correctamente
                            //Guarda la info y obtenemos su UID
                            String UID = mAuth.getUid();

                            //Creamos al usuario
                            Usuario agricultor = new Usuario(nombre,apellidos,telefono,correo,contraseña);

                            DatabaseReference dref = fbdatabase.getReference("usuarios");

                            dref.child(UID).setValue(agricultor);

                            mAuth.signOut();

                            setResult(RESULT_OK);
                            finish();

                            Context context = getApplicationContext();
                            int duration = Toast.LENGTH_SHORT;

                            Toast toast = Toast.makeText(context, R.string.registro_ok, duration);
                            toast.show();

                            return;



                        }else{
                            //voy a comprobar que no se haga un doble registro
                            if(task.getException() instanceof FirebaseAuthUserCollisionException){

                                Context context = getApplicationContext();
                                int duration = Toast.LENGTH_SHORT;

                                Toast toast = Toast.makeText(context, R.string.doble_usuario_error, duration);
                                toast.show();
                            }else {
                                //Emitir un error
                                Context context = getApplicationContext();
                                int duration = Toast.LENGTH_SHORT;

                                Toast toast = Toast.makeText(context, R.string.registro_nook, duration);
                                toast.show();
                            }
                        }
                    }
                });
            }
        });

        botonCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent volverAInicio = new Intent(RegisterActivity.this,MainActivity.class);
                startActivity(volverAInicio);
            }
        });

    }
}

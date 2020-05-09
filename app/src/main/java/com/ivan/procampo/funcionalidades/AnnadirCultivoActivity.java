package com.ivan.procampo.funcionalidades;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.ivan.procampo.MainActivity;
import com.ivan.procampo.MenuPrincipal;
import com.ivan.procampo.R;
import com.ivan.procampo.fragmentsMenu.CultivosFragment;
import com.ivan.procampo.modelos.Cultivos;

import java.util.ArrayList;
import java.util.UUID;

public class AnnadirCultivoActivity extends AppCompatActivity {

    //Variables
    //private Button subirFoto;

    //private StorageReference mStorage;

    //private static final int GALLERY_INTENT = 1;

    //private ImageView mImageView;

    //private ProgressDialog mProgressDialog;

    private StorageReference mStorageReference;

    private Button botonCancelar;

    //Variables subida de cultivo

    private EditText nombreCultivo;

    private Spinner tipoAceituna;

    String tipoDeAceituna;

    private EditText hectareasCultivo;

    private EditText localizacionCultivo;

    private Button botonSubirNuevoCultivo;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_annadir_cultivo);

        //
        //mStorage = FirebaseStorage.getInstance().getReference();
        //mImageView = findViewById(R.id.imagenCultivoAnnadir);
        mStorageReference = FirebaseStorage.getInstance().getReference();

        mAuth = FirebaseAuth.getInstance();
        /////////////////////////

        //Relacionar
        tipoAceituna = findViewById(R.id.tipoAceituna);

        botonCancelar = findViewById(R.id.botonCancelarAnnadirCultivo);

        nombreCultivo = findViewById(R.id.nombreCultivo);
        hectareasCultivo = findViewById(R.id.hectareasCultivo);
        localizacionCultivo = findViewById(R.id.localizacionCultivo);

        botonSubirNuevoCultivo = findViewById(R.id.botonSubirNuevoCultivo);

        //Opciones del spinner de tipo de aceituna
        ArrayList<String> tiposAceitunas = new ArrayList<>();


        tiposAceitunas.add("Seleccione un tipo");
        tiposAceitunas.add("Hojiblanca");
        tiposAceitunas.add("Gordal");
        tiposAceitunas.add("Manzanillas");
        tiposAceitunas.add("Picual");
        tiposAceitunas.add("Marteñas");
        tiposAceitunas.add("Pajareras");
        tiposAceitunas.add("Mixto");

        ArrayAdapter adapter;
        adapter = new ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,tiposAceitunas);

        tipoAceituna.setAdapter(adapter);

        tipoAceituna.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tipoDeAceituna = (String) tipoAceituna.getAdapter().getItem(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //Inicializamos firebase
        inicializarFirebase();
        //Boton subir
        //subirFoto = findViewById(R.id.botonSubirFotoCultivo);

        /*subirFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Abrir la galeria
                Intent irAGaleria = new Intent(Intent.ACTION_PICK);
                irAGaleria.setType("image/*");
                startActivityForResult(irAGaleria,GALLERY_INTENT);
            }
        });*/

        //PULSAR BOTON CANCELAR
        botonCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //PULSAR BOTON AÑADIR
        //Comprobaremos que ningun campo; excepto la foto, esten rellenos




        botonSubirNuevoCultivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String nombre = nombreCultivo.getText().toString();
                final String hectareas = hectareasCultivo.getText().toString();
                final String tipoAceituna = tipoDeAceituna;//???
                final String localizacion = localizacionCultivo.getText().toString();

                    validacion();

                    //Añadimos a la base de datos
                    //Crearemos un objetos del modelo Cultivo


                    Cultivos cultivo = new Cultivos();
                    cultivo.setCodigoCultivo(UUID.randomUUID().toString());
                    cultivo.setNombreCultivo(nombre);
                    cultivo.setHectareasCultivo(hectareas);
                if (tipoAceituna.equals("Seleccione un tipo")){
                    cultivo.setTipoDeAceituna("Sin especificar");
                }else{
                    cultivo.setTipoDeAceituna(tipoDeAceituna);
                }

                    cultivo.setLocalizacionCultivo(localizacion);

                    databaseReference.child("CULTIVOS").child(mAuth.getCurrentUser().getUid()).child(cultivo.getCodigoCultivo()).setValue(cultivo);
                    Toast.makeText(AnnadirCultivoActivity.this,"AÑADIDO",Toast.LENGTH_SHORT).show();

                    finish();

            }


        });


    }

    private void inicializarFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    private void validacion() {
        final String nombre = nombreCultivo.getText().toString();
        final String hectareas = hectareasCultivo.getText().toString();
        final String localizacion = localizacionCultivo.getText().toString();

        if (nombre.equals("")){
            nombreCultivo.setError("Campo Obligatorio");
        }

        if (hectareas.equals("")){
            hectareasCultivo.setError("Campo Obligatorio");
        }

        if (localizacion.equals("")){
            localizacionCultivo.setError("Campo Obligatorio");
        }

        Toast.makeText(AnnadirCultivoActivity.this,R.string.validacionCampos,Toast.LENGTH_SHORT).show();
    }

    //Obtener la foto

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        /*if (requestCode==GALLERY_INTENT && resultCode==RESULT_OK){
            //Mostrar dialogo

            //mProgressDialog.setMessage("Su foto se esta subiendo. Un momento");
            //mProgressDialog.setCancelable(false);
            //mProgressDialog.show();

            //Guardamos la foto en un URI
            final Uri uri = data.getData();

            //Almacenamos la foto
            final StorageReference filePath = mStorage.child("fotosCultivos").child(uri.getLastPathSegment());



            //Subimos la foto y lanzamos un toast
            filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    //mProgressDialog.dismiss();

                    //Obtener url de la imagen
                        String downloadUri =taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();




                  Toast.makeText(AnnadirCultivoActivity.this,downloadUri,Toast.LENGTH_SHORT).show();

                    Glide.with(AnnadirCultivoActivity.this).load(downloadUri).into(mImageView);

                }

            });

        }
*/
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////




}

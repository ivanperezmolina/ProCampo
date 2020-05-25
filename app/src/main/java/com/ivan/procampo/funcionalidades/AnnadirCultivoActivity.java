package com.ivan.procampo.funcionalidades;

import androidx.annotation.NonNull;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.ivan.procampo.MainActivity;
import com.ivan.procampo.MenuPrincipal;
import com.ivan.procampo.R;
import com.ivan.procampo.fragmentsMenu.CultivosFragment;
import com.ivan.procampo.modelos.Cultivos;
import com.ivan.procampo.modelos.TiposAceitunas;

import java.util.ArrayList;
import java.util.List;
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

    String tipoAceitunaSeleccionado = "Sin especificar";
    



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_annadir_cultivo);

        //
        //mStorage = FirebaseStorage.getInstance().getReference();
        //mImageView = findViewById(R.id.imagenCultivoAnnadir);
        mStorageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        mAuth = FirebaseAuth.getInstance();
        /////////////////////////

        //Relacionar
        tipoAceituna = findViewById(R.id.tipoAceituna);

        botonCancelar = findViewById(R.id.botonCancelarAnnadirCultivo);

        nombreCultivo = findViewById(R.id.nombreCultivo);
        hectareasCultivo = findViewById(R.id.hectareasCultivo);
        localizacionCultivo = findViewById(R.id.localizacionCultivo);


        //Inicializamos firebase
        inicializarFirebase();
        botonSubirNuevoCultivo = findViewById(R.id.botonSubirNuevoCultivo);

        //Opciones del spinner de tipo de aceituna
        obtenerDatosTipoAceitunas();



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

                    validacionYSubida();

            }


        });


    }

    public void obtenerDatosTipoAceitunas(){
        final List<TiposAceitunas> tiposAceitunas = new ArrayList<>();
        databaseReference.child("tiposAceitunas").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot ds: dataSnapshot.getChildren()){
                        String id = ds.getKey();
                        String nombre = ds.child("tipo").getValue().toString();
                        tiposAceitunas.add(new TiposAceitunas(id,nombre));
                    }

                    ArrayAdapter<TiposAceitunas> arrayAdapter = new ArrayAdapter<>(AnnadirCultivoActivity.this, R.layout.support_simple_spinner_dropdown_item,tiposAceitunas);
                    tipoAceituna.setAdapter(arrayAdapter);
                    tipoAceituna.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            tipoAceitunaSeleccionado = parent.getItemAtPosition(position).toString();
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

    private void inicializarFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    private void validacionYSubida() {
        final String nombre = nombreCultivo.getText().toString();
        final String hectareas = hectareasCultivo.getText().toString();
        final String tipoAceituna = tipoAceitunaSeleccionado;//???
        final String localizacion = localizacionCultivo.getText().toString();

        Cultivos cultivo = new Cultivos();
        cultivo.setCodigoCultivo(UUID.randomUUID().toString());

        if (nombre.equals("")){
            nombreCultivo.setError("Campo Obligatorio");
        }

        if (hectareas.equals("")){
            hectareasCultivo.setError("Campo Obligatorio");
        }

        if (localizacion.equals("")){
            localizacionCultivo.setError("Campo Obligatorio");
        }





        if (nombre.equals("") || hectareas.equals("") || localizacion.equals("") || tipoAceituna.equals("Seleccione un tipo")) {
            Toast.makeText(AnnadirCultivoActivity.this,R.string.validacionCampos,Toast.LENGTH_SHORT).show();
        }else {
            cultivo.setNombreCultivo(nombre);
            cultivo.setHectareasCultivo(hectareas);
            cultivo.setLocalizacionCultivo(localizacion);
            cultivo.setTipoDeAceituna(tipoAceituna);
            databaseReference.child("CULTIVOS").child(mAuth.getCurrentUser().getUid()).child(cultivo.getCodigoCultivo()).setValue(cultivo);
            Toast.makeText(AnnadirCultivoActivity.this, "AÑADIDO", Toast.LENGTH_SHORT).show();

            finish();
        }
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

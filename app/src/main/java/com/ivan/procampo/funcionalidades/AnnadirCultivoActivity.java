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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.ivan.procampo.MainActivity;
import com.ivan.procampo.MenuPrincipal;
import com.ivan.procampo.R;
import com.ivan.procampo.fragmentsMenu.CultivosFragment;

import java.util.ArrayList;

public class AnnadirCultivoActivity extends AppCompatActivity {

    //Variables

    private Spinner tipoAceituna;

    String tipoDeAceituna;

    private Button subirFoto;

    private StorageReference mStorage;

    private static final int GALLERY_INTENT = 1;

    private ImageView mImageView;

    private ProgressDialog mProgressDialog;

    private StorageReference mStorageReference;

    private Button botonCancelar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_annadir_cultivo);

        //
        mStorage = FirebaseStorage.getInstance().getReference();
        mImageView = findViewById(R.id.imagenCultivoAnnadir);
        mStorageReference = FirebaseStorage.getInstance().getReference();

        /////////////////////////

        //Relacionar
        tipoAceituna = findViewById(R.id.tipoAceituna);

        botonCancelar = findViewById(R.id.botonCancelarAnnadirCultivo);

        //Opciones del spinner de tipo de aceituna
        ArrayList<String> tiposAceitunas = new ArrayList<>();


        tiposAceitunas.add("Hojiblanca");
        tiposAceitunas.add("Gordal");
        tiposAceitunas.add("Manzanillas");
        tiposAceitunas.add("Picual");

        ArrayAdapter adapter;
        adapter = new ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,tiposAceitunas);

        tipoAceituna.setAdapter(adapter);

        tipoAceituna.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tipoDeAceituna = (String) tipoAceituna.getAdapter().getItem(position);

                Toast.makeText(AnnadirCultivoActivity.this,"Seleccionaste:"+tipoDeAceituna,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //Boton subir
        subirFoto = findViewById(R.id.botonSubirFotoCultivo);

        subirFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Abrir la galeria
                Intent irAGaleria = new Intent(Intent.ACTION_PICK);
                irAGaleria.setType("image/*");
                startActivityForResult(irAGaleria,GALLERY_INTENT);
            }
        });

        //PULSAR BOTON CANCELAR
        botonCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    //Obtener la foto

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==GALLERY_INTENT && resultCode==RESULT_OK){
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

    }




}

package com.ivan.procampo.funcionalidades;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
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
import com.ivan.procampo.R;
import com.ivan.procampo.adaptadores.RecolectaAdapter;
import com.ivan.procampo.modelos.Recolectas;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class AnnadirFotoValeRecolecta extends AppCompatActivity {

    private Button mUploadBtn;
    private StorageReference mStorage;
    private static final int GALLERY_INTENT = 1;
    private ImageView mImageView;
    private ProgressDialog mProgressDialog;

    //Firebase
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private FirebaseAuth mAuth;

    private boolean subida = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_annadir_foto_vale_recolecta);

        mStorage = FirebaseStorage.getInstance().getReference();
        //Inicializar firebase
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        mAuth = FirebaseAuth.getInstance();
        mUploadBtn = findViewById(R.id.btnSubir);
        mImageView = findViewById(R.id.SubirImagen);
        mProgressDialog = new ProgressDialog(this);

        mUploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,GALLERY_INTENT);
            }
        });

        Bundle extrasDeLaRecolecta = getIntent().getExtras();
        final String elCodigoRecolecta = extrasDeLaRecolecta.getString("codigoRecolecta");

        llenar();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK){
            mProgressDialog.setTitle("Subiendo foto");
            mProgressDialog.setMessage("Subiendo foto a la nube");
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();

            Uri uri = data.getData();

            //Creamos la carpeta y subimos la foto que hay en el uri
            final StorageReference filePath = mStorage.child("VALES").child(uri.getLastPathSegment());


            filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    mProgressDialog.dismiss();
                    Task<Uri> firebaseUri = taskSnapshot.getStorage().getDownloadUrl();
                    firebaseUri.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String url = uri.toString();

                            //Una vez tenemos la url pasamos a almacenarla en la recolecta pertinente
                            Bundle extrasDeLaRecolecta = getIntent().getExtras();
                            final String elCodigo = extrasDeLaRecolecta.getString("codigoRecolecta");

                            Map<String,Object> recolectaMap = new HashMap<>();

                            recolectaMap.put("fotoValeRecolecta",url);

                            //Subida
                            databaseReference.child("RECOLECTAS").child(mAuth.getCurrentUser().getUid()).child(elCodigo).updateChildren(recolectaMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(AnnadirFotoValeRecolecta.this,"Guardada en su ficha",Toast.LENGTH_SHORT).show();
                                    subida = true;

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(AnnadirFotoValeRecolecta.this, "Hubo un error al añadir en su ficha", Toast.LENGTH_SHORT).show();
                                }
                            });


                            //Toast.makeText(AnnadirFotoValeRecolecta.this,url,Toast.LENGTH_LONG).show();
                        }
                    });

                }
            });

            if (subida=true){
            llenar();
            }


        }
    }

    private void llenar(){
        //Una vez tengo la url ya en el firebase; para mas seguridad la cogere de alli

        Bundle extrasDeLaRecolecta = getIntent().getExtras();
        final String elCodigoRecolecta = extrasDeLaRecolecta.getString("codigoRecolecta");

        databaseReference.child("RECOLECTAS").child(mAuth.getCurrentUser().getUid()).child(elCodigoRecolecta).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                        String codigoRecolecta = dataSnapshot.child("codigoRecolecta").getValue().toString();
                        String urlDelValeDelUsuarioConComillas = dataSnapshot.child("fotoValeRecolecta").getValue().toString();
                        String urlDelValeDelUsuarioSinComillas = urlDelValeDelUsuarioConComillas.replace("\"","");

                        Glide.with(getApplicationContext()).load(urlDelValeDelUsuarioSinComillas).fitCenter().centerCrop().into(mImageView);




                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //Y la mostrare con Glide


    }

}

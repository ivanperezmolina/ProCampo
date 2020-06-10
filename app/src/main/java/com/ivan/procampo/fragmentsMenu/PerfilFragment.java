package com.ivan.procampo.fragmentsMenu;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.ivan.procampo.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PerfilFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PerfilFragment extends Fragment {
    TextView textoNombre;
    TextView textoCorreo;
    //Foto de perfil
    private ImageView photoImageView;

    FirebaseAuth mAuth;
    DatabaseReference databaseReference;

    private Button btnCompartir;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PerfilFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PerfilFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PerfilFragment newInstance(String param1, String param2) {
        PerfilFragment fragment = new PerfilFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }





    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View vista = inflater.inflate(R.layout.fragment_perfil, container, false);
        //Referencia a las variables de nombre y correo
        textoNombre = vista.findViewById(R.id.textViewNombrePefil);
        textoCorreo = vista.findViewById(R.id.textViewCorreoPerfil);
        photoImageView = vista.findViewById(R.id.imagenDelPerfil);

        btnCompartir = vista.findViewById(R.id.btnCompartir);

        btnCompartir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent compartir = new Intent(android.content.Intent.ACTION_SEND);
                compartir.setType("text/plain");
                String mensaje = "Te recomiendo ProCampo para organizar tu olivar mejor\n";
                mensaje = mensaje +"https://github.com/ivanperezmolina/ProCampo";
                compartir.putExtra(android.content.Intent.EXTRA_SUBJECT, "ProCampo");
                compartir.putExtra(android.content.Intent.EXTRA_TEXT, mensaje);
                startActivity(Intent.createChooser(compartir, "Compartir vía"));
            }
        });

        mAuth=FirebaseAuth.getInstance();

        traerInfoDelUsuario();

        return vista ;
    }

    private void traerInfoDelUsuario() {
        String id = mAuth.getCurrentUser().getUid();

        if (LoginManager.getInstance()!=null) {
            String nombreUsuario = mAuth.getCurrentUser().getDisplayName();
            String correoUsuario = mAuth.getCurrentUser().getEmail();
            Uri fotoCorreo = mAuth.getCurrentUser().getPhotoUrl();
            String fotoDefecto = "https://firebasestorage.googleapis.com/v0/b/procampo-ce4e3.appspot.com/o/fotopordefectoperfil.jpg?alt=media&token=ea18769d-ede9-44cc-a0ec-f86db595cd36";

            //Asigno valor
            if (textoNombre != null) {
                textoNombre.setText(nombreUsuario);
            }else {
                textoNombre.setText("Nombre no encontrado");
            }
            if (textoCorreo != null) {
                textoCorreo.setText(correoUsuario);
            }else{
                textoCorreo.setText("Correo no encontrado");
            }

            //FOTO DE PERFIL
            if (fotoCorreo != null) {
                Glide.with(this).load(fotoCorreo).apply(RequestOptions.circleCropTransform()).into(photoImageView);
            }else {
                Glide.with(this).load(fotoDefecto).apply(RequestOptions.circleCropTransform()).into(photoImageView);
            }

            //PROBLEMA CON NOMBRE EN FIREBASE


        }else {

            databaseReference.child("usuarios").child(id).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    LoginManager.getInstance().logOut();
                    String nombreUsuarioFirebase = dataSnapshot.child("nombre").getValue().toString();
                    String correoUsuarioFirebase = dataSnapshot.child("email").getValue().toString();

                    if (textoNombre != null) {
                        textoNombre.setText(nombreUsuarioFirebase);
                    }
                    if (textoCorreo != null) {
                        textoCorreo.setText(correoUsuarioFirebase);
                    }
                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }





}

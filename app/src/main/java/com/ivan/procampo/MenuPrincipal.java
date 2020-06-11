package com.ivan.procampo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ivan.procampo.fragmentsMenu.CultivosFragment;
import com.ivan.procampo.fragmentsMenu.FertilizantesFragment;
import com.ivan.procampo.fragmentsMenu.JornalesFragment;
import com.ivan.procampo.fragmentsMenu.PerfilFragment;
import com.ivan.procampo.fragmentsMenu.PodasFragment;
import com.ivan.procampo.fragmentsMenu.RecolectasFragment;
import com.ivan.procampo.fragmentsMenu.SulfatosFragment;
import com.ivan.procampo.funcionalidades.ActualizarSulfatoActivity;
import com.ivan.procampo.funcionalidades.AnnadirNuevoSulfato;
import com.ivan.procampo.funcionalidades.AnnadirRecolectaActivity;
import com.ivan.procampo.funcionalidades.RecordatoriosActivity;
import com.ivan.procampo.modelos.Cultivos;

import java.util.ArrayList;
import java.util.List;


public class MenuPrincipal extends AppCompatActivity {

    //Drawer Layout
    private DrawerLayout drawerLayout;


    private Button btnSignOut, boton;

    private String dondeEsta = "";

    //FireBase
    private FirebaseAuth mAuth;
    private FirebaseDatabase fbdatabase;
    private DatabaseReference databaseReference;

    //Google
    private GoogleSignInClient mGoogleSignInClient;


    //Facebook
    private Button cerrarSesionFB;

    private TextView textonombre;
    private TextView textoCorreo;

    //Foto de perfil
    private ImageView photoImageView;



    LayoutInflater layoutInflater;
    ViewGroup container;
    ConstraintLayout expandableView;
    Button arrowBtn;
    CardView cardView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

        expandableView = findViewById(R.id.expandableView);
        arrowBtn = findViewById(R.id.arrowBtn);
        cardView = findViewById(R.id.cardViewSulfatos);

        /*arrowBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {

                if (expandableView.getVisibility()==View.GONE){
                    TransitionManager.beginDelayedTransition(cardView,new AutoTransition());
                    expandableView.setVisibility(View.VISIBLE);
                    arrowBtn.setBackgroundResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
                }else {
                    TransitionManager.beginDelayedTransition(cardView,new AutoTransition());
                    expandableView.setVisibility(View.GONE);
                    arrowBtn.setBackgroundResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
                }

            }
        });
*/



        mAuth = FirebaseAuth.getInstance();
        databaseReference=FirebaseDatabase.getInstance().getReference();

        //Identificar usuario google
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this,gso);

        setToolbar();

        drawerLayout = findViewById(R.id.drawer_layout);

        final NavigationView navigationView;

        navigationView = findViewById(R.id.nvNavView);

        textonombre = navigationView.getHeaderView(0).findViewById(R.id.textViewNombre);
        textoCorreo = navigationView.getHeaderView(0).findViewById(R.id.textViewCorreo);
        photoImageView = navigationView.getHeaderView(0).findViewById(R.id.photoImageView);


        traerInfoUserCorreoFirebase();



        setDefaultFragment();

        //Para que al principio aparezca marcada la opcion de "INICIO"
        navigationView.getMenu().getItem(0).setChecked(true);



        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                boolean doChange=false;
                Fragment fragment = null;
                switch (menuItem.getItemId()){
                    case R.id.itHome:


                        doChange = true;

                        navigationView.getMenu().getItem(0).setChecked(true);
                        fragment = new DefaultFragment();

                        break;
                    case R.id.itCultivos:
                        uncheckItems(navigationView);
                        doChange=true;
                        navigationView.getMenu().getItem(1).setChecked(true);
                        fragment = new CultivosFragment();

                        break;
                    case R.id.itRecolectas:

                        doChange = true;
                        navigationView.getMenu().getItem(2).setChecked(true);
                        fragment = new RecolectasFragment();

                        break;
                    case R.id.itPodas:

                        doChange = true;
                        navigationView.getMenu().getItem(3).setChecked(true);
                        fragment = new PodasFragment();

                        break;
                    case R.id.itSulfatos:

                        doChange = true;
                        fragment = new SulfatosFragment();
                        uncheckItems(navigationView);
                        break;
                    case R.id.itFertilizantes:
                        Intent intento = new Intent(MenuPrincipal.this,TiendaActivity.class);
                        startActivity(intento);
                        break;
                    //
                    case R.id.irRecordatorios:
                        Intent irARecordatorios = new Intent(MenuPrincipal.this, RecordatoriosActivity.class);
                        startActivity(irARecordatorios);
                        break;


                    case R.id.itPerfil:

                        doChange = true;
                        fragment = new PerfilFragment();
                        break;
                    case R.id.itSalir:
                        //Cierro sesión en Firebase
                        mAuth.signOut();
                        //Si detecto una instancia de facebook es que estoy en facebook
                        if(LoginManager.getInstance()!=null){
                            LoginManager.getInstance().logOut();

                        }

                        //Si no estoy con facebook, pues cierro revocando la sesión
                            mGoogleSignInClient.signOut();
                            mGoogleSignInClient.revokeAccess();

                        //

                        //Muestro un mensaje
                        Context context = getApplicationContext();
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(context, "Sesión cerrada ", duration);
                        toast.show();
                        //Mando de vuelta a la pantalla de inicio
                        Intent volverAInicio = new Intent(MenuPrincipal.this,MainActivity.class);
                        startActivity(volverAInicio);
                        break;
                }
                if(doChange){

                    changeFragment(fragment,menuItem);


                }
                return true;
            }
        });


    }



    private void uncheckItems(NavigationView navigationView) {
        int size = navigationView.getMenu().size();

        for (int i = 0; i < size; i++){
            navigationView.getMenu().getItem(i).setChecked(false);
        }
    }

    private void setDefaultFragment() {
        changeFragment(new DefaultFragment(),null);

    }

    private void changeFragment(Fragment fragment, MenuItem item) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame,fragment)
                .addToBackStack(null)
                .commit();

        if (item!=null){
            item.setChecked(true);
            getSupportActionBar().setTitle("ProCampo - "+item.getTitle());
            item.setChecked(false);
        }


        drawerLayout.closeDrawers();

    }

    private void setToolbar() {
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_burguer);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }




    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                //Necesario para desplegar el Navigation Drawer
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void traerInfoUserCorreoFirebase() {
        String id = mAuth.getCurrentUser().getUid();

        if (LoginManager.getInstance()!=null) {
            String nombreUsuario = mAuth.getCurrentUser().getDisplayName();
            String correoUsuario = mAuth.getCurrentUser().getEmail();
            Uri fotoCorreo = mAuth.getCurrentUser().getPhotoUrl();
            String fotoDefecto = "https://firebasestorage.googleapis.com/v0/b/procampo-ce4e3.appspot.com/o/fotopordefectoperfil.jpg?alt=media&token=ea18769d-ede9-44cc-a0ec-f86db595cd36";

            //Asigno valor
            if (textonombre != null) {
                textonombre.setText(nombreUsuario);
            }else {
                textonombre.setText("Nombre no encontrado");
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

                    if (textonombre != null) {
                        textonombre.setText(nombreUsuarioFirebase);
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

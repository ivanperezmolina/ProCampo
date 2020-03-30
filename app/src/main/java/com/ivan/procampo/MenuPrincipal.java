package com.ivan.procampo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ivan.procampo.fragmentsMenu.CultivosFragment;



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

    LayoutInflater layoutInflater;
    ViewGroup container;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);




        mAuth = FirebaseAuth.getInstance();
        databaseReference=FirebaseDatabase.getInstance().getReference();

        setToolbar();

        drawerLayout = findViewById(R.id.drawer_layout);

        final NavigationView navigationView;

        navigationView = findViewById(R.id.nvNavView);

        textonombre = navigationView.getHeaderView(0).findViewById(R.id.textViewNombre);
        textoCorreo = navigationView.getHeaderView(0).findViewById(R.id.textViewCorreo);


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
                        fragment = new DefaultFragment();
                        break;
                    case R.id.itCultivos:
                        doChange=true;
                        fragment = new CultivosFragment();
                        break;
                    case R.id.itRecolectas:
                        break;
                    case R.id.itPodas:
                        break;
                    case R.id.itSulfatos:
                        break;
                    case R.id.itFertilizantes:
                        break;
                    //
                    case R.id.itPerfil:
                        break;
                    case R.id.itSalir:
                        //Cierro sesión en Firebase
                        mAuth.signOut();
                        //Muestro un mensaje
                        Context context = getApplicationContext();
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(context, "Sesión cerrada de Firebase", duration);
                        toast.show();
                        //Mando de vuelta a la pantalla de inicio
                        Intent volverAInicio = new Intent(MenuPrincipal.this,MainActivity.class);
                        startActivity(volverAInicio);
                        break;
                }
                if(doChange){
                    uncheckItems(navigationView);
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
                .commit();

        if (item!=null){
            item.setChecked(true);
            getSupportActionBar().setTitle("ProCampo - "+item.getTitle());
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

    private void traerInfoUserCorreoFirebase(){
        String id = mAuth.getCurrentUser().getUid();
        String nombreUsuario = mAuth.getCurrentUser().getDisplayName();
        String correoUsuario = mAuth.getCurrentUser().getEmail();

        //Asigno valor
        if (textonombre!= null){
            textonombre.setText(nombreUsuario);
        }
        if (textoCorreo!=null){
            textoCorreo.setText(correoUsuario);
        }

    }
}

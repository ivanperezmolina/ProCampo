package com.ivan.procampo;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.ivan.procampo.fragmentsMenu.CultivosFragment;



public class MenuPrincipal extends AppCompatActivity {

    //Drawer Layout
    private DrawerLayout drawerLayout;


    private Button btnSignOut, boton;

    private String dondeEsta = "";

    //FireBase
    private FirebaseAuth mAuth;
    private FirebaseDatabase fbdatabase;

    //Google
    private GoogleSignInClient mGoogleSignInClient;

    //Facebook
    private Button cerrarSesionFB;

    private TextView texto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

        setToolbar();

        drawerLayout = findViewById(R.id.drawer_layout);

        final NavigationView navigationView;

        navigationView = findViewById(R.id.nvNavView);


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
}

package com.ivan.procampo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;


import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;



public class LoginActivity extends AppCompatActivity  {

    //Google
    private SignInButton signInButton;
    private GoogleSignInClient mGoogleSignInClient;
    private String TAG ="LoginActivity";
    private int RC_SIGN_IN = 1;

    //Facebook
    private LoginButton loginButton;
    private CallbackManager callbackManager;


    //Declaracion de variables
    private EditText emailLogin;
    private EditText contraseñaLogin;

    private Button btnAcceder, btnCancelar, btnReset;



    //FireBase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    private FirebaseDatabase fbdatabase ;

    private FirebaseAuth firebaseAuth;



    //Dialog Fragment
    DialogFragment dialogoResetPass;

    @Override
    protected void onStart() {
        super.onStart();

        firebaseAuth.addAuthStateListener(firebaseAuthListener);
        FirebaseUser currentUser = mAuth.getCurrentUser();
        firebaseAuth.getCurrentUser();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        fbdatabase =  FirebaseDatabase.getInstance() ;

        //Google
        signInButton = findViewById(R.id.sign_in_button);

        //Facebook
        callbackManager = CallbackManager.Factory.create();
        loginButton = findViewById(R.id.login_button_fb);

        //Referenciar los campos y botones
        //Se puede hacer con la herramienta @BindView
        emailLogin = findViewById(R.id.correoLogin);
        contraseñaLogin = findViewById(R.id.passwordLogin);

        btnAcceder = findViewById(R.id.botonAccederLogin);
        btnCancelar = findViewById(R.id.botonCancelarLogin);
        btnReset = findViewById(R.id.botonReset);
        ////////////////////////////////////////////////////////////////////////////////////////////
        //AUTENTICACIÓN CON GOOGLE

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this,gso);

        //LISTENER EN EL BOTON
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciaSesion();
            }
        });


        ////////////////////////////////////////////////////////////////////////////////////////////
        //AUTENTICACIÓN CON FACEBOOK
        loginButton.setReadPermissions("email");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                //Si se hace correctamente; vamos a la pantalla del Menu principal
               handleFacebookAccessToken(loginResult.getAccessToken());
                firebaseAuth.getCurrentUser();
            }

            @Override
            public void onCancel() {
                Context context = getApplicationContext();
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, R.string.login_cancelado, duration);
                toast.show();
            }

            @Override
            public void onError(FacebookException error) {

                //Emitir un error
                Context context = getApplicationContext();
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, "ERROR", duration);
                toast.show();
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser userFb = firebaseAuth.getCurrentUser();
                if(userFb !=null){
                    Intent intentoAlMenuFb = new Intent(LoginActivity.this, MenuPrincipal.class);
                    startActivity(intentoAlMenuFb);
                    firebaseAuth.getCurrentUser();
                }

            }
        };

        ////////////////////////////////////////////////////////////////////////////////////////////
        btnAcceder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                //Obtener el valor de los campos
                final String email = emailLogin.getText().toString().trim();
                final String contraseña = contraseñaLogin.getText().toString().trim();


                //Comprobamos que los campos tienen informacion
                if (email.isEmpty() || contraseña.isEmpty())
                {
                    Context context = getApplicationContext();
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, R.string.campo_vacio, duration);
                    toast.show();


                    return ;
                }

                //Loguear al usuario mediante correp y contraseña
                mAuth.signInWithEmailAndPassword(email,contraseña).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){//Logueado correctamente

                            Context context = getApplicationContext();
                            int duration = Toast.LENGTH_SHORT;
                            Toast toast = Toast.makeText(context, R.string.login_ok, duration);
                            toast.show();
                            setResult(RESULT_OK);
                            finish();

                            Intent menuppal = new Intent(LoginActivity.this,MenuPrincipal.class);
                            startActivity(menuppal);

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
                                Toast toast = Toast.makeText(context, R.string.login_nook, duration);
                                toast.show();
                            }
                        }
                    }
                });
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent volverAInicio = new Intent(LoginActivity.this,MainActivity.class);
                startActivity(volverAInicio);
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialogoResetPass = new ResetPassDialogo();
                dialogoResetPass.show(getSupportFragmentManager(),"resetPassDialog");
            }
        });


    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //Métodos necesarios para iniciar sesión con Google

    private void iniciaSesion() {
        Intent intentoInicioSesion = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(intentoInicioSesion,RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Facebook
        callbackManager.onActivityResult(requestCode,resultCode,data);
        //Google
        if (requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }


    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try{
            GoogleSignInAccount acc = completedTask.getResult(ApiException.class);
            Context context = getApplicationContext();
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, R.string.login_ok, duration);
            toast.show();
            FirebaseGoogleAuth(acc);
        }catch (ApiException e){
            Context context = getApplicationContext();
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, R.string.login_nook, duration);
            toast.show();
            FirebaseGoogleAuth(null);
        }
    }

    private void FirebaseGoogleAuth(final GoogleSignInAccount acct) {
        AuthCredential authCredential = GoogleAuthProvider.getCredential(acct.getIdToken(),acct.getIdToken());
        mAuth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful() || acct.getIdToken()!=null){
                    Context context = getApplicationContext();
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, R.string.login_ok, duration);
                    toast.show();
                    FirebaseUser user = mAuth.getCurrentUser();
                    updateUI(user);
                }else{
                    Context context = getApplicationContext();
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, R.string.login_nook, duration);
                    toast.show();
                    updateUI(null);
                }

            }
        });
    }

    private void updateUI(Object o) {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());

        if(account!=null){
            String personName = account.getDisplayName();
            String personGivenName = account.getGivenName();
            String personFamilyName = account.getFamilyName();
            String personEmail = account.getEmail();
            String personId = account.getId();
            Uri personPhoto = account.getPhotoUrl();

            if(personName!=null || personEmail!=null) {
                Context context = getApplicationContext();
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, R.string.texto_bienvenida+" " + personName, duration);
                toast.show();

                Intent menuppal = new Intent(LoginActivity.this, MenuPrincipal.class);
                startActivity(menuppal);
            }else{
                Context context = getApplicationContext();
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, R.string.login_nook, duration);
                toast.show();
            }
        }
    }

    //FACEBOOK
    private void handleFacebookAccessToken(AccessToken accessToken) {
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()) {
                    Context context = getApplicationContext();
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, "No loguen con fb", duration);
                    toast.show();
                }else{
                     firebaseAuth.getCurrentUser();


                }
            }
        });
    }



    @Override
    protected void onStop() {
        super.onStop();
        firebaseAuth.removeAuthStateListener(firebaseAuthListener);
    }

    //if(AccessToken.getCurrentAccessToken()==null){
    //  Intent volverAlLogin = new Intent(MenuPrincipal.this,LoginActivity.class);
    //startActivity(volverAlLogin);
    //}
}

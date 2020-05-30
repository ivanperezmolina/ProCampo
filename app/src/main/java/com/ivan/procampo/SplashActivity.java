package com.ivan.procampo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashActivity extends AppCompatActivity {

    ImageView imageSplash;

    Animation fromtop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        /**
         * Doy animacion
         */
        imageSplash = findViewById(R.id.imageSplash);

        fromtop = AnimationUtils.loadAnimation(this,R.anim.nav_default_exit_anim);

        imageSplash.setAnimation(fromtop);


        /**
         * Nos sirve para mostrar primero el logo y despues que ya se vaya a la aplicación
         */
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        },2500);
    }
}

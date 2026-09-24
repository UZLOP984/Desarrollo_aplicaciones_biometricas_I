package com.example.aplicacion_huella_dactilar;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView imgLogo = findViewById(R.id.imgLogo);
        Animation animacion = AnimationUtils.loadAnimation(this,R.anim.acercamiento_and_zoom);
        imgLogo.startAnimation(animacion);

        TextView txtTitulo = findViewById(R.id.textLogo_1);
        txtTitulo.startAnimation(animacion);

        TextView txtsubtitulo = findViewById(R.id.textLogo_2);
        txtsubtitulo.startAnimation(animacion);

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, 2000);
    }
}
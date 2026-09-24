package com.example.aplicacion_huella_dactilar;

import android.content.Intent;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private TextView txtStatus;
    private ImageView imgStatus;
    private Button btnLogin;

    private FingerprintManager fingerprintManager;
    private CancellationSignal cancellationSignal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtStatus = findViewById(R.id.txtStatus);
        imgStatus = findViewById(R.id.imgStatus);
        btnLogin = findViewById(R.id.btnLogin);

        fingerprintManager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);

        Button btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarAutenticacionHuella();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (txtStatus != null && imgStatus != null) {
            txtStatus.setText("");
            txtStatus.setVisibility(View.GONE);
            imgStatus.setVisibility(View.GONE);
        }
    }

    private void iniciarAutenticacionHuella(){
        if (fingerprintManager == null || !fingerprintManager.isHardwareDetected()){
            Toast.makeText(this, "El dispositivo no cuenta con lector de huella", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!fingerprintManager.hasEnrolledFingerprints()){
            Toast.makeText(this, "No hay huellas dactilares registradas en el dispositivo", Toast.LENGTH_SHORT).show();
            return;
        }

        if (cancellationSignal != null) {
            if (!cancellationSignal.isCanceled()) {
                cancellationSignal.cancel();
            }
            cancellationSignal = null;
        }

        cancellationSignal = new CancellationSignal();

        txtStatus.setVisibility(View.VISIBLE);
        imgStatus.setVisibility(View.VISIBLE);
        txtStatus.setText("Coloque su dedo en el lector de huella...");

        cancellationSignal = new CancellationSignal();

        FingerprintManager.AuthenticationCallback authenticationCallback = new FingerprintManager.AuthenticationCallback() {
            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                txtStatus.setText("Escaneo fallido, huella dactilar no registrada");
                imgStatus.setImageResource(R.drawable.wrong);
            }

            @Override
            public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                txtStatus.setText("¡Escaneo de huella dactilar exitoso! \n Iniciando sesión...");
                imgStatus.setImageResource(R.drawable.check);

                Toast.makeText(LoginActivity.this, "Autenticación exitosa", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(LoginActivity.this, Home_activity.class);
                startActivity(intent);
            }

            @Override
            public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
                super.onAuthenticationHelp(helpCode, helpString);
                txtStatus.setText("AYUDA: Vuelve a escanear de nuevo");
                imgStatus.setImageResource(R.drawable.information);
            }

            @Override
            public void onAuthenticationError(int errorCode, CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                txtStatus.setText("ERROR: Intente de nuevo");
                imgStatus.setImageResource(R.drawable.warning);

                if (cancellationSignal != null) {
                    cancellationSignal.cancel();
                    cancellationSignal = null;
                }
            }
        };

        try {
            fingerprintManager.authenticate(null, cancellationSignal, 0, authenticationCallback, null);
        } catch (Exception e) {
            e.printStackTrace();
            txtStatus.setText("ERROR: Intente de nuevo");
            imgStatus.setImageResource(R.drawable.warning);
            cancellationSignal = null;
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (cancellationSignal != null && !cancellationSignal.isCanceled()) {
            cancellationSignal.cancel();
        }
    }

}


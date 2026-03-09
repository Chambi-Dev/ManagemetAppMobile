package com.gadel.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    //credenciales temporales para el login

    /*private static String USER = "admin@gmail.com";
    private static String PASS = "12345678";
    int intentos = 0;*/

    FirebaseAuth mAuth;
    ProgressDialog progressDialog;
    String usuario="", password="";


    TextInputLayout textEmail, textPassword;
    TextView lblRegistrarme;
    Button btnIngresar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        textEmail = findViewById(R.id.txtUser);
        textPassword = findViewById(R.id.txtPass);

        btnIngresar = findViewById(R.id.Enter);

        mAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setTitle(getString(R.string.log_iniciando_sesion));
        progressDialog.setCanceledOnTouchOutside(false);

        //al pulsar el boton de ingresar
        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                validarDatos();
            }
        });

        // Al  darle click ene registrarse
        lblRegistrarme = findViewById(R.id.lblRegistrarme);
        lblRegistrarme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegistroUActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }

    private void validarDatos() {

        textEmail.setErrorEnabled(false);
        textPassword.setErrorEnabled(false);

        usuario = textEmail.getEditText() != null ? textEmail.getEditText().getText().toString().trim() : "";
        password = textPassword.getEditText() != null ? textPassword.getEditText().getText().toString().trim() : "";

        boolean error = false;

        if (!Patterns.EMAIL_ADDRESS.matcher(usuario).matches()){
            textEmail.setError(getString(R.string.error_usuario));
            error = true;
        } else if (TextUtils.isEmpty(password)) {
            textPassword.setError(getString(R.string.error_password));
            error = true;
        } else {
            login();
        }

        if (error){
            return;
        }

    }

    private void login() {
        progressDialog.setMessage(getString(R.string.log_iniciando_sesion));
        progressDialog.show();
        mAuth.signInWithEmailAndPassword(usuario, password)
                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()){
                            startActivity(new Intent(MainActivity.this, DashboardActivity.class));
                            finish();
                        } else {
                            Toast.makeText(MainActivity.this, getString(R.string.log_error_iniciar_sesion), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
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
        progressDialog.setTitle("Iniciando sesión");
        progressDialog.setCanceledOnTouchOutside(false);

        //al pulsar el boton de ingresar
        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*intentos++;

                textEmail.setErrorEnabled(false);
                textPassword.setErrorEnabled(false);


                //leer loq ue el usuario escribio
                String emailIngrsado = textEmail.getEditText() != null ? textEmail.getEditText().getText().toString().trim() : "";
                String passIngrsado = textPassword.getEditText() != null ? textPassword.getEditText().getText().toString().trim() : "";

                boolean error = false;

                if (emailIngrsado.isEmpty()) {
                    textEmail.setError("El email no puede estar vacio");
                    Toast.makeText(MainActivity.this,"Debe ingresar un email", Toast.LENGTH_SHORT).show();
                    error = true;
                } else if (!emailIngrsado.contains("@")) {
                    textEmail.setError("El email no es valido");
                    Toast.makeText(MainActivity.this,"El email no es valido", Toast.LENGTH_SHORT).show();
                    error = true;
                }

                if (passIngrsado.isEmpty()){
                    textPassword.setError("La contraseña no puede estar vacia");
                    Toast.makeText(MainActivity.this,"Debe ingresar una contraseña", Toast.LENGTH_SHORT).show();
                    error = true;
                } else if (passIngrsado.length() < 8) {
                    textPassword.setError("La contraseña debe tener al menos 8 caracteres");
                    Toast.makeText(MainActivity.this,"La contraseña debe tener al menos 8 caracteres", Toast.LENGTH_SHORT).show();
                    error = true;
                }

                if (error){
                    return;
                }

                //comparar lo que escribio el usuario con el usuario y contraseña guardados
                if (emailIngrsado.equals(USER) && passIngrsado.equals(PASS)) {
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Exito")
                            .setMessage("Incisisaste Sesion Correctamente")
                            .setPositiveButton("Acepar", null)
                            .show();
                } else {
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Error")
                            .setMessage("Usuario o Contraseña Incorrecta")
                            .setPositiveButton("Acepar", null)
                            .show();
                }*/
                
                validarDatos();
            }
        });




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
        usuario = textEmail.getEditText() != null ? textEmail.getEditText().getText().toString().trim() : "";
        password = textPassword.getEditText() != null ? textPassword.getEditText().getText().toString().trim() : "";

        if (!Patterns.EMAIL_ADDRESS.matcher(usuario).matches()){
            Toast.makeText(this, "debe ingresar un usuario", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Debe ingresar uan acontraseña", Toast.LENGTH_SHORT).show();
        } else {
            login();
        }

    }

    private void login() {
        progressDialog.setMessage("Iniciando Sesion ...");
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
                            Toast.makeText(MainActivity.this, "Correo o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
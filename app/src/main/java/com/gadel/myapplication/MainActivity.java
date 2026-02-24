package com.gadel.myapplication;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputLayout;

public class MainActivity extends AppCompatActivity {

    private static String USER = "admin@gmail.com";
    private static String PASS = "12345678";
    int intentos = 0;


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

        //al pulsar el boton de ingresar
        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                intentos++;

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
                }
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
}
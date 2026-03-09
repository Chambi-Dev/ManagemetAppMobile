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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegistroUActivity extends AppCompatActivity {

    TextInputLayout etNuevoUsuario, etNuevoAppelido, etNuevoCorreo,etNuevaPassword, etConfirmarPassword;

    TextView lblVolverMain;
    Button btnRegistrar;
    FirebaseAuth mAuth;
    String nombre="", apellido="", password="",correo="", confirmarPassword="";

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registro_uactivity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        etNuevoUsuario = findViewById(R.id.RNombreUsuario);
        etNuevoAppelido = findViewById(R.id.RApellidoUsuario);
        etNuevoCorreo = findViewById(R.id.REmailUsuario);
        etNuevaPassword = findViewById(R.id.RNuevaPass);
        etConfirmarPassword = findViewById(R.id.RConfirmarPass);
        btnRegistrar = findViewById(R.id.btnRegistrar);

        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle(getString(R.string.log_espera));
        progressDialog.setCanceledOnTouchOutside(false);


        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validarDatos();
            }
        });


        //para volver al login
        lblVolverMain = findViewById(R.id.lblVolverLogin);
        lblVolverMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegistroUActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void validarDatos() {
        etNuevoUsuario.setErrorEnabled(false);
        etNuevoAppelido.setErrorEnabled(false);
        etNuevoCorreo.setErrorEnabled(false);
        etNuevaPassword.setErrorEnabled(false);
        etConfirmarPassword.setErrorEnabled(false);

        nombre = etNuevoUsuario.getEditText() != null ? etNuevoUsuario.getEditText().getText().toString().trim() : "";
        apellido = etNuevoAppelido.getEditText() != null ? etNuevoAppelido.getEditText().getText().toString().trim() : "";
        correo = etNuevoCorreo.getEditText() != null ? etNuevoCorreo.getEditText().getText().toString().trim() : "";
        password = etNuevaPassword.getEditText() != null ? etNuevaPassword.getEditText().getText().toString().trim() : "";
        confirmarPassword = etConfirmarPassword.getEditText() != null ? etConfirmarPassword.getEditText().getText().toString().trim() : "";

        boolean error = false;

        if (TextUtils.isEmpty(nombre)) {
            etNuevoUsuario.setError(getString(R.string.error_empty_nombre));
            error = true;
        } else if (TextUtils.isEmpty(apellido)) {
            etNuevoAppelido.setError(getString(R.string.error_empty_apellido));
            error = true;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            etNuevoCorreo.setError(getString(R.string.error_usuario));
            error = true;
        } else if (TextUtils.isEmpty(password)|| password.length()<8) {
            etNuevaPassword.setError(getString(R.string.error_length_password));
            error = true;
        } else if (TextUtils.isEmpty(confirmarPassword)|| confirmarPassword.length()<8) {
            etConfirmarPassword.setError(getString(R.string.error_confirmar_password));
            error = true;
        } else if (!password.equals(confirmarPassword)) {
            etNuevaPassword.setError(getString(R.string.error_password_diferent));
            etConfirmarPassword.setError(getString(R.string.error_password_diferent));
            error =true;
        }else {
            registrar();
        }
        if (error){
            return;
        }
    }

    private void registrar() {
        progressDialog.setMessage(getString(R.string.log_registrando));
        progressDialog.show();
        mAuth.createUserWithEmailAndPassword(correo, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        guardarUsuario();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        if (e instanceof FirebaseAuthUserCollisionException) {
                            Toast.makeText(RegistroUActivity.this, getString(R.string.error_email_ya_Registrado), Toast.LENGTH_LONG).show();
                        } else if (e instanceof FirebaseAuthWeakPasswordException) {
                            Toast.makeText(RegistroUActivity.this, getString(R.string.error_8caracteres_password), Toast.LENGTH_LONG).show();
                        } else if (e instanceof FirebaseAuthInvalidCredentialsException) {
                            Toast.makeText(RegistroUActivity.this, getString(R.string.error_correo_invalido), Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(RegistroUActivity.this, getString(R.string.error) + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                        
                    }
                });
    }

    private void guardarUsuario() {
        progressDialog.setMessage(getString(R.string.log_guardando));
        progressDialog.show();

        String uid = mAuth.getUid();
        HashMap<String, String> datousuario = new HashMap<>();
        datousuario.put("uid", uid);
        datousuario.put("nombre", nombre);
        datousuario.put("apellido", apellido);
        datousuario.put("correo", correo);
        datousuario.put("password", password);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Usuarios");
        databaseReference.child(uid).setValue(datousuario).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                progressDialog.dismiss();
                Toast.makeText(RegistroUActivity.this, getString(R.string.usuario_creado), Toast.LENGTH_SHORT).show();
                startActivity(new Intent(RegistroUActivity.this, DashboardActivity.class));
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(RegistroUActivity.this, getString(R.string.error_guardar_usuario)+ e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });



    }
}
package com.gadel.myapplication.ui.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.gadel.myapplication.R;
import com.gadel.myapplication.data.remote.dto.JwtPayloadDTO;
import com.gadel.myapplication.ui.dashboard.DashboardActivity;
import com.gadel.myapplication.utils.SessionManager; // ¡NUEVO IMPORT!
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private TextInputLayout textEmail, textPassword;
    private Button btnIngresar;

    private LoginViewModel viewModel;
    private SessionManager sessionManager; // 1. Declaramos la Bóveda

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

        // 2. Inicializamos el SessionManager
        sessionManager = new SessionManager(this);
        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        if (viewModel.isAlreadyLoggedIn()) {
            goToDashboard();
            return;
        }

        textEmail = findViewById(R.id.txtUser);
        textPassword = findViewById(R.id.txtPass);
        btnIngresar = findViewById(R.id.Enter);

        setupProgressDialog();
        setupObservers();

        btnIngresar.setOnClickListener(v -> validarDatos());
    }

    private void setupProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.log_iniciando_sesion));
        progressDialog.setCanceledOnTouchOutside(false);
    }

    private void setupObservers() {
        viewModel.getIsLoading().observe(this, isLoading -> {
            if (isLoading) progressDialog.show();
            else progressDialog.dismiss();
        });

        viewModel.getErrorMessage().observe(this, errorMsg -> {
            if (errorMsg != null) {
                Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void validarDatos() {
        textEmail.setErrorEnabled(false);
        textPassword.setErrorEnabled(false);

        // Atrapamos el nombre de usuario que escribió la persona
        String usuario = textEmail.getEditText() != null ? textEmail.getEditText().getText().toString().trim() : "";
        String password = textPassword.getEditText() != null ? textPassword.getEditText().getText().toString().trim() : "";

        if (TextUtils.isEmpty(usuario)){
            textEmail.setError(getString(R.string.error_usuario));
            return;
        }
        if (TextUtils.isEmpty(password)) {
            textPassword.setError(getString(R.string.error_password));
            return;
        }

        viewModel.attemptLogin(usuario, password).observe(this, payload -> {
            viewModel.hideLoading();
            if (payload != null && payload.accounts != null && !payload.accounts.isEmpty()) {
                // 3. Le pasamos el "usuario" al siguiente método
                mostrarDialogoRoles(payload, usuario);
            } else {
                viewModel.showError("Credenciales incorrectas o error de red");
            }
        });
    }

    // 4. Recibimos el "usuario" aquí
    private void mostrarDialogoRoles(JwtPayloadDTO payload, String usuario) {
        JwtPayloadDTO.AccountDTO account = payload.accounts.get(0);
        List<JwtPayloadDTO.RoleDTO> roles = account.roles;

        String[] nombresRoles = new String[roles.size()];
        for (int i = 0; i < roles.size(); i++) {
            nombresRoles[i] = roles.get(i).name;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Seleccione su Rol");
        builder.setCancelable(false);

        builder.setItems(nombresRoles, (dialog, which) -> {
            JwtPayloadDTO.RoleDTO rolSeleccionado = roles.get(which);

            // 5. Se lo pasamos al último método
            confirmarRolEnBackend(account.userId, rolSeleccionado.id, payload.token, usuario);
        });

        builder.show();
    }

    // 6. Recibimos el "usuario" en el último paso
    private void confirmarRolEnBackend(Integer userId, Integer roleId, String preAuthToken, String usuario) {
        viewModel.confirmRole(userId, roleId, preAuthToken).observe(this, isSuccess -> {
            viewModel.hideLoading();
            if (isSuccess) {

                // Guardamos el nombre en la bóveda
                sessionManager.saveUsername(usuario);

                goToDashboard();
            } else {
                viewModel.showError("Error al asignar el rol.");
            }
        });
    }

    private void goToDashboard() {
        startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
        finish();
    }
}
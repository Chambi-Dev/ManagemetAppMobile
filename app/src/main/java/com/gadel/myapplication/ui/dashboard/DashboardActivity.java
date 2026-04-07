package com.gadel.myapplication.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

// ¡ESTE ES EL IMPORT QUE FALTABA Y QUE GRADLE AHORA SÍ ENCONTRARÁ!
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.gadel.myapplication.R;
import com.gadel.myapplication.ui.login.LoginActivity;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class DashboardActivity extends AppCompatActivity {

    private DashboardViewModel viewModel;
    private CategoryAdapter adapter;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dashboard);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.recyclerViewCategories);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        TextView lblUserName = findViewById(R.id.lblUserName);
        TextView btnLogout = findViewById(R.id.btnLogout);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CategoryAdapter();
        recyclerView.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(DashboardViewModel.class);

        lblUserName.setText(viewModel.getUserName());

        swipeRefreshLayout.setOnRefreshListener(() -> {
            viewModel.forceSync();
        });

        viewModel.getIsLoading().observe(this, isLoading -> {
            swipeRefreshLayout.setRefreshing(isLoading != null && isLoading);
        });

        btnLogout.setOnClickListener(v -> {
            new MaterialAlertDialogBuilder(this)
                    .setTitle("Cerrar Sesión")
                    .setMessage("¿Estás seguro que deseas salir? Los cambios no sincronizados se perderán.")
                    .setPositiveButton("Salir", (dialog, which) -> {
                        viewModel.logout();
                        Intent intent = new Intent(DashboardActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
        });

        adapter.setOnItemClickListener(category -> {
            Intent intent = new Intent(DashboardActivity.this, ApprovalListActivity.class);
            intent.putExtra("CATEGORY_NAME", category.categoryName);
            startActivity(intent);
        });

        viewModel.forceSync();

        viewModel.getCategoryCounts().observe(this, categories -> {
            if (categories != null && !categories.isEmpty()) {
                adapter.setCategories(categories);
                recyclerView.setVisibility(View.VISIBLE);
            }
        });

        viewModel.getTokenExpiredEvent().observe(this, isExpired -> {
            if (isExpired != null && isExpired) {
                viewModel.logout();
                Toast.makeText(this, "Tu sesión expiró. Inicia sesión de nuevo.", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(DashboardActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
    }
}
package com.gadel.myapplication.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gadel.myapplication.R;

public class ApprovalListActivity extends AppCompatActivity {

    private ApprovalListViewModel viewModel;
    private ApprovalTaskAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_approval_list);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.recyclerViewTasks);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ApprovalTaskAdapter();
        recyclerView.setAdapter(adapter);

        // =========================================================
        // ¡LA CONEXIÓN! Atrapamos el clic de la tarjeta de la tarea
        // =========================================================
        adapter.setOnTaskClickListener(task -> {
            // Creamos el "pasaje de avión" hacia el ApprovalDetailActivity
            Intent intent = new Intent(ApprovalListActivity.this, ApprovalDetailActivity.class);

            // Empacamos el correlativo del ERP en la maleta bajo la llave "TASK_ID"
            intent.putExtra("TASK_ID", task.taskId);

            // ¡Iniciamos el viaje!
            startActivity(intent);
        });

        // Recuperamos el nombre de la categoría del Dashboard (Ej. "Requerimientos de Compra")
        String categoryName = getIntent().getStringExtra("CATEGORY_NAME");
        TextView lblTitle = findViewById(R.id.lblListTitle);
        if (categoryName != null) {
            lblTitle.setText(categoryName);
        }

        viewModel = new ViewModelProvider(this).get(ApprovalListViewModel.class);

        if (categoryName != null) {
            viewModel.getTasksByCategory(categoryName).observe(this, tasks -> {
                if (tasks != null && !tasks.isEmpty()) {
                    adapter.setTasks(tasks);
                    recyclerView.setVisibility(View.VISIBLE);
                }
            });
        }
    }
}
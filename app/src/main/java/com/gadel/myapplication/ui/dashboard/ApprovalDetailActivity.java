package com.gadel.myapplication.ui.dashboard;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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

import com.gadel.myapplication.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class ApprovalDetailActivity extends AppCompatActivity {

    private ApprovalDetailViewModel viewModel;
    private ApprovalDetailAdapter adapter;
    private RecyclerView recyclerView;
    private String documentId; // Guardamos el ID a nivel global de la clase

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_approval_detail);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.recyclerViewItems);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ApprovalDetailAdapter();
        recyclerView.setAdapter(adapter);

        // Recuperamos el ID del documento
        documentId = getIntent().getStringExtra("TASK_ID");
        TextView lblDocumentIdDisplay = findViewById(R.id.lblDocumentIdDisplay);
        if (documentId != null) {
            lblDocumentIdDisplay.setText(documentId);
        }

        viewModel = new ViewModelProvider(this).get(ApprovalDetailViewModel.class);

        if (documentId != null) {
            viewModel.getDetails(documentId).observe(this, details -> {
                if (details != null && !details.isEmpty()) {
                    adapter.setDetails(details);
                    recyclerView.setVisibility(View.VISIBLE);
                }
            });
        }

        // =========================================================
        // ¡NUEVO! Lógica de los botones
        // =========================================================
        Button btnApprove = findViewById(R.id.btnApprove);
        Button btnReject = findViewById(R.id.btnReject);

        // Acción: APROBAR
        btnApprove.setOnClickListener(v -> {
            if (documentId != null) {
                viewModel.approveTask(documentId);
                Toast.makeText(this, "Aprobado: " + documentId, Toast.LENGTH_SHORT).show();
                finish(); // Cerramos esta pantalla y volvemos a la lista
            }
        });

        // Acción: RECHAZAR (Abre popup para pedir comentario)
        btnReject.setOnClickListener(v -> {
            if (documentId != null) {
                showRejectDialog();
            }
        });
    }

    // Método para mostrar el cuadro de diálogo de rechazo
    private void showRejectDialog() {
        // Creamos una cajita de texto dinámicamente
        final EditText inputComment = new EditText(this);
        inputComment.setHint("Escribe la razón del rechazo...");

        // Le damos un poco de margen para que se vea bien
        LinearLayout layout = new LinearLayout(this);
        layout.setPadding(50, 20, 50, 0);
        layout.addView(inputComment);

        // Construimos la ventana emergente (Material Design)
        new MaterialAlertDialogBuilder(this)
                .setTitle("Rechazar Documento")
                .setMessage("Por favor, ingresa un comentario para justificar el rechazo:")
                .setView(layout)
                .setPositiveButton("Confirmar Rechazo", (dialog, which) -> {
                    String comment = inputComment.getText().toString().trim();
                    if (comment.isEmpty()) {
                        Toast.makeText(this, "El comentario es obligatorio para rechazar", Toast.LENGTH_LONG).show();
                    } else {
                        viewModel.rejectTask(documentId, comment);
                        Toast.makeText(this, "Rechazado: " + documentId, Toast.LENGTH_SHORT).show();
                        finish(); // Cerramos la pantalla
                    }
                })
                .setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss())
                .show();
    }
}
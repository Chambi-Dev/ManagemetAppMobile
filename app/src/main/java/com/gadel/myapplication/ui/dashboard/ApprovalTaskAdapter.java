package com.gadel.myapplication.ui.dashboard;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gadel.myapplication.R;
import com.gadel.myapplication.data.local.entity.ApprovalTask;

import java.util.ArrayList;
import java.util.List;

public class ApprovalTaskAdapter extends RecyclerView.Adapter<ApprovalTaskAdapter.TaskViewHolder> {

    private List<ApprovalTask> taskList = new ArrayList<>();

    // ¡NUEVO! Interfaz de clic
    private OnTaskClickListener listener;

    public interface OnTaskClickListener {
        void onTaskClick(ApprovalTask task);
    }

    public void setOnTaskClickListener(OnTaskClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_approval_task_card, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        ApprovalTask currentTask = taskList.get(position);

        holder.lblTaskId.setText(currentTask.taskId);

        String requester = currentTask.requesterName != null ? currentTask.requesterName : "Sin Especificar";
        holder.lblRequesterName.setText(requester);

        double amount = currentTask.totalAmount != null ? currentTask.totalAmount : 0.0;
        holder.lblTotalAmount.setText(String.format("$ %.2f", amount));

        // ¡NUEVO! Escuchamos el toque en la tarjeta
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onTaskClick(currentTask);
            }
        });
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public void setTasks(List<ApprovalTask> tasks) {
        this.taskList = tasks;
        notifyDataSetChanged();
    }

    class TaskViewHolder extends RecyclerView.ViewHolder {
        private TextView lblTaskId;
        private TextView lblRequesterName;
        private TextView lblTotalAmount;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            lblTaskId = itemView.findViewById(R.id.lblTaskId);
            lblRequesterName = itemView.findViewById(R.id.lblRequesterName);
            lblTotalAmount = itemView.findViewById(R.id.lblTotalAmount);
        }
    }
}
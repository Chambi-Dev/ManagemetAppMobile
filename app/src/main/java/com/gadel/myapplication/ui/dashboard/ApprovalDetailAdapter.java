package com.gadel.myapplication.ui.dashboard;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gadel.myapplication.R;
import com.gadel.myapplication.data.local.entity.ApprovalDetail;

import java.util.ArrayList;
import java.util.List;

public class ApprovalDetailAdapter extends RecyclerView.Adapter<ApprovalDetailAdapter.DetailViewHolder> {

    private List<ApprovalDetail> detailsList = new ArrayList<>();

    @NonNull
    @Override
    public DetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_approval_detail_card, parent, false);
        return new DetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailViewHolder holder, int position) {
        ApprovalDetail currentItem = detailsList.get(position);

        holder.lblItemDescription.setText(currentItem.description != null ? currentItem.description : "Sin descripción");
        holder.lblItemCode.setText("Cod: " + (currentItem.materialNo != null ? currentItem.materialNo : "N/A"));

        double qty = currentItem.quantity != null ? currentItem.quantity : 0.0;
        double price = currentItem.unitPrice != null ? currentItem.unitPrice : 0.0;

        holder.lblItemQtyPrice.setText(String.format("Cant: %.2f | P.U: $%.2f", qty, price));

        // Calculamos el subtotal de este ítem
        double subtotal = qty * price;
        holder.lblItemSubtotal.setText(String.format("$ %.2f", subtotal));
    }

    @Override
    public int getItemCount() {
        return detailsList.size();
    }

    public void setDetails(List<ApprovalDetail> details) {
        this.detailsList = details;
        notifyDataSetChanged();
    }

    class DetailViewHolder extends RecyclerView.ViewHolder {
        private TextView lblItemDescription, lblItemCode, lblItemQtyPrice, lblItemSubtotal;

        public DetailViewHolder(@NonNull View itemView) {
            super(itemView);
            lblItemDescription = itemView.findViewById(R.id.lblItemDescription);
            lblItemCode = itemView.findViewById(R.id.lblItemCode);
            lblItemQtyPrice = itemView.findViewById(R.id.lblItemQtyPrice);
            lblItemSubtotal = itemView.findViewById(R.id.lblItemSubtotal);
        }
    }
}
package com.gadel.myapplication.ui.dashboard;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gadel.myapplication.R;
import com.gadel.myapplication.data.local.model.CategoryCount;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private List<CategoryCount> categoryList = new ArrayList<>();

    // Interfaz para escuchar los clics
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(CategoryCount category);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category_card, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        CategoryCount currentItem = categoryList.get(position);
        holder.lblCategoryName.setText(currentItem.categoryName);
        holder.lblPendingCount.setText(String.valueOf(currentItem.pendingCount));

        // ponemos el evento "Click" a toda la tarjeta
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(currentItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public void setCategories(List<CategoryCount> categories) {
        this.categoryList = categories;
        notifyDataSetChanged();
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder {
        private TextView lblCategoryName;
        private TextView lblPendingCount;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            lblCategoryName = itemView.findViewById(R.id.lblCategoryName);
            lblPendingCount = itemView.findViewById(R.id.lblPendingCount);
        }
    }
}
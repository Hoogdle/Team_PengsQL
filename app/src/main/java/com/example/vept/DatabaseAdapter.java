package com.example.vept;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class DatabaseAdapter extends RecyclerView.Adapter<DatabaseAdapter.ViewHolder> {
    private List<String> databaseNames;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onEditClick(String databaseName);
        void onDeleteClick(String databaseName);
    }

    public DatabaseAdapter(List<String> databaseNames, OnItemClickListener listener) {
        this.databaseNames = new ArrayList<>(databaseNames); // 🔥 리스트 복사
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.database_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String databaseName = databaseNames.get(position);
        holder.tvDatabaseName.setText(databaseName);

        holder.btnEdit.setOnClickListener(v -> listener.onEditClick(databaseName));
        holder.btnDelete.setOnClickListener(v -> listener.onDeleteClick(databaseName));
    }

    // 🔥 리스트 갱신 메서드 추가
    public void updateData(List<String> newDatabaseNames) {
        this.databaseNames.clear();
        this.databaseNames.addAll(newDatabaseNames);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return databaseNames.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDatabaseName;
        Button btnEdit, btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDatabaseName = itemView.findViewById(R.id.tvDatabaseName);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}

package com.example.voiceversa;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class SettingsAdapter extends RecyclerView.Adapter<SettingsAdapter.ViewHolder> {

    private List<String> modelNames;
    private Context context;
    TextView lang_name;

    public SettingsAdapter(Context context, List<String> modelNames) {
        this.context = context;
        this.modelNames = modelNames;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.model_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String modelName = modelNames.get(position);
        holder.modelNameTextView.setText(modelName);
    }

    @Override
    public int getItemCount() {
        return modelNames.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView modelNameTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            modelNameTextView = itemView.findViewById(R.id.modelNameTextView);
        }
    }
}
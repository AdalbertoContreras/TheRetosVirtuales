package com.thereto.theretosvirtuales.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.thereto.theretosvirtuales.R;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    // Define tus datos aquí, como una lista de elementos

    public class MyViewHolder extends RecyclerView.ViewHolder {
        // Configura tus vistas y establece los datos
        public MyViewHolder(View itemView) {
            super(itemView);
            // Configura tus elementos de lista aquí
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        // Configura tus elementos de lista aquí
    }

    @Override
    public int getItemCount() {
        // Devuelve la cantidad de elementos en tus datos
        return 0;
    }
}
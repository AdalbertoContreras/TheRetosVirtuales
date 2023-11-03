package com.thereto.theretosvirtuales.adapter;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.thereto.theretosvirtuales.R;

public class InstruccionesAdapter extends RecyclerView.Adapter<InstruccionesAdapter.TextViewHolder> {
    private ArrayList<String> textList;
    private Context context;

    public InstruccionesAdapter(Context context, ArrayList<String> textList) {
        this.context = context;
        this.textList = textList;
    }

    @Override
    public TextViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutResId = (viewType % 2 == 0) ? R.layout.item_text_right : R.layout.item_text_left;

        View view = LayoutInflater.from(parent.getContext()).inflate(layoutResId, parent, false);
        return new TextViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TextViewHolder holder, int position) {
        String text = textList.get(position);
        holder.bind(text);
    }

    @Override
    public int getItemCount() {
        return textList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position; // Establece la posición como el tipo de vista para diferenciar entre pares e impares
    }

    public class TextViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;

        public TextViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView);
        }

        public void bind(String text) {
            textView.setText(text);
        }
    }
}

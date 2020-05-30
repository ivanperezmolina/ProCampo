package com.ivan.procampo.adaptadores;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ivan.procampo.R;
import com.ivan.procampo.modelos.Podas;
import com.ivan.procampo.modelos.Recolectas;

import java.util.ArrayList;

public class PodaAdapter extends RecyclerView.Adapter<PodaAdapter.ViewHolder> {
    private int resource;
    private ArrayList<Podas> podasList;
    private int index;

    public PodaAdapter(ArrayList<Podas> podasList, int resource){
        this.podasList = podasList;
        this.resource = resource;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resource , parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int index) {
        Podas poda = podasList.get(index);

        holder.textViewTextoCodigoPoda.setText(poda.getCodigoPoda());
        holder.textViewTextoFecha.setText(poda.getFechaPoda());
        holder.textViewTextoCultivoPoda.setText(poda.getCultivoPoda());

        holder.BindHolder(podasList.get(index));


    }

    @Override
    public int getItemCount() {
        return podasList.size();
    }

    public int getIndex() {
        return index;
    }

    //Métodos necesarios


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{

        //Referencias
        private TextView textViewTextoCodigoPoda;
        private TextView textViewTextoFecha;
        private TextView textViewTextoCultivoPoda;

        public View view;

        public ViewHolder(View view){
            super(view);

            this.view = view;

            textViewTextoCodigoPoda = view.findViewById(R.id.textoCodigoPoda);
            textViewTextoFecha = view.findViewById(R.id.textoFechaPoda);
            textViewTextoCultivoPoda = view.findViewById(R.id.textoCultivoPoda);

            view.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        }

        public void BindHolder(Podas item){
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    index = getAdapterPosition();
                    //notify();
                    return false;
                }
            });
        }
    }

}

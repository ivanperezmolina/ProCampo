package com.ivan.procampo.adaptadores;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ivan.procampo.R;
import com.ivan.procampo.modelos.Recolectas;

import java.util.ArrayList;

public class RecolectaAdapter extends RecyclerView.Adapter<RecolectaAdapter.ViewHolder> {
    private int resource;
    private ArrayList<Recolectas> recolectasList;
    private int index;

    //Creación de métodos obligatorios
    public RecolectaAdapter(ArrayList<Recolectas> recolectasList, int resource){
        this.recolectasList = recolectasList;
        this.resource = resource;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) { //Se crea la vista
        View view = LayoutInflater.from(parent.getContext()).inflate(resource , parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int index){
        //Definimos los datos que vamos a mostrar

        Recolectas recolecta = recolectasList.get(index);

        holder.textViewTextoFecha.setText(recolecta.getFechaRecolecta());
        holder.textViewTextoKilos.setText(recolecta.getKilosRecolecta());
        holder.textViewTextoCultivoRecolectas.setText(recolecta.getCultivoRecolecta());
        holder.textViewTextoCodigoRecolecta.setText(recolecta.getCodigoRecolecta());

        holder.BindHolder(recolectasList.get(index));
    }

    @Override
    public int getItemCount() { //numero de vistas obtenidas
        return recolectasList.size();
    }

    public int getIndex() {
        return index;
    }


    //------------------------------//
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{

        //Referencias
        private TextView textViewTextoFecha;
        private TextView textViewTextoKilos;
        private TextView textViewTextoCultivoRecolectas;
        private TextView textViewTextoCodigoRecolecta;

        public View view;

        public ViewHolder(View view){
            super(view);

            this.view = view;

            textViewTextoFecha = view.findViewById(R.id.textoFechaRecolecta);
            textViewTextoKilos = view.findViewById(R.id.textoKilosRecolecta);
            textViewTextoCultivoRecolectas = view.findViewById(R.id.textoCultivoRecolecta);
            textViewTextoCodigoRecolecta = view.findViewById(R.id.textoCodigoRecolecta);

            view.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        }

        public void BindHolder(Recolectas item){
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    index = getAdapterPosition();
                    notifyDataSetChanged();
                    return false;
                }
            });
        }
    }
}

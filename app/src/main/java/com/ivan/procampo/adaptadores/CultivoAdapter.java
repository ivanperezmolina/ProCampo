package com.ivan.procampo.adaptadores;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.ivan.procampo.R;
import com.ivan.procampo.modelos.Cultivos;

import java.util.ArrayList;
import java.util.List;

public class CultivoAdapter extends RecyclerView.Adapter<CultivoAdapter.ViewHolder> {
    private int resource;
    private ArrayList<Cultivos> cultivosList;

    private int index;

    public CultivoAdapter(ArrayList<Cultivos> cultivosList, int resource){
        this.cultivosList = cultivosList;
        this.resource = resource;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) { //Se crea la vista
        View view = LayoutInflater.from(parent.getContext()).inflate(resource , parent, false);

        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int index) { //Definimos los datos que queremos mostrar


        Cultivos cultivo = cultivosList.get(index);

        holder.textViewTextoCultivo.setText(cultivo.getNombreCultivo());
        holder.textViewTextoLocalizacion.setText(cultivo.getLocalizacionCultivo());
        holder.textViewTextoTipoAceituna.setText(cultivo.getTipoDeAceituna());
        holder.textViewTextoHectareas.setText(cultivo.getHectareasCultivo());
        holder.textViewTextoCodigoCultivo.setText(cultivo.getCodigoCultivo());

        holder.BindHolder(cultivosList.get(index)) ;


    }

    @Override
    public int getItemCount() { //numero de vistas obtenidas
        return cultivosList.size();
    }



    public int getIndex() {
        return index;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{

        //Referencias

        private TextView textViewTextoCultivo;
        private TextView textViewTextoLocalizacion;
        private TextView textViewTextoTipoAceituna;
        private TextView textViewTextoHectareas;
        private TextView textViewTextoCodigoCultivo;

        public View view;

        public ViewHolder(View view){
            super(view);

            this.view = view;

            textViewTextoCultivo = view.findViewById(R.id.textoCultivo);
            textViewTextoLocalizacion = view.findViewById(R.id.textoLocalizacion);
            textViewTextoTipoAceituna = view.findViewById(R.id.textoTipoAceituna);
            textViewTextoHectareas = view.findViewById(R.id.textoHectareas);

            textViewTextoCodigoCultivo = view.findViewById(R.id.textoCodigoCultivo);

            view.setOnCreateContextMenuListener(this);


        }



        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {

        }

        public void BindHolder(Cultivos item){
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

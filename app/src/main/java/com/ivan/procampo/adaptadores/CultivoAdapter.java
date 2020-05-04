package com.ivan.procampo.adaptadores;

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
    private List<Cultivos> cultivosList;

    private int index;

    public CultivoAdapter(List<Cultivos> cultivosList, int resource){
        this.cultivosList = cultivosList;
        this.resource = resource;
    }

    public CultivoAdapter(FragmentActivity activity) {
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) { //Se crea la vista
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cultivo_view , null, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int index) { //Definimos los datos que queremos mostrar
        Cultivos cultivo = cultivosList.get(index);

        holder.textViewTextoCultivo.setText(cultivo.getNombreCultivo());
        holder.textViewTextoHectareas.setText(cultivo.getHectareasCultivo());
        holder.textViewTextoTipoAceituna.setText(cultivo.getTipoDeAceituna());
        holder.textViewTextoLocalizacion.setText(cultivo.getLocalizacionCultivo());
        holder.textViewTextoCodigoCultivo.setText(cultivo.getCodigoCultivo());





        holder.BindHolder(cultivosList.get(index)) ;
    }

    @Override
    public int getItemCount() { //numero de vistas obtenidas
        return cultivosList.size();
    }

    public int getIndex(){
        return index;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{

        //Referencias

        private TextView textViewTextoCultivo;
        private TextView textViewTextoHectareas;
        private TextView textViewTextoTipoAceituna;
        private TextView textViewTextoLocalizacion;
        private  TextView textViewTextoCodigoCultivo;

        public View view;

        public ViewHolder(View view){
            super(view);

            this.view = view;

            this.textViewTextoCultivo = view.findViewById(R.id.textoCultivo);
            this.textViewTextoHectareas = view.findViewById(R.id.textoHectareas);
            this.textViewTextoTipoAceituna = view.findViewById(R.id.textoTipoAceituna);
            this.textViewTextoLocalizacion = view.findViewById(R.id.textoLocalizacion);
            this.textViewTextoCodigoCultivo = view.findViewById(R.id.textoCodigoCultivo);

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
                    return false;
                }
            });
        }
    }
}

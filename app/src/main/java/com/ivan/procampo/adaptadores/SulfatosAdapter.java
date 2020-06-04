package com.ivan.procampo.adaptadores;

import android.os.Build;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.ivan.procampo.R;
import com.ivan.procampo.modelos.Sulfatos;

import java.util.ArrayList;

public class SulfatosAdapter extends RecyclerView.Adapter<SulfatosAdapter.ViewHolder> {
    private int resource;
    private ArrayList<Sulfatos> sulfatosList;
    private int index;

    public SulfatosAdapter(ArrayList<Sulfatos> sulfatosList, int resource){
        this.sulfatosList = sulfatosList;
        this.resource = resource;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resource,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int index) {
        Sulfatos sulfato = sulfatosList.get(index);

        holder.textViewTextoCodigoSulfato.setText(sulfato.getCodigoSulfato());
        holder.textViewTextoCultivoTratamiento.setText(sulfato.getCultivoSulfato());
        holder.textViewTextoFechaSulfato.setText(sulfato.getFechaSulfato());
        holder.textViewTextoTratamientoSulfato.setText(sulfato.getTratamientoSulfato());

        holder.BindHolder(sulfatosList.get(index));

    }

    @Override
    public int getItemCount() {
        return sulfatosList.size();
    }

    public int getIndex() {
        return index;
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{

        //Referencias
        private TextView textViewTextoCodigoSulfato;
        private TextView textViewTextoFechaSulfato;
        private TextView textViewTextoTratamientoSulfato;
        private TextView textViewTextoCultivoTratamiento;

        ConstraintLayout expandableView;
        Button arrowBtn;
        CardView cardView;

        public View view;

        public ViewHolder(View view){
            super(view);

            this.view = view;

            textViewTextoCodigoSulfato = view.findViewById(R.id.textoCodigoSulfato);
            textViewTextoFechaSulfato = view.findViewById(R.id.textoFechaSulfato);
            textViewTextoCultivoTratamiento = view.findViewById(R.id.textoCultivoSulfato);
            textViewTextoTratamientoSulfato = view.findViewById(R.id.textoTratamientoSulfato);

            view.setOnCreateContextMenuListener(this);
        }


        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        }

        public void BindHolder(Sulfatos item){
            view.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onClick(View v) {

                    expandableView = view.findViewById(R.id.expandableView);
                    arrowBtn = view.findViewById(R.id.arrowBtn);
                    cardView = view.findViewById(R.id.cardViewSulfatos);

                    if (expandableView.getVisibility()==View.GONE){
                        TransitionManager.beginDelayedTransition(cardView,new AutoTransition());
                        expandableView.setVisibility(View.VISIBLE);
                        arrowBtn.setBackgroundResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
                    }else {
                        TransitionManager.beginDelayedTransition(cardView,new AutoTransition());
                        expandableView.setVisibility(View.GONE);
                        arrowBtn.setBackgroundResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
                    }

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

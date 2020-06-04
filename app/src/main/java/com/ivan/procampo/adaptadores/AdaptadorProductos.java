package com.ivan.procampo.adaptadores;


import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ivan.procampo.R;
import com.ivan.procampo.modelos.Producto;
import com.ivan.procampo.modelos.Sulfatos;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdaptadorProductos extends RecyclerView.Adapter<AdaptadorProductos.ViewHolder> {

    private int resource;
    private ArrayList<Producto> productosList;
    private int index;

    public AdaptadorProductos(ArrayList<Producto> productosList,int resource) {
        this.productosList = productosList;
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
        Producto producto = productosList.get(index);
        Picasso.get().load(productosList.get(index).getImage()).into(holder.imageFer);
        holder.textViewtvNomProducto.setText(producto.getNomProducto());
        holder.textViewtvPrecio.setText(producto.getPrecio());
        holder.textViewCodigoProducto.setText(producto.getIdProducto());

        holder.BindHolder(productosList.get(index));

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{

        private TextView textViewCodigoProducto;
        private TextView textViewtvNomProducto;
        private TextView textViewtvPrecio;
        ImageView imageFer;

        public View view;

        public ViewHolder(View view){
            super(view);

            this.view = view;
            textViewCodigoProducto = view.findViewById(R.id.codigoProducto);
            textViewtvNomProducto = view.findViewById(R.id.tvNomProducto);
            textViewtvPrecio = view.findViewById(R.id.tvPrecio);
            imageFer = view.findViewById(R.id.imageFer);

            view.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        }

        public void BindHolder(Producto item){
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

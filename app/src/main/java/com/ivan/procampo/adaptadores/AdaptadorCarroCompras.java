package com.ivan.procampo.adaptadores;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ivan.procampo.CarroCompra;
import com.ivan.procampo.R;
import com.ivan.procampo.modelos.Fertilizantes;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AdaptadorCarroCompras extends RecyclerView.Adapter<AdaptadorCarroCompras.ViewHolder> {
    Context context;
    ArrayList<Fertilizantes> carroCompra;
    TextView tvTotal;
    double total = 0;
    double IVA = 0;

    public AdaptadorCarroCompras(Context context, ArrayList<Fertilizantes> carroCompra, TextView tvTotal) {
        this.context = context;
        this.carroCompra = carroCompra;
        this.tvTotal = tvTotal;

        for (int i = 0; i < carroCompra.size();i++){
            total = total + Double.parseDouble(""+carroCompra.get(i).getPrecio());
        }

        //Aquí añadimos el 21% de IVA
        IVA = total * 0.21;



        double elTotal = (total+IVA);

        NumberFormat df = NumberFormat.getNumberInstance(Locale.GERMAN);
        tvTotal.setText(""+(df.format(elTotal)));

    }

    @NonNull
    @Override
    public AdaptadorCarroCompras.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_rv_carro,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorCarroCompras.ViewHolder holder, int position) {
        holder.nombre.setText(carroCompra.get(position).getNombre());
        holder.precio.setText(""+carroCompra.get(position).getPrecio());
        Picasso.get().load(carroCompra.get(position).getImage()).into(holder.image);



    }

    @Override
    public int getItemCount() {
        return carroCompra.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView nombre,precio;
        ImageView image;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nombre = itemView.findViewById(R.id.tvNomProducto);
            precio = itemView.findViewById(R.id.tvPrecio);
            image = itemView.findViewById(R.id.fertilizantePic);


        }
    }
}

package com.ivan.procampo.adaptadores;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ivan.procampo.CarroCompra;
import com.ivan.procampo.R;
import com.ivan.procampo.TiendaActivity;
import com.ivan.procampo.modelos.Fertilizantes;
import com.ivan.procampo.modelos.Sulfatos;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;

public class FertilizanteAdapter extends RecyclerView.Adapter<FertilizanteAdapter.MyViewHolder> {

    Context context;
    ArrayList<Fertilizantes> fertilizantes;
    ArrayList<Fertilizantes> carroCompra;
    TextView tvCantProductos;
    Button btnVerCarro;


    public FertilizanteAdapter(Context context, ArrayList<Fertilizantes> fertilizantes, ArrayList<Fertilizantes> carroCompra, TextView tvCantProductos, Button btnVerCarro) {
        this.context = context;
        this.fertilizantes = fertilizantes;
        this.carroCompra = carroCompra;
        this.tvCantProductos = tvCantProductos;
        this.btnVerCarro = btnVerCarro;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.list_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.nombre.setText(fertilizantes.get(position).getNombre());
        holder.precio.setText(""+fertilizantes.get(position).getPrecio());
        Picasso.get().load(fertilizantes.get(position).getImage()).into(holder.image);

        holder.cbCarroFer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (holder.cbCarroFer.isChecked() == true){
                    tvCantProductos.setText(""+(Integer.parseInt(tvCantProductos.getText().toString().trim())+1));
                    carroCompra.add(fertilizantes.get(position));
                }else if (holder.cbCarroFer.isChecked()==false){
                    tvCantProductos.setText(""+(Integer.parseInt(tvCantProductos.getText().toString().trim())-1));
                    carroCompra.remove(fertilizantes.get(position));
                }
            }
        });

        btnVerCarro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CarroCompra.class);
                intent.putExtra("CarroCompras", carroCompra);
                context.startActivity(intent);
            }
        });

            }

    @Override
    public int getItemCount() {
        return fertilizantes.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView nombre,precio;
        ImageView image;
        CheckBox cbCarroFer;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            nombre = itemView.findViewById(R.id.tvNomProducto);
            precio = itemView.findViewById(R.id.tvPrecio);
            image = itemView.findViewById(R.id.fertilizantePic);


            cbCarroFer = itemView.findViewById(R.id.cbCarroFer);
        }
    }
}

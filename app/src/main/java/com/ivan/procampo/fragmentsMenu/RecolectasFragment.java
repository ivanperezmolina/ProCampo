package com.ivan.procampo.fragmentsMenu;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ivan.procampo.R;
import com.ivan.procampo.adaptadores.RecolectaAdapter;
import com.ivan.procampo.funcionalidades.ActualizarRecolectaActivity;
import com.ivan.procampo.funcionalidades.AnnadirFotoDatRecolecta;
import com.ivan.procampo.funcionalidades.AnnadirFotoValeRecolecta;
import com.ivan.procampo.funcionalidades.AnnadirRecolectaActivity;
import com.ivan.procampo.modelos.Cultivos;
import com.ivan.procampo.modelos.Recolectas;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecolectasFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecolectasFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //-------------------------------------------//
    // MIS VARIABLES //
    private DatabaseReference mDatabase;

    private FirebaseAuth mAuth;

    private Button botonNuevaRecolecta;

    private RecyclerView recyclerViewRecolecta;

    private ArrayList<Recolectas> listaRecolectas = new ArrayList<>();

    private RecolectaAdapter adapter;

    Boolean vengoDeFoto = false;


    //-------------------------------------------//

    public RecolectasFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RecolectasFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RecolectasFragment newInstance(String param1, String param2) {
        RecolectasFragment fragment = new RecolectasFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


//Declaro las variables para autenticación y BBDD
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        llenarLista();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View vista = inflater.inflate(R.layout.fragment_recolectas, container, false);

        //Declaro las variables para autenticación y BBDD
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        //Referencia a las variables
        botonNuevaRecolecta = vista.findViewById(R.id.botonAnnadirRecolecta);
        recyclerViewRecolecta = vista.findViewById(R.id.recyclerViewRecolectas);

        //Lanzamos el Layout Manager
        recyclerViewRecolecta.setLayoutManager(new LinearLayoutManager(getActivity()));

        //Lanzamos metodo para llenar la lista




        //Pasamos el parametro
        registerForContextMenu(recyclerViewRecolecta);

        //Cuando se pulse en añadir, reemplazamos el fragment

        botonNuevaRecolecta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listaRecolectas.clear();
                //Vamos a la activity de añadir cultivo
                Intent nuevaRecolecta = new Intent(getActivity(), AnnadirRecolectaActivity.class);
                startActivity(nuevaRecolecta);
            }
        });


        return vista;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    /**
     * Método creado para coger los datos de Firebase
     *
     */

    private void llenarLista(){
        mDatabase.child("RECOLECTAS").child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot ds : dataSnapshot.getChildren()){
                        String codigoRecolecta = ds.child("codigoRecolecta").getValue().toString();
                        String cultivoRecolecta = ds.child("cultivoRecolecta").getValue().toString();
                        String fechaRecolecta = ds.child("fechaRecolecta").getValue().toString();
                        String kilosRecolecta = ds.child("kilosRecolecta").getValue().toString();

                        listaRecolectas.add(new Recolectas(codigoRecolecta,fechaRecolecta,kilosRecolecta,cultivoRecolecta));

                    }

                    adapter = new RecolectaAdapter(listaRecolectas,R.layout.recolecta_view);
                    recyclerViewRecolecta.setAdapter(adapter);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.menu_contextual_recolectas,menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

            switch (item.getItemId()){
                //EDITAR RECOLECTA
                case R.id.ctxModRecolecta:
                    vengoDeFoto = false;
                    final Recolectas recolecta = listaRecolectas.get(adapter.getIndex());

                    //Vamos a la actividad, pasando los datos
                    Intent irAEditarRecolecta = new Intent(getActivity(), ActualizarRecolectaActivity.class);

                    irAEditarRecolecta.putExtra("codigoRecolecta",recolecta.getCodigoRecolecta());
                    irAEditarRecolecta.putExtra("cultivoRecolecta",recolecta.getCultivoRecolecta());
                    irAEditarRecolecta.putExtra("fechaRecolecta",recolecta.getFechaRecolecta());
                    irAEditarRecolecta.putExtra("kilosRecolecta",recolecta.getKilosRecolecta());

                    listaRecolectas.clear();

                    startActivity(irAEditarRecolecta);

                    break;

                    case R.id.ctxDelRecolecta:
                        vengoDeFoto = false;
                    Recolectas recolectaAdios = listaRecolectas.get(adapter.getIndex());

                    String cultivoRecolecta = recolectaAdios.getCultivoRecolecta();
                    String fechaRecolecta = recolectaAdios.getFechaRecolecta();

                    AlertDialog.Builder myBuild = new AlertDialog.Builder(getContext());

                    myBuild.setTitle(R.string.confi_borrar);
                    myBuild.setMessage("¿Quiere eliminar la recolecta en el cultivo "+cultivoRecolecta+" con fecha "+fechaRecolecta+" ?");
                    //SI
                    myBuild.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            Recolectas recolectas = listaRecolectas.get(adapter.getIndex());
                            String codigo = recolectas.getCodigoRecolecta();
                            listaRecolectas.clear();
                            mDatabase.child("RECOLECTAS").child(mAuth.getCurrentUser().getUid()).child(codigo).removeValue();
                            listaRecolectas.clear();
                            listaRecolectas.remove(true);
                            //listaRecolectas.notify();

                            //listaCultivos.clear();

                            //llenarLista();

                        }
                    });

                    //NO
                    myBuild.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    //Construir el alert
                    AlertDialog dialog = myBuild.create();
                    dialog.show();


                    break;

                case R.id.annadirFotoVale:
                   //Vamos a la actividad
                    vengoDeFoto = true;
                    final Recolectas recolectaPaFoto = listaRecolectas.get(adapter.getIndex());
                    Intent irAFotoVale = new Intent(getActivity(), AnnadirFotoValeRecolecta.class);
                    irAFotoVale.putExtra("codigoRecolecta",recolectaPaFoto.getCodigoRecolecta());
                    irAFotoVale.putExtra("cultivoRecolecta",recolectaPaFoto.getCultivoRecolecta());
                    irAFotoVale.putExtra("fechaRecolecta",recolectaPaFoto.getFechaRecolecta());
                    irAFotoVale.putExtra("kilosRecolecta",recolectaPaFoto.getKilosRecolecta());
                   // listaRecolectas.clear();
                    startActivity(irAFotoVale);

                    break;

                case R.id.annadirFotoDat:
                    vengoDeFoto=true;
                    final Recolectas recolectaPaFotoDAT = listaRecolectas.get(adapter.getIndex());
                    Intent irAFotoValeDAT = new Intent(getActivity(), AnnadirFotoDatRecolecta.class);
                    irAFotoValeDAT.putExtra("codigoRecolecta",recolectaPaFotoDAT.getCodigoRecolecta());
                    irAFotoValeDAT.putExtra("cultivoRecolecta",recolectaPaFotoDAT.getCultivoRecolecta());
                    irAFotoValeDAT.putExtra("fechaRecolecta",recolectaPaFotoDAT.getFechaRecolecta());
                    irAFotoValeDAT.putExtra("kilosRecolecta",recolectaPaFotoDAT.getKilosRecolecta());
                    //listaRecolectas.clear();
                    startActivity(irAFotoValeDAT);

                    break;
            }



        return super.onContextItemSelected(item);
    }
}

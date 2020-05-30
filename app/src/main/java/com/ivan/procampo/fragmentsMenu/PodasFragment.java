package com.ivan.procampo.fragmentsMenu;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ivan.procampo.R;
import com.ivan.procampo.adaptadores.PodaAdapter;
import com.ivan.procampo.adaptadores.RecolectaAdapter;
import com.ivan.procampo.funcionalidades.ActualizarPodaActivity;
import com.ivan.procampo.funcionalidades.ActualizarRecolectaActivity;
import com.ivan.procampo.funcionalidades.AnnadirFotoValeRecolecta;
import com.ivan.procampo.funcionalidades.AnnadirPodaActivity;
import com.ivan.procampo.funcionalidades.AnnadirRecolectaActivity;
import com.ivan.procampo.modelos.Podas;
import com.ivan.procampo.modelos.Recolectas;

import java.util.ArrayList;


public class PodasFragment extends Fragment {
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

    private Button botonNuevaPoda;

    private RecyclerView recyclerViewPoda;

    private ArrayList<Podas> listaPodas = new ArrayList<>();

    private PodaAdapter adapter;


    //-------------------------------------------//

    public PodasFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PodasFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PodasFragment newInstance(String param1, String param2) {
        PodasFragment fragment = new PodasFragment();
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
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vista = inflater.inflate(R.layout.fragment_podas, container, false);

        //Declaro las variables para autenticación y BBDD
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        //Referencia a las variables
        botonNuevaPoda = vista.findViewById(R.id.botonAnnadirPoda);
        recyclerViewPoda = vista.findViewById(R.id.recyclerViewPodas);

        //Lanzamos el Layout Manager
        recyclerViewPoda.setLayoutManager(new LinearLayoutManager(getActivity()));

        //Lanzamos metodo para llenar la lista
        llenarLista();

        //Pasamos el parametro
        registerForContextMenu(recyclerViewPoda);

        //Cuando se pulse en añadir, reemplazamos el fragment
        botonNuevaPoda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listaPodas.clear();
                //Vamos a la activity de añadir cultivo
                Intent nuevaPoda = new Intent(getActivity(), AnnadirPodaActivity.class);
                startActivity(nuevaPoda);
            }
        });

        return vista;
    }

    /**
     * Mñetodo que me trae los datos de firebase
     */
    private void llenarLista() {
        mDatabase.child("PODAS").child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot ds : dataSnapshot.getChildren()){
                        String codigoPoda = ds.child("codigoPoda").getValue().toString();
                        String cultivoPoda = ds.child("cultivoPoda").getValue().toString();
                        String fechaPoda = ds.child("fechaPoda").getValue().toString();


                        listaPodas.add(new Podas(codigoPoda,cultivoPoda,fechaPoda));

                    }

                    adapter = new PodaAdapter(listaPodas,R.layout.poda_view);
                    recyclerViewPoda.setAdapter(adapter);


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
        inflater.inflate(R.menu.menu_contextual_podas,menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        switch (item.getItemId()){
            //EDITAR RECOLECTA
            case R.id.ctxModPoda:
                final Podas poda = listaPodas.get(adapter.getIndex());

                //Vamos a la actividad, pasando los datos
                Intent irAEditarPoda = new Intent(getActivity(), ActualizarPodaActivity.class);

                irAEditarPoda.putExtra("codigoPoda",poda.getCodigoPoda());
                irAEditarPoda.putExtra("cultivoPoda",poda.getCultivoPoda());
                irAEditarPoda.putExtra("fechaPoda",poda.getFechaPoda());

                startActivity(irAEditarPoda);

                break;

            case R.id.ctxDelPoda:
                Podas podaAdios = listaPodas.get(adapter.getIndex());

                String cultivoPoda = podaAdios.getCultivoPoda();
                String fechaPoda = podaAdios.getFechaPoda();

                AlertDialog.Builder myBuild = new AlertDialog.Builder(getContext());

                myBuild.setTitle("CONFIRMACIÓN DE BORRADO");
                myBuild.setMessage("¿Quiere eliminar la poda en el cultivo "+cultivoPoda+" con fecha "+fechaPoda+" ?");
                //SI
                myBuild.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Podas podas = listaPodas.get(adapter.getIndex());
                        String codigo = podas.getCodigoPoda();

                        mDatabase.child("PODAS").child(mAuth.getCurrentUser().getUid()).child(codigo).removeValue();

                        listaPodas.notify();

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


        }
        return super.onContextItemSelected(item);
    }
}

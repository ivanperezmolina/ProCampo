package com.ivan.procampo.fragmentsMenu;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ivan.procampo.R;
import com.ivan.procampo.adaptadores.CultivoAdapter;
import com.ivan.procampo.funcionalidades.AnnadirCultivoActivity;
import com.ivan.procampo.modelos.Cultivos;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CultivosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CultivosFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    // MIS VARIABLES
    private Button botonNuevoCultivo;

    RecyclerView recyclerViewCultivos;

    ArrayList<Cultivos> listaCultivos = new ArrayList<>();

    private BottomNavigationView bottomNavigationView;

    private DatabaseReference mDatabase;

    private FirebaseAuth mAuth;

    CultivoAdapter adapter;

    public CultivosFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CultivosFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CultivosFragment newInstance(String param1, String param2) {
        CultivosFragment fragment = new CultivosFragment();


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

        View vista = inflater.inflate(R.layout.fragment_cultivos, container, false);

        //Declaramos las variables para autenticación y BBDD
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        //Referencia a las variables
        botonNuevoCultivo = vista.findViewById(R.id.botonAnnadirCultivo);
        recyclerViewCultivos = vista.findViewById(R.id.recyclerViewCultivos);

        //Lanzamos el LayoutManager
        recyclerViewCultivos.setLayoutManager(new LinearLayoutManager(getActivity()));

        //Lanzamos el metodo para llenar la lista

        llenarLista();

        //Pasamos el parametro
        registerForContextMenu(recyclerViewCultivos);


        //Reemplazar el fragment para añadir

        botonNuevoCultivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Vamos a la activity de añadir cultivo
                listaCultivos.clear();
                Intent nuevoCultivo = new Intent(getActivity(), AnnadirCultivoActivity.class);
                startActivity(nuevoCultivo);

            }
        });


        // Inflate the layout for this fragment
        return vista ;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    /**
     * Metodo creado para coger los datos de Firebase
     *
     */
    private void llenarLista() {
        mDatabase.child("CULTIVOS").child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot ds : dataSnapshot.getChildren()){
                        String codigoCultivo = ds.child("codigoCultivo").getValue().toString();
                        String nombreCultivo = ds.child("nombreCultivo").getValue().toString();
                        String hectareasCultivo = ds.child("hectareasCultivo").getValue().toString();
                        String tipoAceituna = ds.child("tipoDeAceituna").getValue().toString();
                        String localizacionCultivo = ds.child("localizacionCultivo").getValue().toString();

                        Log.i("CULTIVO:", String.valueOf(listaCultivos));



                        listaCultivos.add(new Cultivos(codigoCultivo,nombreCultivo,localizacionCultivo,hectareasCultivo,tipoAceituna));

                    }

                    adapter = new CultivoAdapter(listaCultivos,R.layout.cultivo_view);
                    recyclerViewCultivos.setAdapter(adapter);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}

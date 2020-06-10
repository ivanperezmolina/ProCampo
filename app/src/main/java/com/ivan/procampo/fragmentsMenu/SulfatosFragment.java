package com.ivan.procampo.fragmentsMenu;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.transition.AutoTransition;
import android.transition.TransitionManager;
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
import com.ivan.procampo.adaptadores.SulfatosAdapter;
import com.ivan.procampo.funcionalidades.ActualizarPodaActivity;
import com.ivan.procampo.funcionalidades.ActualizarSulfatoActivity;
import com.ivan.procampo.funcionalidades.AnnadirNuevoSulfato;
import com.ivan.procampo.funcionalidades.AnnadirPodaActivity;
import com.ivan.procampo.modelos.Podas;
import com.ivan.procampo.modelos.Sulfatos;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SulfatosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SulfatosFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //-----------------------------------------------------------------
    // MIS VARIABLES
        ConstraintLayout expandableView;
        Button arrowBtn;
        CardView cardView;
        private DatabaseReference mDatabase;

        private FirebaseAuth mAuth;

        private Button botonNuevoSulfato;

        private RecyclerView recyclerViewSulfatos;

        private ArrayList<Sulfatos> listaSulfatos = new ArrayList<>();

        private SulfatosAdapter adapter;

    //-----------------------------------------------------------------
    public SulfatosFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SulfatosFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SulfatosFragment newInstance(String param1, String param2) {
        SulfatosFragment fragment = new SulfatosFragment();
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
       // recyclerViewSulfatos =findViewById(R.id.recyclerViewSulfatos);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_sulfatos, container, false);
        View vistaExpansible = inflater.inflate(R.layout.sulfato_view, container, false);

        expandableView = vistaExpansible.findViewById(R.id.expandableView);
        arrowBtn = vistaExpansible.findViewById(R.id.arrowBtn);
        cardView = vistaExpansible.findViewById(R.id.cardViewSulfatos);

        arrowBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {

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


        //Declaro las variables para autenticación y BBDD
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        //Referencia a las variables
        botonNuevoSulfato = vista.findViewById(R.id.botonAnnadirSulfato);
        recyclerViewSulfatos = vista.findViewById(R.id.recyclerViewSulfatos);

        //Lanzamos el Layout Manager
        recyclerViewSulfatos.setLayoutManager(new LinearLayoutManager(getActivity()));

        //Lanzamos metodo para llenar la lista
        llenarLista();

        //Pasamos el parametro
        registerForContextMenu(recyclerViewSulfatos);

        //Cuando se pulse en añadir, reemplazamos el fragment
        botonNuevoSulfato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listaSulfatos.clear();
                //Vamos a la activity de añadir cultivo
                Intent nuevoSulfato = new Intent(getActivity(), AnnadirNuevoSulfato.class);
                startActivity(nuevoSulfato);
            }
        });

        // Inflate the layout for this fragment
        return vista;
    }

    /**
     * Metodo que me trae los datos de firebase
     */
    private void llenarLista() {
        mDatabase.child("SULFATOS").child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot ds : dataSnapshot.getChildren()){
                        String codigoSulfato = ds.child("codigoSulfato").getValue().toString();
                        String fechaSulfato = ds.child("fechaSulfato").getValue().toString();
                        String cultivoSulfato = ds.child("cultivoSulfato").getValue().toString();
                        String tratamientoSulfato = ds.child("tratamientoSulfato").getValue().toString();




                        listaSulfatos.add(new Sulfatos(codigoSulfato,fechaSulfato,cultivoSulfato,tratamientoSulfato));

                    }


                    adapter = new SulfatosAdapter(listaSulfatos,R.layout.sulfato_view);
                    recyclerViewSulfatos.setAdapter(adapter);


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
        inflater.inflate(R.menu.menu_contextual_sulfatos,menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.ctxModSulfato:
                final Sulfatos sulfato = listaSulfatos.get(adapter.getIndex());

                //Vamos a la actividad, pasando los datos
                Intent irAEditarSulfato = new Intent(getActivity(), ActualizarSulfatoActivity.class);

                irAEditarSulfato.putExtra("codigoSulfato",sulfato.getCodigoSulfato());
                irAEditarSulfato.putExtra("cultivoSulfato",sulfato.getCultivoSulfato());
                irAEditarSulfato.putExtra("fechaSulfato",sulfato.getFechaSulfato());
                irAEditarSulfato.putExtra("tratamientoSulfato",sulfato.getTratamientoSulfato());
                listaSulfatos.clear();
                startActivity(irAEditarSulfato);

                break;

            case R.id.ctxDelSulfato:

                Sulfatos sulfatoAdios = listaSulfatos.get(adapter.getIndex());

                String cultivoSulfato = sulfatoAdios.getCultivoSulfato();
                String fechaSulfato = sulfatoAdios.getFechaSulfato();

                AlertDialog.Builder myBuild = new AlertDialog.Builder(getContext());

                myBuild.setTitle("CONFIRMACIÓN DE BORRADO");
                myBuild.setMessage("¿Quiere eliminar el sulfato en el cultivo "+cultivoSulfato+" con fecha "+fechaSulfato+" ?");
                //SI
                myBuild.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Sulfatos sulfatos = listaSulfatos.get(adapter.getIndex());
                        String codigo = sulfatos.getCodigoSulfato();

                        listaSulfatos.clear();
                        mDatabase.child("SULFATOS").child(mAuth.getCurrentUser().getUid()).child(codigo).removeValue();
                        listaSulfatos.clear();
                        listaSulfatos.remove(true);
                        //listaSulfatos.notify();

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

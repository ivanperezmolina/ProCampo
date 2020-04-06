package com.ivan.procampo.fragmentsMenu;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.ivan.procampo.R;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View vista = inflater.inflate(R.layout.fragment_cultivos, container, false);

        //Referencia a las variables
        botonNuevoCultivo = vista.findViewById(R.id.botonAnnadirCultivo);

        //Remplazar el fragment

        botonNuevoCultivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                        AnnadirCultivoFragment fragmentNuevoCultivo = new AnnadirCultivoFragment();
                        //Hago la actualización

                        fragmentTransaction.replace(R.id.fragment_container_cultivos, fragmentNuevoCultivo);
                        fragmentTransaction.addToBackStack(null);

                        // Commit a la transacción
                        fragmentTransaction.commit();

            }
        });



        return vista ;

    }
}

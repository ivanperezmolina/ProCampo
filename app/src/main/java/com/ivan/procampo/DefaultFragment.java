package com.ivan.procampo;

import android.animation.ArgbEvaluator;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;

import com.ivan.procampo.adaptadores.SliderAdapter;
import com.ivan.procampo.funcionalidades.AnnadirCultivoActivity;
import com.ivan.procampo.modelos.ModelSlider;

import java.util.ArrayList;
import java.util.List;


public class DefaultFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //--Mis Variables
    ViewPager viewPager;
    SliderAdapter adapter;
    List<ModelSlider> models;
    Integer[] colors = null;
    ArgbEvaluator argbEvaluator = new ArgbEvaluator();

    ViewPager frame;


    public DefaultFragment() {
        // Required empty public constructor
    }


    public static DefaultFragment newInstance(String param1, String param2) {
        DefaultFragment fragment = new DefaultFragment();
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
        final View view = inflater.inflate(R.layout.fragment_default, container, false);



        //Defino que voy a mostrar
        models = new ArrayList<>();
        models.add(new ModelSlider(R.drawable.miscultivos,R.string.gestione_cultivos,R.string.gestione_cultivos_des));
        models.add(new ModelSlider(R.drawable.misrecolectas,R.string.gestione_recolecta, R.string.gestione_recolecta_des));
        models.add(new ModelSlider(R.drawable.mispodas,R.string.gestione_poda, R.string.gestione_poda_des));
        models.add(new ModelSlider(R.drawable.missulfatos,R.string.gestione_sulfatos, R.string.gestione_sulfatos_des));
        models.add(new ModelSlider(R.drawable.fertii,R.string.gestione_ferti, R.string.gestione_ferti_des));
        models.add(new ModelSlider(R.drawable.jornales,R.string.adios_papeleo, R.string.adios_papeleo_des));

        adapter = new SliderAdapter(models,getContext());

        viewPager = view.findViewById(R.id.viewPager);
        //frame = view.findViewById(R.id.frame);
        viewPager.setAdapter(adapter);
        viewPager.setPadding(130,0,130,0);

        Integer[] colors_temp =
                {

                        getResources().getColor(R.color.color1),
                        getResources().getColor(R.color.color2),
                        getResources().getColor(R.color.color3),
                        getResources().getColor(R.color.color4),
                        getResources().getColor(R.color.color5),
                        getResources().getColor(R.color.color6)

                };

        colors = colors_temp;


        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position < (adapter.getCount() -1 ) && position < (colors.length -1 )){
                    viewPager.setBackgroundColor((Integer) argbEvaluator.evaluate(positionOffset,colors[position],colors[position + 1]));


                }else{
                    viewPager.setBackgroundColor(colors[colors.length-1]);
                }


            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });



        return view;
    }
}

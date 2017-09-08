package com.example.konrad.applicationsecond.LekcjaFragmenty;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.konrad.applicationsecond.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class pierwszy extends Fragment {


    public pierwszy() {
        // Required empty public constructor
    }
    private Context context;


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        // zeby zrobic przycisk z wyswietlaniem tekstu zakomentowujemy poprzedni return a tworzymy View i return v;
        View v = inflater.inflate(R.layout.fragment_pierwszy, container, false);
        Button przycisk = (Button) v.findViewById(R.id.btn16);
        przycisk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Zapisane", Toast.LENGTH_SHORT).show();

            }
        });
        return v;
//        return inflater.inflate(R.layout.fragment_pierwszy, container, false);
    }

}

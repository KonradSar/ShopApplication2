package com.example.konrad.applicationsecond.LekcjaFragmenty;


import android.os.Bundle;
import android.support.annotation.Nullable;
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
public class drugi extends Fragment {


    public drugi() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_drugi, container, false);
        View v = inflater.inflate(R.layout.fragment_drugi, container, false);
        Button przycisk = (Button) v.findViewById(R.id.btn17);

        przycisk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), wiadomosc, Toast.LENGTH_SHORT).show();
            }
        });
        return v;
    }
String wiadomosc;
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null){
            wiadomosc = bundle.getString("tajne");
        }
    }
}

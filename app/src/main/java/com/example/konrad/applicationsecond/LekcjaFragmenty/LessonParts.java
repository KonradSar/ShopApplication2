package com.example.konrad.applicationsecond.LekcjaFragmenty;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.FrameLayout;

import com.example.konrad.applicationsecond.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LessonParts extends AppCompatActivity {
    @BindView(R.id.button13) Button buttonOne;
    @BindView(R.id.button12) Button buttonTwo;
    @BindView(R.id.Framelay) FrameLayout layout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_parts);
        ButterKnife.bind(this);
    }
    @OnClick(R.id.button12)
    public void wczytajPierwszyFragment(){
        zaladujFragment(new pierwszy());
    }
    @OnClick(R.id.button13)
    public void wczytajFragmentDrugi(){
        drugi drugiFragment = new drugi();
        Bundle bundle = new Bundle();
        bundle.putString("tajne", "Tajny kod to bleble");
        drugiFragment.setArguments(bundle);
        zaladujFragment(drugiFragment);
    }
    public void zaladujFragment(Fragment fragment){
        FragmentManager fm = getSupportFragmentManager();
        // wybieramy v4 version
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.Framelay, fragment);
        fragmentTransaction.commit();
    }
}

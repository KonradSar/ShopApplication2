package com.example.konrad.applicationsecond;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;



public class Weatherapi extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_INTERNET = 1;
    final String TAG = "KonradApp";
    MediaPlayer myThirdSound;

    @BindView(R.id.cityEditText)
    EditText cityField;
    @BindView(R.id.temperatureView)
    TextView tempView;
    @BindView(R.id.selectBtn)
    Button selectCity;
    @BindView(R.id.secondText) TextView secondTextView;
    @BindView(R.id.pressureView) TextView pressureView;
    @BindView(R.id.cityName) TextView nameOfSelectedCity;
    @BindView(R.id.clouds) TextView allClouds;
    @BindView(R.id.description) TextView weatherDescription;
    @BindView(R.id.thirdItem) TextView townName;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        ButterKnife.bind(this);
        myThirdSound = MediaPlayer.create(this, R.raw.klikanie);

        getSupportActionBar().setTitle("What is the weather like today?");


        if (selectCity.equals("")) {
            nameOfSelectedCity.setText("Wpisz Miasto");
        }
        else{
            nameOfSelectedCity.setText(selectCity.getText());
        }


        selectCity.setBackgroundColor(R.color.yellowish);
    }
    public void onBackPressed() {
        myThirdSound.start();
        Intent intent = new Intent(Weatherapi.this, ShopRecyclerView.class);
        startActivity(intent);

    }

    @OnClick(R.id.cityEditText)
    public void show(){
        myThirdSound.start();

    }

    @OnClick(R.id.selectBtn)
    public void checkTemp() {
        myThirdSound.start();
        String name = cityField.getText().toString();
        AsyncTask<Void, Void, WeatherResults> execute = new Weather(name).execute();
        try {
            WeatherResults weatherResults = execute.get();
            nameOfSelectedCity.setText("Selected city: " + name);
            String f = weatherResults.getTemperature();
            tempView.setText("Temperature: " + String.format(weatherResults.getTemperature(), f));
            String g = weatherResults.getWindSpeed();
            secondTextView.setText("Wind speed "+ String.format(weatherResults.getWindSpeed(), g)+" m/s");
            pressureView.setText("Pressure: " + weatherResults.getPressure()+" hPa");
            allClouds.setText("Clouds: "+ weatherResults.getClouds() + "%");
            weatherDescription.setText("Humidity: " + weatherResults.getHumidity() + "%");
            townName.setText("Country short: " + weatherResults.getCountryName());


        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}








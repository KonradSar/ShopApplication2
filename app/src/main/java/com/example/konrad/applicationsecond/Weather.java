package com.example.konrad.applicationsecond;

import android.os.AsyncTask;
import android.support.v7.widget.Toolbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Konrad on 27.06.2017.
 */

public class Weather extends AsyncTask<Void,Void,WeatherResults> {
    //

    private static String cityName;
    public Weather(String cityName) {
        this.cityName = cityName;
    }

    @Override
    protected WeatherResults doInBackground(Void... params) {
        URL url = null;
        try {

            url = new URL("http://api.openweathermap.org/data/2.5/weather?q=" + cityName + "&mode=JSON&units=metric&APPID=d2ba89f8c8804f13b22578b589a56f26");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("GET");
            http.connect();
            int responseCode = http.getResponseCode();
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(http.getInputStream()));
            String line = null;
            String result = null;
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            JSONObject jsonObject = new JSONObject(stringBuilder.toString());
            JSONObject clouds = (JSONObject) jsonObject.get("clouds");
            JSONObject main = (JSONObject) jsonObject.get("main");
            JSONObject wind = (JSONObject) jsonObject.get("wind");
            JSONObject sys = (JSONObject) jsonObject.get("sys");
            WeatherResults weatherResults1 = new WeatherResults(main.get("temp").toString(), wind.get("speed").toString(), main.get("pressure").toString(), clouds.get("all").toString(), main.get("humidity").toString(), sys.getString("country"));
            return weatherResults1;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}

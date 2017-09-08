package com.example.konrad.applicationsecond;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.R.attr.type;

/**
 * Created by Konrad on 27.07.2017.
 */

class HttpCallTask extends AsyncTask<Void, Void, List<Currencies>> {
    private static final String CODE = "code";
    private static final String CURRENCY = "currency";
    private static final String MID = "mid";
    private static final String GET = "GET";

    @Override
    protected List<Currencies> doInBackground(Void... params) {
        try {
            String[] tables = {"a", "b"};
            List<String> result = new ArrayList<>();
            for (String table : tables) {
                URL url = null;
                url = new URL("http://api.nbp.pl/api/exchangerates/tables/" + table + "/?format=json");
                HttpURLConnection http = (HttpURLConnection) url.openConnection();
                http.setRequestMethod(GET);
                http.connect();
                int responseCode = http.getResponseCode();
                if (responseCode == 200) { //jeżeli ok TO:
                    BufferedReader bufferedReader = new BufferedReader(
                            new InputStreamReader(http.getInputStream()));
                    String line = null;
                    StringBuilder stringBuilder = new StringBuilder();
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line);
                    }
                    JSONArray jsonArray = new JSONArray(stringBuilder.toString());
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = (JSONObject) jsonArray.get(i);
                        JSONArray ratesArray = (JSONArray) obj.get("rates");
                        List<Currencies> preParse = new ArrayList<>();
                        for (int x = 0; x < ratesArray.length(); x++) {
                            JSONObject o = (JSONObject) ratesArray.get(x);
                            preParse.add(x, new Currencies(o.get(CURRENCY).toString(), o.get(CODE).toString(), (double) o.get(MID)));
                        }
                        return preParse;
                    }

                } else {
                    System.out.println("tabla " + table + " nie obsluzona");
                }
            }

        } catch (ProtocolException e) {
            e.printStackTrace();
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

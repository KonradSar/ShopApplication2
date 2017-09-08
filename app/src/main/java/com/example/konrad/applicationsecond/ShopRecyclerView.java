package com.example.konrad.applicationsecond;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import butterknife.ButterKnife;

public class ShopRecyclerView extends AppCompatActivity {
    private static final int RESULT_LOAD_IMAGE = 2;
    private int MAKE_IMAGE_REQUEST = 3;
    android.support.design.widget.CoordinatorLayout coordinator;
    final String TAG = "KonradApp";
    private Context context1;
    public static double counter;
    public List<String> myList = new ArrayList<>();
    String phone_number = "";
    public static final int REQUEST_CODE = 1;
    public static final int REQUEST_CODE2 = 4;
    private static String odbiorcaSMS;
    public static String photoPath = "";
    public static String label2 = "";
    public final List<Float> listOfPrices = new ArrayList<>();
    public final List<String> listOfNames = new ArrayList<>();
    public List<Shopping> listOfPricesCreator = new ArrayList<>();
    MediaPlayer myFirstSound;
    MediaPlayer mySecondSound;
    MediaPlayer myThirdSound;
    MediaPlayer myFifthSound;
    MediaPlayer mySixthSound;
    MediaPlayer mySeventhSound;
    MediaPlayer myEighthSound;
    MediaPlayer myNinethSound;
    // testowa spacja

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        super.onContextItemSelected(item);
        switch (item.getItemId()) {
            case R.id.additionalInfo:
                Shopping selectedShoppingForExternalMemoryUpdate = NaszeMetody.shoppingList.get(NaszeMetody.position);
                final List<Shopping> listFromExternalMemory = readFromExternalMemory();
                for (int i = 0; i < listFromExternalMemory.size(); i++) {
                    if (listFromExternalMemory.get(i).getShoppingIndex()==selectedShoppingForExternalMemoryUpdate.getShoppingIndex()
                            && selectedShoppingForExternalMemoryUpdate.getAdditionalInfo() != null) {
                        mySecondSound.start();
                        String infoAdditional = selectedShoppingForExternalMemoryUpdate.getAdditionalInfo();
                        List<String> additionalInfoList = new ArrayList<>();
                        additionalInfoList.add(infoAdditional);
                        String[] additionals = new String[additionalInfoList.size()];
                        additionals = additionalInfoList.toArray(additionals);

                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ShopRecyclerView.this);
                        alertDialog.setTitle(R.string.AdditionalInfoAlert);
                        alertDialog.setItems(additionals, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        alertDialog.create();
                        alertDialog.show();

                    } else if (listFromExternalMemory.get(i).getShoppingIndex()==selectedShoppingForExternalMemoryUpdate.getShoppingIndex() && selectedShoppingForExternalMemoryUpdate.getAdditionalInfo() == null){
                        mySeventhSound.start();
                        Toast.makeText(getApplicationContext(), R.string.AdditionalInfo, Toast.LENGTH_SHORT).show();
                    }
                }
                return true;
            case R.id.goShopping:
                mySecondSound.start();
                NaszeMetody.shoppingList.get(NaszeMetody.position).setRedBackgroundColor(false);
                shoppingAdapter1.notifyDataSetChanged();
                zapiszDoPamieci(NaszeMetody.shoppingList);
                return true;
            case R.id.goBack:
                mySecondSound.start();
                NaszeMetody.shoppingList.get(NaszeMetody.position).setRedBackgroundColor(true);
                shoppingAdapter1.notifyDataSetChanged();
                zapiszDoPamieci(NaszeMetody.shoppingList);
                return true;
            case R.id.checkWeather:
                if (isNetworkAvailable()){
                    mySecondSound.start();
                    Intent intent = new Intent(ShopRecyclerView.this, Weatherapi.class);
                    startActivity(intent);
                }
                else
                {   mySeventhSound.start();
                    Toast.makeText(getApplicationContext(), R.string.NoInternetInformation, Toast.LENGTH_SHORT).show();
                }

                return true;
            case R.id.getCurrency:
                if (isNetworkAvailable()){
                    mySecondSound.start();
                    try {
                        AsyncTask<Void, Void, List<Currencies>> execute = new HttpCallTask().execute();
                        final List<Currencies> mojaLista = execute.get();
                        final List<String> listaaaa = new ArrayList<>();
                        final List<String> listForAdapter = new ArrayList<>();
                        final List<Double> exchangeRateList = new ArrayList<>();
                        for (Currencies mojItem : mojaLista) {
                            listaaaa.add(mojItem.getCode() + " : " + mojItem.getValue() + " PLN");
                            listForAdapter.add(mojItem.getCode());
                            exchangeRateList.add(mojItem.getValue());
                            NaszeMetody.exchangeRates = exchangeRateList;
                            NaszeMetody.menuCurrencies = listForAdapter;
                            zapiszDoPamieciListeWalut(listForAdapter);
                        }

                        String[] currencies = new String[mojaLista.size()];
                        currencies = listaaaa.toArray(currencies);
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ShopRecyclerView.this);
                        alertDialog.setTitle(R.string.CurrencyChoice);
                        alertDialog.setItems(currencies, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mojRecycler.setAdapter(shoppingAdapter1);
                                Currencies currency = mojaLista.get(which);
                                double db = currency.getValue();
                                String currencyLabel = currency.getCode();


                                if (!NaszeMetody.shoppingList.get(NaszeMetody.position).currencyLabel.equals("PLN")){
                                    String label = NaszeMetody.shoppingList.get(NaszeMetody.position).getCurrencyLabel();
                                    label2 = label;
                                    Map<String, Double> map = new HashMap<String, Double>();
                                    for (Currencies listElement : mojaLista){
                                        map.put(listElement.getCode(), listElement.getValue());
                                        NaszeMetody.listForRecycler = map;
                                    }
                                    Double valueForCurrency = map.get(label);
                                    double value = NaszeMetody.shoppingList.get(NaszeMetody.position).totalUpdatedPriceOfProduct;
                                    int numberOFItems = NaszeMetody.shoppingList.get(NaszeMetody.position).getNumberOfProducts();
                                    double countedValue = value/valueForCurrency/numberOFItems;

                                    if (NaszeMetody.shoppingList.get(NaszeMetody.position).isDustProductOn) {

                                        NaszeMetody.shoppingList.get(NaszeMetody.position).setPriceOf100grams(countedValue * db);
                                        NaszeMetody.shoppingList.get(NaszeMetody.position).setCurrencyLabel(currencyLabel);
                                        double newPriceOf100G = NaszeMetody.shoppingList.get(NaszeMetody.position).getPriceOf100grams();
                                        float newPriceOf100G2 = (float) newPriceOf100G;
                                        double weightOfProd = NaszeMetody.shoppingList.get(NaszeMetody.position).getWeightOfProduct();
                                        float weightOfProd2 = (float) weightOfProd;
                                        double priceForShoppingOne = newPriceOf100G*weightOfProd;
                                        float priceForShoppingOne2 = newPriceOf100G2*weightOfProd2;
                                        NaszeMetody.shoppingList.get(NaszeMetody.position).setTotalUpdatedPriceOfProduct(priceForShoppingOne);
                                        NaszeMetody.shoppingList.get(NaszeMetody.position).setTotalPriceOfProductInFloat(priceForShoppingOne2);
                                        zapiszDoPamieci(NaszeMetody.shoppingList);
                                        myEighthSound.start();

                                        if (label.equals(NaszeMetody.shoppingList.get(NaszeMetody.position).getCurrencyLabel())){
                                            Toast.makeText(getApplicationContext(), R.string.theSameCurrencySelected, Toast.LENGTH_SHORT).show();
                                        }

                                    } else {

                                        NaszeMetody.shoppingList.get(NaszeMetody.position).setPriceOfASingleProduct(countedValue * db);
                                        NaszeMetody.shoppingList.get(NaszeMetody.position).setCurrencyLabel(currencyLabel);
                                        double newPriceOfSingleProd = NaszeMetody.shoppingList.get(NaszeMetody.position).getPriceOfASingleProduct();
                                        float newPriceOfSingleProd2 = (float) newPriceOfSingleProd;
                                        int numberOfProducts = NaszeMetody.shoppingList.get(NaszeMetody.position).getNumberOfProducts();
                                        float numberOfProducts2 = (float) numberOfProducts;
                                        double newPrice = newPriceOfSingleProd*numberOfProducts;
                                        float newPrice2 = newPriceOfSingleProd2*numberOfProducts2;
                                        NaszeMetody.shoppingList.get(NaszeMetody.position).setTotalUpdatedPriceOfProduct(newPrice);
                                        NaszeMetody.shoppingList.get(NaszeMetody.position).setTotalPriceOfProductInFloat(newPrice2);
                                        zapiszDoPamieci(NaszeMetody.shoppingList);
                                        myEighthSound.start();

                                        if (label.equals(NaszeMetody.shoppingList.get(NaszeMetody.position).getCurrencyLabel())){
                                            mySeventhSound.start();
                                            Toast.makeText(getApplicationContext(), R.string.theSameCurrencySelected, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                                else {
                                    if (NaszeMetody.shoppingList.get(NaszeMetody.position).isDustProductOn) {
                                        double priceDownloaded = NaszeMetody.shoppingList.get(NaszeMetody.position).getPriceOf100grams();
                                        NaszeMetody.shoppingList.get(NaszeMetody.position).setPriceOf100grams(priceDownloaded * db);
                                        NaszeMetody.shoppingList.get(NaszeMetody.position).setCurrencyLabel(currencyLabel);
                                        double newPriceOf100G = NaszeMetody.shoppingList.get(NaszeMetody.position).getPriceOf100grams();
                                        float newPriceOf100G2 = (float) newPriceOf100G;
                                        double weightOfProd = NaszeMetody.shoppingList.get(NaszeMetody.position).getWeightOfProduct();
                                        float weightOfProd2 = (float) weightOfProd;
                                        double priceForShoppingOne = newPriceOf100G*weightOfProd;
                                        float priceForShoppingOne2 = newPriceOf100G2*weightOfProd2;
                                        NaszeMetody.shoppingList.get(NaszeMetody.position).setTotalUpdatedPriceOfProduct(priceForShoppingOne);
                                        NaszeMetody.shoppingList.get(NaszeMetody.position).setTotalPriceOfProductInFloat(priceForShoppingOne2);
                                        zapiszDoPamieci(NaszeMetody.shoppingList);
                                        myEighthSound.start();

                                        if (label2.equals(NaszeMetody.shoppingList.get(NaszeMetody.position).getCurrencyLabel())){
                                            mySeventhSound.start();
                                            Toast.makeText(getApplicationContext(), R.string.theSameCurrencySelected, Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        double priceDownloaded2 = NaszeMetody.shoppingList.get(NaszeMetody.position).getPriceOfASingleProduct();
                                        NaszeMetody.shoppingList.get(NaszeMetody.position).setPriceOfASingleProduct(priceDownloaded2 * db);
                                        NaszeMetody.shoppingList.get(NaszeMetody.position).setCurrencyLabel(currencyLabel);
                                        double newPriceOfSingleProd = NaszeMetody.shoppingList.get(NaszeMetody.position).getPriceOfASingleProduct();
                                        float newPriceOfSingleProd2 = (float) newPriceOfSingleProd;
                                        int numberOfProducts = NaszeMetody.shoppingList.get(NaszeMetody.position).getNumberOfProducts();
                                        float numberOfProducts2 = (float) numberOfProducts;
                                        double newPrice = newPriceOfSingleProd*numberOfProducts;
                                        float newPrice2 = newPriceOfSingleProd2*numberOfProducts2;
                                        NaszeMetody.shoppingList.get(NaszeMetody.position).setTotalUpdatedPriceOfProduct(newPrice);
                                        NaszeMetody.shoppingList.get(NaszeMetody.position).setTotalPriceOfProductInFloat(newPrice2);
                                        zapiszDoPamieci(NaszeMetody.shoppingList);
                                        myEighthSound.start();

                                        if (label2.equals(NaszeMetody.shoppingList.get(NaszeMetody.position).getCurrencyLabel())){
                                            mySeventhSound.start();
                                            Toast.makeText(getApplicationContext(), R.string.theSameCurrencySelected, Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                }
                            }
                        });

                        alertDialog.create();
                        alertDialog.show();

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    mySeventhSound.start();
                    Toast.makeText(getApplicationContext(), R.string.NoInternetInformation, Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.getPhoto:
                mySecondSound.start();
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(ShopRecyclerView.this);
                alertDialog.setTitle(R.string.Options);

                final CharSequence[] items = {getString(R.string.TakePhoto), getString(R.string.SelectFromGallery)};

                alertDialog.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            zrobZdjecie();
                        } else if (which == 1) {
                            wybierzZdjecie();
                        }
                    }
                });

                alertDialog.setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                alertDialog.create();
                alertDialog.show();
                return true;

            case R.id.getPLNLabel:
                if (isNetworkAvailable()) {
                    mySecondSound.start();
                    if (!NaszeMetody.shoppingList.get(NaszeMetody.position).getCurrencyLabel().equals("PLN")){
                        try {
                            AsyncTask<Void, Void, List<Currencies>> execute = new HttpCallTask().execute();
                            final List<Currencies> mojaLista = execute.get();
                            final List<String> listaaaa = new ArrayList<>();
                            final List<String> listForAdapter = new ArrayList<>();
                            final List<Double> exchangeRateList = new ArrayList<>();
                            for (Currencies mojItem : mojaLista) {
                                listaaaa.add(mojItem.getCode() + " : " + mojItem.getValue() + " PLN");
                                listForAdapter.add(mojItem.getCode());
                                exchangeRateList.add(mojItem.getValue());
                                NaszeMetody.exchangeRates = exchangeRateList;
                                NaszeMetody.menuCurrencies = listForAdapter;
                                zapiszDoPamieciListeWalut(listForAdapter);
                            }
                            mojRecycler.setAdapter(shoppingAdapter1);
                            String label = NaszeMetody.shoppingList.get(NaszeMetody.position).getCurrencyLabel();
                            Map<String, Double> map = new HashMap<String, Double>();
                            for (Currencies listElement : mojaLista) {
                                map.put(listElement.getCode(), listElement.getValue());
                                NaszeMetody.listForRecycler = map;
                            }
                            Double valueForCurrency = map.get(label);
                            double value = NaszeMetody.shoppingList.get(NaszeMetody.position).totalUpdatedPriceOfProduct;
                            int numberOFItems = NaszeMetody.shoppingList.get(NaszeMetody.position).getNumberOfProducts();
                            double countedValue = value/valueForCurrency/numberOFItems;


                            if (NaszeMetody.shoppingList.get(NaszeMetody.position).isDustProductOn) {

                                NaszeMetody.shoppingList.get(NaszeMetody.position).setPriceOf100grams(countedValue );
                                NaszeMetody.shoppingList.get(NaszeMetody.position).setCurrencyLabel("PLN");
                                double newPriceOf100G = NaszeMetody.shoppingList.get(NaszeMetody.position).getPriceOf100grams();
                                float newPriceOf100G2 = (float) newPriceOf100G;
                                double weightOfProd = NaszeMetody.shoppingList.get(NaszeMetody.position).getWeightOfProduct();
                                float weightOfProd2 = (float) weightOfProd;
                                double priceForShoppingOne = newPriceOf100G*weightOfProd;
                                float priceForShoppingOne2 = (float) priceForShoppingOne;
                                NaszeMetody.shoppingList.get(NaszeMetody.position).setTotalUpdatedPriceOfProduct(priceForShoppingOne);
                                NaszeMetody.shoppingList.get(NaszeMetody.position).setTotalPriceOfProductInFloat(priceForShoppingOne2);
                                zapiszDoPamieci(NaszeMetody.shoppingList);
                                shoppingAdapter1.notifyDataSetChanged();
                                myEighthSound.start();
                                Toast.makeText(getApplicationContext(), R.string.restoredPLNCUrrencyAgain, Toast.LENGTH_SHORT).show();
                                Toast.makeText(getApplicationContext(), R.string.NowClearTheCartAfterRestoringCurrency, Toast.LENGTH_SHORT).show();
                                NaszeMetody.finalResult = 0.00;
                                NaszeMetody.counterCurrencyTimes = 0;
                                NaszeMetody.isVisiblePLNTotalCosts = 0;
                                NaszeMetody.resultForAnotherCurrency1 = 0;
                                NaszeMetody.resultForAnotherCurrency2 = 0;
                                NaszeMetody.currencyLabelNumberOne = null;
                                NaszeMetody.currencyLabelNumberTwo = null;
                                NaszeMetody.announceLimit = false;
                                shoppingAdapter1.clearCheckboxes = true;
                                shoppingAdapter1.notifyDataSetChanged();

                            } else {

                                NaszeMetody.shoppingList.get(NaszeMetody.position).setPriceOfASingleProduct(countedValue);
                                NaszeMetody.shoppingList.get(NaszeMetody.position).setCurrencyLabel("PLN");
                                double newPriceOfSingleProd = NaszeMetody.shoppingList.get(NaszeMetody.position).getPriceOfASingleProduct();
                                float newPriceOfSingleProd2 = (float) newPriceOfSingleProd;
                                int numberOfProducts = NaszeMetody.shoppingList.get(NaszeMetody.position).getNumberOfProducts();
                                float numberOfProducts2 = (float) numberOfProducts;
                                double newPrice = newPriceOfSingleProd*numberOfProducts;
                                float newPrice2 = newPriceOfSingleProd2*numberOfProducts2;
                                NaszeMetody.shoppingList.get(NaszeMetody.position).setTotalUpdatedPriceOfProduct(newPrice);
                                NaszeMetody.shoppingList.get(NaszeMetody.position).setTotalPriceOfProductInFloat(newPrice2);
                                zapiszDoPamieci(NaszeMetody.shoppingList);
                                shoppingAdapter1.notifyDataSetChanged();
                                myEighthSound.start();
                                Toast.makeText(getApplicationContext(), R.string.restoredPLNCUrrencyAgain, Toast.LENGTH_SHORT).show();
                                Toast.makeText(getApplicationContext(), R.string.NowClearTheCartAfterRestoringCurrency, Toast.LENGTH_SHORT).show();
                                NaszeMetody.finalResult = 0.00;
                                NaszeMetody.counterCurrencyTimes = 0;
                                NaszeMetody.isVisiblePLNTotalCosts = 0;
                                NaszeMetody.resultForAnotherCurrency1 = 0;
                                NaszeMetody.resultForAnotherCurrency2 = 0;
                                NaszeMetody.currencyLabelNumberOne = null;
                                NaszeMetody.currencyLabelNumberTwo = null;
                                NaszeMetody.announceLimit = false;
                                shoppingAdapter1.clearCheckboxes = true;
                                shoppingAdapter1.notifyDataSetChanged();
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                    }
                    else{
                        mySeventhSound.start();
                        Toast.makeText(getApplicationContext(), R.string.thereIsAlreadySelectedPLNCurrencyLabel, Toast.LENGTH_SHORT).show();
                    }
                    }
                    else {
                    mySeventhSound.start();
                    Toast.makeText(getApplicationContext(), R.string.NoInternetInformation, Toast.LENGTH_SHORT).show();
                    }

                return true;
            default:
                return super.onContextItemSelected(item);
        }

    }
    public void UpdateExtrnalMemory() {
        List<Shopping> downloadedList = new ArrayList<>();
        downloadedList = readFromExternalMemory();
        downloadedList.get(NaszeMetody.position).setPhotoPath(photoPath);
        zapiszDoPamieci(downloadedList);
    }

    final static String NAZWA_PLIKU2 = "CurrenciesList.sda";

    public void zapiszDoPamieciListeWalut(List<String> currenciesList) {
        File plik = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), NAZWA_PLIKU2);
        plik.delete();
        String lokalizacjFolderu = Environment.getExternalStorageDirectory().toString();
        File file = new File(lokalizacjFolderu, NAZWA_PLIKU2);
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        String jsonCurrenciesList = gson.toJson(currenciesList);

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);

            fileOutputStream.write(jsonCurrenciesList.getBytes());
            fileOutputStream.close();


        } catch (Exception e) {
            e.printStackTrace();


        }
    }
    public void zapiszDoPamieci(List<Shopping> shoppingList) {
        File plik = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), NAZWA_PLIKU);
        plik.delete();
        String lokalizacjFolderu = Environment.getExternalStorageDirectory().toString();
        File file = new File(lokalizacjFolderu, NAZWA_PLIKU);
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        String jsonShoppingList = gson.toJson(shoppingList);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);

            fileOutputStream.write(jsonShoppingList.getBytes());
            fileOutputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void zrobZdjecie() {
        Intent takePhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takePhoto.resolveActivity(getPackageManager()) != null){
            startActivityForResult(takePhoto, MAKE_IMAGE_REQUEST);
        }
    }

    private void wybierzZdjecie() {
        // daje to, ze otwiera sie okna ze zdjeciami z telefonu
        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // otwiera sie aplikacja z zaciagnietym zdjeciem
        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            photoPath = cursor.getString(columnIndex);
            cursor.close();
            if (NaszeMetody.shoppingList.get(NaszeMetody.position).getPhotoPath()==null){
                GoAhead();
            }
            else {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(ShopRecyclerView.this);
                alertDialog.setTitle(R.string.ThereIsPhotoSaveDForSelectedShopping);
                alertDialog.setMessage(R.string.WouldYouLikeTOOverrideCurrentPhotoSavedForSelectedShopping);
                alertDialog.setPositiveButton(R.string.Okey, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        GoAhead();

                    }
                });

                alertDialog.setNegativeButton(R.string.Deny, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                alertDialog.create();
                alertDialog.show();

            }

        } else  if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();
                String[] projection = {ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME};

                Cursor cursor = getContentResolver().query(uri, projection,
                        null, null, null);
                cursor.moveToFirst();

                int numberColumnIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                odbiorcaSMS = cursor.getString(numberColumnIndex);
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(ShopRecyclerView.this);
                alertDialog.setTitle(R.string.Options);

                final CharSequence[] items = {getString(R.string.sendOnlyBoughtShopp),getString(R.string.sendOnlyShoppInCart), getString(R.string.sendWholeShoppList)};

                alertDialog.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            sendSMS3(odbiorcaSMS);
                        } else if (which == 1) {
                            sendSMS2(odbiorcaSMS);
                        }
                        else if (which == 2){
                            sendSMS(odbiorcaSMS);

                        }
                    }
                });

                alertDialog.setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                alertDialog.create();
                alertDialog.show();
            }
        }
        else if (requestCode == MAKE_IMAGE_REQUEST && resultCode == RESULT_OK && null != data){

            Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{MediaStore.Images.Media.DATA, MediaStore.Images.Media.DATE_ADDED, MediaStore.Images.ImageColumns.ORIENTATION}, MediaStore.Images.Media.DATE_ADDED, null, MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC");
            if (cursor != null && cursor.moveToFirst()) {

                cursor.moveToFirst();
                Uri uri = Uri.parse(cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA)));
                photoPath = uri.toString();
                cursor.close();

                if (NaszeMetody.shoppingList.get(NaszeMetody.position).getPhotoPath()==null){
                    GoAhead();
                }
                else {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(ShopRecyclerView.this);
                    alertDialog.setTitle(R.string.ThereIsPhotoSaveDForSelectedShopping);
                    alertDialog.setMessage(R.string.WouldYouLikeTOOverrideCurrentPhotoSavedForSelectedShopping);
                    alertDialog.setPositiveButton(R.string.Okey, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            GoAhead();
                        }
                    });

                    alertDialog.setNegativeButton(R.string.Deny, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    alertDialog.create();
                    alertDialog.show();

                }

            }

        }
    }

    private void GoAhead() {
        NaszeMetody.shoppingList.get(NaszeMetody.position).setPhotoPath(photoPath);
        UpdateExtrnalMemory();
        shoppingAdapter1.notifyDataSetChanged();
    }



    RecyclerView mojRecycler;
    ShoppingAdapter1 shoppingAdapter1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_recycler_view);
        ButterKnife.bind(this);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.floatingbtn);
        mojRecycler = (RecyclerView) findViewById(R.id.mojRecyclerView2);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mojRecycler.setLayoutManager(linearLayoutManager);
        shoppingAdapter1 = new ShoppingAdapter1(NaszeMetody.shoppingList, ShopRecyclerView.this);
        mojRecycler.setAdapter(shoppingAdapter1);
        String photoPath = "";
        myFirstSound = MediaPlayer.create(this, R.raw.kasa);
        // Utwór w formacie mp3 o nazwie Cha Ching Register na licencji Attribution 3.0 pobrany dnia 04.09.2017 godz. 20:00 ze strony https://soundbible.com/
        mySecondSound = MediaPlayer.create(this, R.raw.klikanie);
        // Utwór w formacie mp3 o nazwie Click On na licencji Attribution 3.0 pobrany dnia 04.09.2017 godz. 20:00 ze strony https://soundbible.com/
        myThirdSound = MediaPlayer.create(this, R.raw.odklikanie);
        // Utwór  w formacie mp3 o nazwie Button Click Off na licencji Attribution 3.0 pobrany dnia 04.09.2017 godz. 20:00 ze strony https://soundbible.com/
        myFifthSound = MediaPlayer.create(this, R.raw.pickgraphic);
        // Utwór  w formacie mp3 o nazwie Stapler na licencji Public Domain pobrany dnia 04.09.2017 godz. 20:00 ze strony https://soundbible.com/
        mySixthSound = MediaPlayer.create(this, R.raw.emptybin);
        // Utwór  w formacie mp3 o nazwie A Tone na licencji Public Domain pobrany dnia 04.09.2017 godz. 20:00 ze strony https://soundbible.com/
        mySeventhSound = MediaPlayer.create(this, R.raw.error);
        // Utwór w formacie mp3 o nazwie Computer Error na licencji Attribution 3.0 pobrany dnia 04.09.2017 godz. 20:00 ze strony https://soundbible.com/
        myEighthSound = MediaPlayer.create(this, R.raw.coin);
        // Utwór  w formacie mp3 o nazwie Coin Drop na licencji Public Domain pobrany dnia 04.09.2017 godz. 20:00 ze strony https://soundbible.com/
        myNinethSound = MediaPlayer.create(this, R.raw.menurolling);
        // Utwór  w formacie mp3 o nazwie Finger Breaking na licencji Attribution 3.0 pobrany dnia 04.09.2017 godz. 20:00 ze strony https://soundbible.com/

        // Wszystkie Grafiki oraz Zdjęcia znajdujące się w niniejszej aplikacji zakupowej zostały pobrane z darmowego servisu https://pixabay.com/pl/ jako Darmowe do użytku komercyjnego nie wymagające przypisania w dniach 01.07.2017-05.09.2017

        final TextView summary = (TextView) findViewById(R.id.shoppingsummary);

        final ImageButton trolley = (ImageButton) findViewById(R.id.trolleybtn);

        final ImageButton clearTrolley = (ImageButton) findViewById(R.id.cleartrolleybtn);

        final ImageButton downloadFromExternalMemory = (ImageButton) findViewById(R.id.readExternalDatabtn);

        final ImageButton sendMessageWithCreatedShoppingList = (ImageButton) findViewById(R.id.sendMessageBtn);

        final ImageButton clearWholeListInView = (ImageButton) findViewById(R.id.clearListInShopRecycler);

        final ImageButton goToChart = (ImageButton) findViewById(R.id.goToCharts);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarView);

        final Toolbar toolbarBottom = (Toolbar) findViewById(R.id.bottomtoolbar);



        if (NaszeMetody.finalResult==0.00){
            double zeroResult = 0.00;
            String zeroRessult = String.valueOf(zeroResult);
            summary.setText(zeroRessult+" PLN");

        }

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
        ImageLoader.getInstance().init(config);

        toolbarBottom.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(ShopRecyclerView.this);
                alertDialog.setTitle(R.string.sources);
                List<String> sources = new ArrayList<String>();
                sources.add(getString(R.string.line1)+'\n');
                sources.add(getString(R.string.line2)+'\n');
                sources.add(getString(R.string.line3)+'\n');
                sources.add(getString(R.string.line4)+'\n');
                sources.add(getString(R.string.line5)+'\n');
                sources.add(getString(R.string.line6)+'\n');
                sources.add(getString(R.string.line7)+'\n');
                sources.add(getString(R.string.line8)+'\n');
                sources.add(getString(R.string.line9)+'\n');
                sources.add(getString(R.string.line10));
                alertDialog.setMessage(sources.toString());
                alertDialog.create();
                alertDialog.show();
                return false;
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mySecondSound.start();
                Toast.makeText(getApplicationContext(), R.string.NewProductAddingNow, Toast.LENGTH_SHORT).show();
                NaszeMetody.finalResult = 0.00;
                Intent intent = new Intent(ShopRecyclerView.this, AddProduct.class);
                startActivity(intent);
            }
        });

        trolley.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myFirstSound.start();
                if(NaszeMetody.shoppingList!=null){
                    if (NaszeMetody.isVisiblePLNTotalCosts==1 && NaszeMetody.resultForAnotherCurrency1==0.00 & NaszeMetody.resultForAnotherCurrency2==0.00){
                        int counterForPLN = counterOnlyForPLN();
                        if (counterForPLN < 30){
                            summary.setText(String.format("%.2f", NaszeMetody.finalResult)+" PLN");
                        }
                        else {
                            Toast.makeText(getApplicationContext(), R.string.PriceInPLNTooBigToDisplay, Toast.LENGTH_SHORT).show();
                        }
                    }
                    else if (NaszeMetody.isVisiblePLNTotalCosts==1 && NaszeMetody.resultForAnotherCurrency1!=0.00 & NaszeMetody.resultForAnotherCurrency2==0.00) {
                        int counterForPLN = 0;
                        int counterForAnotherCurrency = 0;
                        counterForPLN = counterOnlyForPLN();
                        counterForAnotherCurrency = countAnotherCurrencyOne();
                        if (counterForPLN < 12) {
                            if (counterForAnotherCurrency < 12) {
                                summary.setText(String.format("%.2f", NaszeMetody.finalResult) + " PLN" + ", " + (String.format("%.2f", NaszeMetody.resultForAnotherCurrency1)) + " " + NaszeMetody.currencyLabelNumberOne);
                            } else if (counterForAnotherCurrency > 12) {
                                int valueToCompare = counterForPLN + 14 + counterForAnotherCurrency;
                                if (valueToCompare <= 38) {
                                    summary.setText(String.format("%.2f", NaszeMetody.finalResult) + " PLN" + ", " + (String.format("%.2f", NaszeMetody.resultForAnotherCurrency1)) + " " + NaszeMetody.currencyLabelNumberOne);
                                } else {
                                    summary.setText(String.format("%.2f", NaszeMetody.finalResult) + " PLN");
                                    Toast.makeText(getApplicationContext(), getString(R.string.PriceIn) + NaszeMetody.currencyLabelNumberOne + getString(R.string.IsTooBig), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } else if (counterForPLN > 12){
                            if (counterForAnotherCurrency < 12) {
                                int valueToCompare = counterForPLN + 14 + counterForAnotherCurrency;
                                if (valueToCompare <= 38) {
                                    summary.setText(String.format("%.2f", NaszeMetody.finalResult) + " PLN" + ", " + (String.format("%.2f", NaszeMetody.resultForAnotherCurrency1)) + " " + NaszeMetody.currencyLabelNumberOne);
                                } else {
                                    if (counterForPLN < 30) {
                                        summary.setText(String.format("%.2f", NaszeMetody.finalResult) + " PLN");
                                    } else if (counterForAnotherCurrency < 30) {
                                        summary.setText((String.format("%.2f", NaszeMetody.resultForAnotherCurrency1)) + " " + NaszeMetody.currencyLabelNumberOne);
                                        Toast.makeText(getApplicationContext(), R.string.PriceInPLNTooBigToDisplay, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getApplicationContext(), R.string.tobigpricesinthecarttodisplay, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                            else {
                                if (counterForPLN > counterForAnotherCurrency){
                                    if (counterForPLN < 30){
                                        summary.setText(String.format("%.2f", NaszeMetody.finalResult) + " PLN");
                                        Toast.makeText(getApplicationContext(), getString(R.string.PriceIn) + NaszeMetody.currencyLabelNumberOne + getString(R.string.IsTooBig), Toast.LENGTH_SHORT).show();
                                    }

                                }
                                else if (counterForAnotherCurrency > counterForPLN){
                                    if (counterForAnotherCurrency < 30){
                                        summary.setText((String.format("%.2f", NaszeMetody.resultForAnotherCurrency1)) + " " + NaszeMetody.currencyLabelNumberOne);
                                        Toast.makeText(getApplicationContext(), R.string.PriceInPLNTooBigToDisplay, Toast.LENGTH_SHORT).show();
                                    }

                                }
                                else {
                                    if (counterForPLN < 30){
                                        summary.setText(String.format("%.2f", NaszeMetody.finalResult) + " PLN");
                                        Toast.makeText(getApplicationContext(), getString(R.string.PriceIn) + NaszeMetody.currencyLabelNumberOne + getString(R.string.IsTooBig), Toast.LENGTH_SHORT).show();

                                    }
                                    else if (counterForAnotherCurrency < 30){
                                        summary.setText((String.format("%.2f", NaszeMetody.resultForAnotherCurrency1)) + " " + NaszeMetody.currencyLabelNumberOne);
                                        Toast.makeText(getApplicationContext(), R.string.PriceInPLNTooBigToDisplay, Toast.LENGTH_SHORT).show();

                                    }
                                    else {
                                        Toast.makeText(getApplicationContext(), R.string.tobigpricesinthecarttodisplay, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }


                    }
                    else if (NaszeMetody.isVisiblePLNTotalCosts==1 && NaszeMetody.resultForAnotherCurrency1==0.00 & NaszeMetody.resultForAnotherCurrency2!=0.00){
                        summary.setText(String.format("%.2f", NaszeMetody.finalResult)+" PLN"+ ", "+ (String.format("%.2f", NaszeMetody.resultForAnotherCurrency2))+ " " + NaszeMetody.currencyLabelNumberTwo);
                        int counterForPLN = 0;
                        int counterForAnotherCurrency2 = 0;
                        counterForPLN = counterOnlyForPLN();
                        counterForAnotherCurrency2 = countAnotherCurrencyTwo();
                        if (counterForPLN < 12) {
                            if (counterForAnotherCurrency2 < 12) {
                                summary.setText(String.format("%.2f", NaszeMetody.finalResult) + " PLN" + ", " + (String.format("%.2f", NaszeMetody.resultForAnotherCurrency2)) + " " + NaszeMetody.currencyLabelNumberTwo);
                            } else if (counterForAnotherCurrency2 > 12) {
                                int valueToCompare = counterForPLN + 14 + counterForAnotherCurrency2;
                                if (valueToCompare <= 38) {
                                    summary.setText(String.format("%.2f", NaszeMetody.finalResult) + " PLN" + ", " + (String.format("%.2f", NaszeMetody.resultForAnotherCurrency2)) + " " + NaszeMetody.currencyLabelNumberTwo);
                                } else {
                                    summary.setText(String.format("%.2f", NaszeMetody.finalResult) + " PLN");
                                    Toast.makeText(getApplicationContext(), getString(R.string.PriceIn) + NaszeMetody.currencyLabelNumberTwo + getString(R.string.IsTooBig), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } else if (counterForPLN > 12){
                            if (counterForAnotherCurrency2 < 12) {
                                int valueToCompare = counterForPLN + 14 + counterForAnotherCurrency2;
                                if (valueToCompare <= 38) {
                                    summary.setText(String.format("%.2f", NaszeMetody.finalResult) + " PLN" + ", " + (String.format("%.2f", NaszeMetody.resultForAnotherCurrency2)) + " " + NaszeMetody.currencyLabelNumberTwo);
                                } else {
                                    if (counterForPLN < 30) {
                                        summary.setText(String.format("%.2f", NaszeMetody.finalResult) + " PLN");
                                    } else  {
                                        summary.setText((String.format("%.2f", NaszeMetody.resultForAnotherCurrency2)) + " " + NaszeMetody.currencyLabelNumberTwo);
                                        Toast.makeText(getApplicationContext(), R.string.PriceInPLNTooBigToDisplay, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                            else {
                                if (counterForPLN > counterForAnotherCurrency2){
                                    if (counterForPLN < 30){
                                        summary.setText(String.format("%.2f", NaszeMetody.finalResult) + " PLN");
                                        Toast.makeText(getApplicationContext(), getString(R.string.PriceIn) + NaszeMetody.currencyLabelNumberTwo + getString(R.string.IsTooBig), Toast.LENGTH_SHORT).show();
                                    }

                                }
                                else if (counterForAnotherCurrency2 > counterForPLN){
                                    if (counterForAnotherCurrency2 < 30){
                                        summary.setText((String.format("%.2f", NaszeMetody.resultForAnotherCurrency2)) + " " + NaszeMetody.currencyLabelNumberTwo);
                                        Toast.makeText(getApplicationContext(), R.string.PriceInPLNTooBigToDisplay, Toast.LENGTH_SHORT).show();
                                    }

                                }
                                else {
                                    if (counterForPLN < 30){
                                        summary.setText(String.format("%.2f", NaszeMetody.finalResult) + " PLN");
                                        Toast.makeText(getApplicationContext(), getString(R.string.PriceIn) + NaszeMetody.currencyLabelNumberTwo + getString(R.string.IsTooBig), Toast.LENGTH_SHORT).show();

                                    }
                                    else if (counterForAnotherCurrency2 < 30){
                                        summary.setText((String.format("%.2f", NaszeMetody.resultForAnotherCurrency2)) + " " + NaszeMetody.currencyLabelNumberTwo);
                                        Toast.makeText(getApplicationContext(), R.string.PriceInPLNTooBigToDisplay, Toast.LENGTH_SHORT).show();

                                    }
                                    else {
                                        Toast.makeText(getApplicationContext(), R.string.tobigpricesinthecarttodisplay, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }
                    }
                    else if (NaszeMetody.isVisiblePLNTotalCosts==1 && NaszeMetody.resultForAnotherCurrency1!=0.00 & NaszeMetody.resultForAnotherCurrency2!=0.00){
                        summary.setText(String.format("%.2f", NaszeMetody.finalResult)+" PLN"+ ", " + (String.format("%.2f", NaszeMetody.resultForAnotherCurrency1)) + " " + NaszeMetody.currencyLabelNumberOne + ", " + (String.format("%.2f", NaszeMetody.resultForAnotherCurrency2) + " " + NaszeMetody.currencyLabelNumberTwo));
                        int counterForAnotherCurrency1 = 0;
                        int counterForAnotherCurrency2 = 0;
                        int counterForPLN = 0;
                        counterForPLN = counterOnlyForPLN();
                        counterForAnotherCurrency2 = countAnotherCurrencyTwo();
                        counterForAnotherCurrency1 = countAnotherCurrencyOne();
                        if (counterForPLN < 5 && counterForAnotherCurrency2 < 5 && counterForAnotherCurrency1 < 5){
                            summary.setText(String.format("%.2f", NaszeMetody.finalResult)+" PLN"+ ", " + (String.format("%.2f", NaszeMetody.resultForAnotherCurrency1)) + " " + NaszeMetody.currencyLabelNumberOne + ", " + (String.format("%.2f", NaszeMetody.resultForAnotherCurrency2) + " " + NaszeMetody.currencyLabelNumberTwo));
                        }else if (counterForPLN > 5 && counterForAnotherCurrency2 < 5 && counterForAnotherCurrency1 < 5){
                            summary.setText((String.format("%.2f", NaszeMetody.resultForAnotherCurrency1)) + " " + NaszeMetody.currencyLabelNumberOne + ", " + (String.format("%.2f", NaszeMetody.resultForAnotherCurrency2) + " " + NaszeMetody.currencyLabelNumberTwo));
                            Toast.makeText(getApplicationContext(), R.string.PriceInPLNTooBigToDisplay, Toast.LENGTH_SHORT).show();

                        }
                        else if (counterForPLN > 5 && counterForAnotherCurrency2 > 5 && counterForAnotherCurrency1 < 5){
                            int value = 0;
                            value = counterForPLN+counterForAnotherCurrency1+14;
                            if (value <= 38){
                                summary.setText(String.format("%.2f", NaszeMetody.finalResult)+" PLN"+ ", " + (String.format("%.2f", NaszeMetody.resultForAnotherCurrency1)) + " " + NaszeMetody.currencyLabelNumberOne);
                                Toast.makeText(getApplicationContext(), getString(R.string.PriceIn) + NaszeMetody.currencyLabelNumberTwo + getString(R.string.IsTooBig), Toast.LENGTH_SHORT).show();
                            }
                            else {
                                summary.setText((String.format("%.2f", NaszeMetody.resultForAnotherCurrency1)) + " " + NaszeMetody.currencyLabelNumberOne);
                                Toast.makeText(getApplicationContext(), getString(R.string.PricesInTwoCurrencies) + NaszeMetody.currencyLabelNumberTwo + getString(R.string.tooBig), Toast.LENGTH_SHORT).show();
                            }


                        }
                        else if (counterForPLN > 5 && counterForAnotherCurrency2 > 5 && counterForAnotherCurrency1 > 5){
                            if (counterForPLN < 30){
                                summary.setText(String.format("%.2f", NaszeMetody.finalResult)+" PLN");
                                Toast.makeText(getApplicationContext(), getString(R.string.PricesInCurrencies) + NaszeMetody.currencyLabelNumberTwo + getString(R.string.and) + NaszeMetody.currencyLabelNumberOne + getString(R.string.tooBig), Toast.LENGTH_SHORT).show();
                            }

                        }
                        else if (counterForPLN > 5 && counterForAnotherCurrency2 < 5 && counterForAnotherCurrency1 > 5){
                            int value = 0;
                            value = counterForPLN+counterForAnotherCurrency2+14;
                            if (value <= 38){
                                summary.setText(String.format("%.2f", NaszeMetody.finalResult)+" PLN"+ ", " + (String.format("%.2f", NaszeMetody.resultForAnotherCurrency2)) + " " + NaszeMetody.currencyLabelNumberTwo);
                                Toast.makeText(getApplicationContext(), getString(R.string.PriceIn) + NaszeMetody.currencyLabelNumberOne + getString(R.string.IsTooBig), Toast.LENGTH_SHORT).show();
                            }
                            else {
                                summary.setText((String.format("%.2f", NaszeMetody.resultForAnotherCurrency2)) + " " + NaszeMetody.currencyLabelNumberTwo);
                                Toast.makeText(getApplicationContext(), getString(R.string.PricesInTwoCurrencies) + NaszeMetody.currencyLabelNumberOne + getString(R.string.tooBig), Toast.LENGTH_SHORT).show();
                            }

                        }
                        else if (counterForPLN < 5 && counterForAnotherCurrency2 > 5 && counterForAnotherCurrency1 < 5){
                            summary.setText(String.format("%.2f", NaszeMetody.finalResult)+" PLN"+ ", " + (String.format("%.2f", NaszeMetody.resultForAnotherCurrency1)) + " " + NaszeMetody.currencyLabelNumberOne);
                            Toast.makeText(getApplicationContext(), getString(R.string.PriceIn) + NaszeMetody.currencyLabelNumberTwo + getString(R.string.IsTooBig), Toast.LENGTH_SHORT).show();

                        }
                        else if (counterForPLN > 5 && counterForAnotherCurrency2 > 5 && counterForAnotherCurrency1 < 5){
                            int value = 0;
                            value = counterForPLN+counterForAnotherCurrency1+14;
                            if (value <= 38){
                                summary.setText(String.format("%.2f", NaszeMetody.finalResult)+" PLN"+ ", " + (String.format("%.2f", NaszeMetody.resultForAnotherCurrency1)) + " " + NaszeMetody.currencyLabelNumberOne);
                                Toast.makeText(getApplicationContext(), getString(R.string.PriceIn) + NaszeMetody.currencyLabelNumberTwo + getString(R.string.IsTooBig), Toast.LENGTH_SHORT).show();
                            }
                            else {
                                summary.setText((String.format("%.2f", NaszeMetody.resultForAnotherCurrency1)) + " " + NaszeMetody.currencyLabelNumberOne);
                                Toast.makeText(getApplicationContext(), getString(R.string.PricesInTwoCurrencies) + NaszeMetody.currencyLabelNumberTwo + getString(R.string.tooBig), Toast.LENGTH_SHORT).show();
                            }

                        }
                        else if (counterForPLN < 5 && counterForAnotherCurrency2 > 5 && counterForAnotherCurrency1 > 5){
                            summary.setText(String.format("%.2f", NaszeMetody.finalResult)+" PLN");
                            Toast.makeText(getApplicationContext(), getString(R.string.PricesInCurrencies) + NaszeMetody.currencyLabelNumberTwo + getString(R.string.and) + NaszeMetody.currencyLabelNumberOne + getString(R.string.tooBig), Toast.LENGTH_SHORT).show();
                        }
                    }
                    else if (NaszeMetody.isVisiblePLNTotalCosts==0 && NaszeMetody.resultForAnotherCurrency1==0.00 & NaszeMetody.resultForAnotherCurrency2==0.00){
                        summary.setText(R.string.EmptyCart);
                    }
                    else if (NaszeMetody.isVisiblePLNTotalCosts==0 && NaszeMetody.resultForAnotherCurrency1!=0.00 & NaszeMetody.resultForAnotherCurrency2==0.00){
                        summary.setText(String.format("%.2f", NaszeMetody.resultForAnotherCurrency1) + " " + NaszeMetody.currencyLabelNumberOne);
                        int counterForCurrency1 = countAnotherCurrencyOne();
                        if (counterForCurrency1 < 30){
                            summary.setText(String.format("%.2f", NaszeMetody.resultForAnotherCurrency1) + " " + NaszeMetody.currencyLabelNumberOne);
                        }
                        else {
                            Toast.makeText(getApplicationContext(), R.string.PriceIn + NaszeMetody.currencyLabelNumberOne + R.string.IsTooBig, Toast.LENGTH_SHORT).show();
                        }

                    }
                    else if (NaszeMetody.isVisiblePLNTotalCosts==0 && NaszeMetody.resultForAnotherCurrency1==0.00 & NaszeMetody.resultForAnotherCurrency2!=0.00){
                        int counterForAnotherCurrency2 = 0;
                        counterForAnotherCurrency2 = countAnotherCurrencyTwo();
                        if (counterForAnotherCurrency2 < 30){
                            summary.setText(String.format("%.2f", NaszeMetody.resultForAnotherCurrency2) + " " + NaszeMetody.currencyLabelNumberTwo);
                        }
                        else {
                            Toast.makeText(getApplicationContext(), R.string.PriceIn + NaszeMetody.currencyLabelNumberTwo + R.string.IsTooBig, Toast.LENGTH_SHORT).show();
                        }
                    }
                    else if (NaszeMetody.isVisiblePLNTotalCosts==0 && NaszeMetody.resultForAnotherCurrency1!=0.00 & NaszeMetody.resultForAnotherCurrency2!=0.00){

                        summary.setText((String.format("%.2f", NaszeMetody.resultForAnotherCurrency1)) + " " + NaszeMetody.currencyLabelNumberOne + ", " + (String.format("%.2f", NaszeMetody.resultForAnotherCurrency2) + " " + NaszeMetody.currencyLabelNumberTwo));
                        int counterForAnotherCurrency1 = 0;
                        int counterForAnotherCurrency2 = 0;
                        counterForAnotherCurrency1 = countAnotherCurrencyOne();
                        counterForAnotherCurrency2 = countAnotherCurrencyTwo();
                        if (counterForAnotherCurrency1 < 12) {
                            if (counterForAnotherCurrency2 < 12) {
                                summary.setText(String.format("%.2f", NaszeMetody.resultForAnotherCurrency1) + " " + NaszeMetody.currencyLabelNumberOne + ", " + (String.format("%.2f", NaszeMetody.resultForAnotherCurrency2)) + " " + NaszeMetody.currencyLabelNumberTwo);
                            } else if (counterForAnotherCurrency2 > 12) {
                                int valueToCompare = counterForAnotherCurrency1 + 14 + counterForAnotherCurrency2;
                                if (valueToCompare <= 38) {
                                    summary.setText(String.format("%.2f", NaszeMetody.resultForAnotherCurrency1) + " " + NaszeMetody.currencyLabelNumberOne + ", " + (String.format("%.2f", NaszeMetody.resultForAnotherCurrency2)) + " " + NaszeMetody.currencyLabelNumberTwo);
                                } else {
                                    summary.setText(String.format("%.2f", NaszeMetody.resultForAnotherCurrency1) + " " + NaszeMetody.currencyLabelNumberOne);
                                    Toast.makeText(getApplicationContext(), R.string.PriceIn + NaszeMetody.currencyLabelNumberTwo + R.string.IsTooBig, Toast.LENGTH_SHORT).show();
                                }
                            }
                        } else if (counterForAnotherCurrency1 > 12){
                            if (counterForAnotherCurrency2 < 12) {
                                int valueToCompare = counterForAnotherCurrency1 + 14 + counterForAnotherCurrency2;
                                if (valueToCompare <= 38) {
                                    summary.setText(String.format("%.2f", NaszeMetody.resultForAnotherCurrency1) + " " + NaszeMetody.currencyLabelNumberOne + ", " + (String.format("%.2f", NaszeMetody.resultForAnotherCurrency2)) + " " + NaszeMetody.currencyLabelNumberTwo);
                                } else {
                                    if (counterForAnotherCurrency1 < 30) {
                                        summary.setText(String.format("%.2f", NaszeMetody.resultForAnotherCurrency1) + " " + NaszeMetody.currencyLabelNumberOne);
                                    } else  {
                                        summary.setText(String.format("%.2f", NaszeMetody.resultForAnotherCurrency2) + " " + NaszeMetody.currencyLabelNumberTwo);
                                        Toast.makeText(getApplicationContext(), R.string.PriceIn + NaszeMetody.currencyLabelNumberOne + R.string.IsTooBig, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                            else {
                                if (counterForAnotherCurrency1 > counterForAnotherCurrency2){
                                    if (counterForAnotherCurrency1 < 30){
                                        summary.setText(String.format("%.2f", NaszeMetody.resultForAnotherCurrency1) + " " + NaszeMetody.currencyLabelNumberOne);
                                        Toast.makeText(getApplicationContext(), R.string.PriceIn + NaszeMetody.currencyLabelNumberTwo + R.string.IsTooBig, Toast.LENGTH_SHORT).show();
                                    }

                                }
                                else if (counterForAnotherCurrency2 > counterForAnotherCurrency1){
                                    if (counterForAnotherCurrency2 < 30){
                                        summary.setText(String.format("%.2f", NaszeMetody.resultForAnotherCurrency2) + " " + NaszeMetody.currencyLabelNumberTwo);
                                        Toast.makeText(getApplicationContext(), R.string.PriceIn + NaszeMetody.currencyLabelNumberOne + R.string.IsTooBig, Toast.LENGTH_SHORT).show();
                                    }

                                }
                                else {
                                    if (counterForAnotherCurrency1 < 30){
                                        summary.setText(String.format("%.2f", NaszeMetody.resultForAnotherCurrency1) + " " + NaszeMetody.currencyLabelNumberOne);
                                        Toast.makeText(getApplicationContext(), R.string.PriceIn + NaszeMetody.currencyLabelNumberTwo + R.string.IsTooBig, Toast.LENGTH_SHORT).show();

                                    }
                                    else if (counterForAnotherCurrency2 < 30){
                                        summary.setText(String.format("%.2f", NaszeMetody.resultForAnotherCurrency2) + " " + NaszeMetody.currencyLabelNumberTwo);
                                        Toast.makeText(getApplicationContext(), R.string.PriceIn + NaszeMetody.currencyLabelNumberOne + R.string.IsTooBig, Toast.LENGTH_SHORT).show();

                                    }
                                    else {
                                        Toast.makeText(getApplicationContext(), R.string.tobigpricesinthecarttodisplay, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }
                    }
                    if (NaszeMetody.addThirdCurrencyLabel){
                        summary.setText((String.format("%.2f", NaszeMetody.resultForAnotherCurrency1)) + " " + NaszeMetody.currencyLabelNumberOne + ", " + (String.format("%.2f", NaszeMetody.resultForAnotherCurrency2) + " " + NaszeMetody.currencyLabelNumberTwo) + ", " + (String.format("%.2f", NaszeMetody.resultForAnotherCurrency3) + " " + NaszeMetody.currencyLabelNumberThree));
                        int counterForAnotherCurrency1 = 0;
                        int counterForAnotherCurrency2 = 0;
                        int counterForAnotherCurrency3 = 0;
                        counterForAnotherCurrency3 = countAnotherCurrencyThree();
                        counterForAnotherCurrency2 = countAnotherCurrencyTwo();
                        counterForAnotherCurrency1 = countAnotherCurrencyOne();
                        if (counterForAnotherCurrency3 < 5 && counterForAnotherCurrency2 < 5 && counterForAnotherCurrency1 < 5){
                            summary.setText((String.format("%.2f", NaszeMetody.resultForAnotherCurrency1)) + " " + NaszeMetody.currencyLabelNumberOne + ", " + (String.format("%.2f", NaszeMetody.resultForAnotherCurrency2) + " " + NaszeMetody.currencyLabelNumberTwo) + ", " + (String.format("%.2f", NaszeMetody.resultForAnotherCurrency3) + " " + NaszeMetody.currencyLabelNumberThree));
                        }else if (counterForAnotherCurrency3 > 5 && counterForAnotherCurrency2 < 5 && counterForAnotherCurrency1 < 5){
                            summary.setText((String.format("%.2f", NaszeMetody.resultForAnotherCurrency1)) + " " + NaszeMetody.currencyLabelNumberOne + ", " + (String.format("%.2f", NaszeMetody.resultForAnotherCurrency2) + " " + NaszeMetody.currencyLabelNumberTwo));
                            Toast.makeText(getApplicationContext(), R.string.PriceIn + NaszeMetody.currencyLabelNumberThree + R.string.IsTooBig, Toast.LENGTH_SHORT).show();

                        }
                        else if (counterForAnotherCurrency3 > 5 && counterForAnotherCurrency2 > 5 && counterForAnotherCurrency1 < 5){
                            summary.setText((String.format("%.2f", NaszeMetody.resultForAnotherCurrency1)) + " " + NaszeMetody.currencyLabelNumberOne);
                            Toast.makeText(getApplicationContext(), getString(R.string.PricesInCurrencies) + NaszeMetody.currencyLabelNumberThree + getString(R.string.and) + NaszeMetody.currencyLabelNumberTwo + R.string.tooBig, Toast.LENGTH_SHORT).show();

                        }
                        else if (counterForAnotherCurrency3 > 5 && counterForAnotherCurrency2 > 5 && counterForAnotherCurrency1 > 5){

                            Toast.makeText(getApplicationContext(), getString(R.string.PricesInCurrencies) + NaszeMetody.currencyLabelNumberThree + ", " + NaszeMetody.currencyLabelNumberTwo + ", " + NaszeMetody.currencyLabelNumberOne + R.string.tooBig, Toast.LENGTH_SHORT).show();

                        }
                        else if (counterForAnotherCurrency3 > 5 && counterForAnotherCurrency2 < 5 && counterForAnotherCurrency1 > 5){
                            summary.setText((String.format("%.2f", NaszeMetody.resultForAnotherCurrency2)) + " " + NaszeMetody.currencyLabelNumberTwo);
                            Toast.makeText(getApplicationContext(), getString(R.string.PricesInCurrencies) + NaszeMetody.currencyLabelNumberThree + getString(R.string.and) + NaszeMetody.currencyLabelNumberOne + R.string.tooBig, Toast.LENGTH_SHORT).show();

                        }
                        else if (counterForAnotherCurrency3 < 5 && counterForAnotherCurrency2 > 5 && counterForAnotherCurrency1 < 5){
                            summary.setText((String.format("%.2f", NaszeMetody.resultForAnotherCurrency1)) + " " + NaszeMetody.currencyLabelNumberOne + ", " + (String.format("%.2f", NaszeMetody.resultForAnotherCurrency3) + " " + NaszeMetody.currencyLabelNumberThree));
                            Toast.makeText(getApplicationContext(), R.string.PriceIn + NaszeMetody.currencyLabelNumberTwo + R.string.IsTooBig, Toast.LENGTH_SHORT).show();

                        }
                        else if (counterForAnotherCurrency3 > 5 && counterForAnotherCurrency2 > 5 && counterForAnotherCurrency1 < 5){
                            summary.setText((String.format("%.2f", NaszeMetody.resultForAnotherCurrency1)) + " " + NaszeMetody.currencyLabelNumberOne);
                            Toast.makeText(getApplicationContext(), getString(R.string.PricesInCurrencies) + NaszeMetody.currencyLabelNumberThree + getString(R.string.and) + NaszeMetody.currencyLabelNumberTwo + R.string.tooBig, Toast.LENGTH_SHORT).show();

                        }
                        else if (counterForAnotherCurrency3 < 5 && counterForAnotherCurrency2 > 5 && counterForAnotherCurrency1 > 5){
                            summary.setText((String.format("%.2f", NaszeMetody.resultForAnotherCurrency3)) + " " + NaszeMetody.currencyLabelNumberThree);
                            Toast.makeText(getApplicationContext(), getString(R.string.PricesInCurrencies) + NaszeMetody.currencyLabelNumberTwo + getString(R.string.and) + NaszeMetody.currencyLabelNumberOne + R.string.tooBig, Toast.LENGTH_SHORT).show();

                        }

                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), R.string.pleaseAddProductToList, Toast.LENGTH_SHORT).show();
                }
            }
        });
        clearTrolley.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mySixthSound.start();
                if (NaszeMetody.shoppingList!=null){
                    NaszeMetody.finalResult = 0.00;
                    NaszeMetody.counterCurrencyTimes = 0;
                    NaszeMetody.isVisiblePLNTotalCosts = 0;
                    NaszeMetody.resultForAnotherCurrency1 = 0;
                    NaszeMetody.resultForAnotherCurrency2 = 0;
                    NaszeMetody.currencyLabelNumberOne = null;
                    NaszeMetody.currencyLabelNumberTwo = null;
                    NaszeMetody.announceLimit = false;
                    summary.setText(String.valueOf(0.00)+ " PLN");
                    shoppingAdapter1.clearCheckboxes = true;
                    shoppingAdapter1.notifyDataSetChanged();
                }
                else {
                    Toast.makeText(getApplicationContext(), R.string.pleaseAddProductToList, Toast.LENGTH_SHORT).show();
                }
            }
        });
        downloadFromExternalMemory.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mySecondSound.start();
                File plik = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), NAZWA_PLIKU);
                if (plik.exists()) {
                    adjustDataForAdapter();
                    summary.setText(String.valueOf(0.00)+ " PLN");

                    Toast.makeText(getApplicationContext(), R.string.ThereIsNoDataInExternalMem, Toast.LENGTH_SHORT).show();
                } else {
                    mySeventhSound.start();
                    Toast.makeText(getApplicationContext(), R.string.NoDataInExternalMem, Toast.LENGTH_SHORT).show();
                }
                File currenciesList = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), NAZWA_PLIKU);
                if (currenciesList.exists()){
                    NaszeMetody.menuCurrencies = readFromExternalMemoryCurrenciesList();
                }
                else {

                }
            }
        });
        clearWholeListInView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mySecondSound.start();
                if (NaszeMetody.shoppingList!=null){
                    NaszeMetody.shoppingList.clear();
                    shoppingAdapter1.notifyDataSetChanged();
                    NaszeMetody.finalResult = 0.00;
                    NaszeMetody.counterCurrencyTimes = 0;
                    NaszeMetody.isVisiblePLNTotalCosts = 0;
                    NaszeMetody.resultForAnotherCurrency1 = 0;
                    NaszeMetody.resultForAnotherCurrency2 = 0;
                    NaszeMetody.currencyLabelNumberOne = null;
                    NaszeMetody.currencyLabelNumberTwo = null;
                    NaszeMetody.announceLimit = false;
                    summary.setText(String.valueOf(0.00)+ " PLN");
                }
                else {
                    Toast.makeText(getApplicationContext(), R.string.pleaseAddProductToList, Toast.LENGTH_SHORT).show();
                }

            }
        });
        goToChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Shopping> list = new ArrayList<Shopping>();
                list = readFromExternalMemory();
                if (list!=null){
                    mySecondSound.start();
                    List<Shopping> list33 = new ArrayList<Shopping>();
                    list33 = NaszeMetody.shoppingList;
                    Map<Float, String> map = new HashMap<Float, String>();
                    for (Shopping listElement : list33){
                        map.put(listElement.getTotalPriceOfProductInFloat(), listElement.getProductName());
                        NaszeMetody.listForDataPieChart = map;
                    }

                    listOfPricesCreator = NaszeMetody.shoppingList;
                    float[] list2 = new float[listOfPricesCreator.size()];
                    String[] list1 = new String[listOfPricesCreator.size()];
                    for (int i = 0; i < listOfPricesCreator.size(); i++){
                        list2[i] = listOfPricesCreator.get(i).getTotalPriceOfProductInFloat();
                        list1[i] = listOfPricesCreator.get(i).getCurrencyLabel();
                        NaszeMetody.listBefore = list2;
                        NaszeMetody.listBefore2 = list1;

                    }
                    Toast.makeText(getApplicationContext(), R.string.betterCompareTheSameCurrencies, Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(ShopRecyclerView.this, Graphic.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(getApplicationContext(), R.string.EmptyShoppingList, Toast.LENGTH_SHORT).show();
                }


            }
        });


        sendMessageWithCreatedShoppingList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mySecondSound.start();
                if (NaszeMetody.shoppingList!=null){
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(ShopRecyclerView.this);
                    alertDialog.setTitle(R.string.Options);

                    final CharSequence[] items = {getString(R.string.sendSms),getString(R.string.sendMail)};

                    alertDialog.setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (which == 0) {
                                Uri uri2 = Uri.parse("content://contacts");
                                Intent intent2 = new Intent(Intent.ACTION_PICK, uri2);
                                intent2.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
                                startActivityForResult(intent2, REQUEST_CODE);
                            } else if (which == 1) {
                                Intent i = new Intent(Intent.ACTION_SEND);
                                i.setType("message/rfc822");
                                i.putExtra(Intent.EXTRA_EMAIL, "konradsar2015@gmail.com");
                                i.putExtra(Intent.EXTRA_TEXT, sendMail());
                                startActivity(Intent.createChooser(i, "teraz wslemy ..."));
                            }

                        }
                    });

                    alertDialog.setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    alertDialog.create();
                    alertDialog.show();
                }
                else {
                    Toast.makeText(getApplicationContext(), R.string.pleaseAddProductToList, Toast.LENGTH_SHORT).show();
                }
            }

        });
    }

    public int counterOnlyForPLN() {
        int counterForPLN;
        if (NaszeMetody.finalResult > 9999999999999999999999999999999.999){
            counterForPLN = 32;

        }
        else if (NaszeMetody.finalResult > 999999999999999999999999999999.999){
            counterForPLN = 31;

        }
        else if (NaszeMetody.finalResult > 99999999999999999999999999999.999){
            counterForPLN = 30;

        }
        else if (NaszeMetody.finalResult > 9999999999999999999999999999.999){
            counterForPLN = 29;

        }
        else if (NaszeMetody.finalResult > 999999999999999999999999999.999){
            counterForPLN = 28;

        }
        else if (NaszeMetody.finalResult > 99999999999999999999999999.999){
            counterForPLN = 27;

        }
        else if (NaszeMetody.finalResult > 9999999999999999999999999.999){
            counterForPLN = 26;

        }
        else if (NaszeMetody.finalResult > 999999999999999999999999.999){
            counterForPLN = 25;

        }
        else if (NaszeMetody.finalResult > 99999999999999999999999.999){
            counterForPLN = 24;

        }
        else if (NaszeMetody.finalResult > 9999999999999999999999.999){
            counterForPLN = 23;

        }
        else if (NaszeMetody.finalResult > 999999999999999999999.999){
            counterForPLN = 22;

        }
        else if (NaszeMetody.finalResult > 99999999999999999999.999){
            counterForPLN = 21;

        }
        else if (NaszeMetody.finalResult > 9999999999999999999.999){
            counterForPLN = 20;

        }
        else if (NaszeMetody.finalResult > 999999999999999999.999){
            counterForPLN = 19;

        }
        else if (NaszeMetody.finalResult > 99999999999999999.999){
            counterForPLN = 18;

        }
        else if (NaszeMetody.finalResult > 9999999999999999.999){
            counterForPLN = 17;

        }
        else if (NaszeMetody.finalResult > 999999999999999.999){
            counterForPLN = 16;

        }
        else if (NaszeMetody.finalResult > 99999999999999.999){
            counterForPLN = 15;

        }
        else if (NaszeMetody.finalResult > 9999999999999.999){
            counterForPLN = 14;

        }
        else if (NaszeMetody.finalResult > 999999999999.999){
            counterForPLN = 13;

        }
        else if (NaszeMetody.finalResult > 99999999999.999){
            counterForPLN = 12;

        }
        else if (NaszeMetody.finalResult > 9999999999.999){
            counterForPLN = 11;

        }
        else if (NaszeMetody.finalResult > 999999999.99){
            counterForPLN = 10;

        }
        else if (NaszeMetody.finalResult > 99999999.999){
            counterForPLN = 9;

        }
        else if (NaszeMetody.finalResult > 9999999.999){
            counterForPLN = 8;

        }
        else if (NaszeMetody.finalResult > 999999.999){
            counterForPLN = 7;

        }
        else if (NaszeMetody.finalResult > 99999.999){
            counterForPLN = 6;

        }
        else if (NaszeMetody.finalResult > 9999.999){
            counterForPLN = 5;

        }
        else if (NaszeMetody.finalResult > 999.999){
            counterForPLN = 4;

        }
        else if (NaszeMetody.finalResult > 99.999){
            counterForPLN = 3;

        }
        else if (NaszeMetody.finalResult > 9.999){
            counterForPLN = 2;

        }
        else {
            counterForPLN = 1;

        }
        return counterForPLN;
    }

    public int countAnotherCurrencyOne() {
        int counterForAnotherCurrency;
        if (NaszeMetody.resultForAnotherCurrency1 > 9999999999999999999999999999999.999){
            counterForAnotherCurrency = 32;
        }
        else if (NaszeMetody.resultForAnotherCurrency1 > 999999999999999999999999999999.999){
            counterForAnotherCurrency = 31;
        }
        else if (NaszeMetody.resultForAnotherCurrency1 > 99999999999999999999999999999.999){
            counterForAnotherCurrency = 30;
        }
        else if (NaszeMetody.resultForAnotherCurrency1 > 9999999999999999999999999999.999){
            counterForAnotherCurrency = 29;
        }
        else if (NaszeMetody.resultForAnotherCurrency1 > 999999999999999999999999999.999){
            counterForAnotherCurrency = 28;
        }
        else if (NaszeMetody.resultForAnotherCurrency1 > 99999999999999999999999999.999){
            counterForAnotherCurrency = 27;
        }
        else if (NaszeMetody.resultForAnotherCurrency1 > 9999999999999999999999999.999){
            counterForAnotherCurrency = 26;
        }
        else if (NaszeMetody.resultForAnotherCurrency1 > 999999999999999999999999.999){
            counterForAnotherCurrency = 25;
        }
        else if (NaszeMetody.resultForAnotherCurrency1 > 99999999999999999999999.999){
            counterForAnotherCurrency = 24;
        }
        else if (NaszeMetody.resultForAnotherCurrency1 > 9999999999999999999999.999){
            counterForAnotherCurrency = 23;
        }
        else if (NaszeMetody.resultForAnotherCurrency1 > 999999999999999999999.999){
            counterForAnotherCurrency = 22;
        }
        else if (NaszeMetody.resultForAnotherCurrency1 > 99999999999999999999.999){
            counterForAnotherCurrency = 21;
        }
        else if (NaszeMetody.resultForAnotherCurrency1 > 9999999999999999999.999){
            counterForAnotherCurrency = 20;
        }
        else if (NaszeMetody.resultForAnotherCurrency1 > 999999999999999999.999){
            counterForAnotherCurrency = 19;
        }
        else if (NaszeMetody.resultForAnotherCurrency1 > 99999999999999999.999){
            counterForAnotherCurrency = 18;
        }
        else if (NaszeMetody.resultForAnotherCurrency1 > 9999999999999999.999){
            counterForAnotherCurrency = 17;
        }
        else if (NaszeMetody.resultForAnotherCurrency1 > 999999999999999.999){
            counterForAnotherCurrency = 16;
        }
        else if (NaszeMetody.resultForAnotherCurrency1 > 99999999999999.999){
            counterForAnotherCurrency = 15;
        }
        else if (NaszeMetody.resultForAnotherCurrency1 > 9999999999999.999){
            counterForAnotherCurrency = 14;
        }
        else if (NaszeMetody.resultForAnotherCurrency1 > 999999999999.999){
            counterForAnotherCurrency = 13;
        }
        else if (NaszeMetody.resultForAnotherCurrency1 > 99999999999.999){
            counterForAnotherCurrency = 12;
        }
        else if (NaszeMetody.resultForAnotherCurrency1 > 9999999999.999){
            counterForAnotherCurrency = 11;
        }
        else if (NaszeMetody.resultForAnotherCurrency1 > 999999999.99){
            counterForAnotherCurrency = 10;
        }
        else if (NaszeMetody.resultForAnotherCurrency1 > 99999999.999){
            counterForAnotherCurrency = 9;
        }
        else if (NaszeMetody.resultForAnotherCurrency1 > 9999999.999){
            counterForAnotherCurrency = 8;
        }
        else if (NaszeMetody.resultForAnotherCurrency1 > 999999.999){
            counterForAnotherCurrency = 7;
        }
        else if (NaszeMetody.resultForAnotherCurrency1 > 99999.999){
            counterForAnotherCurrency = 6;
        }
        else if (NaszeMetody.resultForAnotherCurrency1 > 9999.999){
            counterForAnotherCurrency = 5;
        }
        else if (NaszeMetody.resultForAnotherCurrency1 > 999.999){
            counterForAnotherCurrency = 4;
        }
        else if (NaszeMetody.resultForAnotherCurrency1 > 99.999){
            counterForAnotherCurrency = 3;
        }
        else if (NaszeMetody.resultForAnotherCurrency1 > 9.999){
            counterForAnotherCurrency = 2;
        }
        else {
            counterForAnotherCurrency = 1;
        }
        return counterForAnotherCurrency;
    }
    public int countAnotherCurrencyTwo() {
        int counterForAnotherCurrency2;
        if (NaszeMetody.resultForAnotherCurrency2 > 9999999999999999999999999999999.999){
            counterForAnotherCurrency2 = 32;
        }
        else if (NaszeMetody.resultForAnotherCurrency2 > 999999999999999999999999999999.999){
            counterForAnotherCurrency2 = 31;
        }
        else if (NaszeMetody.resultForAnotherCurrency2 > 99999999999999999999999999999.999){
            counterForAnotherCurrency2 = 30;
        }
        else if (NaszeMetody.resultForAnotherCurrency2 > 9999999999999999999999999999.999){
            counterForAnotherCurrency2 = 29;
        }
        else if (NaszeMetody.resultForAnotherCurrency2 > 999999999999999999999999999.999){
            counterForAnotherCurrency2 = 28;
        }
        else if (NaszeMetody.resultForAnotherCurrency2 > 99999999999999999999999999.999){
            counterForAnotherCurrency2 = 27;
        }
        else if (NaszeMetody.resultForAnotherCurrency2 > 9999999999999999999999999.999){
            counterForAnotherCurrency2 = 26;
        }
        else if (NaszeMetody.resultForAnotherCurrency2 > 999999999999999999999999.999){
            counterForAnotherCurrency2 = 25;
        }
        else if (NaszeMetody.resultForAnotherCurrency2 > 99999999999999999999999.999){
            counterForAnotherCurrency2 = 24;
        }
        else if (NaszeMetody.resultForAnotherCurrency2 > 9999999999999999999999.999){
            counterForAnotherCurrency2 = 23;
        }
        else if (NaszeMetody.resultForAnotherCurrency2 > 999999999999999999999.999){
            counterForAnotherCurrency2 = 22;
        }
        else if (NaszeMetody.resultForAnotherCurrency2 > 99999999999999999999.999){
            counterForAnotherCurrency2 = 21;
        }
        else if (NaszeMetody.resultForAnotherCurrency2 > 9999999999999999999.999){
            counterForAnotherCurrency2 = 20;
        }
        else if (NaszeMetody.resultForAnotherCurrency2 > 999999999999999999.999){
            counterForAnotherCurrency2 = 19;
        }
        else if (NaszeMetody.resultForAnotherCurrency2 > 99999999999999999.999){
            counterForAnotherCurrency2 = 18;
        }
        else if (NaszeMetody.resultForAnotherCurrency2 > 9999999999999999.999){
            counterForAnotherCurrency2 = 17;
        }
        else if (NaszeMetody.resultForAnotherCurrency2 > 999999999999999.999){
            counterForAnotherCurrency2 = 16;
        }
        else if (NaszeMetody.resultForAnotherCurrency2 > 99999999999999.999){
            counterForAnotherCurrency2 = 15;
        }
        else if (NaszeMetody.resultForAnotherCurrency2 > 9999999999999.999){
            counterForAnotherCurrency2 = 14;
        }
        else if (NaszeMetody.resultForAnotherCurrency2 > 999999999999.999){
            counterForAnotherCurrency2 = 13;
        }
        else if (NaszeMetody.resultForAnotherCurrency2 > 99999999999.999){
            counterForAnotherCurrency2 = 12;
        }
        else if (NaszeMetody.resultForAnotherCurrency2 > 9999999999.999){
            counterForAnotherCurrency2 = 11;
        }
        else if (NaszeMetody.resultForAnotherCurrency2 > 999999999.99){
            counterForAnotherCurrency2 = 10;
        }
        else if (NaszeMetody.resultForAnotherCurrency2 > 99999999.999){
            counterForAnotherCurrency2 = 9;
        }
        else if (NaszeMetody.resultForAnotherCurrency2 > 9999999.999){
            counterForAnotherCurrency2 = 8;
        }
        else if (NaszeMetody.resultForAnotherCurrency2 > 999999.999){
            counterForAnotherCurrency2 = 7;
        }
        else if (NaszeMetody.resultForAnotherCurrency2 > 99999.999){
            counterForAnotherCurrency2 = 6;
        }
        else if (NaszeMetody.resultForAnotherCurrency2 > 9999.999){
            counterForAnotherCurrency2 = 5;
        }
        else if (NaszeMetody.resultForAnotherCurrency2 > 999.999){
            counterForAnotherCurrency2 = 4;
        }
        else if (NaszeMetody.resultForAnotherCurrency2 > 99.999){
            counterForAnotherCurrency2 = 3;
        }
        else if (NaszeMetody.resultForAnotherCurrency2 > 9.999){
            counterForAnotherCurrency2 = 2;
        }
        else {
            counterForAnotherCurrency2 = 1;
        }
        return counterForAnotherCurrency2;
    }

    public int countAnotherCurrencyThree() {
        int counterForAnotherCurrency3;
        if (NaszeMetody.resultForAnotherCurrency2 > 9999999999999999999999999999999.999){
            counterForAnotherCurrency3 = 32;
        }
        else if (NaszeMetody.resultForAnotherCurrency2 > 999999999999999999999999999999.999){
            counterForAnotherCurrency3 = 31;
        }
        else if (NaszeMetody.resultForAnotherCurrency2 > 99999999999999999999999999999.999){
            counterForAnotherCurrency3 = 30;
        }
        else if (NaszeMetody.resultForAnotherCurrency2 > 9999999999999999999999999999.999){
            counterForAnotherCurrency3 = 29;
        }
        else if (NaszeMetody.resultForAnotherCurrency2 > 999999999999999999999999999.999){
            counterForAnotherCurrency3 = 28;
        }
        else if (NaszeMetody.resultForAnotherCurrency2 > 99999999999999999999999999.999){
            counterForAnotherCurrency3 = 27;
        }
        else if (NaszeMetody.resultForAnotherCurrency2 > 9999999999999999999999999.999){
            counterForAnotherCurrency3 = 26;
        }
        else if (NaszeMetody.resultForAnotherCurrency2 > 999999999999999999999999.999){
            counterForAnotherCurrency3 = 25;
        }
        else if (NaszeMetody.resultForAnotherCurrency2 > 99999999999999999999999.999){
            counterForAnotherCurrency3 = 24;
        }
        else if (NaszeMetody.resultForAnotherCurrency2 > 9999999999999999999999.999){
            counterForAnotherCurrency3 = 23;
        }
        else if (NaszeMetody.resultForAnotherCurrency2 > 999999999999999999999.999){
            counterForAnotherCurrency3 = 22;
        }
        else if (NaszeMetody.resultForAnotherCurrency2 > 99999999999999999999.999){
            counterForAnotherCurrency3 = 21;
        }
        else if (NaszeMetody.resultForAnotherCurrency2 > 9999999999999999999.999){
            counterForAnotherCurrency3 = 20;
        }
        else if (NaszeMetody.resultForAnotherCurrency2 > 999999999999999999.999){
            counterForAnotherCurrency3 = 19;
        }
        else if (NaszeMetody.resultForAnotherCurrency2 > 99999999999999999.999){
            counterForAnotherCurrency3 = 18;
        }
        else if (NaszeMetody.resultForAnotherCurrency2 > 9999999999999999.999){
            counterForAnotherCurrency3 = 17;
        }
        else if (NaszeMetody.resultForAnotherCurrency2 > 999999999999999.999){
            counterForAnotherCurrency3 = 16;
        }
        else if (NaszeMetody.resultForAnotherCurrency2 > 99999999999999.999){
            counterForAnotherCurrency3 = 15;
        }
        else if (NaszeMetody.resultForAnotherCurrency2 > 9999999999999.999){
            counterForAnotherCurrency3 = 14;
        }
        else if (NaszeMetody.resultForAnotherCurrency2 > 999999999999.999){
            counterForAnotherCurrency3 = 13;
        }
        else if (NaszeMetody.resultForAnotherCurrency2 > 99999999999.999){
            counterForAnotherCurrency3 = 12;
        }
        else if (NaszeMetody.resultForAnotherCurrency2 > 9999999999.999){
            counterForAnotherCurrency3 = 11;
        }
        else if (NaszeMetody.resultForAnotherCurrency2 > 999999999.99){
            counterForAnotherCurrency3 = 10;
        }
        else if (NaszeMetody.resultForAnotherCurrency2 > 99999999.999){
            counterForAnotherCurrency3 = 9;
        }
        else if (NaszeMetody.resultForAnotherCurrency2 > 9999999.999){
            counterForAnotherCurrency3 = 8;
        }
        else if (NaszeMetody.resultForAnotherCurrency2 > 999999.999){
            counterForAnotherCurrency3 = 7;
        }
        else if (NaszeMetody.resultForAnotherCurrency2 > 99999.999){
            counterForAnotherCurrency3 = 6;
        }
        else if (NaszeMetody.resultForAnotherCurrency2 > 9999.999){
            counterForAnotherCurrency3 = 5;
        }
        else if (NaszeMetody.resultForAnotherCurrency2 > 999.999){
            counterForAnotherCurrency3 = 4;
        }
        else if (NaszeMetody.resultForAnotherCurrency2 > 99.999){
            counterForAnotherCurrency3 = 3;
        }
        else if (NaszeMetody.resultForAnotherCurrency2 > 9.999){
            counterForAnotherCurrency3 = 2;
        }
        else {
            counterForAnotherCurrency3 = 1;
        }
        return counterForAnotherCurrency3;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void onBackPressed() {
        Intent intent = new Intent(ShopRecyclerView.this, AddProduct.class);
        startActivity(intent);

    }

    public String sendMail(){
        String mailContent = "";
        List<Shopping> my_list = NaszeMetody.shoppingList;
        List<String> listForSms = new ArrayList<String>();
        for (int i = 0; i < my_list.size(); i++) {
            Shopping shopping = my_list.get(i);
            String name = shopping.getProductName();
            int numberOfProd = shopping.getNumberOfProducts();
            double priceOfSingleProd2 = shopping.getPriceOfASingleProduct();
            String priceOfSingleProdConverted = String.format(Locale.US, "%.2f", priceOfSingleProd2);
            double weightOfDustProd2 = shopping.getWeightOfProduct();
            String weightOfDustProdConverted = String.format(Locale.US, "%.2f", weightOfDustProd2);
            double priceOf100grams2 = shopping.getPriceOf100grams();
            String priceOf100gramsConverted = String.format(Locale.US, "%.2f", priceOf100grams2);
            boolean dustProd = shopping.isDustProductOn();
            double finalPriceWholeProd = priceOfSingleProd2 * numberOfProd;
            String finalPriceWholeProdShorted = String.format(Locale.US, "%.2f", finalPriceWholeProd);
            double finalPriceDustProd = weightOfDustProd2 * priceOf100grams2;
            String finalPriceDustProdShorted = String.format(Locale.US, "%.2f", finalPriceDustProd);
            String currency = shopping.getCurrencyLabel();

            if (dustProd) {
                String singleLine = getString(R.string.productNo) + (i + 1) + " : " + name + "," + '\n' + getString(R.string.weightOfProd) + weightOfDustProdConverted + "," + '\n' + getString(R.string.priceOfKg) + priceOf100gramsConverted + " " + currency +"," + '\n' + getString(R.string.totalPrice) + finalPriceDustProdShorted + " "+currency+ '\n';
                listForSms.add(singleLine);
            } else if (!dustProd) {
                String singleLine = getString(R.string.productNo) + (i + 1) + " : " + name + "," + '\n' + getString(R.string.numberOfProddd) + String.valueOf(numberOfProd) + "," + '\n' + getString(R.string.unitPrice) + priceOfSingleProdConverted +" "+ currency+"," + '\n' + getString(R.string.totalPrice) + finalPriceWholeProdShorted +" "+currency+ '\n';
                listForSms.add(singleLine);
            }
        }
        mailContent = listForSms.toString();
        return mailContent;

    }

    private void sendSMS(String phone) {
        Uri uri = Uri.parse("smsto:" + phone);
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        List<Shopping> my_list = NaszeMetody.shoppingList;
        List<String> listForSms = new ArrayList<String>();
        for (int i = 0; i < my_list.size(); i++) {
            Shopping shopping = my_list.get(i);
            String name = shopping.getProductName();
            int numberOfProd = shopping.getNumberOfProducts();
            double priceOfSingleProd2 = shopping.getPriceOfASingleProduct();
            String priceOfSingleProdConverted = String.format(Locale.US, "%.2f", priceOfSingleProd2);
            double weightOfDustProd2 = shopping.getWeightOfProduct();
            String weightOfDustProdConverted = String.format(Locale.US, "%.2f", weightOfDustProd2);
            double priceOf100grams2 = shopping.getPriceOf100grams();
            String priceOf100gramsConverted = String.format(Locale.US, "%.2f", priceOf100grams2);
            boolean dustProd = shopping.isDustProductOn();
            double finalPriceWholeProd2 = priceOfSingleProd2 * numberOfProd;
            String finalPriceWholeProdShorted = String.format(Locale.US, "%.2f", finalPriceWholeProd2);
            double finalPriceDustProd2 = weightOfDustProd2 * priceOf100grams2;
            String finalPriceDustProdShorted = String.format(Locale.US, "%.2f", finalPriceDustProd2);
            String currency = shopping.getCurrencyLabel();
            if (dustProd) {
                String singleLine = getString(R.string.productNo) + (i + 1) + " " + name + "," + '\n' + getString(R.string.weightOfProd) + weightOfDustProdConverted + "," + '\n' + getString(R.string.priceOfKg) + priceOf100gramsConverted + " " + currency + "," + '\n' + getString(R.string.totalPrice) + finalPriceDustProdShorted + " "+currency+ '\n';
                listForSms.add(singleLine);
            } else if (dustProd == false) {
                String singleLine = getString(R.string.productNo) + (i + 1) + " " + name + "," + '\n' + getString(R.string.numberOfProddd) + String.valueOf(numberOfProd) + "," + '\n' + getString(R.string.unitPrice) + priceOfSingleProdConverted + " "+currency+"," + '\n' + getString(R.string.totalPrice) + finalPriceWholeProdShorted + " "+currency+ '\n';
                listForSms.add(singleLine);
            }
        }
        intent.putExtra("sms_body", String.valueOf(listForSms));
        startActivity(intent);
    }

    private void sendSMS2(String phone) {
        Uri uri = Uri.parse("smsto:" + phone);
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        List<Shopping> my_list = NaszeMetody.shoppingList;
        List<String> listForSms = new ArrayList<String>();
        for (int i = 0; i < my_list.size(); i++) {
            Shopping shopping = my_list.get(i);
            String name = shopping.getProductName();
            int numberOfProd = shopping.getNumberOfProducts();
            double priceOfSingleProd2 = shopping.getPriceOfASingleProduct();
            String priceOfSingleProdConverted = String.format(Locale.US, "%.2f", priceOfSingleProd2);
            double weightOfDustProd2 = shopping.getWeightOfProduct();
            String weightOfDustProdConverted = String.format(Locale.US, "%.2f", weightOfDustProd2);
            double priceOf100grams2 = shopping.getPriceOf100grams();
            String priceOf100gramsConverted = String.format(Locale.US, "%.2f", priceOf100grams2);
            boolean dustProd = shopping.isDustProductOn();
            double finalPriceWholeProd2 = priceOfSingleProd2 * numberOfProd;
            String finalPriceWholeProdShorted = String.format(Locale.US, "%.2f", finalPriceWholeProd2);
            double finalPriceDustProd2 = weightOfDustProd2 * priceOf100grams2;
            String finalPriceDustProdShorted = String.format(Locale.US, "%.2f", finalPriceDustProd2);
            String currency = shopping.getCurrencyLabel();
            boolean isAdded = shopping.isCheckedCheckBox;
            if (dustProd && isAdded) {
                String singleLine = getString(R.string.productNo) + (i + 1) + " " + name + "," + '\n' + getString(R.string.weightOfProd) + weightOfDustProdConverted + "," + '\n' + getString(R.string.priceOfKg) + priceOf100gramsConverted + " " +currency + "," + '\n' + getString(R.string.totalPrice) + finalPriceDustProdShorted + " "+currency+ '\n';
                listForSms.add(singleLine);
            } else if (!dustProd && isAdded) {
                String singleLine = getString(R.string.productNo) + (i + 1) + " " + name + "," + '\n' + getString(R.string.numberOfProddd) + String.valueOf(numberOfProd) + "," + '\n' + getString(R.string.unitPrice) + priceOfSingleProdConverted + " "+currency+"," + '\n' + getString(R.string.totalPrice) + finalPriceWholeProdShorted + " "+currency+ '\n';
                listForSms.add(singleLine);
            }
        }
        intent.putExtra("sms_body", String.valueOf(listForSms));
        startActivity(intent);
    }

    private void sendSMS3(String phone) {
        Uri uri = Uri.parse("smsto:" + phone);
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        List<Shopping> my_list = NaszeMetody.shoppingList;
        List<String> listForSms = new ArrayList<String>();
        for (int i = 0; i < my_list.size(); i++) {
            Shopping shopping = my_list.get(i);
            String name = shopping.getProductName();
            int numberOfProd = shopping.getNumberOfProducts();
            double priceOfSingleProd2 = shopping.getPriceOfASingleProduct();
            String priceOfSingleProdConverted = String.format(Locale.US, "%.2f", priceOfSingleProd2);
            double weightOfDustProd2 = shopping.getWeightOfProduct();
            String weightOfDustProdConverted = String.format(Locale.US, "%.2f", weightOfDustProd2);
            double priceOf100grams2 = shopping.getPriceOf100grams();
            String priceOf100gramsConverted = String.format(Locale.US, "%.2f", priceOf100grams2);
            boolean dustProd = shopping.isDustProductOn();
            double finalPriceWholeProd2 = priceOfSingleProd2 * numberOfProd;
            String finalPriceWholeProdShorted = String.format(Locale.US, "%.2f", finalPriceWholeProd2);
            double finalPriceDustProd2 = weightOfDustProd2 * priceOf100grams2;
            String finalPriceDustProdShorted = String.format(Locale.US, "%.2f", finalPriceDustProd2);
            String currency = shopping.getCurrencyLabel();
            boolean isBought = shopping.isRedBackgroundColor;
            if (dustProd && !isBought) {
                String singleLine = getString(R.string.productNo) + (i + 1) + " " + name + "," + '\n' + getString(R.string.weightOfProd) + weightOfDustProdConverted + "," + '\n' + getString(R.string.priceOfKg) + priceOf100gramsConverted + " " + currency + "," + '\n' + getString(R.string.totalPrice) + finalPriceDustProdShorted + " "+currency+ '\n';
                listForSms.add(singleLine);
            } else if (!dustProd && !isBought) {
                String singleLine = getString(R.string.productNo) + (i + 1) + " " + name + "," + '\n' + getString(R.string.numberOfProddd) + String.valueOf(numberOfProd) + "," + '\n' + getString(R.string.unitPrice) + priceOfSingleProdConverted + " " +currency+ "," + '\n' + getString(R.string.totalPrice) + finalPriceWholeProdShorted + " "+currency+ '\n';
                listForSms.add(singleLine);
            }
        }
        intent.putExtra("sms_body", String.valueOf(listForSms));
        startActivity(intent);
    }

    final static String NAZWA_PLIKU = "ShoppingList.sda";

    public List<Shopping> readFromExternalMemory() {
        File plik = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), NAZWA_PLIKU);
        StringBuilder sb = new StringBuilder();
        try {
            FileInputStream fis = new FileInputStream(plik);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Type type = new TypeToken<List<Shopping>>() {
        }.getType();

        List<Shopping> odczytaneProdukty;

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        odczytaneProdukty = gson.fromJson(sb.toString(), type);


        return odczytaneProdukty;

    }

    public List<String> readFromExternalMemoryCurrenciesList() {
        File plik = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), NAZWA_PLIKU2);
        StringBuilder sb = new StringBuilder();
        try {
            FileInputStream fis = new FileInputStream(plik);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Type type = new TypeToken<List<String>>() {
        }.getType();

        List<String> odczytaneWaluty;

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        odczytaneWaluty = gson.fromJson(sb.toString(), type);


        return odczytaneWaluty;

    }

    public void adjustDataForAdapter() {
        NaszeMetody.shoppingList = readFromExternalMemory();
        shoppingAdapter1 = new ShoppingAdapter1(NaszeMetody.shoppingList, ShopRecyclerView.this);
        mojRecycler.setAdapter(shoppingAdapter1);

    }
}

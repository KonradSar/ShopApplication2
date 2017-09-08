package com.example.konrad.applicationsecond;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.adapters.Converters;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.media.MediaPlayer;
import android.provider.CalendarContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.charts.ScatterChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.greenrobot.greendao.annotation.Convert;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class Graphic extends AppCompatActivity {

    int counter;
    private float[] yData = NaszeMetody.listBefore;
    private String[] xData = NaszeMetody.listBefore2;
    PieChart pieChart;
    MediaPlayer myFifthSound;
    MediaPlayer mySecondSound;
    MediaPlayer myNinethSound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graphic);
        pieChart = (PieChart) findViewById(R.id.pieChart);
        setUpPieCharts();
        counter = 0;
        pieChart.getDescription().setEnabled(false);
        pieChart.getLegend().setEnabled(false);
//        pieChart.setDrawSliceText(false);
        myFifthSound = MediaPlayer.create(this, R.raw.pickgraphic);
        // Utwór  w formacie mp3 o nazwie Stapler na licencji Public Domain pobrany dnia 04.09.2017 godz. 20:00 ze strony soundbible.com
        mySecondSound = MediaPlayer.create(this, R.raw.klikanie);
        // Utwór w formacie mp3 o nazwie Click On na licencji Attribution 3.0 pobrany dnia 04.09.2017 godz. 20:00 ze strony soundbible.com

        // Wszystkie Grafiki oraz Zdjęcia znajdujące się w niniejszej aplikacji zakupowej zostały pobrane z darmowego servisu https://pixabay.com/pl/ jako Darmowe do użytku komercyjnego nie wymagające przypisania w dniach 01.07.2017-05.09.2017
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.TRANSPARENT);
        pieChart.setHoleRadius(50f);
        pieChart.setCenterText("Lista Zakupów");
        pieChart.setCenterTextSize(16);
        pieChart.setCenterTextColor(Color.WHITE);
        pieChart.invalidate();


        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {

            @Override
            public void onValueSelected(Entry e, Highlight h) {
                if (e == null){
                    return;
                }
                else {
                    myFifthSound.start();
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(Graphic.this);
                    alertDialog.setTitle(R.string.descriptionChart);
                    List<Shopping> shop = new ArrayList<Shopping>();
                    shop = NaszeMetody.shoppingList;
                    double totalPriceForGraphic = 0.00;
                    Float indexOfMapElement = e.getY();
                    String valueForSelectedItem = NaszeMetody.listForDataPieChart.get(indexOfMapElement);
                    for (Shopping element : shop){
                        if (valueForSelectedItem.equals(element.getProductName())){
                            if (element.isDustProductOn){
                                for (Shopping shopping : shop){
                                    totalPriceForGraphic += shopping.getTotalUpdatedPriceOfProduct();
                                }
                                Double shoppingParticipation = (element.getTotalUpdatedPriceOfProduct()/totalPriceForGraphic)*100;
                                String shopParticipation = String.format(Locale.US, "%.2f", shoppingParticipation);
                                String name = element.getProductName();
                                Double weight = element.getWeightOfProduct();
                                Double priceOf100Grams2 = element.getPriceOf100grams();
                                String price100Grams = String.format(Locale.US, "%.2f", priceOf100Grams2);
                                String currency = element.getCurrencyLabel();
                                Double totalPrice = element.getTotalUpdatedPriceOfProduct();
                                String totalPriceShorted = String.format(Locale.US, "%.2f", totalPrice);
                                if (element.getAdditionalInfo()==null){
                                    alertDialog.setMessage(getString(R.string.namE)+name+"\n"+getString(R.string.currencY)+currency+"\n"+getString(R.string.pricekg)+price100Grams+" "+currency+"\n"+getString(R.string.weighttOfPRodd)+String.valueOf(weight)+" [Kg]"+"\n"+getString(R.string.priceTotall)+totalPriceShorted+" "+currency+"\n"+getString(R.string.equity)+shopParticipation+" "+ "%");
                                    alertDialog.create();
                                    alertDialog.show();


                                }
                                else {
                                    String additionalInfo = element.getAdditionalInfo();
                                    alertDialog.setMessage(getString(R.string.namE)+name+"\n"+getString(R.string.currencY)+currency+"\n"+getString(R.string.pricekg)+price100Grams+" "+currency+"\n"+getString(R.string.weighttOfPRodd)+String.valueOf(weight)+" [Kg]"+"\n"+getString(R.string.priceTotall)+String.valueOf(totalPrice)+" "+currency+"\n"+getString(R.string.descriptions)+totalPriceShorted+additionalInfo+"\n"+getString(R.string.equity)+shopParticipation+" "+ "%");
                                    alertDialog.create();
                                    alertDialog.show();
                                }
                            }
                            else {
                                for (Shopping shopping : shop){
                                    totalPriceForGraphic += shopping.getTotalUpdatedPriceOfProduct();
                                }
                                Double shoppingParticipationx2 = (element.getTotalUpdatedPriceOfProduct()/totalPriceForGraphic)*100;
                                String shopParticipation2 = String.format(Locale.US, "%.2f", shoppingParticipationx2);
                                String name = element.getProductName();
                                Integer number = element.getNumberOfProducts();
                                Double price2 = element.getPriceOfASingleProduct();
                                String priceSingleProdShorted2 = String.format(Locale.US, "%.2f", price2);
                                String currency = element.getCurrencyLabel();
                                Double totalPrice2 = element.getTotalUpdatedPriceOfProduct();
                                String totalPriceShorted2 = String.format(Locale.US, "%.2f", totalPrice2);
                                if (element.getAdditionalInfo()==null){
                                    alertDialog.setMessage(getString(R.string.namE)+name+"\n"+getString(R.string.currencY)+currency+"\n"+getString(R.string.numbeR)+String.valueOf(number)+"\n"+getString(R.string.priceForASing)+priceSingleProdShorted2+" "+currency+"\n"+getString(R.string.priceTotall)+totalPriceShorted2+" "+currency+"\n"+getString(R.string.equity)+shopParticipation2+" "+ "%");
                                    alertDialog.create();
                                    alertDialog.show();

                                }
                                else {
                                    String additionalInfo = element.getAdditionalInfo();
                                    alertDialog.setMessage(getString(R.string.namE)+name+"\n"+getString(R.string.currencY)+currency+"\n"+getString(R.string.numbeR)+String.valueOf(number)+"\n"+getString(R.string.priceForASing)+priceSingleProdShorted2+" "+currency+"\n"+getString(R.string.priceTotall)+totalPriceShorted2+" "+currency+"\n"+getString(R.string.descriptions)+additionalInfo+"\n"+getString(R.string.equity)+shopParticipation2+" "+ "%");
                                    alertDialog.create();
                                    alertDialog.show();
                                }
                            }
                        }
                        else {

                        }
                    }
                }
            }

            @Override
            public void onNothingSelected() {

            }
        });

        }
    public void onBackPressed() {
        mySecondSound.start();
        Intent intent = new Intent(Graphic.this, ShopRecyclerView.class);
        startActivity(intent);
    }

    private void setUpPieCharts() {

        List<PieEntry> pieEntries = new ArrayList<>();
        for (int i = 0; i < yData.length; i++) {
            pieEntries.add(new PieEntry(yData[i], xData[i]));
        }
        PieDataSet pieDataSet = new PieDataSet(pieEntries, "klucz1");
        PieData data = new PieData(pieDataSet);
        pieDataSet.setSliceSpace(5);
        pieChart.setData(data);
        pieChart.invalidate();
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieChart.animateY(2000);
        pieChart.invalidate();
        pieChart.setBackgroundColor(Color.TRANSPARENT);
        pieChart.invalidate();
    }
}

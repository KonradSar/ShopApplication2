package com.example.konrad.applicationsecond;

import android.content.Context;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Locale;

/**
 * Created by Konrad on 17.06.2017.
 */

public class ShoppingAdapter1 extends RecyclerView.Adapter<ShoppingAdapter1.MyViewHolder> {
//
    private static  MediaPlayer mySound;
    private List<Shopping> shoppingList;
    ImageLoader imageLoader;
    private Context context1;
    public static boolean isCheckBoxInAdapterChecked(Boolean condition){
        return condition;
    }
    public static boolean permission;
    public static double result = 0.00;
    public int position1;
    int THRESHOLD = 16;
    double THRESHOLD2 = 9999999.99;
    int THRESHOLD3 = 99999;
    boolean clearCheckboxes = false;
    final static String NAZWA_PLIKU = "ShoppingList.sda";
    private static  MediaPlayer mySound2;
    public static int positionForRecycler(int condition){
        return condition;
    }
    public int getPosition1() {
        return position;
    }

    public void setPosition1(int position) {
        this.position = position;

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


    private int position;


    public int getPosition() {
        return position;
    }

    public int setPosition(int position) {
        this.position = position;
        return position;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.singleline, parent, false);
        return new MyViewHolder(v);
    }
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        if (clearCheckboxes){
            holder.checkBox.setChecked(false);
        }



        final Shopping shopping = shoppingList.get(position);

        if (shopping.isDustProductOn==false){
            holder.name.setText((shopping.productName.length() > THRESHOLD) ? shopping.productName.substring(0, THRESHOLD) + ".." : shopping.productName);
            double f = shopping.priceOfASingleProduct*shopping.numberOfProducts;
            String tiki = String.format(Locale.US, "%.2f", f);
            holder.price.setText(tiki);
            holder.numberOfProduct.setText(String.valueOf(shopping.numberOfProducts));
            holder.currencyLabel.setText(shopping.currencyLabel);
            ShopRecyclerView shopRecyclerView;

        }
        else if (shopping.isDustProductOn){
            holder.name.setText((shopping.productName.length() > THRESHOLD) ? shopping.productName.substring(0, THRESHOLD) + "..." : shopping.productName);
            double g = shopping.priceOf100grams*shopping.weightOfProduct;
            String tiki2 = String.format(Locale.US, "%.2f", g);
            holder.price.setText(tiki2);
            holder.numberOfProduct.setText(String.valueOf(shopping.weightOfProduct));
            holder.currencyLabel.setText(shopping.currencyLabel);
        }
        if (shopping.isDustProductOn){
            holder.typeLabel.setText("Kg");
        }
        else {
            holder.typeLabel.setText(R.string.pieces);
        }

        if (shopping.isRedBackgroundColor){
            holder.relativeLay.setBackgroundResource(R.drawable.but2);
        }
        else {
            holder.relativeLay.setBackgroundResource(R.drawable.but3);
        }


        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked() && NaszeMetody.announceLimit){
                    mySound = MediaPlayer.create(context1, R.raw.klikanie);
                    mySound.start();
                    shopping.isCheckedCheckBox = true;
                    zapiszDoPamieci(NaszeMetody.shoppingList);
                    selectOptons(holder);
                    Toast.makeText(context1, R.string.YouCanOnlyOverrideCurrentCurrenciesInCart, Toast.LENGTH_SHORT).show();
                }
                else if (buttonView.isChecked() && !NaszeMetody.announceLimit) {
                    mySound = MediaPlayer.create(context1, R.raw.klikanie);
                    mySound.start();
                    shopping.isCheckedCheckBox = true;
                    zapiszDoPamieci(NaszeMetody.shoppingList);
                    Toast.makeText(context1, R.string.productAddedToCart, Toast.LENGTH_SHORT).show();
                    selectOptons(holder);
                }
                else if (!buttonView.isChecked()){
                    mySound = MediaPlayer.create(context1, R.raw.odklikanie);
                    mySound.start();
                    shopping.isCheckedCheckBox = false;
                    zapiszDoPamieci(NaszeMetody.shoppingList);
                    Toast.makeText(context1, R.string.RemovedFromCart, Toast.LENGTH_SHORT).show();
                    isCheckBoxInAdapterChecked(false);
                }
            }
        });

        holder.deleteimagebtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                mySound = MediaPlayer.create(context1, R.raw.emptybin);
                mySound.start();
                shoppingList.remove(position);
                notifyItemRemoved(position);

            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mySound2 = MediaPlayer.create(context1, R.raw.menurolling);
                mySound2.start();
                setPosition(holder.getAdapterPosition());
                NaszeMetody.position = setPosition(holder.getAdapterPosition());
                positionForRecycler(position);
                return false;
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mySound2 = MediaPlayer.create(context1, R.raw.menurolling);
                mySound2.start();
                LinearLayout linearLayout = new LinearLayout(context1);
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                ImageView imageView = new ImageView(context1);
                int width = 600;
                int height = 600;
                LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(width,height, Gravity.CENTER_HORIZONTAL);
                parms.bottomMargin = 15;
                parms.topMargin = 30;
                parms.leftMargin = 10;
                parms.rightMargin = 10;
                imageView.setLayoutParams(parms);
                linearLayout.addView(imageView);
                String imageResource = shopping.getPhotoPath();

                Glide.with(context1).load("file://" + imageResource).into(imageView);
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context1);
                alertDialog.setView(linearLayout);
                alertDialog.setTitle(R.string.SelectedPhhoto);
                alertDialog.create();
                alertDialog.show();
            }
        });
    }

    public void selectOptons(MyViewHolder holder) {
        isCheckBoxInAdapterChecked(true);
        for (int i = 0; i < NaszeMetody.menuCurrencies.size(); i++){
            if (NaszeMetody.menuCurrencies.get(i).equals(holder.currencyLabel.getText().toString()) && NaszeMetody.resultForAnotherCurrency1==0.00){
                String priceOfProductFromHolderForTrolley = holder.price.getText().toString();
                double przeliczonaCena = Double.parseDouble(priceOfProductFromHolderForTrolley);
                NaszeMetody.resultForAnotherCurrency1 = przeliczonaCena;
                NaszeMetody.currencyLabelNumberOne = NaszeMetody.menuCurrencies.get(i);
            }
            else if (NaszeMetody.menuCurrencies.get(i).equals(holder.currencyLabel.getText().toString()) && NaszeMetody.resultForAnotherCurrency2==0.00){
                if (NaszeMetody.currencyLabelNumberOne.equals(NaszeMetody.menuCurrencies.get(i))){
                    String priceOfProductFromHolderForTrolley = holder.price.getText().toString();
                    double przeliczonaCena = Double.parseDouble(priceOfProductFromHolderForTrolley);
                    NaszeMetody.resultForAnotherCurrency1 += przeliczonaCena;
                    NaszeMetody.overrideResultForCurrencyOne = true;
                }
                else {
                    String priceOfProductFromHolderForTrolley = holder.price.getText().toString();
                    double przeliczonaCena = Double.parseDouble(priceOfProductFromHolderForTrolley);
                    NaszeMetody.resultForAnotherCurrency2 = przeliczonaCena;
                    NaszeMetody.currencyLabelNumberTwo = NaszeMetody.menuCurrencies.get(i);

                }
            }
            else if (NaszeMetody.menuCurrencies.get(i).equals(holder.currencyLabel.getText().toString()) && NaszeMetody.resultForAnotherCurrency1!=0.00){
                if (NaszeMetody.currencyLabelNumberOne.equals(NaszeMetody.menuCurrencies.get(i))){
                    String priceOfProductFromHolderForTrolley = holder.price.getText().toString();
                    double przeliczonaCena = Double.parseDouble(priceOfProductFromHolderForTrolley);
                    NaszeMetody.resultForAnotherCurrency1 += przeliczonaCena;
                    NaszeMetody.overrideResultForCurrencyOne = true;
                }
                if (NaszeMetody.currencyLabelNumberTwo.equals(NaszeMetody.menuCurrencies.get(i))){
                    String priceOfProductFromHolderForTrolley = holder.price.getText().toString();
                    double przeliczonaCena = Double.parseDouble(priceOfProductFromHolderForTrolley);
                    NaszeMetody.resultForAnotherCurrency2 += przeliczonaCena;
                    NaszeMetody.overrideResultForCurrencyTwo = true;
                }
                if (!NaszeMetody.currencyLabelNumberOne.equals(NaszeMetody.menuCurrencies.get(i)) &&
                        !NaszeMetody.currencyLabelNumberTwo.equals(NaszeMetody.menuCurrencies.get(i)) && NaszeMetody.isVisiblePLNTotalCosts==0){
                    if (NaszeMetody.resultForAnotherCurrency3==0.00){
                        String priceOfProductFromHolderForTrolley = holder.price.getText().toString();
                        double przeliczonaCena = Double.parseDouble(priceOfProductFromHolderForTrolley);
                        NaszeMetody.resultForAnotherCurrency3 = przeliczonaCena;
                        NaszeMetody.currencyLabelNumberThree = NaszeMetody.menuCurrencies.get(i);
                        NaszeMetody.addThirdCurrencyLabel = true;
                    }
                    else {
                        String priceOfProductFromHolderForTrolley = holder.price.getText().toString();
                        double przeliczonaCena = Double.parseDouble(priceOfProductFromHolderForTrolley);
                        NaszeMetody.resultForAnotherCurrency3 += przeliczonaCena;
                        NaszeMetody.overrideResultForCurrencyThree = true;
                        NaszeMetody.addThirdCurrencyLabel = true;
                    }
                }
            }
        }
        if (holder.currencyLabel.getText().toString().equals("PLN")){
            String priceOfProductFromHolderForTrolley = holder.price.getText().toString();
            double przeliczonaCena = Double.parseDouble(priceOfProductFromHolderForTrolley);
            NaszeMetody.counterHelper = przeliczonaCena;
            NaszeMetody.isVisiblePLNTotalCosts = 1;
            if (NaszeMetody.finalResult==0.00){
                NaszeMetody.finalResult=NaszeMetody.counterHelper;
            }
            else if (NaszeMetody.finalResult!=0.00){
                NaszeMetody.finalResult = NaszeMetody.finalResult + NaszeMetody.counterHelper;
            }
        }
    }
    @Override
    public int getItemCount() {
        int size = 0;
        try {
            size = NaszeMetody.shoppingList.size();
        }
        catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context1, "Shopping List Is Empty", Toast.LENGTH_SHORT).show();
        }
        return size;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{
        public TextView name;
        public TextView price;
        public CheckBox checkBox;
        public ImageButton deleteimagebtn;
        public TextView numberOfProduct;
        public RelativeLayout relativeLay;
        public TextView currencyLabel;
        public TextView typeLabel;
        public MyViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.nazwaZakupu);
            price = (TextView) itemView.findViewById(R.id.cena2);
            checkBox = (CheckBox) itemView.findViewById(R.id.checkbox2);
            deleteimagebtn = (ImageButton) itemView.findViewById(R.id.deletebtn);
            numberOfProduct = (TextView) itemView.findViewById(R.id.cena);
            itemView.setOnCreateContextMenuListener(this);
            // dodaje odnosnik do tla w singleLine
            relativeLay = (RelativeLayout) itemView.findViewById(R.id.recyclerViewLay);
            currencyLabel = (TextView) itemView.findViewById(R.id.PLNlabel);
            typeLabel = (TextView) itemView.findViewById(R.id.numberOfProductLabel);

        }
        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            MenuInflater menuInflater = getMenuInflater();
            menuInflater.inflate(R.menu.menu_shoppinglist, menu);
            RelativeLayout relativeLay = (RelativeLayout) v.findViewById(R.id.recyclerViewLay);
            NaszeMetody.addThirdCurrencyLabel = false;
            NaszeMetody.overrideResultForCurrencyOne = false;
            NaszeMetody.overrideResultForCurrencyTwo = false;
        }

        private MenuInflater getMenuInflater() {
            return new MenuInflater(context1);
        }

    }
    public ShoppingAdapter1(List<Shopping> shoppingList, Context context){
        this.shoppingList = shoppingList;
        this.context1 = context;



    }
}

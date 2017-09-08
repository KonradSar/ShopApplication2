package com.example.konrad.applicationsecond;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.konrad.applicationsecond.NaszeMetody.shoppingList;

public class AddProduct extends AppCompatActivity {

    private int MAKE_IMAGE_REQUEST = 3;
    private static final int RESULT_LOAD_IMAGE = 2;
    public static final int REQUEST_CODE = 1;
    android.support.design.widget.CoordinatorLayout coordinator;
    final String TAG = "KonradApp";
    final int resultForStart = 1;
    String pathToPhoto = "";
    @BindView(R.id.nameOfProductsEditText) EditText nameOfProduct;
    @BindView(R.id.numberOfProductsEditText) EditText numberOfProducts;
    @BindView(R.id.priceEditText) EditText priceOfProduct;
    @BindView(R.id.weightOfProductEditText) EditText weightOfProduct;
    @BindView(R.id.priceFor100gramsOfProductEditText) EditText priceOf100grams;
    @BindView(R.id.btnSwitch) Switch choiceBtn;
    @BindView(R.id.btnRegister) Button registerBtn;
    @BindView(R.id.gotoListBtn) Button goToListBtn;
    @BindView(R.id.clearExternalMemory) Button clearExternalMemory;
    @BindView(R.id.btnSwitchAddInfo) Switch addExtraInfoAboutProduct;
    @BindView(R.id.additionalInfo) EditText additionalInfoEditText;
    @BindView(R.id.numberOfProductsLay) TextInputLayout numberOfProdCont;
    @BindView(R.id.weightOfProductLay) TextInputLayout weightProdCont;
    @BindView(R.id.priceLay) TextInputLayout priceOfASingleProdCont;
    @BindView(R.id.priceFor100gramsOfProductLay) TextInputLayout priceOf100gProdCont;
    @BindView(R.id.nameOfProductLay) TextInputLayout nameOfPRodCont;
    @BindView(R.id.additionalInfoOfProductLay) TextInputLayout additionalInfoCont;
    @BindView(R.id.btnAddPhoto) Switch addPhotobtn;
    private Context context;
    boolean stopAdding = false;
    MediaPlayer myFourthSound;
    MediaPlayer myThirdSound;
    Shopping shoppingOne;
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
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            pathToPhoto = picturePath;

        }
        else if (requestCode == MAKE_IMAGE_REQUEST && resultCode == RESULT_OK && null != data){
            String photoPath = "";

            Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{MediaStore.Images.Media.DATA, MediaStore.Images.Media.DATE_ADDED, MediaStore.Images.ImageColumns.ORIENTATION}, MediaStore.Images.Media.DATE_ADDED, null, MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC");
            if (cursor != null && cursor.moveToFirst()) {

                cursor.moveToFirst();
                Uri uri = Uri.parse(cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA)));
                photoPath = uri.toString();
                cursor.close();
                pathToPhoto = photoPath;
            }

        }
    }
    @OnClick(R.id.btnRegister)
    public void show() {
        myThirdSound.start();
        if (choiceBtn.isChecked() && addExtraInfoAboutProduct.isChecked() && addPhotobtn.isChecked()) {
            NaszeMetody.varrriablee = "bbbbbb";
            if (nameOfProduct.getText().toString().matches("")) {
                Toast.makeText(getApplicationContext(), R.string.FillEachLine, Toast.LENGTH_SHORT).show();
            } else if (weightOfProduct.getText().toString().matches("")) {
                Toast.makeText(getApplicationContext(), R.string.FillEachLine, Toast.LENGTH_SHORT).show();
            } else if (priceOf100grams.getText().toString().matches("")) {
                Toast.makeText(getApplicationContext(), R.string.FillEachLine, Toast.LENGTH_SHORT).show();
            } else if (additionalInfoEditText.getText().toString().matches("")){
                Toast.makeText(getApplicationContext(), R.string.FillEachLine, Toast.LENGTH_SHORT).show();
            }
            if (!weightOfProduct.getText().toString().matches("")) {
                String weightOfProduct2 = weightOfProduct.getText().toString();
                Double countedWeightOfProduct = Double.parseDouble(weightOfProduct2);
                if (countedWeightOfProduct > 999.99) {
                    stopAdding = true;
                    Toast.makeText(getApplicationContext(), R.string.maximumWeightOfProductInAddProdActivity, Toast.LENGTH_SHORT).show();
                } else {
                    stopAdding = false;
                }
            }
            if (!priceOf100grams.getText().toString().matches("")){
                String priceOf100Grams = priceOf100grams.getText().toString();
                Double countedPriceOf100Gramms = Double.parseDouble(priceOf100Grams);
                        if (countedPriceOf100Gramms > 999999.99){
                            stopAdding = true;
                            Toast.makeText(getApplicationContext(), R.string.MAximumValueForPriceOf100Grammms, Toast.LENGTH_SHORT).show();
                        }
            }

            if (!nameOfProduct.getText().toString().matches("") && !weightOfProduct.getText().toString().matches("") && !priceOf100grams.getText().toString().matches("") && !additionalInfoEditText.getText().toString().matches("") && !stopAdding && !pathToPhoto.equals("")) {
                String priceOf100Grams = priceOf100grams.getText().toString();
                double convertedPriceOf100Grams = Double.parseDouble(priceOf100Grams);
                float convertedPriceOf100Grams2 = Float.parseFloat(priceOf100Grams);
                String dsd = weightOfProduct.getText().toString();
                double convertedWeightOfProd = Double.parseDouble(dsd);
                float convertedWeightOfProd2 = Float.parseFloat(dsd);
                double countedValue = convertedPriceOf100Grams*convertedWeightOfProd;
                float countedValue2 = convertedPriceOf100Grams2*convertedWeightOfProd2;


                shoppingList.add(new Shopping(nameOfProduct.getText().toString(), Double.valueOf(weightOfProduct.getText().toString()), Double.valueOf(priceOf100grams.getText().toString()), choiceBtn.isChecked(), true, "PLN", NaszeMetody.shoppingList.size()+1, additionalInfoEditText.getText().toString(), pathToPhoto, false, countedValue, countedValue2));
                sprawdzCzyJestDostep();
                shoppingOne = (new Shopping(nameOfProduct.getText().toString(), Double.valueOf(weightOfProduct.getText().toString()), Double.valueOf(priceOf100grams.getText().toString()), choiceBtn.isChecked(), true, "PLN", NaszeMetody.shoppingList.size()+1, additionalInfoEditText.getText().toString(), pathToPhoto, false, countedValue, countedValue2));
                SaveToExternalMEmory(shoppingOne);
                Toast.makeText(getApplicationContext(), R.string.AddedProduct, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AddProduct.this, ShopRecyclerView.class);
                startActivity(intent);
            }
            else if (!nameOfProduct.getText().toString().matches("") && !weightOfProduct.getText().toString().matches("") && !priceOf100grams.getText().toString().matches("") && !stopAdding && pathToPhoto.equals("")){
                Toast.makeText(getApplicationContext(), R.string.FillEachLine, Toast.LENGTH_SHORT).show();
            }

        }
            else if (choiceBtn.isChecked() && !addExtraInfoAboutProduct.isChecked() && addPhotobtn.isChecked()){
            NaszeMetody.varrriablee = "bbbbbb";
            if (nameOfProduct.getText().toString().matches("")) {
                Toast.makeText(getApplicationContext(), R.string.FillEachLine, Toast.LENGTH_SHORT).show();
            } else if (weightOfProduct.getText().toString().matches("")) {
                Toast.makeText(getApplicationContext(), R.string.FillEachLine, Toast.LENGTH_SHORT).show();
            } else if (priceOf100grams.getText().toString().matches("")) {
                Toast.makeText(getApplicationContext(), R.string.FillEachLine, Toast.LENGTH_SHORT).show();
            }
            if (!weightOfProduct.getText().toString().matches("")) {
                String weightOfProduct2 = weightOfProduct.getText().toString();
                Double countedWeightOfProduct = Double.parseDouble(weightOfProduct2);
                if (countedWeightOfProduct > 999.99) {
                    stopAdding = true;
                    Toast.makeText(getApplicationContext(), R.string.maximumWeightOfProductInAddProdActivity, Toast.LENGTH_SHORT).show();
                } else {
                    stopAdding = false;
                }
            }
            if (!priceOf100grams.getText().toString().matches("")) {
                String priceOf100Grams = priceOf100grams.getText().toString();
                Double countedPriceOf100Gramms = Double.parseDouble(priceOf100Grams);
                if (countedPriceOf100Gramms > 999999.99){
                    stopAdding = true;
                    Toast.makeText(getApplicationContext(), R.string.MAximumValueForPriceOf100Grammms, Toast.LENGTH_SHORT).show();
                }
            }
            if (!nameOfProduct.getText().toString().matches("") && !weightOfProduct.getText().toString().matches("") && !priceOf100grams.getText().toString().matches("") && !stopAdding && !pathToPhoto.equals("") && !stopAdding) {
                String priceOf100Grams = priceOf100grams.getText().toString();
                double convertedPriceOf100Grams = Double.parseDouble(priceOf100Grams);
                float convertedPriceOf100Grams2 = Float.parseFloat(priceOf100Grams);
                String dsd = weightOfProduct.getText().toString();
                double convertedWeightOfProd = Double.parseDouble(dsd);
                float convertedWeightOfProd2 = Float.parseFloat(dsd);
                double countedValue = convertedPriceOf100Grams*convertedWeightOfProd;
                float countedValue2 = convertedPriceOf100Grams2*convertedWeightOfProd2;

                shoppingList.add(new Shopping(nameOfProduct.getText().toString(), Double.valueOf(weightOfProduct.getText().toString()), Double.valueOf(priceOf100grams.getText().toString()), choiceBtn.isChecked(), true, "PLN", NaszeMetody.shoppingList.size()+1, null, pathToPhoto, false, countedValue, countedValue2));
                sprawdzCzyJestDostep();
                shoppingOne = (new Shopping(nameOfProduct.getText().toString(), Double.valueOf(weightOfProduct.getText().toString()), Double.valueOf(priceOf100grams.getText().toString()), choiceBtn.isChecked(), true, "PLN", NaszeMetody.shoppingList.size()+1, null, pathToPhoto, false, countedValue, countedValue2));
                SaveToExternalMEmory(shoppingOne);
                Toast.makeText(getApplicationContext(), R.string.AddedProduct, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AddProduct.this, ShopRecyclerView.class);
                startActivity(intent);
            }
            else if (!nameOfProduct.getText().toString().matches("") && !weightOfProduct.getText().toString().matches("") && !priceOf100grams.getText().toString().matches("") && !stopAdding && pathToPhoto.equals("")){
                Toast.makeText(getApplicationContext(), R.string.FillEachLine, Toast.LENGTH_SHORT).show();
            }
        }
        else if (!choiceBtn.isChecked() && addExtraInfoAboutProduct.isChecked() && addPhotobtn.isChecked()) {
            NaszeMetody.varrriablee = "aaaaa";
            if (nameOfProduct.getText().toString().matches("")) {
                Toast.makeText(getApplicationContext(), R.string.FillEachLine, Toast.LENGTH_SHORT).show();
            } else if (numberOfProducts.getText().toString().matches("")) {
                Toast.makeText(getApplicationContext(), R.string.FillEachLine, Toast.LENGTH_SHORT).show();
            } else if (priceOfProduct.getText().toString().matches("")) {
                Toast.makeText(getApplicationContext(), R.string.FillEachLine, Toast.LENGTH_SHORT).show();
            }else if (additionalInfoEditText.getText().toString().matches("")){
                Toast.makeText(getApplicationContext(), R.string.FillEachLine, Toast.LENGTH_SHORT).show();
            }
            if (!numberOfProducts.getText().toString().matches("")) {
                String ww = String.valueOf(numberOfProducts.getText().toString());
                double countedNumbOfPrd1 = Double.parseDouble(ww);
                double max = 999.99;
                if (countedNumbOfPrd1 > max) {
                    stopAdding = true;
                    Toast.makeText(getApplicationContext(), R.string.maximumValueForNumbOfProducts, Toast.LENGTH_SHORT).show();
                } else {
                    stopAdding = false;
                }
            }
            if (!priceOfProduct.getText().toString().equals("")){
                String priceOfASinglePr = priceOfProduct.getText().toString();
                Double countedPrice = Double.parseDouble(priceOfASinglePr);
                if (countedPrice > 999999.99){
                    stopAdding = true;
                    Toast.makeText(getApplicationContext(), R.string.MaximumPriceOfASingProdd, Toast.LENGTH_SHORT).show();
                }
            }
            if (!nameOfProduct.getText().toString().matches("") && !numberOfProducts.getText().toString().matches("") && !priceOfProduct.getText().toString().matches("") && !additionalInfoEditText.getText().toString().matches("") && !stopAdding && !pathToPhoto.equals("")){
                String numberOfProd = numberOfProducts.getText().toString();
                int convertedNumberOfProduct = Integer.parseInt(numberOfProd);
                float convertedNumberOfProduct2 = Float.parseFloat(numberOfProd);
                String priceOfSingle = priceOfProduct.getText().toString();
                double convertedPriceOfSinglePr = Double.parseDouble(priceOfSingle);
                float convertedPriceOfSinglePr2 = Float.parseFloat(priceOfSingle);
                double countedValue = convertedNumberOfProduct*convertedPriceOfSinglePr;
                float countedValue2 = convertedNumberOfProduct2*convertedPriceOfSinglePr2;

                shoppingList.add(new Shopping(nameOfProduct.getText().toString(), Integer.valueOf(numberOfProducts.getText().toString()), Double.valueOf(priceOfProduct.getText().toString()), choiceBtn.isChecked(), true, "PLN", NaszeMetody.shoppingList.size()+1, additionalInfoEditText.getText().toString(), pathToPhoto, false, countedValue, countedValue2));
                sprawdzCzyJestDostep();
                shoppingOne = (new Shopping(nameOfProduct.getText().toString(), Integer.valueOf(numberOfProducts.getText().toString()), Double.valueOf(priceOfProduct.getText().toString()), choiceBtn.isChecked(), true, "PLN", NaszeMetody.shoppingList.size()+1, additionalInfoEditText.getText().toString(), pathToPhoto, false, countedValue, countedValue2));
                SaveToExternalMEmory(shoppingOne);
                Toast.makeText(getApplicationContext(), R.string.AddedProduct, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AddProduct.this, ShopRecyclerView.class);
                startActivity(intent);

            }
            else if (!nameOfProduct.getText().toString().matches("") && !numberOfProducts.getText().toString().matches("") && !priceOfProduct.getText().toString().matches("") && !stopAdding && pathToPhoto.equals("")){
                Toast.makeText(getApplicationContext(), R.string.FillEachLine, Toast.LENGTH_SHORT).show();
            }

        }
            else if (!choiceBtn.isChecked() && !addExtraInfoAboutProduct.isChecked() && addPhotobtn.isChecked()){
            NaszeMetody.varrriablee = "aaaaa";
            if (nameOfProduct.getText().toString().matches("")) {
                Toast.makeText(getApplicationContext(), R.string.FillEachLine, Toast.LENGTH_SHORT).show();
            } else if (numberOfProducts.getText().toString().equals("")) {
                Toast.makeText(getApplicationContext(), R.string.FillEachLine, Toast.LENGTH_SHORT).show();
            } else if (priceOfProduct.getText().toString().matches("")) {
                Toast.makeText(getApplicationContext(), R.string.FillEachLine, Toast.LENGTH_SHORT).show();
            }
            if (!numberOfProducts.getText().toString().matches("")) {
                String ww = String.valueOf(numberOfProducts.getText().toString());
                double countedNumbOfPrd1 = Double.parseDouble(ww);
                double max = 999.99;
                if (countedNumbOfPrd1 > max) {
                    stopAdding = true;
                    Toast.makeText(getApplicationContext(), R.string.maximumValueForNumbOfProducts, Toast.LENGTH_SHORT).show();
                } else {
                    stopAdding = false;
                }
            }
            if (!priceOfProduct.getText().toString().equals("")) {
                String priceOfASinglePr = priceOfProduct.getText().toString();
                Double countedPrice = Double.parseDouble(priceOfASinglePr);
                if (countedPrice > 999999.99){
                    stopAdding = true;
                    Toast.makeText(getApplicationContext(), R.string.MaximumPriceOfASingProdd, Toast.LENGTH_SHORT).show();
                }
            }
            if (!nameOfProduct.getText().toString().matches("") && !numberOfProducts.getText().toString().matches("") && !priceOfProduct.getText().toString().matches("") && !stopAdding && !pathToPhoto.equals("") && !stopAdding) {
                String numberOfProd = numberOfProducts.getText().toString();
                int convertedNumberOfProduct = Integer.parseInt(numberOfProd);
                float convertedNumberOfProduct2 = Float.parseFloat(numberOfProd);
                String priceOfSingle = priceOfProduct.getText().toString();
                double convertedPriceOfSinglePr = Double.parseDouble(priceOfSingle);
                float convertedPriceOfSinglePr2 = Float.parseFloat(priceOfSingle);
                double countedValue = convertedNumberOfProduct*convertedPriceOfSinglePr;
                float countedValue2 = convertedNumberOfProduct2*convertedPriceOfSinglePr2;

                shoppingList.add(new Shopping(nameOfProduct.getText().toString(), Integer.valueOf(numberOfProducts.getText().toString()), Double.valueOf(priceOfProduct.getText().toString()), choiceBtn.isChecked(), true, "PLN", NaszeMetody.shoppingList.size()+1, null, pathToPhoto, false, countedValue, countedValue2));
                sprawdzCzyJestDostep();
                shoppingOne = (new Shopping(nameOfProduct.getText().toString(), Integer.valueOf(numberOfProducts.getText().toString()), Double.valueOf(priceOfProduct.getText().toString()), choiceBtn.isChecked(), true, "PLN", NaszeMetody.shoppingList.size()+1, null, pathToPhoto, false, countedValue, countedValue2));
                SaveToExternalMEmory(shoppingOne);
                Toast.makeText(getApplicationContext(), R.string.AddedProduct, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AddProduct.this, ShopRecyclerView.class);
                startActivity(intent);

            }
            else if (!nameOfProduct.getText().toString().matches("") && !numberOfProducts.getText().toString().matches("") && !priceOfProduct.getText().toString().matches("") && !stopAdding && pathToPhoto.equals("")){
                Toast.makeText(getApplicationContext(), R.string.FillEachLine, Toast.LENGTH_SHORT).show();
            }

        }
        else if (choiceBtn.isChecked() && addExtraInfoAboutProduct.isChecked() && !addPhotobtn.isChecked()){
            NaszeMetody.varrriablee = "bbbbbb";
            if (nameOfProduct.getText().toString().equals("")) {
                Toast.makeText(getApplicationContext(), R.string.FillEachLine, Toast.LENGTH_SHORT).show();
                stopAdding = true;
            }
            if (weightOfProduct.getText().toString().equals("")) {
                Toast.makeText(getApplicationContext(), R.string.FillEachLine, Toast.LENGTH_SHORT).show();
                stopAdding = true;
            }
            if (priceOf100grams.getText().toString().equals("")) {
                Toast.makeText(getApplicationContext(), R.string.FillEachLine, Toast.LENGTH_SHORT).show();
                stopAdding = true;
            }
            if (additionalInfoEditText.getText().toString().equals("")){
                Toast.makeText(getApplicationContext(), R.string.FillEachLine, Toast.LENGTH_SHORT).show();
                stopAdding = true;
            }
            if (!weightOfProduct.getText().toString().equals("")) {
                String weightOfProduct2 = weightOfProduct.getText().toString();
                Double countedWeightOfProduct = Double.parseDouble(weightOfProduct2);
                if (countedWeightOfProduct > 999.99) {
                    stopAdding = true;
                    Toast.makeText(getApplicationContext(), R.string.maximumWeightOfProductInAddProdActivity, Toast.LENGTH_SHORT).show();
                }
            }
            if (!priceOf100grams.getText().toString().equals("")) {
                String priceOf100Grams = priceOf100grams.getText().toString();
                Double countedPriceOf100Gramms = Double.parseDouble(priceOf100Grams);
                if (countedPriceOf100Gramms > 999999.99){
                    stopAdding = true;
                    Toast.makeText(getApplicationContext(), R.string.MAximumValueForPriceOf100Grammms, Toast.LENGTH_SHORT).show();
                }
            }
            if (!nameOfProduct.getText().toString().equals("") && !weightOfProduct.getText().toString().equals("") && !priceOf100grams.getText().toString().equals("") && !additionalInfoEditText.getText().toString().equals("") && !stopAdding) {
                String priceOf100Grams = priceOf100grams.getText().toString();
                double convertedPriceOf100Grams = Double.parseDouble(priceOf100Grams);
                float convertedPriceOf100Grams2 = Float.parseFloat(priceOf100Grams);
                String dsd = weightOfProduct.getText().toString();
                double convertedWeightOfProd = Double.parseDouble(dsd);
                float convertedWeightOfProd2 = Float.parseFloat(dsd);
                double countedValue = convertedPriceOf100Grams*convertedWeightOfProd;
                float countedValue2 = convertedPriceOf100Grams2*convertedWeightOfProd2;


                shoppingList.add(new Shopping(nameOfProduct.getText().toString(), Double.valueOf(weightOfProduct.getText().toString()), Double.valueOf(priceOf100grams.getText().toString()), choiceBtn.isChecked(), true, "PLN", NaszeMetody.shoppingList.size()+1, additionalInfoEditText.getText().toString(), null, false, countedValue, countedValue2));
                sprawdzCzyJestDostep();
                shoppingOne = (new Shopping(nameOfProduct.getText().toString(), Double.valueOf(weightOfProduct.getText().toString()), Double.valueOf(priceOf100grams.getText().toString()), choiceBtn.isChecked(), true, "PLN", NaszeMetody.shoppingList.size()+1, additionalInfoEditText.getText().toString(), null, false, countedValue, countedValue2));
                SaveToExternalMEmory(shoppingOne);
                Toast.makeText(getApplicationContext(), R.string.AddedProduct, Toast.LENGTH_SHORT).show();
                pathToPhoto = "";
                Intent intent = new Intent(AddProduct.this, ShopRecyclerView.class);
                startActivity(intent);

            }
        }

        else if (choiceBtn.isChecked() && !addExtraInfoAboutProduct.isChecked() && !addPhotobtn.isChecked()){
            NaszeMetody.varrriablee = "bbbbbb";
            if (nameOfProduct.getText().toString().matches("")) {
                Toast.makeText(getApplicationContext(), R.string.FillEachLine, Toast.LENGTH_SHORT).show();
            } else if (weightOfProduct.getText().toString().matches("")) {
                Toast.makeText(getApplicationContext(), R.string.FillEachLine, Toast.LENGTH_SHORT).show();
            } else if (priceOf100grams.getText().toString().matches("")) {
                Toast.makeText(getApplicationContext(), R.string.FillEachLine, Toast.LENGTH_SHORT).show();
            }
            if (!weightOfProduct.getText().toString().matches("")) {
                String weightOfProduct2 = weightOfProduct.getText().toString();
                Double countedWeightOfProduct = Double.parseDouble(weightOfProduct2);
                if (countedWeightOfProduct > 999.99) {
                    stopAdding = true;
                    Toast.makeText(getApplicationContext(), R.string.maximumWeightOfProductInAddProdActivity, Toast.LENGTH_SHORT).show();
                } else {
                    stopAdding = false;
                }
            }
            if (!priceOf100grams.getText().toString().matches("")) {
                String priceOf100Grams = priceOf100grams.getText().toString();
                Double countedPriceOf100Gramms = Double.parseDouble(priceOf100Grams);
                if (countedPriceOf100Gramms > 999999.99){
                    stopAdding = true;
                    Toast.makeText(getApplicationContext(), R.string.MAximumValueForPriceOf100Grammms, Toast.LENGTH_SHORT).show();
                }
            }
            if (!nameOfProduct.getText().toString().matches("") && !weightOfProduct.getText().toString().matches("") && !priceOf100grams.getText().toString().matches("") && !stopAdding) {
                String priceOf100Grams = priceOf100grams.getText().toString();
                double convertedPriceOf100Grams = Double.parseDouble(priceOf100Grams);
                float convertedPriceOf100Grams2 = Float.parseFloat(priceOf100Grams);
                String dsd = weightOfProduct.getText().toString();
                double convertedWeightOfProd = Double.parseDouble(dsd);
                float convertedWeightOfProd2 = Float.parseFloat(dsd);
                double countedValue = convertedPriceOf100Grams*convertedWeightOfProd;
                float countedValue2 = convertedPriceOf100Grams2*convertedWeightOfProd2;

                shoppingList.add(new Shopping(nameOfProduct.getText().toString(), Double.valueOf(weightOfProduct.getText().toString()), Double.valueOf(priceOf100grams.getText().toString()), choiceBtn.isChecked(), true, "PLN", NaszeMetody.shoppingList.size()+1, null, null, false, countedValue, countedValue2));
                sprawdzCzyJestDostep();
                shoppingOne = (new Shopping(nameOfProduct.getText().toString(), Double.valueOf(weightOfProduct.getText().toString()), Double.valueOf(priceOf100grams.getText().toString()), choiceBtn.isChecked(), true, "PLN", NaszeMetody.shoppingList.size()+1, null, null, false, countedValue, countedValue2));
                SaveToExternalMEmory(shoppingOne);
                Toast.makeText(getApplicationContext(), R.string.AddedProduct, Toast.LENGTH_SHORT).show();
                pathToPhoto = "";
                Intent intent = new Intent(AddProduct.this, ShopRecyclerView.class);
                startActivity(intent);

            }

        }
        else if (!choiceBtn.isChecked() && addExtraInfoAboutProduct.isChecked() && !addPhotobtn.isChecked()){
            NaszeMetody.varrriablee = "aaaaa";
            if (nameOfProduct.getText().toString().matches("")) {
                Toast.makeText(getApplicationContext(), R.string.FillEachLine, Toast.LENGTH_SHORT).show();
            } else if (numberOfProducts.getText().toString().matches("")) {
                Toast.makeText(getApplicationContext(), R.string.FillEachLine, Toast.LENGTH_SHORT).show();
            } else if (priceOfProduct.getText().toString().matches("")) {
                Toast.makeText(getApplicationContext(), R.string.FillEachLine, Toast.LENGTH_SHORT).show();
            }else if (additionalInfoEditText.getText().toString().matches("")){
                Toast.makeText(getApplicationContext(), R.string.FillEachLine, Toast.LENGTH_SHORT).show();
            }
            if (!numberOfProducts.getText().toString().matches("")) {
                String ww = String.valueOf(numberOfProducts.getText().toString());
                double countedNumbOfPrd1 = Double.parseDouble(ww);
                double max = 999.99;
                if (countedNumbOfPrd1 > max) {
                    stopAdding = true;
                    Toast.makeText(getApplicationContext(), R.string.maximumValueForNumbOfProducts, Toast.LENGTH_SHORT).show();
                } else {
                    stopAdding = false;
                }
            }
            if (!priceOfProduct.getText().toString().equals("")) {
                String priceOfASinglePr = priceOfProduct.getText().toString();
                Double countedPrice = Double.parseDouble(priceOfASinglePr);
                if (countedPrice > 999999.99){
                    stopAdding = true;
                    Toast.makeText(getApplicationContext(), R.string.MaximumPriceOfASingProdd, Toast.LENGTH_SHORT).show();
                }
            }
            if (!nameOfProduct.getText().toString().matches("") && !numberOfProducts.getText().toString().matches("") && !priceOfProduct.getText().toString().matches("") && !additionalInfoEditText.getText().toString().matches("") && !stopAdding){
                String numberOfProd = numberOfProducts.getText().toString();
                int convertedNumberOfProduct = Integer.parseInt(numberOfProd);
                float convertedNumberOfProduct2 = Float.parseFloat(numberOfProd);
                String priceOfSingle = priceOfProduct.getText().toString();
                double convertedPriceOfSinglePr = Double.parseDouble(priceOfSingle);
                float convertedPriceOfSinglePr2 = Float.parseFloat(priceOfSingle);
                double countedValue = convertedNumberOfProduct*convertedPriceOfSinglePr;
                float countedValue2 = convertedNumberOfProduct2*convertedPriceOfSinglePr2;
                shoppingList.add(new Shopping(nameOfProduct.getText().toString(), Integer.valueOf(numberOfProducts.getText().toString()), Double.valueOf(priceOfProduct.getText().toString()), choiceBtn.isChecked(), true, "PLN", NaszeMetody.shoppingList.size()+1, additionalInfoEditText.getText().toString(), null, false, countedValue, countedValue2));
                sprawdzCzyJestDostep();
                shoppingOne = (new Shopping(nameOfProduct.getText().toString(), Integer.valueOf(numberOfProducts.getText().toString()), Double.valueOf(priceOfProduct.getText().toString()), choiceBtn.isChecked(), true, "PLN", NaszeMetody.shoppingList.size()+1, additionalInfoEditText.getText().toString(), null, false, countedValue, countedValue2));
                SaveToExternalMEmory(shoppingOne);
                Toast.makeText(getApplicationContext(), R.string.AddedProduct, Toast.LENGTH_SHORT).show();
                pathToPhoto = "";
                Intent intent = new Intent(AddProduct.this, ShopRecyclerView.class);
                startActivity(intent);

            }

        }
        else if (!choiceBtn.isChecked() && !addExtraInfoAboutProduct.isChecked() && !addPhotobtn.isChecked()){
            NaszeMetody.varrriablee = "aaaaa";
            if (nameOfProduct.getText().toString().equals("")) {
                Toast.makeText(getApplicationContext(), R.string.FillEachLine, Toast.LENGTH_SHORT).show();
            } else if (numberOfProducts.getText().toString().equals("")) {
                Toast.makeText(getApplicationContext(), R.string.FillEachLine, Toast.LENGTH_SHORT).show();
            } else if (priceOfProduct.getText().toString().equals("")) {
                Toast.makeText(getApplicationContext(), R.string.FillEachLine, Toast.LENGTH_SHORT).show();
            }

            if (!numberOfProducts.getText().toString().equals("")) {
                String ww = String.valueOf(numberOfProducts.getText().toString());
                double countedNumbOfPrd1 = Double.parseDouble(ww);
                double max = 999.99;
                if (countedNumbOfPrd1 > max) {
                    stopAdding = true;
                    Toast.makeText(getApplicationContext(), R.string.maximumValueForNumbOfProducts, Toast.LENGTH_SHORT).show();
                } else {
                    stopAdding = false;
                }
            }

            if (!priceOfProduct.getText().toString().equals("")) {
                String priceOfASinglePr = priceOfProduct.getText().toString();
                double countedPrice = Double.parseDouble(priceOfASinglePr);
                double max2 = 999999.99;
                if (countedPrice > 999999.99){
                    stopAdding = true;
                    Toast.makeText(getApplicationContext(), R.string.MaximumPriceOfASingProdd, Toast.LENGTH_SHORT).show();
                }
            }

            if (!nameOfProduct.getText().toString().matches("") && !numberOfProducts.getText().toString().matches("") && !priceOfProduct.getText().toString().matches("") && !stopAdding) {
                String numberOfProd = numberOfProducts.getText().toString();
                int convertedNumberOfProduct = Integer.parseInt(numberOfProd);
                float convertedNumberOfProduct2 = Float.parseFloat(numberOfProd);
                String priceOfSingle = priceOfProduct.getText().toString();
                double convertedPriceOfSinglePr = Double.parseDouble(priceOfSingle);
                float convertedPriceOfSinglePr2 = Float.parseFloat(priceOfSingle);
                double countedValue = convertedNumberOfProduct*convertedPriceOfSinglePr;
                float countedValue2 = convertedNumberOfProduct2*convertedPriceOfSinglePr2;

                shoppingList.add(new Shopping(nameOfProduct.getText().toString(), Integer.valueOf(numberOfProducts.getText().toString()), Double.valueOf(priceOfProduct.getText().toString()), choiceBtn.isChecked(), true, "PLN", NaszeMetody.shoppingList.size()+1, null, null, false, countedValue, countedValue2));
                sprawdzCzyJestDostep();
                shoppingOne = (new Shopping(nameOfProduct.getText().toString(), Integer.valueOf(numberOfProducts.getText().toString()), Double.valueOf(priceOfProduct.getText().toString()), choiceBtn.isChecked(), true, "PLN", NaszeMetody.shoppingList.size()+1, null, null, false, countedValue, countedValue2));
                SaveToExternalMEmory(shoppingOne);
                Toast.makeText(getApplicationContext(), R.string.AddedProduct, Toast.LENGTH_SHORT).show();
                pathToPhoto = "";
                Intent intent = new Intent(AddProduct.this, ShopRecyclerView.class);
                startActivity(intent);
            }
        }
    }
    @OnClick(R.id.btnSwitch)
    public void select(){
        myFourthSound.start();
        if(choiceBtn.isChecked()){
            numberOfProdCont.setVisibility(View.GONE);
            priceOfASingleProdCont.setVisibility(View.GONE);
            weightProdCont.setVisibility(View.VISIBLE);
            priceOf100gProdCont.setVisibility(View.VISIBLE);
            nameOfPRodCont.setVisibility(View.VISIBLE);
        }else if (!choiceBtn.isChecked()){
            weightProdCont.setVisibility(View.GONE);
            priceOf100gProdCont.setVisibility(View.GONE);
            numberOfProdCont.setVisibility(View.VISIBLE);
            nameOfPRodCont.setVisibility(View.VISIBLE);
            priceOfASingleProdCont.setVisibility(View.VISIBLE);
            priceOf100gProdCont.setVisibility(View.GONE);

        }
    }
    @OnClick(R.id.btnSwitchAddInfo)
    public void selectAdditionalInfo(){
        myFourthSound.start();
        if (addExtraInfoAboutProduct.isChecked()){
            additionalInfoEditText.setVisibility(View.VISIBLE);
            additionalInfoCont.setVisibility(View.VISIBLE);
        }
        else if (!addExtraInfoAboutProduct.isChecked()){
            additionalInfoCont.setVisibility(View.GONE);
        }
    }
    @OnClick(R.id.btnAddPhoto)
    public void addAPhotoToShoppingOne(){
        myFourthSound.start();
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(AddProduct.this);
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

    }
    private void zrobZdjecie() {
        Intent takePhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takePhoto.resolveActivity(getPackageManager()) != null){
            startActivityForResult(takePhoto, MAKE_IMAGE_REQUEST);
        }
    }

    private void wybierzZdjecie() {
        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }
    public void onBackPressed() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(AddProduct.this);
        alertDialog.setTitle(R.string.DoYouWantToExitApp);
        alertDialog.setPositiveButton(R.string.Okey, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finishAffinity();

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
        return;
    }
    private void closeApplication() {
        this.finish();
    }


    @OnClick(R.id.gotoListBtn)
    public void goToListOnly(){
        myThirdSound.start();
        Intent intent = new Intent(AddProduct.this, ShopRecyclerView.class);
        startActivity(intent);
    }

    @OnClick(R.id.clearExternalMemory)
    public void clearExternalMemoryFile(){
        myThirdSound.start();
        File plik = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), NAZWA_PLIKU);
        plik.delete();
        Toast.makeText(getApplicationContext(), R.string.CzyszczeniePamięci, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        ButterKnife.bind(this);
        NaszeMetody.InicjalizacjaNaszeMetody(context);
        boolean stopAdding = false;
        String pathToPhoto = "";
        myFourthSound = MediaPlayer.create(this, R.raw.zmiana);
        // Utwór w formacie mp3 o nazwie Switch na licencji Attribution 3.0 pobrany dnia 04.09.2017 godz. 20:00 ze strony soundbible.com
        myThirdSound = MediaPlayer.create(this, R.raw.klikanie);
        // Utwór w formacie mp3 o nazwie Click On na licencji Attribution 3.0 pobrany dnia 04.09.2017 godz. 20:00 ze strony soundbible.com

        // Wszystkie Grafiki oraz Zdjęcia znajdujące się w niniejszej aplikacji zakupowej zostały pobrane z darmowego servisu https://pixabay.com/pl/
        // jako Darmowe do użytku komercyjnego nie wymagające przypisania w dniach 01.07.2017-05.09.2017

        if (Locale.getDefault().getCountry().matches("PL")) {
            registerBtn.setBackgroundResource(R.drawable.dodajpolski);
            clearExternalMemory.setBackgroundResource(R.drawable.usun);
            goToListBtn.setBackgroundResource(R.drawable.listazakupow);

        } else {

        }
        if (resultForStart == 1) {
            weightProdCont.setVisibility(View.GONE);
            priceOf100gProdCont.setVisibility(View.GONE);
            nameOfProduct.setVisibility(View.VISIBLE);
            numberOfProducts.setVisibility(View.VISIBLE);
            priceOfProduct.setVisibility(View.VISIBLE);
            additionalInfoEditText.setVisibility(View.GONE);
            additionalInfoCont.setVisibility(View.GONE);

        }
    }

    final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;

    public void sprawdzCzyJestDostep(){
        if(android.support.v4.app.ActivityCompat.checkSelfPermission(AddProduct.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(AddProduct.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                    // jesli dostep blokowal pokazujemy po co nam to potrzebne
                    showExplanation("Potrzebujemy pozwolenia", "Chcemy zapisać w pamięci zewnętrznej menu",
                            Manifest.permission.WRITE_EXTERNAL_STORAGE, MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                }else{

                    requestPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                }
            }
        }


        private void requestPermissions(String permissionName, int permissionRequestCode){
            ActivityCompat.requestPermissions(this, new String[]{permissionName} , permissionRequestCode);
        }


    private void showExplanation(String title, String message, final String permission, final int permissionRequestCode){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(title);
            builder.setMessage(message);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    requestPermissions(permission, permissionRequestCode);
                }
            });
            builder.show();
        }

    @Override
        public void onRequestPermissionsResult(int requestCode,
        String permissions[], int[] grantResults) {
            switch (requestCode) {
                case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                    if (grantResults.length > 0
                            && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        SaveToExternalMEmory(shoppingOne); // dostep przyznany - mozemy zrobic co chcemy
                        Log.d(NaszeMetody.LOG_TAG, "Dostęp przyznany");
                    } else {
                        Log.d(NaszeMetody.LOG_TAG, "Dostęp nie przyznany");
                        if (grantResults[0] == PackageManager.PERMISSION_DENIED) {

                            boolean showRationale = ActivityCompat.shouldShowRequestPermissionRationale(AddProduct.this,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE);
                            if (!showRationale) {
                                Log.d(NaszeMetody.LOG_TAG, "Uzytkownik zaznaczyl never ask again");
                            }
                        }
                    }
                    return;
                }
            }
        }
    public List<Shopping> readFromExternalMemory(){
        File plik = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), NAZWA_PLIKU);
        StringBuilder sb = new StringBuilder();
        try {
            FileInputStream fis =  new FileInputStream(plik);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            sb = new StringBuilder();
            String line;
            while((line = br.readLine()) != null){
                sb.append(line);
            }

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), R.string.BrakZakupów, Toast.LENGTH_SHORT).show();

        }
        Type type = new TypeToken<List<Shopping>>(){}.getType();

        List<Shopping> odczytaneProdukty;

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        odczytaneProdukty = gson.fromJson(sb.toString(), type);


        return odczytaneProdukty;

    }
    final static String NAZWA_PLIKU = "ShoppingList.sda";

    public boolean czyPlikIstnieje(){
        File plik = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), NAZWA_PLIKU);
        boolean zwrotka = false;
        if(plik.exists()){
            zwrotka = true;
        }
        return zwrotka;
    }

    public void SaveToExternalMEmory(Shopping shopping) {
        if (czyPlikIstnieje() == false){
            List<Shopping> myList1 = new ArrayList<>();
            myList1.add(shoppingOne);
            zapiszDoPamieci(myList1);
        }
        else{
            List<Shopping> myList1 = readFromExternalMemory();

            myList1.add(shoppingOne);
            zapiszDoPamieci(myList1);

        }
    }

    public void zapiszDoPamieci(List<Shopping> shoppingList) {
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

}

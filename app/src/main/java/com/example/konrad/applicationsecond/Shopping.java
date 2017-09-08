package com.example.konrad.applicationsecond;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.io.Serializable;

/**
 * Created by Konrad on 17.06.2017.
 */

public class Shopping implements Serializable{
    public String productName;
    public int numberOfProducts;
    public String additionalInfo;
    public double priceOfASingleProduct;
    public double weightOfProduct;
    public double priceOf100grams;
    public boolean isDustProductOn;
    public boolean isRedBackgroundColor;
    public String currencyLabel;
    public int shoppingIndex;
    public String photoPath;
    public boolean isCheckedCheckBox;
    public double totalUpdatedPriceOfProduct;
    public float totalPriceOfProductInFloat;


    public Shopping(String productName, int numberOfProducts, String additionalInfox) {
        this.productName = productName;
        this.numberOfProducts = numberOfProducts;
        this.additionalInfo = additionalInfo;
    }

    public Shopping(String productName, int numberOfProducts, double priceOfASingleProduct, boolean isDustProductOn, boolean isRedBackgroundColor, String currencyLabel, int shoppingIndex, String additionalInfo, String photoPath, boolean isCheckedCheckBox, double totalUpdatedPriceOfProduct, float totalPriceOfProductInFloat) {
        this.productName = productName;
        this.numberOfProducts = numberOfProducts;
        this.priceOfASingleProduct = priceOfASingleProduct;
        this.isDustProductOn = isDustProductOn;
        this.isRedBackgroundColor = isRedBackgroundColor;
        this.currencyLabel = currencyLabel;
        this.shoppingIndex = shoppingIndex;
        this.additionalInfo = additionalInfo;
        this.photoPath = photoPath;
        this.isCheckedCheckBox = isCheckedCheckBox;
        this.totalUpdatedPriceOfProduct = totalUpdatedPriceOfProduct;
        this.totalPriceOfProductInFloat = totalPriceOfProductInFloat;

    }
    public String getPhotoPath() {
        return photoPath;

    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public Shopping(String productName, double weightOfProduct, double priceOf100grams, boolean isDustProductOn, boolean isRedBackgroundColor, String currencyLabel, int shoppingIndex, String additionalInfo, String photoPath, boolean isCheckedCheckBox, double totalUpdatedPriceOfProduct, float totalPriceOfProductInFloat) {
        this.productName = productName;
        this.weightOfProduct = weightOfProduct;
        this.priceOf100grams = priceOf100grams;
        this.isDustProductOn = isDustProductOn;
        this.isRedBackgroundColor = isRedBackgroundColor;
        this.currencyLabel = currencyLabel;
        this.shoppingIndex = shoppingIndex;
        this.additionalInfo = additionalInfo;
        this.photoPath = photoPath;
        this.isCheckedCheckBox = isCheckedCheckBox;
        this.totalUpdatedPriceOfProduct = totalUpdatedPriceOfProduct;
        this.totalPriceOfProductInFloat = totalPriceOfProductInFloat;

    }

    public float getTotalPriceOfProductInFloat() {
        return totalPriceOfProductInFloat;
    }

    public void setTotalPriceOfProductInFloat(float totalPriceOfProductInFloat) {
        this.totalPriceOfProductInFloat = totalPriceOfProductInFloat;
    }

    public double getTotalUpdatedPriceOfProduct() {
        return totalUpdatedPriceOfProduct;
    }

    public void setTotalUpdatedPriceOfProduct(double totalUpdatedPriceOfProduct) {
        this.totalUpdatedPriceOfProduct = totalUpdatedPriceOfProduct;
    }

    public boolean isCheckedCheckBox() {
        return isCheckedCheckBox;
    }

    public void setCheckedCheckBox(boolean checkedCheckBox) {
        isCheckedCheckBox = checkedCheckBox;
    }


    public int getShoppingIndex() {
        return shoppingIndex;
    }

    public void setShoppingIndex(int shoppingIndex) {
        this.shoppingIndex = shoppingIndex;
    }

    public String getCurrencyLabel() {
        return currencyLabel;
    }

    public void setCurrencyLabel(String currencyLabel) {
        this.currencyLabel = currencyLabel;
    }

    public boolean isDustProductOn() {
        return isDustProductOn;
    }

    public boolean isRedBackgroundColor() {
        return isRedBackgroundColor;
    }

    public void setRedBackgroundColor(boolean redBackgroundColor) {
        isRedBackgroundColor = redBackgroundColor;
    }

    public void setDustProductOn(boolean dustProductOn) {
        isDustProductOn = dustProductOn;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getNumberOfProducts() {
        return numberOfProducts;
    }

    public void setNumberOfProducts(int numberOfProducts) {
        this.numberOfProducts = numberOfProducts;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public double getPriceOfASingleProduct() {
        return priceOfASingleProduct;
    }

    public void setPriceOfASingleProduct(double priceOfASingleProduct) {
        this.priceOfASingleProduct = priceOfASingleProduct;
    }

    public double getWeightOfProduct() {
        return weightOfProduct;
    }

    public void setWeightOfProduct(double weightOfProduct) {
        this.weightOfProduct = weightOfProduct;
    }

    public double getPriceOf100grams() {
        return priceOf100grams;
    }

    public void setPriceOf100grams(double priceOf100grams) {
        this.priceOf100grams = priceOf100grams;
    }
}

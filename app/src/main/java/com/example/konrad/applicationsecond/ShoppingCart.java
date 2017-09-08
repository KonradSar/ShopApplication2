package com.example.konrad.applicationsecond;

/**
 * Created by Konrad on 14.07.2017.
 */

public class ShoppingCart {
    public String productName;
    public int numberOfProducts;
    public double priceOfASingleProduct;
    public double weightOfProduct;
    public double priceOf100grams;
    public double firstVariable;
    public double secondVariable;

    public ShoppingCart(String productName) {
        this.productName = productName;
    }

    public ShoppingCart(String productName, int numberOfProducts, double priceOfASingleProduct) {
        this.productName = productName;
        this.numberOfProducts = numberOfProducts;
        this.priceOfASingleProduct = priceOfASingleProduct;
    }

    public ShoppingCart(String productName, double weightOfProduct, double priceOf100grams) {
        this.productName = productName;
        this.weightOfProduct = weightOfProduct;
        this.priceOf100grams = priceOf100grams;
    }

    public ShoppingCart(int numberOfProducts, double priceOfASingleProduct) {
        this.numberOfProducts = numberOfProducts;
        this.priceOfASingleProduct = priceOfASingleProduct;
    }


    public ShoppingCart(double firstVariable, double secondVariable){
        this.firstVariable = firstVariable;
        this.secondVariable = secondVariable;
    }

    public double getFirstVariable() {
        return firstVariable;
    }

    public void setFirstVariable(double firstVariable) {
        this.firstVariable = firstVariable;
    }

    public double getSecondVariable() {
        return secondVariable;
    }

    public void setSecondVariable(double secondVariable) {
        this.secondVariable = secondVariable;
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

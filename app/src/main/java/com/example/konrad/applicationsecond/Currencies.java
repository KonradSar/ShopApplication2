package com.example.konrad.applicationsecond;

/**
 * Created by Konrad on 01.08.2017.
 */

public class Currencies {
    public String currency;
    public String code;
    public double value;

    public Currencies(String currency, String code, double value) {
        this.currency = currency;
        this.code = code;
        this.value = value;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}

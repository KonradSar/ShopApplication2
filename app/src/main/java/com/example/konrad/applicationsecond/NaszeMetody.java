package com.example.konrad.applicationsecond;

import android.content.Context;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.text.Html;
import android.text.Spanned;
import android.view.MenuItem;
import android.widget.RelativeLayout;


import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Konrad on 03.07.2017.
 */

public class NaszeMetody {


   public static Context AppContext;
   public static void InicjalizacjaNaszeMetody(Context appContext){
      AppContext = appContext;
   }
   public static List<Shopping> shoppingList = new ArrayList<>();
   public static List<ShoppingCart> shoppingCart = new ArrayList<>();
   public static double counterHelper = 0.00;
   public static double finalResult = 0.00;
   public static double resultForAnotherCurrency1 = 0.00;
   public static double resultForAnotherCurrency2 = 0.00;
   public static double resultForAnotherCurrency3 = 0.00;
   public static String currencyLabelNumberOne = "";
   public static String currencyLabelNumberTwo = "";
   public static String currencyLabelNumberThree = "";
   public static boolean addThirdCurrencyLabel = false;
   public static boolean announceLimit = false;
   public static int counterCurrencyTimes = 0;
   // testowa spacja

   public static String kurwajegomac;
   public static int isVisiblePLNTotalCosts = 0;
   public static boolean overrideResultForCurrencyOne = false;
   public static boolean overrideResultForCurrencyTwo = false;
   public static boolean overrideResultForCurrencyThree = false;
   public static final String LOG_TAG = "KonradApp";
   public static int position;
   public static List<Shopping> shoppingListSecond = new ArrayList<>();
   public static List<String> menuCurrencies = new ArrayList<>();
   public static List<Double> exchangeRates = new ArrayList<>();
   public static Map<String, Double> listForRecycler = new HashMap<String, Double>();
   public static Map<Float, String> listForDataPieChart = new HashMap<>();
   public static float[] listBefore = new float[NaszeMetody.shoppingList.size()];
   public static String[] listBefore2 = new String[NaszeMetody.shoppingList.size()];

   @SuppressWarnings("deprecation")
   public static Spanned fromHtml(String html){
      Spanned result;
      if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
         result = Html.fromHtml(html,Html.FROM_HTML_MODE_LEGACY);
      } else {
         result = Html.fromHtml(html);
      }
      return result;
   }


}


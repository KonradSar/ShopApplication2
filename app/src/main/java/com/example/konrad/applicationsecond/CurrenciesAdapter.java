package com.example.konrad.applicationsecond;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

/**
 * Created by Konrad on 27.07.2017.
 */

public class CurrenciesAdapter extends BaseAdapter {
    private Context appContext;
    private List<Currencies> currencies;

    public CurrenciesAdapter(Context context, List<Currencies> currencies) {
        this.appContext = context;
        this.currencies = currencies;
    }

    public CurrenciesAdapter(List<Currencies> currencies) {
        this.currencies = currencies;
    }

    @Override
    public int getCount() {
        return currencies.size();
    }

    @Override
    public Object getItem(int position) {
        return currencies.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = View.inflate(appContext, R.layout.rowsingle, null);
        TextView listViewElement = (TextView) v.findViewById(R.id.textView1111);
        listViewElement.setText(currencies.get(position).getCurrency() + currencies.get(position).getValue());
        return v;
    }


}

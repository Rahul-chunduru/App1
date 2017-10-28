package com.example.mycseapp;

import android.content.Context;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * This adapter is used to set chat messages to list view
 */
public class chatadapter extends ArrayAdapter<Item> {
    private ArrayList<Item> dataSet;
    Context mContext;
    public chatadapter(Context context, ArrayList<Item> itemsArrayList) {

        super(context, R.layout.list_view, itemsArrayList);

        this.mContext = context;
        this.dataSet = itemsArrayList;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        /** 1. Create inflater */
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        /** 2. Get rowView  from inflater */
        View rowView = inflater.inflate(R.layout.list_view, parent, false);
        TextView valueView = (TextView) rowView.findViewById(R.id.message);
        /** add text to text view according to chat image's pos field */
        if(dataSet.get(position).pos)
        {
            valueView.setText(Html.fromHtml("<b>" +  "You" +  "</b>" +"<br>"  + dataSet.get(position).getMessage()));

            valueView.setGravity(Gravity.RIGHT);
        }
        else
        {
            valueView.setText(Html.fromHtml("<b>" + dataSet.get(position).getName()+ "</b>" +  "<br>"  + dataSet.get(position).getMessage()));
            valueView.setGravity(Gravity.LEFT);
        }

        return rowView;
    }


}

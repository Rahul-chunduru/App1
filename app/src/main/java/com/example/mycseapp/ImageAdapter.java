package com.example.mycseapp;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

/**
 * Created by sri on 10/22/17.
 */

public class ImageAdapter extends BaseAdapter {
    private Context mContext;

    /** Keep all Images in array*/
    public static Integer[] mThumbIds = {


    };

    // Constructor
    public ImageAdapter(Context c , Integer[] A){
        mContext = c;
        mThumbIds = A ;
    }
    ////////functions in event adapter
    @Override
    public int getCount() {
        return mThumbIds.length;
    }

    @Override
    public Object getItem(int position) {
        return mThumbIds[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    /**
     *  getView controls the layout of the grid view
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView = new ImageView(mContext);
        imageView.setImageResource(mThumbIds[position]);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(new GridView.LayoutParams(200, 250));
        return imageView;
    }
}

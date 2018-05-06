package com.dangjang.dj2015.cart;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * Created by Tacademy on 2015-11-26.
 */
public class CustomSpinnerAdapter extends ArrayAdapter<String>{
    public CustomSpinnerAdapter(Context context, int resource) {
        super(context, resource);
    }

    public CustomSpinnerAdapter(Context context, int resource, List<String> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}

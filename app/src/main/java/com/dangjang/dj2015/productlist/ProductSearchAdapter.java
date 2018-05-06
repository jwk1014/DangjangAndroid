package com.dangjang.dj2015.productlist;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.dangjang.dj2015.main.AddressSearchView;
import com.dangjang.dj2015.publicdata.AddressInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tacademy on 2015-11-02.
 */
public class ProductSearchAdapter extends ArrayAdapter<String> {
    private boolean visible = false;
    public ProductSearchAdapter(Context context, int resource, List<String> list){
        super(context,resource,list);
    }
    public ProductSearchAdapter(Context context, int resource){
        super(context,resource,new ArrayList<String>());
    }
    public void changeAll(List<String> list){
        clear();
        addAll(list);
        notifyDataSetChanged();
    }
    public void setDeleteVisible(boolean b){
        visible = b;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ProductSearchView v;
        if(convertView != null)
            v = (ProductSearchView)convertView;
        else
            v = new ProductSearchView(getContext());
        v.setData(getItem(position));
        v.setDeleteVisible(visible);
        v.setOnCustomClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null)
                    onItemClickListener.onItemClick(position);
            }
        });
        v.setOnDeleteClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemDeleteClickListener != null)
                    onItemDeleteClickListener.onItemDeleteClick(position);
            }
        });
        return v;
    }

    public interface OnItemClickListener{
        void onItemClick(int index);
    }

    OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemDeleteClickListener{
        void onItemDeleteClick(int index);
    }

    OnItemDeleteClickListener onItemDeleteClickListener;

    public void setOnItemDeleteClickListener(OnItemDeleteClickListener onItemDeleteClickListener) {
        this.onItemDeleteClickListener = onItemDeleteClickListener;
    }
}

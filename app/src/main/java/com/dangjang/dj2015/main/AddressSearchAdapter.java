package com.dangjang.dj2015.main;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.dangjang.dj2015.publicdata.AddressInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tacademy on 2015-11-02.
 */
public class AddressSearchAdapter  extends ArrayAdapter<AddressInfo> {
    private boolean visible = false;
    public AddressSearchAdapter(Context context, int resource, List<AddressInfo> list){
        super(context,resource,list);
    }
    public AddressSearchAdapter(Context context, int resource){
        super(context,resource,new ArrayList<AddressInfo>());
    }
    public void changeAll(List<AddressInfo> list){
        clear();
        addAll(list);
        notifyDataSetChanged();
    }
    public void setDeleteVisible(boolean b){
        visible = b;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        AddressSearchView v;
        if(convertView != null)
            v = (AddressSearchView)convertView;
        else
            v = new AddressSearchView(getContext());
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

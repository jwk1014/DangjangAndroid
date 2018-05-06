package com.dangjang.dj2015.categoryselect;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.dangjang.dj2015.productdetail.MartDetailActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tacademy on 2015-10-23.
 */
public class CategorySelectAdapter extends BaseAdapter {
    List<CategorySelectChildItem> items = new ArrayList<>();
    public static final int VIEW_TYPE_COUNT = 2;

    public static final int TYPE_INDEX_MART = 0;
    public static final int TYPE_INDEX_STRING = 1;
    public int currentType = -1;

    public ListView listView;

    public void dataChange(List<CategorySelectChildItem> items){
        this.items.clear();
        if(items.get(items.size()-1) instanceof CategorySelectChildMartItem) {
            currentType = TYPE_INDEX_MART;
            //this.items.add(new CategorySelectChildStringItem("전체"));
        }else
            currentType = TYPE_INDEX_STRING;
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    public void clear(){
        this.items.clear();
        notifyDataSetChanged();
    }

    public void setListView(ListView listView) {
        this.listView = listView;
    }

    public int indexOf(Object o){
        return items.indexOf(o);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return VIEW_TYPE_COUNT;
    }

    @Override
    public int getItemViewType(int position) {
        return getItemViewType(items.get(position));
    }

    public int getItemViewType(Object o){
        if(o instanceof CategorySelectChildMartItem)
            return TYPE_INDEX_MART;
        else
            return TYPE_INDEX_STRING;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        switch (getItemViewType(position)){
            case TYPE_INDEX_MART: {
                    CategorySelectChildMartView view;
                    if (convertView != null && convertView instanceof CategorySelectChildMartView) {
                        view = (CategorySelectChildMartView)convertView;
                    } else {
                        view = new CategorySelectChildMartView(parent.getContext());
                        view.setOnMartInfoClickListener(new CategorySelectChildMartView.OnMartInfoClickListener() {
                            @Override
                            public void onMartInfoClick(int mart_id) {
                                Intent intent = new Intent(parent.getContext(), MartDetailActivity.class);
                                intent.putExtra("mart_id", mart_id);
                                parent.getContext().startActivity(intent);
                            }
                        });
                        view.setClickable(true);
                    }
                view.setData((CategorySelectChildMartItem)items.get(position));
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (listView != null && listView.getOnItemClickListener() != null) {
                                listView.getOnItemClickListener().onItemClick(null, null, position,0);
                            }
                        }
                    });
                    return view;
                }
            default: {
                CategorySelectChildStringView view;
                if (convertView != null && convertView instanceof CategorySelectChildStringView) {
                    view = (CategorySelectChildStringView) convertView;
                } else {
                    view = new CategorySelectChildStringView(parent.getContext());
                }
                CategorySelectChildStringItem item = (CategorySelectChildStringItem) items.get(position);
                view.setData(item);
                if(currentType == TYPE_INDEX_MART) {
                    view.setAlignLeft();
                    view.setTextColorGreen();
                } else if(item.string.equals("전체")) {
                    view.setTextColorGreen();
                }
                return view;
            }
        }
    }
}

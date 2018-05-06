package com.dangjang.dj2015.accodion;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tacademy on 2015-10-27.
 */
public class AccordionAdapter extends BaseExpandableListAdapter {
    List<AccordionItem> items;
    LayoutInflater inflater;

    public AccordionAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        items = new ArrayList<>();
    }

    public void changeAll(List<AccordionItem> items){
        this.items.clear();
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    @Override
    public int getGroupCount() {
        return items.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return (items.get(groupPosition).getContent() == null)?0:1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return items.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return items.get(groupPosition).getContent();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return (long)groupPosition << 32 | 0xFFFFFFFF;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return (long)groupPosition << 32 | childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        AccordionTitleView v;
        if (convertView != null) {
            v = (AccordionTitleView)convertView;
        } else {
            v = new AccordionTitleView(parent.getContext());
        }
        v.setData(items.get(groupPosition).getTitle(),isExpanded);
        return v;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        AccordionContentView v;
        if (convertView != null) {
            v = (AccordionContentView)convertView;
        } else {
            v = new AccordionContentView(parent.getContext());
        }
        v.setData(items.get(groupPosition).getContent());

        return v;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}

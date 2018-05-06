package com.dangjang.dj2015.cart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import com.dangjang.dj2015.parentclass.AppFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tacademy on 2015-10-27.
 */
public class OrderHistoryListAdapter extends BaseExpandableListAdapter {
    List<CartItem> items;
    LayoutInflater inflater;
    AppFragment fragment;

    public OrderHistoryListAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        items = new ArrayList<>();
    }
    public OrderHistoryListAdapter(AppFragment fragment){
        this(fragment.getActivity());
        this.fragment = fragment;
    }

    public void changeAll(List<CartItem> items){
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
        return (items.get(groupPosition).getOrderItems().size() > 0)?items.get(groupPosition).getOrderItems().size()+1:0;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return items.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        if(childPosition < items.get(groupPosition).getOrderItems().size())
            return items.get(groupPosition).getOrderItems().get(childPosition);
        else
            return null;
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
        View v;
        if (convertView != null) {
            ((OrderHeaderView) convertView).setData((CartItem) getGroup(groupPosition));
            v = convertView;
        } else {
            OrderHeaderView headerView = new OrderHeaderView(inflater.getContext());
            headerView.setData((CartItem)getGroup(groupPosition));
            v = headerView;
        }
        ((OrderHeaderView) v).indicateExpand(isExpanded);
        return v;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View v;
        if (convertView != null
                && ((childPosition < ((CartItem)getGroup(groupPosition)).getOrderItems().size() && convertView instanceof CartListItemView) ||
                (childPosition == ((CartItem)getGroup(groupPosition)).getOrderItems().size() && convertView instanceof CartListItemBottomView))) {
            v = convertView;
            if(childPosition < ((CartItem)getGroup(groupPosition)).getOrderItems().size())
                ((CartListItemView)v).setData((OrderItem)getChild(groupPosition,childPosition));
            else if(childPosition == ((CartItem)getGroup(groupPosition)).getOrderItems().size()) {
                CartItem cartItem = (CartItem)getGroup(groupPosition);
                ((CartListItemBottomView) v).setData(cartItem.getId(),cartItem.getMartItem().getDeliveryCost(),cartItem.getTotalcost(),cartItem.getMartItem().getId(),cartItem.getMartItem().getName());
            }
        } else {
            if(childPosition < ((CartItem)getGroup(groupPosition)).getOrderItems().size()) {
                CartListItemView cartListItemView = new CartListItemView(inflater.getContext(),true);
                cartListItemView.setData((OrderItem)getChild(groupPosition,childPosition));
                v = cartListItemView;
            }else {
                CartListItemBottomView cartListItemBottomView = new CartListItemBottomView(inflater.getContext(),true);
                CartItem cartItem = (CartItem)getGroup(groupPosition);
                cartListItemBottomView.setData(cartItem.getId(),cartItem.getMartItem().getDeliveryCost(),cartItem.getTotalcost(),cartItem.getMartItem().getId(),cartItem.getMartItem().getName());
                v = cartListItemBottomView;
            }
        }

        return v;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}

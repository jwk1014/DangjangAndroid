package com.dangjang.dj2015.cart;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by KJW on 2015-11-14.
 */
public class CartListAdapter extends ArrayAdapter<CartListAdapter.Data> {
    public final static int TYPE_TOP = 0;
    public final static int TYPE_ITEM = 1;
    public final static int TYPE_BOTTOM = 2;
    private List<CartItem> list;

    public CartListAdapter(Context context, int resource){
        super(context, resource, new ArrayList<Data>());
    }

    class Data{
        public int type = -1;
        public int position = -1;
        public int sub_position = -1;
        public Data(int type,int position){
            this.type = type;
            this.position = position;
        }
        public Data(int type,int position,int sub_position){
            this(type,position);
            this.sub_position = sub_position;
        }
    }

    public int getListCount(){
        if(list != null)
            return list.size();
        else
            return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).type;
    }

    public void changeAll(List<CartItem> list){
        this.list = list;
        clear();
        for(int i=0;i<list.size();i++){
            add(new Data(TYPE_TOP,i));
            for(int j=0;j<list.get(i).getOrderItems().size();j++){
                add(new Data(TYPE_ITEM,i,j));
            }
            add(new Data(TYPE_BOTTOM,i));
        }
        notifyDataSetChanged();
    }

    public CartItem getCartitem(int mart_id){
        for(CartItem cartItem:list){
            if(cartItem.getMartItem().getId() == mart_id)
                return cartItem;
        }
        return null;
    }

    public CartItem getItemCart(int position) {
        return list.get(getItem(position).position);
    }

    public OrderItem getItemOrder(int position){
        return list.get(getItem(position).position).getOrderItems().get(getItem(position).sub_position);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = null;
        if(convertView != null && isCorrectView(convertView, position))
            v = convertView;
        else {
            switch (getItem(position).type){
                case TYPE_TOP:
                    CartListItemTopView cartListItemTopView = new CartListItemTopView(getContext());
                    if(onMartDetailListener != null)
                        cartListItemTopView.setOnMartDetailListener(onMartDetailListener);
                    v = cartListItemTopView;
                    break;
                case TYPE_ITEM:
                    CartListItemView cartListItemView = new CartListItemView(getContext());
                    cartListItemView.setOnCustomClickListener(onCustomClickListener);
                    cartListItemView.setOnOrderDeleteListener(new CartListItemView.OnOrderDeleteListener() {
                        @Override
                        public void onOrderDelete(OrderItem orderItem) {
                            if (onOrderDeleteListener != null) {
                                CARTBREAK:
                                for (CartItem tempcart : list) {
                                    for (OrderItem temporder : tempcart.getOrderItems()) {
                                        if (temporder.equals(orderItem)) {
                                            onOrderDeleteListener.onOrderDelete(temporder);
                                            break CARTBREAK;
                                        }
                                    }
                                }
                            }
                        }
                    });
                    v = cartListItemView;
                    break;
                case TYPE_BOTTOM:
                    CartListItemBottomView cartListItemBottomView = new CartListItemBottomView(getContext());
                    if(onDeliveryRequestListener != null)
                        cartListItemBottomView.setOnDeliveryRequestListener(onDeliveryRequestListener);
                    if(onCartDeleteListener != null)
                        cartListItemBottomView.setOnCartDeleteListener(onCartDeleteListener);
                    v = cartListItemBottomView;
                    break;
            }
//            if (){
//                v = new ProductCategoryView(getContext());
//                v.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if (onCustomItemClickListener != null)
//                            onCustomItemClickListener.onItemClick(v, getIndex(position), getItem(position).categorySelectChildStringItem);
//                    }
//                });
//            }else {
//                ProductListChildRowView productListChildRowView = new ProductListChildRowView(getContext());
//                productListChildRowView.setOnRowItemClickListener(new ProductListChildRowView.OnRowItemClickListener() {
//                    @Override
//                    public void onRowItemClick(View v, int index) {
//                        if(onCustomItemClickListener != null) {
//                            int indexs[] = getIndex(position);
//                            indexs[1] += index;
//                            onCustomItemClickListener.onItemClick(v, indexs, getItem(position).martProductItems[index]);
//                        }
//                    }
//                });
//                v = productListChildRowView;
//            }
        }
        switch (getItem(position).type) {
            case TYPE_TOP:
                if(v instanceof CartListItemTopView) {
                    CartListItemTopView cartListItemTopView = (CartListItemTopView) v;
                    cartListItemTopView.setData(getItemCart(position).getMartItem());
                }
                break;
            case TYPE_ITEM:
                if(v instanceof CartListItemView) {
                    CartListItemView cartListItemView = (CartListItemView) v;
                    cartListItemView.setData(getItemOrder(position));
                }
                break;
            case TYPE_BOTTOM:
                if(v instanceof CartListItemBottomView) {
                    CartListItemBottomView cartListItemBottomView = (CartListItemBottomView) v;
                    CartItem cartItem = getItemCart(position);
                    cartListItemBottomView.setData(cartItem.getId(), cartItem.getMartItem().getDeliveryCost(), cartItem.getTotalcost(), cartItem.getMartItem().getId(), cartItem.getMartItem().getName());
                }
                break;
        }
        return v;
    }

    boolean isCorrectView(View convertView, int position){
        Data data = getItem(position);
        switch (data.type){
            case TYPE_TOP:
                if(convertView instanceof CartListItemTopView)
                    return true;
                break;
            case TYPE_ITEM:
                if(convertView instanceof CartListItemView)
                    return true;
                break;
            case TYPE_BOTTOM:
                if(convertView instanceof CartListItemBottomView)
                    return true;
                break;
        }
        return false;
    }
//
//    private void viewSetData(View convertView, int position){
//        if(convertView instanceof ProductCategoryView)
//            ((ProductCategoryView)convertView).setData(getItem(position).categorySelectChildStringItem);
//        else if(convertView instanceof ProductListChildRowView)
//            ((ProductListChildRowView)convertView).setData(getItem(position).martProductItems);
//    }
//    private int[] getIndex(int position){
//        int index[] = new int[2];
//        index[0] = -1;
//        index[1] = -1;
//        for(int i=0;i<=position;i++) {
//            if (getItem(i).categorySelectChildStringItem != null) {
//                index[0]++;
//                index[1] = -1;
//            }else{
//                if(index[1] == -1)
//                    index[1]=0;
//                else
//                    index[1]+=3;
//            }
//        }
//        return index;
//    }

    CartListItemTopView.OnMartDetailListener onMartDetailListener;

    public void setOnMartDetailListener(CartListItemTopView.OnMartDetailListener onMartDetailListener) {
        this.onMartDetailListener = onMartDetailListener;
    }

    public interface OnOrderDeleteListener{
        void onOrderDelete(OrderItem order);
    }

    OnOrderDeleteListener onOrderDeleteListener;

    public void setOnOrderDeleteListener(OnOrderDeleteListener onOrderDeleteListener) {
        this.onOrderDeleteListener = onOrderDeleteListener;
    }

    CartListItemView.OnCustomClickListener onCustomClickListener;

    public void setOnCustomItemClickListener(CartListItemView.OnCustomClickListener onCustomClickListener){
        this.onCustomClickListener = onCustomClickListener;
    }

    CartListItemBottomView.OnDeliveryRequestListener onDeliveryRequestListener;

    public void setOnDeliveryRequestListener(CartListItemBottomView.OnDeliveryRequestListener onDeliveryRequestListener) {
        this.onDeliveryRequestListener = onDeliveryRequestListener;
    }

    CartListItemBottomView.OnCartDeleteListener onCartDeleteListener;

    public void setOnCartDeleteListener(CartListItemBottomView.OnCartDeleteListener onCartDeleteListener) {
        this.onCartDeleteListener = onCartDeleteListener;
    }
}

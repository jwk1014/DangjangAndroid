package com.dangjang.dj2015.cart;


import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.dangjang.dj2015.Manager.NetworkManager;
import com.dangjang.dj2015.Manager.StorageManager;
import com.dangjang.dj2015.R;
import com.dangjang.dj2015.dialog.AuthorizationDialogFragment;
import com.dangjang.dj2015.jsonclass.response.OrderPrevious;
import com.dangjang.dj2015.jsonclass.response.Result;
import com.dangjang.dj2015.jsonclass.response.Sms;
import com.dangjang.dj2015.main.MainActivity;
import com.dangjang.dj2015.parentclass.AppFragment;
import com.dangjang.dj2015.publicdata.MartItem;
import com.dangjang.dj2015.publicdata.MartProductItem;
import com.dangjang.dj2015.publicdata.ProductItem;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrderHistoryFragment extends AppFragment {
    OrderHistoryListAdapter adapter;

    public final static int NW_ORDERPREVIOUS = 1;

    public OrderHistoryFragment() {
        fragmentTitleStringResourceId = R.string.orderhistoryfragment_title;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_order_history, container, false);
        ExpandableListView listView = (ExpandableListView)v.findViewById(R.id.f_orderhistory_cartitem_expandablelistview);
        listView.setAdapter(adapter = new OrderHistoryListAdapter(getActivity()));
        NetworkManager.getInstance().orderPrevious(NW_ORDERPREVIOUS,this);
//        List<CartItem> list = new ArrayList<>();
//        CartItem cartItem = new CartItem();
//        cartItem.setId(0);
//        List<OrderItem> orderItems = new ArrayList<>();
//        OrderItem orderItem = new OrderItem();
//        MartProductItem martProductItem = new MartProductItem();
//        ProductItem productItem = new ProductItem();
//        productItem.setName("돼지삼겹살_100G_D)웰빙특화\n/ 100g");
//        MartItem martItem = new MartItem();
//        martItem.setId(0);
//        martItem.setName("임시");
//        martItem.setDeliveryCost(1);
//        martProductItem.setProductItem(productItem);
//        martProductItem.setPrice(3580);
//        orderItem.setMartProductItem(martProductItem);
//        orderItem.setCount(6);
//        orderItems.add(orderItem);
//        cartItem.setOrderItems(orderItems);
//        cartItem.setTotalcost(1);
//        cartItem.setMartItem(martItem);
//        list.add(cartItem);
//
//        cartItem = new CartItem();
//        orderItems = new ArrayList<>();
//        orderItem = new OrderItem();
//        martProductItem = new MartProductItem();
//        productItem = new ProductItem();
//        productItem.setName("탕수육\n/ 1접시");
//        martItem = new MartItem();
//        martItem.setId(0);
//        martItem.setName("임시");
//        martItem.setDeliveryCost(1);
//        martProductItem.setProductItem(productItem);
//        martProductItem.setPrice(3000);
//        orderItem.setMartProductItem(martProductItem);
//        orderItem.setCount(1);
//        orderItems.add(orderItem);
//        orderItem = new OrderItem();
//        martProductItem = new MartProductItem();
//        productItem = new ProductItem();
//        productItem.setName("딸기\n/ 800g");
//        martItem = new MartItem();
//        martItem.setId(0);
//        martItem.setName("임시");
//        martItem.setDeliveryCost(1);
//        martProductItem.setProductItem(productItem);
//        martProductItem.setPrice(10000);
//        orderItem.setMartProductItem(martProductItem);
//        orderItem.setCount(1);
//        orderItems.add(orderItem);
//        cartItem.setOrderItems(orderItems);
//        cartItem.setTotalcost(1);
//        cartItem.setMartItem(martItem);
//        list.add(cartItem);
//
//        adapter.changeAll(list);
        return v;
    }

    @Override
    public void onResponse(int KEY, boolean success, Object object) {
        switch (KEY){

            case NW_ORDERPREVIOUS:
                if(success && object != null){
                    List<OrderPrevious> orderPrevious = (List<OrderPrevious>) object;
                    List<CartItem> list = new ArrayList<>();
                    for(OrderPrevious order : orderPrevious){
                        CartItem cartItem = new CartItem();
                        cartItem.setOrderDate(order.order_date);
                        MartItem martItem = new MartItem();
                        martItem.setId(order.mart_id);
                        martItem.setName(order.mart_name);
                        cartItem.setMartItem(martItem);
                        List<OrderItem> orderItems = new ArrayList<>();
                        for(OrderPrevious.ProList proList:order.pro_list){
                            OrderItem orderItem = new OrderItem();
//                            orderItem.setId();
                            MartProductItem martProductItem = new MartProductItem();
                            ProductItem productItem = new ProductItem();
                            productItem.setId(proList.pro_id);
                            productItem.setName(proList.pro_name);
                            productItem.setImg_url(proList.pro_image);
                            martProductItem.setProductItem(productItem);
                            martProductItem.setPrice(proList.pro_price);
                            //martProductItem.setSalePrice(proList.pro_price);
                            martProductItem.setMartItem(martItem);
                            orderItem.setCount(proList.pro_count);
                            orderItem.setMartProductItem(martProductItem);
                            orderItems.add(orderItem);
                        }
                        cartItem.setOrderItems(orderItems);
                        cartItem.setTotalcost(order.total_price);
                        list.add(cartItem);
                    }
                    adapter.changeAll(list);
                }
                break;

            default:
                super.onResponse(KEY,false,object);

        }
    }
}

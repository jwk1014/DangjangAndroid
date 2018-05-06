package com.dangjang.dj2015.cart;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.dangjang.dj2015.Manager.AppUtil;
import com.dangjang.dj2015.Manager.NetworkManager;
import com.dangjang.dj2015.Manager.ResultCode;
import com.dangjang.dj2015.R;
import com.dangjang.dj2015.dialog.CustomDialogFragment;
import com.dangjang.dj2015.jsonclass.response.Cart;
import com.dangjang.dj2015.jsonclass.response.Result;
import com.dangjang.dj2015.parentclass.AppActivity;
import com.dangjang.dj2015.productdetail.MartDetailActivity;
import com.dangjang.dj2015.productdetail.ProductDetailActivity;
import com.dangjang.dj2015.publicdata.MartItem;
import com.dangjang.dj2015.publicdata.MartProductItem;
import com.dangjang.dj2015.publicdata.ProductItem;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppActivity {
    CartListAdapter adapter;
    ListView listView;
    TextView empty_textview;

    public final static int NW_CART = 1;
    public final static int NW_CARTDELETE = 2;
    public final static int NW_ORDERDELETE = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_cart);

        setTitle(AppUtil.getXmlString(R.string.cartfragment_title));
        setSnackBarContainer(R.id.a_cart_main_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        empty_textview = (TextView)findViewById(R.id.f_cart_textview);

        listView = (ListView)findViewById(R.id.f_cart_listview);
        listView.setAdapter(adapter = new CartListAdapter(this, R.layout.view_cartlistitem));

        adapter.setOnMartDetailListener(new CartListItemTopView.OnMartDetailListener() {
            @Override
            public void onMartDetail(int mart_id) {
                Intent intent = new Intent(CartActivity.this, MartDetailActivity.class);
                intent.putExtra("mart_id", mart_id);
                startActivity(intent);
            }
        });

        adapter.setOnCustomItemClickListener(new CartListItemView.OnCustomClickListener() {
            @Override
            public void onClick(OrderItem orderItem) {
                Intent intent = new Intent(CartActivity.this, ProductDetailActivity.class);
                intent.putExtra("item", orderItem.getMartProductItem());
                intent.putExtra("order_id", orderItem.getId());
                intent.putExtra("count", orderItem.getCount());
                startActivityForResult(intent, 2);
            }
        });

        adapter.setOnOrderDeleteListener(new CartListAdapter.OnOrderDeleteListener() {
            @Override
            public void onOrderDelete(final OrderItem order) {
                CustomDialogFragment customDialogFragment = new CustomDialogFragment();
                customDialogFragment.setContent(
                        order.getMartProductItem().getMartItem().getName() + "의 " +
                                order.getMartProductItem().getProductItem().getName() + " 품목을 삭제하시겠습니까?");
                customDialogFragment.setOnSubmitClickListener(new CustomDialogFragment.OnSubmitClickListener() {
                    @Override
                    public void onSubmit() {
                        //NetworkManager.getInstance().cartDelete(order.getId(), NW_ORDERDELETE, CartActivity.this);
                    }
                });
                customDialogFragment.setCancelVisible(true);
                customDialogFragment.show(getFragmentManager(), CustomDialogFragment.class.getName());
            }
        });

        adapter.setOnDeliveryRequestListener(new CartListItemBottomView.OnDeliveryRequestListener() {
            @Override
            public void onDeliveryRequest(int cart_id, int total_price, int mart_id, String martname) {
                Intent intent = new Intent(CartActivity.this, OrderActivity.class);
                intent.putExtra(OrderActivity.KEY_CARTITEM, adapter.getCartitem(mart_id));
                startActivityForResult(intent, 1);
            }
        });

        adapter.setOnCartDeleteListener(new CartListItemBottomView.OnCartDeleteListener() {
            @Override
            public void onCartDelete(final int cart_id,final int mart_id,String martName) {
                CustomDialogFragment customDialogFragment = new CustomDialogFragment();
                customDialogFragment.setContent(martName + "의 모든 품목을 삭제하시겠습니까?");
                customDialogFragment.setOnSubmitClickListener(new CustomDialogFragment.OnSubmitClickListener() {
                    @Override
                    public void onSubmit() {
                        //NetworkManager.getInstance().cartMartDelete(cart_id, mart_id, NW_CARTDELETE, CartActivity.this);
                    }
                });
                customDialogFragment.setCancelVisible(true);
                customDialogFragment.show(getFragmentManager(), CustomDialogFragment.class.getName());
            }
        });

        List<CartItem> list = new ArrayList<>();
            CartItem cartItem = new CartItem();
            cartItem.setId(1);
            MartItem martItem = new MartItem();
            martItem.setId(3);
            martItem.setName("테스트마트");
            martItem.setDeliveryCost(3000);
            cartItem.setMartItem(martItem);
            List<OrderItem> orderItems = new ArrayList<>();
            OrderItem orderItem = new OrderItem();
            orderItem.setId(1);
            MartProductItem martProductItem = new MartProductItem();
            ProductItem productItem = new ProductItem();
            productItem.setId(1);
            productItem.setName("가지");
            productItem.setImg_url("http://133.130.102.92/mobile/resources/images/A/A_A/AA0000001.JPG");
            martProductItem.setProductItem(productItem);
            martProductItem.setPrice(87500);
            martProductItem.setMartItem(martItem);
            orderItem.setCount(1);
            orderItem.setMartProductItem(martProductItem);
            orderItems.add(orderItem);
            cartItem.setOrderItems(orderItems);
            cartItem.setTotalcost(90500);
            list.add(cartItem);
        adapter.changeAll(list);

        if(adapter.getListCount() > 0)
                empty_textview.setVisibility(View.INVISIBLE);
        else
                empty_textview.setVisibility(View.VISIBLE);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            overridePendingTransition(R.anim.left_in,R.anim.right_out);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode){
            case ResultCode.RESULT_OK:
                NetworkManager.getInstance().cart(NW_CART, this);
                break;
            case ResultCode.RESULT_FAIL:
                break;
            case ResultCode.RESULT_CANCEL:
                break;
        }
    }

    @Override
    public void onResponse(int KEY, boolean success, Object object) {
        switch (KEY){
            case NW_CART:
                if(success && object != null)  {
                    List<Cart> carts = (List<Cart>)object;
                    if(carts.size() > 0) {
                        Cart cart = carts.get(0);
                        List<CartItem> list = new ArrayList<>();
                        for (Cart.CartList cartList : cart.cart_list) {
                            CartItem cartItem = new CartItem();
                            cartItem.setId(cart.cart_id);
                            MartItem martItem = new MartItem();
                            martItem.setId(cartList.mart_id);
                            martItem.setName(cartList.mart_name);
                            martItem.setDeliveryCost(cartList.mart_pee);
                            cartItem.setMartItem(martItem);
                            List<OrderItem> orderItems = new ArrayList<>();
                            for (Cart.CartList.ProList proList : cartList.pro_list) {
                                OrderItem orderItem = new OrderItem();
                                orderItem.setId(proList.cp_id);
                                MartProductItem martProductItem = new MartProductItem();
                                ProductItem productItem = new ProductItem();
                                productItem.setId(proList.pro_id);
                                productItem.setName(proList.pro_name);
                                productItem.setImg_url(proList.pro_image);
                                martProductItem.setProductItem(productItem);
                                martProductItem.setPrice(proList.pro_price);
                                martProductItem.setMartItem(martItem);
                                orderItem.setCount(proList.pro_count);
                                orderItem.setMartProductItem(martProductItem);
                                orderItems.add(orderItem);
                            }
                            cartItem.setOrderItems(orderItems);
                            cartItem.setTotalcost(cartList.total_price);
                            list.add(cartItem);
                        }
                        adapter.changeAll(list);
                    }else {
                        adapter.changeAll(new ArrayList<CartItem>());
                    }

                    if(adapter.getListCount() > 0)
                        empty_textview.setVisibility(View.INVISIBLE);
                    else
                        empty_textview.setVisibility(View.VISIBLE);
                }
                break;
            case NW_CARTDELETE:
                if(success && object != null) {
                    Result result = (Result)object;
                    if(result.result == 1){
                        NetworkManager.getInstance().cart(NW_CART, this);
                    }
                }
                break;
            case NW_ORDERDELETE:
                if(success && object != null) {
                    Result result = (Result)object;
                    if(result.result == 1){
                        NetworkManager.getInstance().cart(NW_CART, this);
                    }
                }
                break;
            default:
                super.onResponse(KEY,false,object);
        }
    }
}

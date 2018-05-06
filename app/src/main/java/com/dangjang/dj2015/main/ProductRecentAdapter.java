package com.dangjang.dj2015.main;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.dangjang.dj2015.Manager.AppUtil;
import com.dangjang.dj2015.R;
import com.dangjang.dj2015.publicdata.MartProductItem;
import com.dangjang.dj2015.publicdata.ProductItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by KJW on 2015-11-29.
 */
public class ProductRecentAdapter extends RecyclerView.Adapter{
    private List<MartProductItem> items;
    Context context;
    public ProductRecentAdapter(Context context){
        items = new ArrayList<>();
        this.context = context;
    }
    public void clear(){
        items.clear();
        notifyDataSetChanged();
    }
    public void add(MartProductItem martProductItem){
        items.add(martProductItem);
        notifyDataSetChanged();
    }
    @Override
    public ProductRecentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_product_recent,parent,false);
        itemView.getLayoutParams().width = AppUtil.getDp(R.dimen.dimen_120dp);
        return new ProductRecentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder,final int position) {
        MartProductItem martProductItem = items.get(position);
        ProductRecentViewHolder productRecentViewHolder = (ProductRecentViewHolder) holder;
        String img_url = martProductItem.getProductItem().getImg_url();
        if(img_url != null && img_url.length() > 0)
            AppUtil.loadImage(context, img_url, 0, productRecentViewHolder.productImage);
        productRecentViewHolder.martname.setText(martProductItem.getMartItem().getName());
        if(martProductItem.getSalePrice() == 0)
            productRecentViewHolder.price.setText(martProductItem.getPrice()+"원");
        else{
            productRecentViewHolder.beforePrice.setText(martProductItem.getPrice()+"원");
            productRecentViewHolder.price.setText(martProductItem.getSalePrice()+"원");
        }
        ProductItem productItem = martProductItem.getProductItem();
        if(productItem.getUnit() == null || productItem.getUnit().length() == 0)
            productRecentViewHolder.nameInfo.setText(productItem.getName());
        else{
            productRecentViewHolder.nameInfo.setText(Html.fromHtml(productItem.getName() + " <font color='#999999'>/ " + productItem.getUnit() + "</font>"));
        }

        productRecentViewHolder.mainlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener != null)
                    onItemClickListener.onItemClick(items.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ProductRecentViewHolder extends RecyclerView.ViewHolder{
        FrameLayout mainlayout;
        ImageView productImage;
        TextView beforePrice,price,nameInfo,martname,off;

        public ProductRecentViewHolder(View itemView) {
            super(itemView);
            mainlayout = (FrameLayout)itemView.findViewById(R.id.v_product_recent_main_layout);
            martname = (TextView)itemView.findViewById(R.id.v_productchilditem_mart_textview);
            productImage = (ImageView)itemView.findViewById(R.id.v_productchilditem_productimage_imageview);
            off = (TextView)itemView.findViewById(R.id.v_productchilditem_off_textview);
            beforePrice = (TextView)itemView.findViewById(R.id.v_productchilditem_beforeprice_textview);
            price = (TextView)itemView.findViewById(R.id.v_productchilditem_price_textview);
            nameInfo = (TextView)itemView.findViewById(R.id.v_productchilditem_nameinfo_textview);
        }
    }

    public interface OnItemClickListener{
        void onItemClick(MartProductItem martProductItem);
    }

    OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}

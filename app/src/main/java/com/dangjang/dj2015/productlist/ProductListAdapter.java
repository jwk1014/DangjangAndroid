package com.dangjang.dj2015.productlist;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.dangjang.dj2015.Manager.AppUtil;
import com.dangjang.dj2015.R;
import com.dangjang.dj2015.categoryselect.CategorySelectChildStringItem;
import com.dangjang.dj2015.publicdata.MartProductItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by KJW on 2015-11-14.
 */
public class ProductListAdapter extends ArrayAdapter<ProductListAdapter.Data> {
    public final static int TYPE_TITLE = 0;
    public final static int TYPE_ITEM = 1;

    class Data{
        CategorySelectChildStringItem categorySelectChildStringItem;
        MartProductItem martProductItems[];
        public Data(CategorySelectChildStringItem categorySelectChildStringItem){
            this.categorySelectChildStringItem = categorySelectChildStringItem;
        }
        public Data(MartProductItem[] martProductItems){
            this.martProductItems = martProductItems;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        Data data = getItem(position);
        if(data.categorySelectChildStringItem != null)
            return TYPE_TITLE;
        else
            return  TYPE_ITEM;
    }

    public ProductListAdapter(Context context, int resource){
        super(context, resource, new ArrayList<Data>());
    }

    public void changeAll(List<ProductListItem> list){
        clear();
        for(ProductListItem productListItem:list){
            add(new Data(productListItem.getCategoryName()));
            int size = productListItem.getProductItems().size();
            for(int i=0;i<size;i+=3) {
                MartProductItem martProductItems[] = new MartProductItem[(size-i >= 3)?3:size-i];
                for(int j=i;j<i+3&&j<size;j++)
                    martProductItems[j-i] = productListItem.getProductItems().get(j);
                add(new Data(martProductItems));
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v;
        if(convertView != null && isCorrectView(convertView, position))
            v = convertView;
        else {
            if (getItem(position).categorySelectChildStringItem != null){
                v = new ProductCategoryView(getContext());
            }else {
                ProductListChildRowView productListChildRowView = new ProductListChildRowView(getContext());
                v = productListChildRowView;
            }
        }
        viewSetData(v, position);
        if(getItem(position).categorySelectChildStringItem == null){
            ProductListChildRowView productListChildRowView = (ProductListChildRowView)v;
            int main_top = 0;
            int main_bottom = 0;
            int contents_top = 0;
            if(getItem(position-1).categorySelectChildStringItem != null){
                main_top = AppUtil.getDp(R.dimen.margin_6dp);
                contents_top = AppUtil.getDp(R.dimen.margin_1dp);
            }
            if(position == getCount()-1 || getItem(position+1).categorySelectChildStringItem != null){
                main_bottom = AppUtil.getDp(R.dimen.margin_6dp);
            }
            productListChildRowView.setMainLayoutMargin(main_top, main_bottom);
            productListChildRowView.setContentsPadding(contents_top);
            productListChildRowView.setOnRowItemClickListener(new ProductListChildRowView.OnRowItemClickListener() {
                @Override
                public void onRowItemClick(View v, int index) {
                    if (onCustomItemClickListener != null) {
                        int indexs[] = getIndex(position);
                        indexs[1] += index;
                        for (MartProductItem martProductItem : getItem(position).martProductItems) {
                            Log.i("martproductlist", martProductItem.getProductItem().getName() + "/" + martProductItem.getPrice() + "/" + martProductItem.getMartItem().getName());
                        }
                        onCustomItemClickListener.onItemClick(v, indexs[0], indexs[1], getItem(position).martProductItems[index]);
                    }
                }
            });
        }else{
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onCustomItemClickListener != null)
                        onCustomItemClickListener.onItemClick(v, getIndex(position)[0], -1, getItem(position).categorySelectChildStringItem);
                }
            });
        }
        return v;
    }

    boolean isCorrectView(View convertView, int position){
        Data data = getItem(position);
        if(convertView instanceof ProductCategoryView && data.categorySelectChildStringItem != null)
            return true;
        else if(convertView instanceof ProductListChildRowView && data.martProductItems != null)
            return true;
        return false;
    }

    private void viewSetData(View convertView, int position){
        if(convertView instanceof ProductCategoryView)
            ((ProductCategoryView)convertView).setData(getItem(position).categorySelectChildStringItem);
        else if(convertView instanceof ProductListChildRowView)
            ((ProductListChildRowView)convertView).setData(getItem(position).martProductItems);
    }
    private int[] getIndex(int position){
        int index[] = new int[2];
        index[0] = -1;
        index[1] = -1;
        for(int i=0;i<=position;i++) {
            if (getItem(i).categorySelectChildStringItem != null) {
                index[0]++;
                index[1] = -1;
            }else{
                if(index[1] == -1)
                    index[1]=0;
                else
                    index[1]+=3;
            }
        }
        return index;
    }

    public interface OnCustomItemClickListener{
        void onItemClick(View v,int parent_index,int child_index, Object object);
    }

    OnCustomItemClickListener onCustomItemClickListener;

    public void setOnCustomItemClickListener(OnCustomItemClickListener onCustomItemClickListener) {
        this.onCustomItemClickListener = onCustomItemClickListener;
    }
}

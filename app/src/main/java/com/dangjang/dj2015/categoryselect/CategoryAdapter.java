package com.dangjang.dj2015.categoryselect;

import android.content.Context;
import android.view.View;

import com.dangjang.dj2015.Manager.AppUtil;
import com.dangjang.dj2015.Manager.MyApplication;
import com.dangjang.dj2015.Manager.NetworkManager;
import com.dangjang.dj2015.R;
import com.dangjang.dj2015.jsonclass.response.Category;
import com.dangjang.dj2015.jsonclass.response.CategoryVO;
import com.dangjang.dj2015.jsonclass.response.MartList;
import com.dangjang.dj2015.jsonclass.response.MartVO;
import com.dangjang.dj2015.productlist.AddressDisplayView;
import com.dangjang.dj2015.productlist.BackCategoryView;
import com.dangjang.dj2015.productlist.ProductListFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created by Tacademy on 2015-11-03.
 */
public class CategoryAdapter {
    public final static int TYPE_MART = 0;
    public final static int TYPE_STRING = 1;
    public final static int TYPE_SORT = 2;

    List<CategoryVO> categories;
    List<MartVO> martLists;

    int select_category_ids[];
    int sort = 0;
    int mart = 0;
    public static final int CATEGORY_ID_NULL = -1;

    public CategorySelectView categorySelectView;
    private AddressDisplayView addressDisplayView;
    private BackCategoryView backCategoryView;

    NetworkManager.NetworkInterface networkInterface;

    private boolean main_pass;

    public CategoryAdapter(){
        select_category_ids = new int[2];
        select_category_ids[0] = CATEGORY_ID_NULL;
        select_category_ids[1] = CATEGORY_ID_NULL;
        sort = 0;
        mart = 0;
    }

    public interface OnDepthChangeListener{
        void onDepthChange(int depth);
    }

    public boolean isMainpass() {
        return main_pass;
    }

    public void setMart(int mart) {
        this.mart = mart;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public int getMart() {
        return mart;
    }

    public int getSort() {
        return sort;
    }

    public int getMainCategoryId(){
        return select_category_ids[0];
    }

    public int getSubCategoryId(){
        return select_category_ids[1];
    }

    public void clearCategory(){
        select_category_ids[0] = CATEGORY_ID_NULL;
        select_category_ids[1] = CATEGORY_ID_NULL;
        sort = 0;
        mart = 0;
        categorySelectView.setData(getCategorySelectItem());
    }

    OnDepthChangeListener onDepthChangeListener;

    public void setOnDepthChangeListener(OnDepthChangeListener onDepthChangeListener) {
        this.onDepthChangeListener = onDepthChangeListener;
    }

    public interface OnChangeListener{
        void onChange(int mart_id,int main_category_id,int sub_category_id,int sort);
    }

    OnChangeListener onChangeListener;

    public void setOnChangeListener(OnChangeListener onChangeListener) {
        this.onChangeListener = onChangeListener;
    }

    public void setNetworkInterface(NetworkManager.NetworkInterface networkInterface) {
        this.networkInterface = networkInterface;
    }

    public int getDepth() {
        if(select_category_ids[0] == CATEGORY_ID_NULL)
            return 1;
        else if(select_category_ids[1] == CATEGORY_ID_NULL)
            return 2;
        else if(sort == 0)
            return 3;
        else
            return 4;
    }

    public int getCurrentCategoryId() {
        if(getDepth() == 1)
            return 0;
        else
            return select_category_ids[((getDepth()==4)?3:getDepth())-2];
    }

    public void setCategories(List<CategoryVO> categories) {
        this.categories = categories;
        categorySelectView.setData(getCategorySelectItem());
    }

    public void setMartLists(List<MartVO> martLists) {
        this.martLists = martLists;
        categorySelectView.setData(getCategorySelectItem());
    }

    public void setCategorySelectView(CategorySelectView categorySelectView){
        this.categorySelectView = categorySelectView;
        categorySelectView.setOnCategoryItemSelectedListener(new CategorySelectView.OnCategoryItemSelectedListener() {
            @Override
            public void onCategoryItemSelected(int type, int position, CategorySelectChildItem item) {
                if (type == TYPE_MART) {
                    if (item instanceof CategorySelectChildStringItem) {
                        mart = 0;
                    } else if (item instanceof CategorySelectChildMartItem) {
                        CategorySelectChildMartItem categorySelectChildMartItem = (CategorySelectChildMartItem) item;
                        mart = categorySelectChildMartItem.martId;
                    }
                } else {
                    switch (getDepth()) {
                        case 1:
                            if (type == TYPE_STRING) {
                                categoryIn((CategorySelectChildStringItem) item);
                                backCategoryView.setData("카테고리 결과 : " + ((CategorySelectChildStringItem) item).getString());
                            }
                            break;
                        case 2:
                            if (type == TYPE_STRING) {
                                categoryIn((CategorySelectChildStringItem) item);
                            }
                            break;
                        case 3:
                        case 4:
                            String strs[] = MyApplication.getContext().getResources().getStringArray(R.array.product_sort);
                            CategorySelectChildStringItem categorySelectChildStringItem = (CategorySelectChildStringItem) item;
                            for (int i = 0; i < strs.length; i++) {
                                if (categorySelectChildStringItem.getString().equals(strs[i]))
                                    sort = i + 1;
                            }
                            break;
                    }
                }
                if (onChangeListener != null)
                    onChangeListener.onChange(mart, select_category_ids[0], select_category_ids[1], sort);
            }
        });
    }
    public CategorySelectItem[] getCategorySelectItem(){
        CategorySelectItem[] categorySelectItems = new CategorySelectItem[2];
        categorySelectItems[0] = new CategorySelectItem();
        categorySelectItems[0].setName("배달 가능한 마트");
        categorySelectItems[0].setContent(new ArrayList<CategorySelectChildItem>());
        categorySelectItems[0].getContent().add(new CategorySelectChildStringItem("전체"));
        if(martLists != null) {
            for (MartVO martList : martLists)
                categorySelectItems[0].getContent().add(new CategorySelectChildMartItem(martList.getIndex(), martList.getName(), ""));
        }
        categorySelectItems[0].setCurrentType(CategorySelectItem.TYPE_INDEX_MART);
        if(getDepth() <= 2){
            categorySelectItems[1] = new CategorySelectItem();
            categorySelectItems[1].setName("카테고리");
            categorySelectItems[1].setContent(getCategorySelectChildStringItem());
            categorySelectItems[1].setCurrentType(CategorySelectItem.TYPE_INDEX_STRING);
        }else{
            categorySelectItems[1] = new CategorySelectItem();
            categorySelectItems[1].setName("상품 정렬");
            categorySelectItems[1].setContent(new ArrayList<CategorySelectChildItem>());
            for(String content:MyApplication.getContext().getResources().getStringArray(R.array.product_sort)){
                categorySelectItems[1].getContent().add(new CategorySelectChildStringItem(content));
            }
            categorySelectItems[1].setCurrentType(CategorySelectItem.TYPE_INDEX_SORT);
        }
        return categorySelectItems;
    }
    public List<CategorySelectChildItem> getCategorySelectChildStringItem(){
        List<CategorySelectChildItem> arr = new ArrayList<>();
        arr.add(new CategorySelectChildStringItem("전체"));
        if(categories != null) {
            if (getDepth() == 1) {
                for (CategoryVO category : categories)
                    arr.add(new CategorySelectChildStringItem(category.getIndex(), category.getName()));
            } else if (getDepth() == 2) {
                for (CategoryVO category : categories) {
                    if (category.getIndex() == getCurrentCategoryId()) {
                        for (CategoryVO subCategoryList : category.getSubList())
                            arr.add(new CategorySelectChildStringItem(subCategoryList.getIndex(), subCategoryList.getName()));
                        break;
                    }
                }
            }
        }
        return arr;
    }
    public void categoryInForSearch(CategorySelectChildStringItem item){
        if(getDepth() < 3) {
            if(getDepth() == 1) {
                main_pass = true;
                select_category_ids[0] = getMainCategoryId(item.categoryId);
                select_category_ids[1] = item.categoryId;
                categorySelectView.setData(getCategorySelectItem());
                categorySelectView.close();
                backCategoryView.setData("카테고리 결과 : " + item.getString());
                if (onDepthChangeListener != null) {
                    onDepthChangeListener.onDepthChange(getDepth());
                }
            }else{
                categoryIn(item);
            }
        }
    }
    public void categoryIn(CategorySelectChildStringItem item){
        if(getDepth() < 3) {
            main_pass = false;
            if (getCurrentCategoryId() == 0) {
                if(addressDisplayView != null)
                    AppUtil.expandAnimation(addressDisplayView, false, 500);
                if (backCategoryView != null)
                    AppUtil.expandAnimation(backCategoryView, true, 500);
            }
            select_category_ids[getDepth() - 1] = item.categoryId;
            categorySelectView.setData(getCategorySelectItem());
            categorySelectView.close();
            backCategoryView.setData("카테고리 결과 : "+item.getString());
            if (onDepthChangeListener != null) {
                onDepthChangeListener.onDepthChange(getDepth());
            }
        }
    }
    public void categoryOut(){
        if(getDepth() > 1 && getCurrentCategoryId() != 0){
            select_category_ids[((getDepth()==4)?3:getDepth())-2] = CATEGORY_ID_NULL;
            if(getCurrentCategoryId() == 0){
                if(addressDisplayView != null)
                    AppUtil.expandAnimation(addressDisplayView, true, 500);
                if(backCategoryView != null)
                    AppUtil.expandAnimation(backCategoryView, false, 500);
            }else
                backCategoryView.setData("카테고리 결과 : "+getCurrentCategoryName());
            categorySelectView.setData(getCategorySelectItem());
            categorySelectView.close();
            if (onDepthChangeListener != null) {
                onDepthChangeListener.onDepthChange(getDepth());
            }
            main_pass = false;
        }
    }
    public void setAddressDisplayView(AddressDisplayView addressDisplayView){
        this.addressDisplayView = addressDisplayView;
    }
    public void setBackCategoryView(BackCategoryView backCategoryView){
        this.backCategoryView = backCategoryView;
    }
    public int getMainCategoryId(int sub_category_id){
        for(CategoryVO category:categories){
            for(CategoryVO subCategoryList:category.getSubList()){
                if(subCategoryList.getIndex() == sub_category_id)
                    return category.getIndex();
            }
        }
        return -1;
    }
    public String getCategoryName(int category_id){
        for(CategoryVO category:categories){
            for(CategoryVO subCategoryList:category.getSubList()){
                if(subCategoryList.getIndex() == category_id)
                    return subCategoryList.getName();
            }
        }
        return null;
    }
    public String getMainCategoryName(int main_category_id){
        for(CategoryVO category:categories){
            if(category.getIndex() == main_category_id)
                return category.getName();
        }
        return null;
    }
    public String getCurrentCategoryName(){
        if(getDepth() == 1)
            return null;
        else if(getDepth() ==2){
            for(CategoryVO category:categories){
                if(category.getIndex() == getCurrentCategoryId())
                    return category.getName();
            }
            return null;
        }else
            return getCategoryName(getCurrentCategoryId());
    }
}

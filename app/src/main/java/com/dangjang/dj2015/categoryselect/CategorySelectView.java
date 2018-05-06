package com.dangjang.dj2015.categoryselect;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.dangjang.dj2015.Manager.AppUtil;
import com.dangjang.dj2015.R;

import java.util.List;

/**
 * Created by Tacademy on 2015-10-23.
 */
public class CategorySelectView  extends FrameLayout {
    public interface OnCategoryItemSelectedListener{
        void onCategoryItemSelected(int type, int position, CategorySelectChildItem item);
    }
    OnCategoryItemSelectedListener categoryItemSelectedListener;

    public CategorySelectView(Context context) {
        super(context);
        init();
    }

    public CategorySelectView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void setOnCategoryItemSelectedListener(OnCategoryItemSelectedListener categoryItemSelectedListener){
        this.categoryItemSelectedListener = categoryItemSelectedListener;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public CategorySelectParentView leftView,rightView;
    CategorySelectItem leftItem,rightItem;
    ListView listView;
    CategorySelectAdapter mAdapter;
    private void init() {
        inflate(getContext(), R.layout.view_category_select, this);
        leftView = (CategorySelectParentView)findViewById(R.id.categorySelectLeft);
        rightView = (CategorySelectParentView)findViewById(R.id.categorySelectRight);
        listView = (ListView)findViewById(R.id.categorySelectListView);
        mAdapter = new CategorySelectAdapter();
        mAdapter.setListView(listView);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CategorySelectChildItem temp = (CategorySelectChildItem) listView.getItemAtPosition(position);
                int type = mAdapter.currentType;
                switch (mAdapter.getItemViewType(temp)) {
                    case CategorySelectAdapter.TYPE_INDEX_MART:                           //마트에서 마트 선택
                        CategorySelectChildMartView martView = (CategorySelectChildMartView) view;
                        leftItem.setSelect(position);
//                        martView.toggleCheck();
                        leftView.refresh();
                        mAdapter.notifyDataSetChanged();
                        break;
                    case CategorySelectAdapter.TYPE_INDEX_STRING:
                        if(mAdapter.currentType == CategorySelectAdapter.TYPE_INDEX_MART){
                            if(mAdapter.indexOf(listView.getItemAtPosition(position))==0){  //마트에서 전체 선택
//                                for(CategorySelectChildItem temp2:leftItem.content){
//                                    if(temp instanceof CategorySelectChildMartItem) {
//                                        CategorySelectChildMartItem item = (CategorySelectChildMartItem) temp2;
//                                        item.check = false;
//                                    }
//                                }
                                leftItem.select = 0;
                            }
                            leftView.refresh();
                            mAdapter.notifyDataSetChanged();
                        }else{
                            CategorySelectChildStringItem item = (CategorySelectChildStringItem) temp;
                            boolean left = (leftItem.content.indexOf(item) >= 0);
                            CategorySelectItem categorySelectItem = (left)?leftItem:rightItem;
                            CategorySelectParentView categorySelectParentView = (left)?leftView:rightView;
                            if(leftView.getCurrentType() == CategorySelectItem.TYPE_INDEX_MART){//왼마트 오카테 일때
                            }else{                                                              //왼카테 오정렬 일때
                                if(!left)
                                    type = CategoryAdapter.TYPE_SORT;
                            }
                            categorySelectItem.select = categorySelectItem.content.indexOf(item);
                            categorySelectParentView.refresh();
                        }
                        break;
                }
                close();
                if(categoryItemSelectedListener != null){
                    categoryItemSelectedListener.onCategoryItemSelected(mAdapter.currentType,mAdapter.indexOf(listView.getItemAtPosition(position)),temp);
                }
            }
        });

    }

    public void setLeftData(CategorySelectItem item) {
        leftView.setData(item);
        leftView.setOnClickListener(new CategorySelectParentOnClickListener(leftView, rightView, item.content));
        this.leftItem = item;
    }
    public void setRightData(CategorySelectItem item) {
        rightView.setData(item);
        rightView.setOnClickListener(new CategorySelectParentOnClickListener(rightView, leftView, item.content));
        this.rightItem = item;
    }

    public void setData(CategorySelectItem[] categorySelectItems){
        setLeftData(categorySelectItems[0]);
        setRightData(categorySelectItems[1]);
    }

    public boolean isOpened(){
        return leftView.getOpenState() || rightView.getOpenState();
    }

    public void close(){
        boolean opened = isOpened();
        if(opened)
            AppUtil.expandAnimation(listView,false, 500);
        leftView.setOpenDropdownImage(false);
        rightView.setOpenDropdownImage(false);
    }

    class CategorySelectParentOnClickListener implements OnClickListener{
        CategorySelectParentView first,second;
        List<CategorySelectChildItem> items;
        public CategorySelectParentOnClickListener(CategorySelectParentView first,CategorySelectParentView second,List<CategorySelectChildItem> items){
            this.first = first;
            this.second = second;
            this.items = items;
        }
        @Override
        public void onClick(View v) {
            boolean secondOpen = second.getOpenState();
            second.setOpenDropdownImage(false);
            if(first.toggleDropdownImage() == CategorySelectParentView.OPEN){
                mAdapter.dataChange(items);
                if(!secondOpen)
                    AppUtil.expandAnimation(listView, mAdapter, true, 500,false);
                else{
                    if(first.getSize() != second.getSize()){
                        AppUtil.expandAnimation(listView,mAdapter,true,200,true);
                    }
                }
            }else
                AppUtil.expandAnimation(listView,mAdapter,false, 500,false);
        }
    }
}

package com.dangjang.dj2015.categoryselect;

import java.util.List;

/**
 * Created by Tacademy on 2015-10-23.
 */
public class CategorySelectItem {
    public static final int TYPE_INDEX_MART = 0;
    public static final int TYPE_INDEX_STRING = 1;
    public static final int TYPE_INDEX_SORT = 2;
    public int currentType = -1;
    String name;
    List<CategorySelectChildItem> content;
    int select = 0;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CategorySelectChildItem> getContent() {
        return content;
    }

    public void setContent(List<CategorySelectChildItem> content) {
        this.content = content;
    }

    public int getSelect() {
        return select;
    }

    public void setSelect(int select) {
        this.select = select;
    }

    public int getCurrentType(){
        return currentType;
    }

    public void setCurrentType(int currentType){
        this.currentType = currentType;
    }
}

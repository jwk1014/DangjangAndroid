package com.dangjang.dj2015.categoryselect;

/**
 * Created by Tacademy on 2015-10-23.
 */
public class CategorySelectChildStringItem implements CategorySelectChildItem {
    int categoryId;
    String string;

    public CategorySelectChildStringItem(String string){
        this.string = string;
    }

    public CategorySelectChildStringItem(int categoryId,String string){
        this.categoryId = categoryId;
        this.string = string;
    }

    public int getCategoryId(){
        return categoryId;
    }

    public void setCategoryId(int categoryId){
        this.categoryId = categoryId;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }
}

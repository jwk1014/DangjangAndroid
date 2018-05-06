package com.dangjang.dj2015.jsonclass.response;

import java.util.List;

/**
 * Created by Tacademy on 2015-11-05.
 */
public class Category extends Result {
    public int main_id;
    public String main_name;
    public class SubCategoryList{
        public int category_id;
        public String sub_name;
    }
    public List<SubCategoryList> sub_category_list;
}

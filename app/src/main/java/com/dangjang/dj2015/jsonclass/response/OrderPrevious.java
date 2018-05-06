package com.dangjang.dj2015.jsonclass.response;

import java.util.Date;
import java.util.List;

/**
 * Created by Tacademy on 2015-11-05.
 */
public class OrderPrevious extends Result {
    public int mart_id;
    public String mart_name;
    public Date order_date;
    public class ProList{
        public int pro_id;
        public String pro_name;
        public String pro_image;
        public int pro_price;
        public int pro_count;
        public int pro_price2;
    }
    public List<ProList> pro_list;
    public int total_price;
}

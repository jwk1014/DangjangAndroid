package com.dangjang.dj2015.jsonclass.response;

import java.util.List;

/**
 * Created by Tacademy on 2015-11-05.
 */
public class Order extends Result {
    String mart_name;
    String phone;
    String ad1;
    String ad2;
    String hollyday;
    class ProList{
        String pro_name;
        int pro_price;
        int pro_count;
        int pro_price2;
    }
    List<ProList> pro_list;
    int total_price;
}

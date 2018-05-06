package com.dangjang.dj2015.jsonclass.response;

import java.util.Date;
import java.util.List;

/**
 * Created by Tacademy on 2015-11-05.
 */
public class ProductPrice extends Result {
    String pro_name;
    class DatePrice{
        Date date;
        int price;
    }
    List<DatePrice> date_price;
}

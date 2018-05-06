package com.dangjang.dj2015.accodion;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Tacademy on 2015-10-23.
 */
public class AccordionTitleItem {
    String name;
    Date date;
    final static SimpleDateFormat sdf;
    static{
        sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
    }
    public AccordionTitleItem(String name){
        this.name = name;
    }

    public AccordionTitleItem(String name, Date date) {
        this.name = name;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public String getDateString(){
        return sdf.format(date);
    }

    public void setDate(Date date) {
        this.date = date;
    }
}

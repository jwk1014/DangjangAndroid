package com.dangjang.dj2015.publicdata;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Tacademy on 2015-10-27.
 */
public class DeliveryTime implements Serializable{
    private Date startTime;
    private Date endTime;

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}

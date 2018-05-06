package com.dangjang.dj2015.categoryselect;

/**
 * Created by Tacademy on 2015-10-23.
 */
public class CategorySelectChildMartItem  implements CategorySelectChildItem {
    int martId;
    String martName;
    String deliveryString;
    boolean check;

    public CategorySelectChildMartItem(int mart_id,String martName,String deliveryString){
        this.martId = mart_id;
        this.martName = martName;
        this.deliveryString = deliveryString;
    }

    public int getMartId(){ return martId; }

    public void setMartId(int martId){
        this.martId = martId;
    }

    public String getMartName() {
        return martName;
    }

    public void setMartName(String martName) {
        this.martName = martName;
    }

    public String getDeliveryString() {
        return deliveryString;
    }

    public void setDeliveryString(String deliveryString) {
        this.deliveryString = deliveryString;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }
}

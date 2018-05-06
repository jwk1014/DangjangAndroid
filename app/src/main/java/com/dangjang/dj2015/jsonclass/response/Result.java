package com.dangjang.dj2015.jsonclass.response;

/**
 * Created by Tacademy on 2015-10-29.
 */
public class Result {
    public int code=-1;
    public String msg;
    public int result=-1;

    @Override
    public String toString() {
        return "code="+code+", msg="+msg+", result="+result;
    }
}

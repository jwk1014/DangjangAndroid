package com.dangjang.dj2015.jsonclass.response;

import java.util.List;

/**
 * Created by Tacademy on 2015-11-05.
 */
public class ProductModifyList extends Result{
    String mart_name;
    String pro_name;
    class RequestList{
        int request_id;
        String content;
    }
    List<RequestList> request_list;
}

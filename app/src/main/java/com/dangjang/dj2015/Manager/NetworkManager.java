package com.dangjang.dj2015.Manager;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;

import com.dangjang.dj2015.jsonclass.response.AddressVO;
import com.dangjang.dj2015.jsonclass.response.CategoryVO;
import com.dangjang.dj2015.jsonclass.response.MartVO;
import com.dangjang.dj2015.jsonclass.response.ProductVO;
import com.dangjang.dj2015.jsonclass.response.Sms;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.dangjang.dj2015.jsonclass.response.Cart;
import com.dangjang.dj2015.jsonclass.response.CartId;
import com.dangjang.dj2015.jsonclass.response.Category;
import com.dangjang.dj2015.jsonclass.response.MartDetail;
import com.dangjang.dj2015.jsonclass.response.MartList;
import com.dangjang.dj2015.jsonclass.response.Notice;
import com.dangjang.dj2015.jsonclass.response.Order;
import com.dangjang.dj2015.jsonclass.response.OrderPrevious;
import com.dangjang.dj2015.jsonclass.response.ProductModifyList;
import com.dangjang.dj2015.jsonclass.response.ProductPrice;
import com.dangjang.dj2015.jsonclass.response.ProductSearch;
import com.dangjang.dj2015.jsonclass.response.Result;
import com.dangjang.dj2015.jsonclass.response.UserInfo;
import com.dangjang.dj2015.jsonclass.response.UserPhoneIdentify;
import com.dangjang.dj2015.jsonclass.response.ZoneList;
import com.squareup.okhttp.OkHttpClient;

import java.lang.reflect.Type;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;
import retrofit.http.DELETE;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * Created by Tacademy on 2015-10-27.
 */
public class NetworkManager {
    private static NetworkManager manager;
    ServerUrl serverUrl;
    private boolean connection;
    OkHttpClient client;

    public final static int MOBILE = 1;
    public final static int WIFI = 2;
    public final static int BLUETOOTH = 3;
    public final static int DISCONNECT = 4;

    private NetworkManager(){
        connection = false;
        client = new OkHttpClient();
        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        client.setCookieHandler(cookieManager);
        Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new DateDeserializer()).create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ServerUrl.base_url)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();
        serverUrl = retrofit.create(ServerUrl.class);
        // client.getConnectionPool().evictAll();
    }

    public class DateDeserializer implements JsonDeserializer<Date> {
        @Override
        public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            String date = json.getAsString();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.KOREA);
            try {
                return format.parse(date.replaceAll("Z$", "+0000"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public static NetworkManager getInstance(){
        if(manager == null)
            manager = new NetworkManager();
        return manager;
    }

    public static void instanceClear(){
        manager = null;
    }

    public interface ServerUrl{
        String base_url = "NONE";
        //http://www.server.com/test/AAA/BBB

        @FormUrlEncoded
        @POST("/mobile/user/join")
        Call<Result> userJoin(@Field("id") String user_id, @Field("pw") String pw, @Field("name") String name, @Field("phone") String phone, @Field("address_index") int zone_id);

        @POST("/user/leave")
        Call<Result> userLeave();

        @GET("/user/info")
        Call<UserInfo> userInfo();

        @FormUrlEncoded
        @POST("/user/info")
        Call<Result> userModify(@Field("name")String name,@Field("phone")String phone);

        @FormUrlEncoded
        @POST("/mobile/user/login")
        Call<Result> userLogin(@Field("id")String id,@Field("pw")String pw);

        @POST("/mobile/user/logout")
        Call<Result> userLogout();

        @FormUrlEncoded
        @POST("/user/pw/change")
        Call<Result> userPwChange(@Field("pw")String pre_pw,@Field("new_pw")String new_pw);//pre_pw->pw

        @FormUrlEncoded
        @POST("/user/pw/find")
        Call<Result> userPwFind(@Field("id")String id,@Field("phone")String phone);//////////

        @FormUrlEncoded
        @POST("/user/phone/identify")
        Call<UserPhoneIdentify> userPhoneIdentify(@Field("phone")String phone);/////////

        @GET("/mobile/zone/search")
        Call<List<AddressVO>> zoneList(@Query("addr3")String address);

        @GET("/mobile/zone/user/address")
        Call<AddressVO> zoneUserAddress();

        @FormUrlEncoded
        @POST("/zone/register")
        Call<Result> zoneRegister(@Field("zone_id")int zone_id);

        @GET("/mobile/mart/list")
        Call<List<MartVO>> martList();

        @GET("/mobile/mart/detail")
        Call<MartVO> martDetail(@Query("mart_index")int mart_id);

        @GET("/mobile/category")
        Call<List<CategoryVO>> category();

        @GET("/mobile/product/search")
        Call<List<ProductVO>> productSearchKeyword(@Query("keyword")String keyword);//@Field("category_id")int category_id);

        @GET("/mobile/product/search")
        Call<List<ProductVO>> productSearchKeywordMaincategory(@Query("keyword")String keyword,@Query("main_category_index")int main_category_id);

        @GET("/mobile/product/search")
        Call<List<ProductVO>> productSearchKeywordSubcategory(@Query("keyword")String keyword,@Query("sub_category_index")int sub_category_id);

        @GET("/mobile/product/search")
        Call<List<ProductVO>> productSearchKeywordSubcategory(@Query("keyword")String keyword,@Query("sub_category_index")int sub_category_id,@Query("sort")int sort);

        @GET("/mobile/product/search")
        Call<List<ProductVO>> productSearchMaincategory(@Query("main_category_index")int main_category_id);

        @GET("/mobile/product/search")
        Call<List<ProductVO>> productSearchSubCategory(@Query("sub_category_index")int sub_category_id);

        @GET("/product/search")
        Call<List<ProductVO>> productSearchSubCategory(@Query("sub_category_index")int sub_category_id,@Query("sort")int sort);

        @FormUrlEncoded
        @POST("/mobile/product/click")
        Call<Result> productClick(@Field("pro_index")int pro_id, @Field("mart_index")int mart_id);

        @GET("/mobile/product/price")
        Call<ProductPrice> productPrice(@Query("pro_id")String pro_id);

        @GET("/product/modify/list")
        Call<ProductModifyList> productModifyList();////////////////////////////

        @GET("/mobile/cart")
        Call<List<Cart>> cart();

        @FormUrlEncoded
        @POST("/mobile/cart/select")
        Call<Result> cartSelect(@Field("cart_id")int cart_id,@Field("mart_id")int mart_id,@Field("pro_id")int pro_id,@Field("pro_count")int pro_count);


        @GET("/mobile/cart/select")
        Call<CartId> cartSelect();

        @FormUrlEncoded
        @POST("/cart/mart/delete")
        Call<Result> cartMartDelete(@Field("cart_id")int cart_id,@Field("mart_id")int mart_id);

        @FormUrlEncoded
        @POST("/cart/zone/delete")
        Call<Result> cartZoneDelete(@Field("zone_id")int zone_id);

        @FormUrlEncoded
        @POST("/cart/delete")
        Call<Result> cartDelete(@Field("cp_id")int cp_id);

        @GET("/order")
        Call<List<Order>> order(@Query("cart_id")int cart_id,@Query("mart_id")int mart_id);

        @FormUrlEncoded
        @POST("/mobile/order/check")
        Call<Result> orderCheck(@Field("cart_id")int cart_id,@Field("mart_id")int mart_id,@Field("order_date")String order_date,@Field("name")String name,@Field("addr")String addr,@Field("phone")String phone,@Field("place")int place,@Field("sold_out")int sold_out,@Field("request")String request,@Field("pay_methode")int pay_methode,@Field("point_card")String point_card);

        @GET("/mobile/order/previous")
        Call<List<OrderPrevious>> orderPrevious();

        @GET("/notice")
        Call<List<Notice>> notice();

        @FormUrlEncoded
        @POST("/save/token")
        Call<Result> saveToken(@Field("token") String token);

        @GET("/user/sms")
        Call<Sms> authorizationPhone(@Query("phone") String phone);

        @FormUrlEncoded
        @POST("/contact")
        Call<Result> contact(@Field("name") String name,@Field("mail") String mail,@Field("content")String content,@Field("date") String date);

        @FormUrlEncoded
        @POST("/partnership")
        Call<Result> partnership(@Field("mname") String mname,@Field("addr") String addr,@Field("tel") String tel,@Field("manager") String manager);

        @GET("/mobile/product/recent")
        Call<List<ProductVO>> productRecent();
    }

    public static int getNetworkState(){
        final ConnectivityManager connectivityManager = (ConnectivityManager)MyApplication.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT < 21) {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null) {
                switch (networkInfo.getType()) {
                    case ConnectivityManager.TYPE_MOBILE:
                        return MOBILE;
                    case ConnectivityManager.TYPE_WIFI:
                        return WIFI;
                    case ConnectivityManager.TYPE_BLUETOOTH:
                        return BLUETOOTH;
                }
            } else
                return DISCONNECT;
        } else {
            Network[] networks = connectivityManager.getAllNetworks();
            NetworkInfo networkInfo;
            for (Network network : networks) {
                networkInfo = connectivityManager.getNetworkInfo(network);
                if (networkInfo.getState().equals(NetworkInfo.State.CONNECTED)) {
                    switch (networkInfo.getType()) {
                        case ConnectivityManager.TYPE_MOBILE:
                            return MOBILE;
                        case ConnectivityManager.TYPE_WIFI:
                            return WIFI;
                        case ConnectivityManager.TYPE_BLUETOOTH:
                            return BLUETOOTH;
                    }
                }
            }
            return DISCONNECT;
        }
        return 0;
    }

    private Callback generateCallback(final String function_name,final int key, final NetworkInterface networkInterface,boolean loading_on, final boolean loading_off){
        if(loading_on)
            networkInterface.showLoadingDialog();
        return new Callback() {
            @Override
            public void onResponse(Response response, Retrofit retrofit) {
                if(response.body() == null)
                    networkInterface.networkErrorMsg(function_name,"result=null");
                if(loading_off)
                    networkInterface.hideLoadingDialog();
                networkInterface.onResponse(key, true, response.body());
            }
            @Override
            public void onFailure(Throwable t) {
                networkInterface.networkErrorMsg(function_name, t.toString());
                if(loading_off)
                    networkInterface.hideLoadingDialog();
                networkInterface.onResponse(key, false, t);
            }
        };
    }

    public void userJoin(String user_id, String pw, String name, String phone, int zone_id, final int key, final NetworkInterface networkInterface){
        getConnection().userJoin(user_id, pw, name, phone, zone_id).enqueue(generateCallback("userJoin", key, networkInterface,true,true));
    }

    public void userLeave(final int key, final NetworkInterface networkInterface){
        getConnection().userLeave().enqueue(generateCallback("userLeave",key,networkInterface,true,true));
    }

    public void userInfo(final int key, final NetworkInterface networkInterface,boolean loading_on, final boolean loading_off){
        getConnection().userInfo().enqueue(generateCallback("userInfo",key,networkInterface,loading_on,loading_off));
    }

    public void userModify(String name,String phone,final int key, final NetworkInterface networkInterface){
        getConnection().userModify(name, phone).enqueue(generateCallback("userModify",key,networkInterface,true,true));
    }

    public void userLogin(String id,String pw,final int key, final NetworkInterface networkInterface){
        getConnection().userLogin(id, pw).enqueue(generateCallback("userLogin",key,networkInterface,true,true));
    }

    public void userLogin(String id,String pw,final int key, final NetworkInterface networkInterface,boolean loading_on,boolean loading_off){
        getConnection().userLogin(id, pw).enqueue(generateCallback("userLogin",key,networkInterface,loading_on,loading_off));
    }

    public void userLogout(final int key, final NetworkInterface networkInterface){
        getConnection().userLogout().enqueue(generateCallback("userLogout",key,networkInterface,true,true));
    }

    public void userPwChange(String pre_pw, String new_pw, final int key, final NetworkInterface networkInterface){
        getConnection().userPwChange(pre_pw, new_pw).enqueue(generateCallback("userPwChange",key,networkInterface,true,true));
    }

    //public void userPwFind(@Field("id")String id,@Field("phone")String phone);//////////

    public void zoneList(String address,final int key, final NetworkInterface networkInterface){
        getConnection().zoneList(address).enqueue(generateCallback("zoneList",key,networkInterface,true,true));
    }

    public void zoneUserAddress(final int key, final NetworkInterface networkInterface,boolean loading_on, final boolean loading_off){
        getConnection().zoneUserAddress().enqueue(generateCallback("zoneUserAddress",key,networkInterface,loading_on,loading_off));
    }

    public void zoneRegister(int zone_id,final int key, final NetworkInterface networkInterface){
        getConnection().zoneRegister(zone_id).enqueue(generateCallback("zoneRegister",key,networkInterface,true,true));
    }

    public void martList(final int key, final NetworkInterface networkInterface,boolean loading_on, final boolean loading_off){
        getConnection().martList().enqueue(generateCallback("martList",key,networkInterface,loading_on,loading_off));
    }

    public void martDetail(int mart_id,final int key, final NetworkInterface networkInterface){
        getConnection().martDetail(mart_id).enqueue(generateCallback("martDetail",key,networkInterface,true,true));
    }

    public void category(final int key, final NetworkInterface networkInterface,boolean loading_on, final boolean loading_off){
        getConnection().category().enqueue(generateCallback("category",key,networkInterface,loading_on,loading_off));
    }

    public void productSearchKeyword(String keyword,final int key, final NetworkInterface networkInterface){
        getConnection().productSearchKeyword(keyword).enqueue(generateCallback("productSearchKeyword",key,networkInterface,true,true));
    }

    public void productSearchKeywordMaincategory(String keyword,int main_category_id,final int key, final NetworkInterface networkInterface){
        getConnection().productSearchKeywordMaincategory(keyword, main_category_id).enqueue(generateCallback("productSearchKeyword",key,networkInterface,true,true));
    }

    public void productSearchKeywordSubCategory(String keyword,int sub_category_id,final int key, final NetworkInterface networkInterface){
        getConnection().productSearchKeywordSubcategory(keyword, sub_category_id).enqueue(generateCallback("productSearchKeyword",key,networkInterface,true,true));
    }

    public void productSearchKeywordSubCategory(String keyword,int sub_category_id,int sort,final int key, final NetworkInterface networkInterface){
        getConnection().productSearchKeywordSubcategory(keyword, sub_category_id,sort).enqueue(generateCallback("productSearchKeyword",key,networkInterface,true,true));
    }

    public void productSearchMaincategory(int main_category_id,final int key, final NetworkInterface networkInterface){
        getConnection().productSearchMaincategory(main_category_id).enqueue(generateCallback("productSearchMaincategory",key,networkInterface,true,true));
    }

    public void productSearchSubCategory(int sub_category_id,final int key, final NetworkInterface networkInterface){
        getConnection().productSearchSubCategory(sub_category_id).enqueue(generateCallback("productSearchSubCategory",key,networkInterface,true,true));
    }

    public void productSearchSubCategory(int sub_category_id,int sort,final int key, final NetworkInterface networkInterface){
        getConnection().productSearchSubCategory(sub_category_id,sort).enqueue(generateCallback("productSearchSubCategory",key,networkInterface,true,true));
    }

    public void productPrice(String pro_id,final int key, final NetworkInterface networkInterface){
        getConnection().productPrice(pro_id).enqueue(generateCallback("productPrice",key,networkInterface,true,true));
    }

    //void productModifyList();////////////////////////////

    public void cart(final int key,final NetworkInterface networkInterface){
        getConnection().cart().enqueue(generateCallback("cart",key,networkInterface,true,true));
    }

    public void cartSelect(int cart_id,int mart_id,int pro_id,int pro_count,final int key, final NetworkInterface networkInterface) {
        getConnection().cartSelect(cart_id, mart_id, pro_id, pro_count).enqueue(generateCallback("cartSelect",key,networkInterface,true,true));
    }

    public void cartSelectId(final int key, final NetworkInterface networkInterface) {
        getConnection().cartSelect().enqueue(generateCallback("cartSelectId",key,networkInterface,true,true));
    }

    public void cartDelete(int cp_id,final int key, final NetworkInterface networkInterface){
        getConnection().cartDelete(cp_id).enqueue(generateCallback("cartDelete",key,networkInterface,true,true));
    }

    public void cartMartDelete(int cart_id,int mart_id,final int key, final NetworkInterface networkInterface){
        getConnection().cartMartDelete(cart_id, mart_id).enqueue(generateCallback("cartMartDelete",key,networkInterface,true,true));
    }

    //void order(@Query("cart_id")int cart_id,@Query("mart_id")int mart_id);

    public void orderCheck(int cart_id, int mart_id, Date order_date, String name, String addr, String phone, int place, int sold_out, String request, int pay_methode, String point_card,final int key,final NetworkInterface networkInterface){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.KOREA);
        String date_string = simpleDateFormat.format(order_date);
        getConnection().orderCheck(cart_id,mart_id,date_string,name,addr,phone,place,sold_out,request,pay_methode,point_card).enqueue(generateCallback("orderCheck",key,networkInterface,true,true));
    }

    //void orderPrevious();

    public void notice(final int key, final NetworkInterface networkInterface){
        getConnection().notice().enqueue(generateCallback("notice",key,networkInterface,true,true));
    }

    public void authorizationPhone(String phone,final int key, final NetworkInterface networkInterface){
        getConnection().authorizationPhone(phone).enqueue(generateCallback("authorizationPhone",key,networkInterface,true,true));
    }

    public void orderPrevious(final int key, final NetworkInterface networkInterface){
        getConnection().orderPrevious().enqueue(generateCallback("orderPrevious",key,networkInterface,true,true));
    }

    public void contact(String name,String mail,String content,Date date,final int key, final NetworkInterface networkInterface){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.KOREA);
        String date_string = simpleDateFormat.format(date);
        getConnection().contact(name, mail, content, date_string).enqueue(generateCallback("contact",key,networkInterface,true,true));
    }

    public void partnership(String mname, String addr, String tel, String manager, final int key, final NetworkInterface networkInterface){
        getConnection().partnership(mname, addr, tel, manager).enqueue(generateCallback("partnership",key,networkInterface,true,true));
    }

    public void productRecent(final int key,final NetworkInterface networkInterface){
        getConnection().productRecent().enqueue(generateCallback("productRecent",key,networkInterface,false,false));
    }

    public ServerUrl getConnection(){
        return serverUrl;
    }

    public interface NetworkInterface{
        void onResponse(final int KEY,boolean success,Object object);
        void showLoadingDialog();
        void hideLoadingDialog();
        void networkErrorMsg(String log_name,String log_detail);
        void networkErrorMsg(String log_name,String log_detail,String message);
    }
}

package com.dangjang.dj2015.productlist;

import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dangjang.dj2015.Manager.AppUtil;
import com.dangjang.dj2015.Manager.NetworkManager;
import com.dangjang.dj2015.Manager.ResultCode;
import com.dangjang.dj2015.Manager.StorageManager;
import com.dangjang.dj2015.cart.CartActivity;
import com.dangjang.dj2015.categoryselect.CategoryAdapter;
import com.dangjang.dj2015.R;
import com.dangjang.dj2015.categoryselect.CategorySelectChildStringItem;
import com.dangjang.dj2015.categoryselect.CategorySelectView;
import com.dangjang.dj2015.dialog.CustomDialogFragment;
import com.dangjang.dj2015.dialog.LoadingDialogFragment;
import com.dangjang.dj2015.jsonclass.response.AddressVO;
import com.dangjang.dj2015.jsonclass.response.Category;
import com.dangjang.dj2015.jsonclass.response.CategoryVO;
import com.dangjang.dj2015.jsonclass.response.MartList;
import com.dangjang.dj2015.jsonclass.response.MartVO;
import com.dangjang.dj2015.jsonclass.response.ProductSearch;
import com.dangjang.dj2015.jsonclass.response.ProductVO;
import com.dangjang.dj2015.jsonclass.response.Result;
import com.dangjang.dj2015.jsonclass.response.UserInfo;
import com.dangjang.dj2015.main.AddressSearchActivity;
import com.dangjang.dj2015.parentclass.AppFragment;
import com.dangjang.dj2015.productdetail.ProductDetailActivity;
import com.dangjang.dj2015.publicdata.AddressInfo;
import com.dangjang.dj2015.publicdata.MartItem;
import com.dangjang.dj2015.publicdata.MartProductItem;
import com.dangjang.dj2015.publicdata.ProductItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProductListFragment extends AppFragment {
    ProductListAdapter adapter;
    CategorySelectView categorySelectView;
    EditText search_edittext;
    CategoryAdapter categoryAdapter;
    AddressDisplayView addressDisplayView;
    BackCategoryView backCategoryView;
    ListView listView;
    View searchLayout;
    OnProductRecentCallback onProductRecentCallback;
    CategoryAdapter.OnChangeListener onChangeListener;
    TextView empty_textview;

    public final static int NW_MARTLIST = 1;
    public final static int NW_CATEGORY = 2;
    public final static int NW_SEARCH = 3;
    public final static int NW_ZONEUSERADDRESS = 4;
    public final static int NW_ZONEREGISTER = 5;

    public final static int ACTIVITY_RESULT_REQUEST_ADDRESS = 1;
    public final static int ACTIVITY_RESULT_REQUEST_PRODUCT = 2;
    public final static int ACTIVITY_RESULT_REQUEST_SEARCH = 3;

    private int mart_temp;

    public ProductListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_product_list, container, false);

        onProductRecentCallback = (OnProductRecentCallback)getActivity();

        NetworkManager.getInstance().martList(NW_MARTLIST, this, true, false);
        empty_textview = (TextView)v.findViewById(R.id.f_productlist_textview);
        listView = (ListView)v.findViewById(R.id.f_productlist_listview);
        listView.setAdapter(adapter = new ProductListAdapter(getActivity(), R.layout.view_product_child));
        categoryAdapter = new CategoryAdapter();
        mart_temp = categoryAdapter.getMart();
        {
            View header = getActivity().getLayoutInflater().inflate(R.layout.view_productlist_header, null);
            listView.addHeaderView(header, "header", true);
            categorySelectView = (CategorySelectView) header.findViewById(R.id.f_productlist_categoryselectview);
            addressDisplayView = (AddressDisplayView) header.findViewById(R.id.f_productlist_addressdisplayview);
            addressDisplayView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), AddressSearchActivity.class);
                    startActivityForResult(intent, ACTIVITY_RESULT_REQUEST_ADDRESS);
                }
            });
            backCategoryView = (BackCategoryView) header.findViewById(R.id.f_productlist_backcategoryview);
            AppUtil.expandAnimation(backCategoryView, false, 0);
            backCategoryView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    back();
                }
            });
            categoryAdapter.setBackCategoryView(backCategoryView);
        }
        categoryAdapter.setCategorySelectView(categorySelectView);
        categoryAdapter.setAddressDisplayView(addressDisplayView);
        categoryAdapter.setNetworkInterface(this);
        categoryAdapter.setOnChangeListener(onChangeListener = new CategoryAdapter.OnChangeListener() {
            @Override
            public void onChange(int mart_id, int main_category_id, int sub_category_id, int sort) {
                if (search_edittext.getText().length() > 0 && sub_category_id > 0 && sort > 0) {
                    NetworkManager.getInstance().productSearchKeywordSubCategory(search_edittext.getText().toString(), sub_category_id, sort, NW_SEARCH, ProductListFragment.this);
                } else if (search_edittext.getText().length() > 0 && sub_category_id > 0) {
                    NetworkManager.getInstance().productSearchKeywordSubCategory(search_edittext.getText().toString(), sub_category_id, NW_SEARCH, ProductListFragment.this);
                } else if (search_edittext.getText().length() > 0 && main_category_id > 0) {
                    NetworkManager.getInstance().productSearchKeywordMaincategory(search_edittext.getText().toString(), main_category_id, NW_SEARCH, ProductListFragment.this);
                } else if (sub_category_id > 0 && sort > 0) {
                    NetworkManager.getInstance().productSearchSubCategory(sub_category_id, sort, NW_SEARCH, ProductListFragment.this);
                } else if (sub_category_id > 0) {
                    NetworkManager.getInstance().productSearchSubCategory(sub_category_id, NW_SEARCH, ProductListFragment.this);
                } else if (main_category_id > 0) {
                    NetworkManager.getInstance().productSearchMaincategory(main_category_id, NW_SEARCH, ProductListFragment.this);
                } else if( search_edittext.getText().length() > 0) {
                    NetworkManager.getInstance().productSearchKeyword(search_edittext.getText().toString(), NW_SEARCH, ProductListFragment.this);
                }
            }
        });
//        categoryAdapter.setOnDepthChangeListener(new CategoryAdapter.OnDepthChangeListener() {
//            @Override
//            public void onDepthChange(int depth) {
//                switch (depth) {
//                    case 1:
//                        adapter.clear();
//                        break;
//                    case 2:
//                        NetworkManager.getInstance().productSearchMaincategory(categoryAdapter.getCurrentCategoryId(), NW_SEARCH_MAIN_CATEGORY, ProductListFragment.this);
//                        break;
//                    case 3:
//                        NetworkManager.getInstance().productSearchSubCategory(categoryAdapter.getCurrentCategoryId(), NW_SEARCH_SUB_CATEGORY, ProductListFragment.this);
//                        break;
//                    case 4:
//                        break;
//                }
//            }
//        });

        adapter.setOnCustomItemClickListener(new ProductListAdapter.OnCustomItemClickListener() {
            @Override
            public void onItemClick(View v, int parent_index, int child_index, Object object) {
                if(child_index == -1){
                    CategorySelectChildStringItem categorySelectChildStringItem = (CategorySelectChildStringItem)object;
                    if(search_edittext.getText().length() == 0)
                        categoryAdapter.categoryIn(categorySelectChildStringItem);
                    else
                        categoryAdapter.categoryInForSearch(categorySelectChildStringItem);
                    onChangeListener.onChange(categoryAdapter.getMart(), categoryAdapter.getMainCategoryId(), categoryAdapter.getSubCategoryId(), categoryAdapter.getSort());
                }else{
                    MartProductItem martProductItem = (MartProductItem)object;
                    NetworkManager.getInstance().getConnection().productClick(martProductItem.getProductItem().getId(),martProductItem.getMartItem().getId()).enqueue(new Callback<Result>() {
                        @Override
                        public void onResponse(Response<Result> response, Retrofit retrofit) {}
                        @Override
                        public void onFailure(Throwable t) {}
                    });
                    Intent intent = new Intent(getActivity(), ProductDetailActivity.class);
                    intent.putExtra("item",martProductItem);
                    AppUtil.hideCurrentKeyboard(getActivity());
                    startActivityForResult(intent,ACTIVITY_RESULT_REQUEST_PRODUCT);
                }
            }
        });

        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setCustomView(searchLayout = View.inflate(getActivity(), R.layout.menu_main_fragment_layout, null), new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, Gravity.CENTER));
        if(mActivityListener != null)
            mActivityListener.setActionBarTitle("");
        search_edittext = (EditText)searchLayout.findViewById(R.id.menu_main_fragment_edittext);
        TextView textView = (TextView)searchLayout.findViewById(R.id.menu_main_layout);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                if (search_edittext.getText().toString().length() > 0)
                    intent.putExtra("search_string", search_edittext.getText().toString());
                startActivityForResult(intent, ACTIVITY_RESULT_REQUEST_SEARCH);
            }
        });

        return v;
    }

    public void back(){
        if ( (search_edittext.getText().length() == 0 && categoryAdapter.getDepth() == 2) ||
                (search_edittext.getText().length() > 0 && categoryAdapter.getDepth() == 1) ){
            AppUtil.expandAnimation(addressDisplayView, true, 0);
            AppUtil.expandAnimation(backCategoryView, false, 0);
            categoryAdapter.clearCategory();
            adapter.clear();
            adapter.notifyDataSetChanged();
            search_edittext.getText().clear();
            NetworkManager.getInstance().productSearchKeyword("", NW_SEARCH, ProductListFragment.this);
        }else if(search_edittext.getText().length() > 0 &&
                    (   (categoryAdapter.getDepth() == 3 && categoryAdapter.isMainpass()) ||
                        (categoryAdapter.getDepth() == 2)  )
                ){
            search(search_edittext.getText().toString());
        }else {
            categoryAdapter.categoryOut();
            onChangeListener.onChange(categoryAdapter.getMart(), categoryAdapter.getMainCategoryId(), categoryAdapter.getSubCategoryId(), categoryAdapter.getSort());
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main_fragment, menu);
    }

    void search(String search_string){
        adapter.clear();
        adapter.notifyDataSetChanged();
        categoryAdapter.clearCategory();
        AppUtil.expandAnimation(addressDisplayView, false, 0);
        backCategoryView.setData("검색 결과 : "+search_string);
        AppUtil.expandAnimation(backCategoryView, true, 0);
        NetworkManager.getInstance().productSearchKeyword(search_string, NW_SEARCH, ProductListFragment.this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_main_fragment_item2 :
                Intent intent = new Intent(getActivity(), CartActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.right_in,R.anim.left_out);
                AppUtil.hideCurrentKeyboard(getActivity());
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onBackKeyPressed() {
        if(categorySelectView.isOpened()){
            categorySelectView.close();
            return true;
        }else if(!(search_edittext.getText().length() == 0 && categoryAdapter.getDepth() == 1)) {
            back();
            return true;
        }else
            return super.onBackKeyPressed();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case ACTIVITY_RESULT_REQUEST_ADDRESS:
                switch (resultCode){
                    case ResultCode.RESULT_OK:
                        AddressInfo addressInfo = (AddressInfo)data.getSerializableExtra(AddressSearchActivity.KEY_ADDRESS);
                        addressDisplayView.setTempData(addressInfo.getAddressString());
                        NetworkManager.getInstance().zoneRegister(addressInfo.getZoneId(),NW_ZONEREGISTER,this);
                        break;
                    case ResultCode.RESULT_CANCEL:
                        break;
                    case ResultCode.RESULT_FAIL:
                        break;
                }
                break;
            case ACTIVITY_RESULT_REQUEST_PRODUCT:
                onProductRecentCallback.onProductRecent();
                switch (resultCode){
                    case ResultCode.RESULT_OK:
//                        Intent intent = new Intent(getActivity(), CartActivity.class);
//                        startActivity(intent);
//                        getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
                        break;
                    case ResultCode.RESULT_CANCEL:
                        break;
                    case ResultCode.RESULT_FAIL:
                        break;
                }
                break;
            case ACTIVITY_RESULT_REQUEST_SEARCH:
                String search_string = data.getStringExtra(SearchActivity.KEY_SEARCH);
                switch (resultCode){
                    case ResultCode.RESULT_OK:
                        search(search_string);
                        break;
                    case ResultCode.RESULT_CANCEL:
                        break;
                    case ResultCode.RESULT_FAIL:
                        break;
                }
                if(search_string != null)
                    search_edittext.setText(search_string);
                else
                    search_edittext.getText().clear();
                break;
        }
    }

    private String depthCategoryName(int sub_category_id){
        switch (categoryAdapter.getDepth()){
            case 1:
                if(search_edittext.getText().length() == 0)
                    return categoryAdapter.getMainCategoryName(categoryAdapter.getMainCategoryId(sub_category_id));
            case 2:
            case 3:
            case 4:
                return categoryAdapter.getCategoryName(sub_category_id);
        }
        return null;
    }

    private int depthCategoryId(int sub_category_id){
        switch (categoryAdapter.getDepth()){
            case 1:
                if(search_edittext.getText().length() == 0)
                    return categoryAdapter.getMainCategoryId(sub_category_id);
            case 2:
            case 3:
            case 4:
                return sub_category_id;
        }
        return -1;
    }

    @Override
    public void onResponse(int KEY, boolean success, Object object) {
        switch (KEY){

            case NW_MARTLIST:
                if(success && object != null){
                    List<MartVO> martLists = (List<MartVO>)object;
                    categoryAdapter.setMartLists(martLists);
                    NetworkManager.getInstance().category(NW_CATEGORY, this,false,false);
                }
                break;
            case NW_CATEGORY:
                if(success && object != null){
                    List<CategoryVO> categories = (List<CategoryVO>)object;
                    categoryAdapter.setCategories(categories);
                    NetworkManager.getInstance().zoneUserAddress(NW_ZONEUSERADDRESS,this,false,true);
                }
                break;
            case NW_SEARCH:
                if(success && object != null) {
                    List<ProductVO> productSearch = (List<ProductVO>)object;
                    List<ProductListItem> list = new ArrayList<>();
                    if(productSearch.size() == 0){
                        if(empty_textview.getVisibility() == View.INVISIBLE)
                            empty_textview.setVisibility(View.VISIBLE);
                    }else {
                        if(empty_textview.getVisibility() == View.VISIBLE)
                            empty_textview.setVisibility(View.INVISIBLE);
                        HashMap<String, ProductListItem> map = new HashMap<>();
                        for (ProductVO search : productSearch) {
                            if (categoryAdapter.getMart() == 0 || categoryAdapter.getMart() == search.getMart_index()) {
                                ProductListItem productListItem;
                                if (map.get(depthCategoryName(search.getCategory_index())) == null) {
                                    productListItem = new ProductListItem();
                                    productListItem.setCategoryName(new CategorySelectChildStringItem(depthCategoryId(search.getCategory_index()), depthCategoryName(search.getCategory_index())));
                                    productListItem.setProductItems(new ArrayList<MartProductItem>());
                                    list.add(productListItem);
                                    map.put(depthCategoryName(search.getCategory_index()), productListItem);
                                } else {
                                    productListItem = map.get(depthCategoryName(search.getCategory_index()));
                                    if (categoryAdapter.getDepth() < 3 && productListItem.getProductItems().size() >= 6)
                                        continue;
                                }
                                MartProductItem martProductItem = new MartProductItem();
                                ProductItem productItem = new ProductItem();
                                productItem.setId(search.getIndex());
                                productItem.setName(search.getName());
                                productItem.setUnit(search.getUnit());
                                productItem.setImg_url(search.getImg_url());
                                martProductItem.setProductItem(productItem);
                                MartItem martItem = new MartItem();
                                martItem.setId(search.getMart_index());
                                martItem.setName(search.getMart_name());
                                martProductItem.setMartItem(martItem);
                                martProductItem.setPrice(search.getSale_price());
                                List<MartProductItem> arr = productListItem.getProductItems();
                                arr.add(martProductItem);
                            }
                        }
                    }
                    adapter.changeAll(list);
                    listView.smoothScrollToPosition(0);
                }
                break;
            case NW_ZONEUSERADDRESS:
                if(success && object != null) {
                    AddressVO address = (AddressVO)object;
                    addressDisplayView.setData(address.getName());
                    onProductRecentCallback.onProductRecent();
                    NetworkManager.getInstance().productSearchKeyword("", NW_SEARCH, ProductListFragment.this);categoryAdapter.clearCategory();
                }
                break;
            case NW_ZONEREGISTER:
                if(success && object != null)  {
                    Result result = (Result)object;
                    if(result.result == 1) {
                        addressDisplayView.setTempApply();
                        showMessage("주소가 설정되었습니다.");
                    }else
                        networkErrorMsg("zoneregister",result.toString());
                }
                break;
            default:
                super.onResponse(KEY,false,object);

        }
    }
}

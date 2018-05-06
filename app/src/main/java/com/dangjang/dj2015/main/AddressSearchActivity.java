package com.dangjang.dj2015.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dangjang.dj2015.Manager.AppUtil;
import com.dangjang.dj2015.Manager.NetworkManager;
import com.dangjang.dj2015.Manager.ResultCode;
import com.dangjang.dj2015.Manager.StorageManager;
import com.dangjang.dj2015.R;
import com.dangjang.dj2015.jsonclass.response.AddressVO;
import com.dangjang.dj2015.jsonclass.response.Result;
import com.dangjang.dj2015.jsonclass.response.ZoneList;
import com.dangjang.dj2015.parentclass.AppActivity;
import com.dangjang.dj2015.parentclass.AppFragment;
import com.dangjang.dj2015.publicdata.AddressInfo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class AddressSearchActivity extends AppActivity implements View.OnClickListener{
    public final static String KEY_ADDRESS = "ADDRESS";
    AddressSearchAdapter adapter;
    EditText editText;
    TextView textView,info_textview;
    ImageView wordDeleteImageView;

    public final static int NW_ZONELIST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_search);
        setSnackBarContainer(R.id.a_addresssearch_main_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        View customView;
        actionBar.setCustomView(customView = View.inflate(this, R.layout.toolbar_address_search, null), new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, Gravity.CENTER));
        AppUtil.viewsSetOnClickListener(customView, this, R.id.toolbar_addresssearch_delete_imageview, R.id.toolbar_addresssearch_search_imageview);
        wordDeleteImageView = (ImageView)customView.findViewById(R.id.toolbar_addresssearch_delete_imageview);
        editText = (EditText)customView.findViewById(R.id.toolbar_addresssearch_edittext);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(final TextView v, int actionId, KeyEvent event) {
                search();
                return true;
            }
        });
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    history();
                } else if (wordDeleteImageView.getVisibility() == View.INVISIBLE) {
                    wordDeleteImageView.setVisibility(View.VISIBLE);
                    wordDeleteImageView.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        textView = (TextView)findViewById(R.id.a_addresssearch_textview);
        info_textview = (TextView)findViewById(R.id.a_addresssearch_info_textview);
        final ListView listView = (ListView)findViewById(R.id.a_addresssearch_listview);
        listView.setAdapter(adapter = new AddressSearchAdapter(this, R.layout.view_address_search));
        adapter.setOnItemDeleteClickListener(new AddressSearchAdapter.OnItemDeleteClickListener() {
            @Override
            public void onItemDeleteClick(int index) {
                StorageManager.getInstance().deleteHistoryAddress(adapter.getItem(index).getZoneId());
                history();
            }
        });
        adapter.setOnItemClickListener(new AddressSearchAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int index) {
                AddressInfo addressInfo = adapter.getItem(index);
                Intent intent = new Intent();
                intent.putExtra(KEY_ADDRESS, addressInfo);
                setResult(ResultCode.RESULT_OK, intent);
                try {
                    StorageManager.getInstance().addHistoryAddress(addressInfo.getZoneId(), addressInfo.getAddressString());
                }catch (Exception e){}
                AppUtil.hideCurrentKeyboard(AddressSearchActivity.this);
                finish();
            }
        });
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                AddressInfo addressInfo = (AddressInfo) listView.getItemAtPosition(position);
//                Intent intent = new Intent();
//                intent.putExtra(KEY_ADDRESS, addressInfo);
//                setResult(ResultCode.RESULT_OK, intent);
//                try {
//                    StorageManager.getInstance().addHistoryAddress(addressInfo.getZoneId(), addressInfo.getAddressString());
//                }catch (Exception e){}
//                finish();
//            }
//        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        if(intent.getStringExtra("address") != null) {
            editText.getText().clear();
            editText.getText().append(intent.getStringExtra("address"));
            search();
        }else
            history();
    }
    private void history(){
        textView.setText("이전 주소 검색 리스트");
        adapter.clear();
        adapter.setDeleteVisible(true);
        ArrayList<AddressInfo> historyAddress = StorageManager.getInstance().getHistoryAddress();
        if(historyAddress != null && historyAddress.size() > 0)
            adapter.changeAll(historyAddress);
        wordDeleteImageView.setVisibility(View.INVISIBLE);
        wordDeleteImageView.setEnabled(false);
        if(adapter.getCount() == 0) {
            info_textview.setText("이전 주소 검색 리스트가 없습니다.");
            info_textview.setVisibility(View.VISIBLE);
        }else if(info_textview.getVisibility() == View.VISIBLE)
            info_textview.setVisibility(View.INVISIBLE);
    }
    private void search(){
        if(!editText.getText().toString().matches(AppUtil.REGEX_HANGUL)) {
            textView.setText("주소 검색 결과");
            info_textview.setText("검색어를 잘못 입력하셨습니다.");
            info_textview.setVisibility(View.VISIBLE);
        }else if (editText.getText().toString().length() == 0) {
            textView.setText("주소 검색 결과");
            info_textview.setText("검색어를 입력해주세요.");
        }else if (editText.getText().toString().length() < 2) {
            textView.setText("주소 검색 결과");
            info_textview.setText("검색어를 2글자 이상 입력해주세요.");
        }else
            NetworkManager.getInstance().zoneList(editText.getText().toString(), NW_ZONELIST, AddressSearchActivity.this);
        AppUtil.hideCurrentKeyboard(AddressSearchActivity.this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.toolbar_addresssearch_delete_imageview:
                editText.getText().clear();
                break;
            case R.id.toolbar_addresssearch_search_imageview:
                search();
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            setResult(ResultCode.RESULT_CANCEL);
            AppUtil.hideCurrentKeyboard(this);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        setResult(ResultCode.RESULT_CANCEL);
        AppUtil.hideCurrentKeyboard(this);
        finish();
    }

    @Override
    public void onResponse(int KEY, boolean success, Object object) {
        switch (KEY){

            case NW_ZONELIST:
                if(success && object != null){
                    List<AddressVO> temp_list = (List<AddressVO>)object;
                    List<AddressInfo> list = new ArrayList<AddressInfo>();
                    for (AddressVO zonelist : temp_list)
                        list.add(new AddressInfo(zonelist.getIndex(), zonelist.getName()));
                    adapter.changeAll(list);
                    textView.setText("주소 검색 결과");
                    if(adapter.getCount() == 0){
                        info_textview.setText("검색 결과가 없습니다.");
                        info_textview.setVisibility(View.VISIBLE);
                    }else {
                        adapter.setDeleteVisible(false);
                        if (info_textview.getVisibility() == View.VISIBLE)
                            info_textview.setVisibility(View.INVISIBLE);
                    }
                }
                break;

            default:
                super.onResponse(KEY,false,object);

        }
    }
}

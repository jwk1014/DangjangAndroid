package com.dangjang.dj2015.productlist;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
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

import com.dangjang.dj2015.Manager.AppUtil;
import com.dangjang.dj2015.Manager.ResultCode;
import com.dangjang.dj2015.Manager.StorageManager;
import com.dangjang.dj2015.R;
import com.dangjang.dj2015.main.AddressSearchAdapter;
import com.dangjang.dj2015.parentclass.AppActivity;
import com.dangjang.dj2015.publicdata.AddressInfo;

import java.util.ArrayList;

public class SearchActivity extends AppActivity implements View.OnClickListener{
    public final static String KEY_SEARCH = "SEARCH";
    EditText editText;
    TextView textView,info_textview;
    ImageView wordDeleteImageView;
    ProductSearchAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setSnackBarContainer(R.id.a_search_main_layout);
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
        editText.setHint("상품 검색");
        Intent intent = getIntent();
        if(intent.getStringExtra("search_string") != null) {
            editText.getText().append(intent.getStringExtra("search_string"));
            editText.selectAll();
        }
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(final TextView v, int actionId, KeyEvent event) {
                search(editText.getText().toString().trim(), true);
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
        textView = (TextView)findViewById(R.id.a_search_textview);
        info_textview = (TextView)findViewById(R.id.a_search_info_textview);
        final ListView listView = (ListView)findViewById(R.id.a_search_listview);
        listView.setAdapter(adapter = new ProductSearchAdapter(this, R.layout.view_product_search));
        adapter.setOnItemDeleteClickListener(new ProductSearchAdapter.OnItemDeleteClickListener() {
            @Override
            public void onItemDeleteClick(int index) {
                StorageManager.getInstance().deleteHistorySearch(adapter.getItem(index));
                history();
            }
        });
        adapter.setOnItemClickListener(new ProductSearchAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int index) {
                String search_word = adapter.getItem(index);
                search(search_word,false);
            }
        });
        history();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    private void history(){
        textView.setText("이전 상품 검색 리스트");
        adapter.clear();
        adapter.setDeleteVisible(true);
        ArrayList<String> historySearch = StorageManager.getInstance().getHistorySearch();
        if(historySearch != null && historySearch.size() > 0)
            adapter.changeAll(historySearch);
        wordDeleteImageView.setVisibility(View.INVISIBLE);
        wordDeleteImageView.setEnabled(false);
        if(adapter.getCount() == 0) {
            info_textview.setText("이전 상품 검색 리스트가 없습니다.");
            info_textview.setVisibility(View.VISIBLE);
        }else if(info_textview.getVisibility() == View.VISIBLE)
            info_textview.setVisibility(View.INVISIBLE);
    }
    private void search(String search_word,boolean save){
        adapter.clear();
        /*if(!search_word.matches(AppUtil.REGEX_HANGUL)) {
            textView.setText("상품 검색 안내");
            info_textview.setText("검색어를 잘못 입력하셨습니다.");
            info_textview.setVisibility(View.VISIBLE);
        }else if (search_word.length() == 0) {
            textView.setText("상품 검색 안내");
            info_textview.setText("검색어를 입력해주세요.");
        }else if (search_word.length() < 2) {
            textView.setText("상품 검색 안내");
            info_textview.setText("검색어를 2글자 이상 입력해주세요.");
        }else */{
            if(save){
                try {
                    StorageManager.getInstance().addHistorySearch(search_word);
                }catch (Exception e){}
            }
            Intent intent = new Intent();
            intent.putExtra(KEY_SEARCH, search_word);
            setResult(ResultCode.RESULT_OK, intent);
            finish();
        }
        AppUtil.hideCurrentKeyboard(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.toolbar_addresssearch_delete_imageview:
                editText.getText().clear();
                break;
            case R.id.toolbar_addresssearch_search_imageview:
                search(editText.getText().toString().trim(),true);
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent();
            intent.putExtra(KEY_SEARCH, editText.getText().toString());
            setResult(ResultCode.RESULT_CANCEL,intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(KEY_SEARCH, editText.getText().toString());
        setResult(ResultCode.RESULT_CANCEL,intent);
        finish();
    }
}

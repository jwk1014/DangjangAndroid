package com.dangjang.dj2015.first;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dangjang.dj2015.Manager.AppUtil;
import com.dangjang.dj2015.Manager.ResultCode;
import com.dangjang.dj2015.R;
import com.dangjang.dj2015.main.AddressSearchActivity;
import com.dangjang.dj2015.parentclass.AppFragment;
import com.dangjang.dj2015.publicdata.AddressInfo;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddressFragment extends AppFragment {
    EditText editText;

    public AddressFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_address, container, false);

        editText = (EditText)v.findViewById(R.id.f_address_edittext);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                search();
                return true;
            }
        });
        CardView cardView = (CardView)v.findViewById(R.id.f_address_button);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search();
            }
        });
        return v;
    }

    void search(){
        String search_string = editText.getText().toString().trim();
        if(search_string.length() == 0){
            showMessage("주소를 입력해주세요.");
        }else if(!search_string.matches(AppUtil.REGEX_HANGUL)){
            showMessage("주소를 올바르게 입력해주세요.");
        }else {
            Intent intent = new Intent(getActivity(), AddressSearchActivity.class);
            intent.putExtra("address", search_string);
            startActivityForResult(intent,1);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode){
            case ResultCode.RESULT_OK:
                AddressInfo addressInfo = (AddressInfo)data.getSerializableExtra(AddressSearchActivity.KEY_ADDRESS);
                if(mActivityListener != null){
                    JoinFragment joinFragment = JoinFragment.newInstance(addressInfo);
                    mActivityListener.doFragmentChange(joinFragment,JoinFragment.class.getName(),R.anim.fade_in,R.anim.fade_out,true);
                }
                break;
            case ResultCode.RESULT_FAIL:
                break;
            case ResultCode.RESULT_CANCEL:
                break;
        }
    }
}

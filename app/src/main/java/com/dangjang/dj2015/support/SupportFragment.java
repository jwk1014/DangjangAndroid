package com.dangjang.dj2015.support;


import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.dangjang.dj2015.Manager.AppUtil;
import com.dangjang.dj2015.R;
import com.dangjang.dj2015.jsonclass.response.UserInfo;
import com.dangjang.dj2015.parentclass.AppFragment;
import com.dangjang.dj2015.parentclass.FragmentCallbackIF;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class SupportFragment extends AppFragment implements View.OnClickListener{
    UserInfo userInfo;
    private static final String ARG_PARAM1 = "param1";

    public SupportFragment() {
        fragmentTitleStringResourceId = R.string.supportfragment_title;
    }

    public static SupportFragment getInstance(UserInfo userInfo){
        SupportFragment supportFragment = new SupportFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, userInfo);
        supportFragment.setArguments(args);
        return supportFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null)
            userInfo = (UserInfo)getArguments().getSerializable(ARG_PARAM1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_support, container, false);
        CardView cardView = (CardView)v.findViewById(R.id.f_support_oneonone_ask);
        cardView.setOnClickListener(this);
        cardView = (CardView)v.findViewById(R.id.f_support_mart_partnership);
        cardView.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()){
            case R.id.f_support_oneonone_ask:
                intent = new Intent(getActivity(),SupportOneononeActivity.class);
                break;
            case R.id.f_support_mart_partnership:
                intent = new Intent(getActivity(),SupportMartPartnershipActivity.class);
                break;
        }
        if(intent != null) {
            if(userInfo != null)
                intent.putExtra("userinfo",userInfo);
            startActivity(intent);
        }
    }
}

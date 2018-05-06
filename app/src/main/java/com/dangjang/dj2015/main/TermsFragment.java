package com.dangjang.dj2015.main;


import android.os.Bundle;
import android.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.dangjang.dj2015.Manager.AppUtil;
import com.dangjang.dj2015.R;
import com.dangjang.dj2015.accodion.AccordionAdapter;
import com.dangjang.dj2015.accodion.AccordionContentItem;
import com.dangjang.dj2015.accodion.AccordionItem;
import com.dangjang.dj2015.accodion.AccordionTitleItem;
import com.dangjang.dj2015.parentclass.AppFragment;
import com.dangjang.dj2015.parentclass.FragmentCallbackIF;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class TermsFragment extends AppFragment {
    AccordionAdapter adapter;

    public TermsFragment() {
        fragmentTitleStringResourceId = R.string.termsfragment_title;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_terms, container, false);

        ExpandableListView listView = (ExpandableListView)v.findViewById(R.id.f_terms_expandablelistview);
        listView.setAdapter(adapter = new AccordionAdapter(getActivity()));

        List<AccordionItem> list = new ArrayList<>();
        AccordionItem item = new AccordionItem();
        item.setTitle(new AccordionTitleItem("이용 약관"));
        item.setContents(new AccordionContentItem(Html.fromHtml(AppUtil.getXmlString(R.string.terms1))));
        list.add(item);
        item = new AccordionItem();
        item.setTitle(new AccordionTitleItem("개인정보 취급 방침"));
        item.setContents(new AccordionContentItem(Html.fromHtml(AppUtil.getXmlString(R.string.terms2))));
        list.add(item);
        /*item = new AccordionItem();
        item.setTitle(new AccordionTitleItem("위치기반 서비스 이용 약관"));
        item.setContents(new AccordionContentItem("content 내용 "));
        list.add(item);
        item = new AccordionItem();
        item.setTitle(new AccordionTitleItem("푸쉬 알림,SMS,이메일 수신 동의"));
        item.setContents(new AccordionContentItem("content 내용 "));
        list.add(item);*/
        adapter.changeAll(list);
        return v;
    }
}

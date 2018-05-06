package com.dangjang.dj2015.main;


import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.dangjang.dj2015.Manager.AppUtil;
import com.dangjang.dj2015.Manager.NetworkManager;
import com.dangjang.dj2015.R;
import com.dangjang.dj2015.accodion.AccordionAdapter;
import com.dangjang.dj2015.accodion.AccordionContentItem;
import com.dangjang.dj2015.accodion.AccordionItem;
import com.dangjang.dj2015.accodion.AccordionTitleItem;
import com.dangjang.dj2015.jsonclass.response.Notice;
import com.dangjang.dj2015.jsonclass.response.Result;
import com.dangjang.dj2015.parentclass.AppFragment;
import com.dangjang.dj2015.parentclass.FragmentCallbackIF;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 */
public class NoticeFragment extends AppFragment {
    AccordionAdapter adapter;

    public final static int NW_NOTICE=1;

    public NoticeFragment() {
        fragmentTitleStringResourceId = R.string.noticefragment_title;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_notice, container, false);

        ExpandableListView listView = (ExpandableListView)v.findViewById(R.id.f_notice_expandablelistview);
        listView.setAdapter(adapter = new AccordionAdapter(getActivity()));

        NetworkManager.getInstance().notice(NW_NOTICE,this);
        return v;
    }

    @Override
    public void onResponse(int KEY, boolean success, Object object) {
        switch (KEY){
            case NW_NOTICE:
                if(success && object != null)  {
                    List<Notice> notices = (List<Notice>)object;
                    List<AccordionItem> list = new ArrayList<>();
                    for(Notice notice:notices) {
                        AccordionItem item = new AccordionItem();
                        item.setTitle(new AccordionTitleItem(notice.title,notice.notice_date));
                        item.setContents(new AccordionContentItem(notice.content));
                        list.add(item);
                    }
                    adapter.changeAll(list);
                }
                break;
            default:
                super.onResponse(KEY,false,object);
        }
    }
}

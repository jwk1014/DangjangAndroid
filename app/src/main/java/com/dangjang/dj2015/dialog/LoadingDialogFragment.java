package com.dangjang.dj2015.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.dangjang.dj2015.Manager.MyApplication;
import com.dangjang.dj2015.R;

/**
 * Created by Tacademy on 2015-11-03.
 */
public class LoadingDialogFragment extends DialogFragment {
    FrameLayout layout;

    public LoadingDialogFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(android.support.v4.app.DialogFragment.STYLE_NO_TITLE, R.style.MyDialog);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_loading_dialog, container, false);
        layout = (FrameLayout)view.findViewById(R.id.f_loadingdialog_mainlayout);
        return view;
    }

    public FrameLayout getLayout(){
        return layout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        int size[] = MyApplication.getDisplaySize();
        Dialog d = getDialog();
        d.getWindow().setLayout(size[0],size[1]);
        WindowManager.LayoutParams params = d.getWindow().getAttributes();
        params.gravity = Gravity.LEFT | Gravity.TOP;
        params.x = 0;
        params.y = 0;
        d.getWindow().setAttributes(params);
    }

    @Override
    public void onStart() {
        super.onStart();
        if(getDialog() != null) {
            getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(android.content.DialogInterface dialog, int keyCode, android.view.KeyEvent event) {
                    if ((keyCode == android.view.KeyEvent.KEYCODE_BACK)) {
                        if (event.getAction() != KeyEvent.ACTION_DOWN) {
                            getActivity().onBackPressed();
                            return true;
                        }
                    }
                    return false;
                }
            });
        }
    }
}

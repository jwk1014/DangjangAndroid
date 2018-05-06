package com.dangjang.dj2015.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dangjang.dj2015.Manager.MyApplication;
import com.dangjang.dj2015.R;

/**
 * Created by Tacademy on 2015-11-03.
 */
public class CustomDialogFragment extends DialogFragment implements View.OnClickListener{
    FrameLayout layout;
    TextView in_textview;
    EditText in_edittext;
    LinearLayout in_layout;
    Button cancel;
    String content;
    boolean cancelVisible;

    public CustomDialogFragment() {
    }

    public void setContent(String content){
        this.content = content;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(android.support.v4.app.DialogFragment.STYLE_NO_TITLE, R.style.MyDialog);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_custom_dialog, container, false);
        layout = (FrameLayout)view.findViewById(R.id.f_customdialog_mainlayout);
        TextView content_textview = (TextView)view.findViewById(R.id.f_customdialog_content_textview);
        content_textview.setText(content);
        in_layout = (LinearLayout)view.findViewById(R.id.f_customdialog_in_linearlayout);
        in_edittext = (EditText)view.findViewById(R.id.f_customdialog_in_edittext);
        in_textview = (TextView)view.findViewById(R.id.f_customdialog_in_textview);
        cancel = (Button)view.findViewById(R.id.f_customdialog_cancel_button);
        if(cancelVisible) {
            cancel.setVisibility(View.VISIBLE);
            cancel.setOnClickListener(this);
        }
        Button b = (Button)view.findViewById(R.id.f_customdialog_submit_button);
        b.setOnClickListener(this);
        return view;
    }

    public void setCancelVisible(boolean visible){
        cancelVisible = visible;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.f_customdialog_cancel_button:
                if(onCancelListener != null)
                    onCancelListener.onCancel();
                break;
            case R.id.f_customdialog_submit_button:
                if(onSubmitClickListener != null)
                    onSubmitClickListener.onSubmit();
                break;
        }
        dismiss();
    }

    public interface OnSubmitClickListener{
        void onSubmit();
    }

    OnSubmitClickListener onSubmitClickListener;

    public void setOnSubmitClickListener(OnSubmitClickListener onSubmitClickListener) {
        this.onSubmitClickListener = onSubmitClickListener;
    }

    public interface OnCancelListener{
        void onCancel();
    }

    OnCancelListener onCancelListener;

    public void setOnCancelListener(OnCancelListener onCancelListener) {
        this.onCancelListener = onCancelListener;
        setCancelVisible(true);
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
}

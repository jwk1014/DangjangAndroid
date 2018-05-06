package com.dangjang.dj2015.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.dangjang.dj2015.Manager.AppUtil;
import com.dangjang.dj2015.R;

/**
 * Created by Tacademy on 2015-11-02.
 */
public class ResultDialogFragment extends DialogFragment {

    private static final String ARG_PARAM1 = "TITLE";
    private static final String ARG_PARAM2 = "MESSAGE";

    private String title;
    private String message;

    public static ResultDialogFragment newInstance(String param1, String param2) {
        ResultDialogFragment fragment = new ResultDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ResultDialogFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            title = getArguments().getString(ARG_PARAM1);
            message = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(AppUtil.getXmlString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismiss();
            }
        });
        return builder.create();
    }
}

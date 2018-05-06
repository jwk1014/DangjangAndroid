package com.dangjang.dj2015.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dangjang.dj2015.Manager.AppUtil;
import com.dangjang.dj2015.R;
import com.dangjang.dj2015.first.AuthorizationCallbackIF;

/**
 * Created by Tacademy on 2015-11-02.
 */
public class AuthorizationDialogFragment extends DialogFragment {
    private static final String ARG_PARAM1 = "AUTORIZATION_NUMBER";

    private int autorization_number;
    private AuthorizationCallbackIF callbackIF;
    private EditText editText;
    private int count = 5;

    public static AuthorizationDialogFragment newInstance(int autorization_number) {
        AuthorizationDialogFragment fragment = new AuthorizationDialogFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, autorization_number);
        fragment.setArguments(args);
        return fragment;
    }

    public AuthorizationDialogFragment() {
    }

    public void setAuthorizationCallback(AuthorizationCallbackIF callbackIF){
        this.callbackIF = callbackIF;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            autorization_number = getArguments().getInt(ARG_PARAM1);
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View innerView = View.inflate(getActivity(), R.layout.fragment_authorization_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle(AppUtil.getXmlString(R.string.authorizationdialogfragment_title));
        editText = (EditText)innerView.findViewById(R.id.f_authorization_edittext);
        Button button = (Button)innerView.findViewById(R.id.f_authorization_button);
        builder.setView(innerView);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String value = editText.getText().toString();
                if (value.length() == 0) {
                    Toast.makeText(getActivity(), AppUtil.getXmlString(R.string.authorization_number_empty), Toast.LENGTH_SHORT).show();
                } else if (value.equals(""+autorization_number)) {
                    if (callbackIF != null) {
                        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(editText.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                        callbackIF.setAuthorization(true);
                        dismiss();
                    }
                }else
                    Toast.makeText(getActivity(), AppUtil.getXmlString(R.string.authorization_dismatch)+"("+AppUtil.getXmlString(R.string.authorization_remain_count)+": " + (--count) + AppUtil.getXmlString(R.string.count_unit)+")", Toast.LENGTH_SHORT).show();
                if(count == 0)
                    dismiss();
            }
        });
        return builder.create();
    }
}

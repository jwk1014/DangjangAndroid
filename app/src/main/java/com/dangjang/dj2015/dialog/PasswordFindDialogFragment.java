package com.dangjang.dj2015.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dangjang.dj2015.Manager.AppUtil;
import com.dangjang.dj2015.R;

/**
 * Created by Tacademy on 2015-11-02.
 */
public class PasswordFindDialogFragment extends DialogFragment {
    private EditText name_editText,email_editText;

    public PasswordFindDialogFragment() {
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View innerView = View.inflate(getActivity(), R.layout.fragment_password_find_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle(AppUtil.getXmlString(R.string.passwordfinddialogfragment_title));
        name_editText = (EditText)innerView.findViewById(R.id.f_passwordfind_name_edittext);
        email_editText = (EditText)innerView.findViewById(R.id.f_passwordfind_email_edittext);
        Button button = (Button)innerView.findViewById(R.id.f_passwordfind_send_button);
        builder.setView(innerView);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = name_editText.getText().toString();
                String email = email_editText.getText().toString();
                if (name.length() == 0) {
                    Toast.makeText(getActivity(), AppUtil.getXmlString(R.string.name_empty), Toast.LENGTH_SHORT).show();
                } else if (email.length() == 0) {
                    Toast.makeText(getActivity(), AppUtil.getXmlString(R.string.email_empty), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), AppUtil.getXmlString(R.string.passwordfind_success), Toast.LENGTH_SHORT).show();
                    dismiss();
                }
            }
        });
        return builder.create();
    }
}

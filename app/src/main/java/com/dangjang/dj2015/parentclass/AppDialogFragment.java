package com.dangjang.dj2015.parentclass;

import android.app.DialogFragment;

/**
 * Created by Tacademy on 2015-10-30.
 */
public class AppDialogFragment extends DialogFragment {
    protected  FragmentCallbackIF mActivityListener;

    public void setOnFragmentCallbackIFListener(FragmentCallbackIF mActivityListener){
        this.mActivityListener = mActivityListener;
    }
}

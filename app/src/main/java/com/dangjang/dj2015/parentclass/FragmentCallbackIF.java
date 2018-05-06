package com.dangjang.dj2015.parentclass;

/**
 * Created by Tacademy on 2015-10-26.
 */
public interface FragmentCallbackIF {
    void doFragmentChange(AppFragment f,String tag,int animation_in,int animation_out,boolean backstack_clear);
    void doDialogShow(AppDialogFragment f,String tag);
    void setActionBarTitle(String name);
    void snackbarShow(String message);
    void showLoadingDialog();
    void hideLoadingDialog();
}

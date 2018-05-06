package com.dangjang.dj2015.parentclass;


import android.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import com.dangjang.dj2015.Manager.NetworkManager;
import com.dangjang.dj2015.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AppFragment extends Fragment implements ActivityToFragmentIF, NetworkManager.NetworkInterface{

    protected  FragmentCallbackIF mActivityListener;
    public int fragmentTitleStringResourceId;

    public void setOnFragmentCallbackIFListener(FragmentCallbackIF mActivityListener){
        this.mActivityListener = mActivityListener;
    }
    public void showMessage(String message){
        if(mActivityListener != null){
            mActivityListener.snackbarShow(message);
        }else{
            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(getActivity() instanceof AppActivity)
            ((AppActivity)getActivity()).setOnBackKeyPressedListener(this);
        if(mActivityListener != null && fragmentTitleStringResourceId != 0)
            mActivityListener.setActionBarTitle(getResources().getString(fragmentTitleStringResourceId));
    }

    @Override
    public boolean onBackKeyPressed() {
        return false;
    }

    @Override
    public void showLoadingDialog() {
        if(mActivityListener != null)
            mActivityListener.showLoadingDialog();
    }

    @Override
    public void hideLoadingDialog() {
        if(mActivityListener != null)
            mActivityListener.hideLoadingDialog();
    }

    @Override
    public void onResponse(int KEY, boolean success, Object object) {
        if(success){
            Log.i("success", "[KEY=" + KEY + " / object=" + object);
        }else{
            Throwable t = (Throwable)object;
            Log.e("fail","[KEY="+KEY+" / detail="+t.toString());
        }
    }

    public void networkErrorMsg(String log_name,String log_detail){
        networkErrorMsg(log_name, log_detail, "네트워크 오류입니다.\n다시 한번 시도해주세요.");
    }

    public void networkErrorMsg(String log_name,String log_detail,String snackbarMessage){
        showMessage(snackbarMessage);
        Log.e(log_name, log_detail);
    }
}

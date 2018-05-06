package com.dangjang.dj2015.Manager;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.util.DisplayMetrics;

import com.bumptech.glide.Glide;
import com.bumptech.glide.MemoryCategory;
import com.dangjang.dj2015.R;

/**
 * Created by Tacademy on 2015-10-27.
 */
public class MyApplication extends Application {
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        NetworkManager.instanceClear();
        AppUtil.overrideFont(getApplicationContext(), "SERIF", "fonts/nanumgothic.ttf");
        Glide.get(mContext).setMemoryCategory(MemoryCategory.HIGH);
    }

    public static Context getContext() {
        return mContext;
    }

    public static int[] getDisplaySize(){
        int result[] = new int[2];
        {
            DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
            result[0] = dm.widthPixels;
            result[1] = dm.heightPixels;

            int statusHeight = 0;
            int screenSizeType = (mContext.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK);

            if(screenSizeType != Configuration.SCREENLAYOUT_SIZE_XLARGE) {
                int resourceId = mContext.getResources().getIdentifier("status_bar_height", "dimen", "android");

                if (resourceId > 0) {
                    statusHeight = mContext.getResources().getDimensionPixelSize(resourceId);
                }
            }
            result[1] -= statusHeight;
        }
        return result;
    }
}

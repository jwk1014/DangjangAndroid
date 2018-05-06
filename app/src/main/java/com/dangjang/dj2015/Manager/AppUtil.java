package com.dangjang.dj2015.Manager;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.DimenRes;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.dangjang.dj2015.R;
import com.dangjang.dj2015.categoryselect.CategorySelectAdapter;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.lang.reflect.Field;

/**
 * Created by Tacademy on 2015-10-28.
 */
public class AppUtil {
    public final static String REGEX_HANGUL = "^[가-힣]{2,30}$";//ㄱ-ㅎㅏ-ㅣ
    public final static String REGEX_EMAIL = "^[_a-zA-Z0-9-\\\\.]+@[\\\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$";
    public final static String REGEX_PHONE = "^01([0|1|6|7|8|9]?)+([0-9]{7,8})$";
    public final static String REGEX_PASSWORD = "^(?=.*[a-zA-Z]+)(?=.*[0-9]+)(?=.*[!@#$%^*+=-]*).{8,16}$";
    public static void expandAnimation(final View view, final boolean expand, long time){
        if(time == 0)
            expandSpeed(view,expand);
        else
            expandAnimation(view, expand, time, false);
    }
    public static void expandSpeed(View view,boolean expand){
        try {
            view.measure(
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(((View) view.getParent()).getMeasuredWidth(), View.MeasureSpec.AT_MOST)
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
        view.setVisibility((expand)?View.VISIBLE:View.GONE);
        view.getLayoutParams().height = (expand)?view.getMeasuredHeight():0;
        view.requestLayout();
    }
    public static void expandAnimation(final View view, final boolean expand, long time, final boolean current) {
        try {
            view.measure(
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(((View) view.getParent()).getMeasuredWidth(), View.MeasureSpec.AT_MOST)
            );
        } catch (Exception e) {
            e.printStackTrace();
        }

        final int initialHeight = view.getMeasuredHeight();
        final int currentHeight = (current)?view.getLayoutParams().height:0;

        if(!current) {
            if (expand) {
                view.getLayoutParams().height = 0;
            } else {
                view.getLayoutParams().height = initialHeight;
            }
        }
        view.setVisibility(View.VISIBLE);

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                int newHeight = 0;
                if(current) {
                    newHeight = currentHeight + (int)((initialHeight - currentHeight) * interpolatedTime);
                }else if (expand) {
                    newHeight = (int) (initialHeight * interpolatedTime);
                } else {
                    newHeight = (int) (initialHeight * (1 - interpolatedTime));
                }
                view.getLayoutParams().height = newHeight;
                view.requestLayout();

                if (interpolatedTime == 1 && !expand)
                    view.setVisibility(View.GONE);
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };
        a.setDuration(time);
        view.startAnimation(a);
    }
    public static void expandAnimation(final ListView view, final CategorySelectAdapter adapter, final boolean expand, long time, final boolean current) {
        try {
            view.measure(
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec((int)(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, adapter.getCount() * 51 + 5, MyApplication.getContext().getResources().getDisplayMetrics())), View.MeasureSpec.AT_MOST)
            );
            //
        } catch (Exception e) {
            e.printStackTrace();
        }

        final int initialHeight = view.getMeasuredHeight();
        final int currentHeight = (current)?view.getLayoutParams().height:0;

        if(!current) {
            if (expand) {
                view.getLayoutParams().height = 0;
            } else {
                view.getLayoutParams().height = initialHeight;
            }
        }
        view.setVisibility(View.VISIBLE);

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                int newHeight = 0;
                if(current) {
                    newHeight = currentHeight + (int)((initialHeight - currentHeight) * interpolatedTime);
                }else if (expand) {
                    newHeight = (int) (initialHeight * interpolatedTime);
                } else {
                    newHeight = (int) (initialHeight * (1 - interpolatedTime));
                }
                view.getLayoutParams().height = newHeight;
                view.requestLayout();

                if (interpolatedTime == 1 && !expand)
                    view.setVisibility(View.GONE);
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };
        a.setDuration(time);
        view.startAnimation(a);
    }
//    public static void buttonSetOnClickListener(View v,View.OnClickListener clickListener,int... ids){
//        for(int id:ids)
//            (v.findViewById(id)).setOnClickListener(clickListener);
//    }
//    public static void textViewSetOnClickListener(View v,View.OnClickListener clickListener,int... ids){
//        for(int id:ids)
//            (v.findViewById(id)).setOnClickListener(clickListener);
//    }
    public static void viewsSetOnClickListener(View v,View.OnClickListener clickListener,int... ids){
        for(int id:ids)
            (v.findViewById(id)).setOnClickListener(clickListener);
    }
    public static String getXmlString(int strings_resource_id){
        return MyApplication.getContext().getResources().getString(strings_resource_id);
    }
    public static void overrideFont(Context context, String defaultFontNameToOverride, String customFontFileNameInAssets) {
        try {
            final Typeface customFontTypeface = Typeface.createFromAsset(context.getAssets(), customFontFileNameInAssets);

            final Field defaultFontTypefaceField = Typeface.class.getDeclaredField(defaultFontNameToOverride);
            defaultFontTypefaceField.setAccessible(true);
            defaultFontTypefaceField.set(null, customFontTypeface);
        } catch (Exception e) {
            Log.e("TypefaceUtil", "Can not set custom font " + customFontFileNameInAssets + " instead of " + defaultFontNameToOverride);
        }
    }
    public static void loadImage(Context context,String img_url,int img_size,ImageView imageView){
        //ImageLoader.getInstance().displayImage("http://bikee.s3.amazonaws.com/detail_1446776196619.jpg",imageView);
        Glide.with(context).load(img_url).placeholder(R.drawable.unloadedimage).fitCenter().thumbnail(0.0001f).into(imageView);
    }
    public static void loadRoundImage(final Context context,String img_url,int img_size,final ImageView imageView){
        Glide.with(context).load(img_url).asBitmap().fitCenter().thumbnail(0.001f).into( new BitmapImageViewTarget(imageView) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                imageView.setImageDrawable(circularBitmapDrawable);
            }
        });
    }
    public static void hideKeyboard(IBinder iBinder){
        InputMethodManager imm = (InputMethodManager)MyApplication.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm != null){
            imm.hideSoftInputFromWindow(iBinder, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
    public static void hideCurrentKeyboard(Activity activity){
        View view = activity.getCurrentFocus();
        if(view != null && (view instanceof EditText || view instanceof MaterialEditText || view instanceof SearchView)) {
            try {
                InputMethodManager inputManager = (InputMethodManager)MyApplication.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            } catch (Exception e) { }
        }
    }
    public static void restoreCurrentKeyboard(Activity activity){
        View view = activity.getCurrentFocus();
        if(view != null && (view instanceof EditText || view instanceof MaterialEditText || view instanceof SearchView)) {
            try {
                InputMethodManager inputManager = (InputMethodManager)MyApplication.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
            } catch (Exception e) { }
        }
    }
    public static int getDp(int dp_id){
        return (int)MyApplication.getContext().getResources().getDimension(dp_id);
    }

    public static int getResourceColor(int color_id){
        if(Build.VERSION.SDK_INT <= 23)
            return MyApplication.getContext().getResources().getColor(color_id);
        else
            return MyApplication.getContext().getResources().getColor(color_id,MyApplication.getContext().getResources().newTheme());
    }
    public static String getMoneyStr(int money){
        if(money < 1000)
            return ""+money;
        StringBuilder stringBuilder = new StringBuilder();
        int count = 0;
        for(int target = 1 ; money/target > 0 ; target*=10){
            if(count == 3) {
                stringBuilder.insert(0, ',');
                count = 0;
            }
            stringBuilder.insert(0,(money%(target*10))/target);
            count++;
        }
        return stringBuilder.toString();
    }
}

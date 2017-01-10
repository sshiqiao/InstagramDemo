package com.start.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.media.AudioManager;
import android.support.design.widget.Snackbar;
import android.text.Layout;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;

import static android.R.attr.targetSdkVersion;


public class Utils {
    public static final String NO_MORE_DATA = "no more data";
    public static final int INVISIBLE_ITEM_COUNT = 5;

    public static final int SEARCH_USER = 0;
    public static final int SEARCH_TAG = 1;
    public static final int SEARCH_LOCATION = 2;

    public static final String VIDEO_TYPE = "video";
    public static final String IMAGE_TYPE = "image";

    public static final int MEDIAVIEW_VIDEO_VIEW_TAG = 0;
    public static final int MEDIAVIEW_FIRST_FRAME_IMAGE_TAG = 1;
    public static final int MEDIAVIEW_ICON_IMAGE_TAG = 2;
    public static final int MEDIAVIEW_LIKE_IMAGE_TAG = 3;
    public static final int MEDIAVIEW_COMMENT_IMAGE_TAG = 4;

    public static int dp2px(int dpValue) {
        final float scale = IGApplication.getGlobalContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5);
    }
    public static void hideSoftInput(View view){
        InputMethodManager imm = (InputMethodManager) IGApplication.getGlobalContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    public static float currentSystemVolume(){
        AudioManager mAudioManager = (AudioManager) IGApplication.getGlobalContext().getSystemService(Context.AUDIO_SERVICE);
        int maxSystemVolume = mAudioManager.getStreamMaxVolume( AudioManager.STREAM_MUSIC );
        int currentSystemVolume = mAudioManager.getStreamVolume( AudioManager.STREAM_MUSIC );
        return (float)currentSystemVolume/maxSystemVolume;
    }
    public static int statusHeight(){
        int statusBarHeight = 0;
        int resourceId = IGApplication.getGlobalContext().getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = IGApplication.getGlobalContext().getResources().getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }
    public static int screenWidth(){
        WindowManager wm = (WindowManager) IGApplication.getGlobalContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metric = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metric);
        return metric.widthPixels;
    }
    public static int screenHeight(){
        WindowManager wm = (WindowManager) IGApplication.getGlobalContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metric = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metric);
        return metric.heightPixels;
    }
    public static String timeInterval(String createTime){
        String timeIntervalString = "";
        long timeInterval = System.currentTimeMillis()/1000 - Long.parseLong(createTime);
        if(timeInterval<60){
            timeIntervalString = "刚刚";
        }else if(timeInterval<(60*60)&&timeInterval>=60){
            timeIntervalString = timeInterval/60+"分钟前";
        }else if(timeInterval<(60*60*24)&&timeInterval>=(60*60)){
            timeIntervalString = timeInterval/60/60+"小时前";
        }else{
            timeIntervalString = timeInterval/60/60/24+"天前";
        }
        return timeIntervalString;
    }


    /**
     * Gson
     */
    @Expose
    protected static Gson gson = new Gson();

    public static Object jsonToModel(String json, Class<?> c) {
        return gson.fromJson(json, c);
    }

    public static String modelToJson(Object o) {
        return gson.toJson(o);
    }



    /**
     * Animation
     */
    public static class AnimationListener implements Animation.AnimationListener{
        @Override
        public void onAnimationStart(Animation animation) {
        }

        @Override
        public void onAnimationEnd(Animation animation) {

        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }
    public static TranslateAnimation translateAnimation(float fromXDelta, float toXDelta, float fromYDelta, float toYDelta, long duration){
        TranslateAnimation translateAnimation = new TranslateAnimation(fromXDelta, toXDelta, fromYDelta, toYDelta);
        translateAnimation.setDuration(duration);
        translateAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        translateAnimation.setFillAfter(true);
        return translateAnimation;
    }
    public static AlphaAnimation alphaAnimation(float fromAlpha, float toAlpha, long duration){
        AlphaAnimation alphaAnimation = new AlphaAnimation(fromAlpha, toAlpha);
        alphaAnimation.setDuration(duration);
        return alphaAnimation;
    }
    public static ScaleAnimation scaleAnimation(float fromX, float toX, float fromY, float toY, long duration){
        ScaleAnimation scaleAnimation = new ScaleAnimation(fromX, toX, fromY, toY, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(duration);
        return scaleAnimation;
    }
}

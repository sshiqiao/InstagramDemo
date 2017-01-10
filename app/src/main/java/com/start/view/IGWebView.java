package com.start.view;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebChromeClient;
import android.webkit.WebView;


public class IGWebView extends WebView{
    public IGWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        configureWebView();
    }

    public void configureWebView(){
        requestFocus();
        getSettings().setJavaScriptEnabled(true);
        getSettings().setSupportZoom(true);
        getSettings().setDomStorageEnabled(true);
        getSettings().setUseWideViewPort(true);
        getSettings().setLoadWithOverviewMode(true);
        getSettings().setSupportZoom(true);
        getSettings().setBuiltInZoomControls(true);
        setWebChromeClient(new WebChromeClient());
    }

}

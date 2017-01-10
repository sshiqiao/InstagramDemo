package com.start.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.start.bussiness.helper.HttpSubscriber;
import com.start.bussiness.helper.RxException;
import com.start.bussiness.oauth.IGOAuthBussiness;
import com.start.entity.response.IGOAuthUserData;
import com.start.utils.IGConfig;
import com.start.view.IGWebView;


public class OAuthActivity extends AppCompatActivity {

    private IGWebView webView;
    private IGConfig igClientInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oauth);

        if(IGConfig.readOAuthUserData()!=null){
            if(IGConfig.getIGOAuthUserData()==null){
                IGConfig.writeOAuthUserData(IGConfig.readOAuthUserData());
            }
            toMainActivity();
        }else {
            igClientInfo = IGConfig.getClientConfig();

            webView = (IGWebView) findViewById(R.id.webview);
            webView.addJavascriptInterface(this, "getHtmlBody");
            webView.loadUrl("https://api.instagram.com/oauth/authorize/?client_id="+igClientInfo.clientId+"&redirect_uri="+igClientInfo.redirectUrl+"&response_type=code&scope=basic+public_content+follower_list+comments+likes+relationships");
            webView.setWebViewClient(new WebViewClient() {
                public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                    return false;
                }
                public void onPageFinished(WebView view, String url) {
                    if (url.contains(igClientInfo.redirectUrl+"?code=")) {
                        view.loadUrl("javascript:window.getHtmlBody.getIGCode(" + "document.getElementsByTagName('body')[0].innerHTML);");
                    }
                }
            });
        }
    }
    public void getIGAccessToken() {
        IGOAuthBussiness.getIGUserInfo(new HttpSubscriber<IGOAuthUserData>() {
            @Override
            public void onSuccessed(IGOAuthUserData igoAuthUserData) {
                saveIGOAuthUserData(igoAuthUserData);
                toMainActivity();
            }
            @Override
            public void onFailed(RxException e) {
            }
        });
    }
    public void saveIGOAuthUserData(IGOAuthUserData igoAuthUserData){
        IGConfig.writeOAuthUserData(igoAuthUserData);
    }
    public void toMainActivity() {
        Intent toMainActivityIntent = new Intent(this, MainActivity.class);
        startActivity(toMainActivityIntent);
        finish();
    }
    /***android sdk api >= 17 add @JavascriptInterface***/
    @JavascriptInterface
    public void getIGCode(String code) {
        this.igClientInfo.code = code;
        webView.post(new Runnable() {
            public void run() {
                getIGAccessToken();
            }
        });
    }
}

package com.start.bussiness.oauth;


import com.start.bussiness.helper.HttpConfig;
import com.start.bussiness.helper.HttpSubscriber;
import com.start.entity.response.IGOAuthUserData;
import com.start.utils.IGConfig;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;



public class IGOAuthAPI {
    private static IGOAuthService service;
    private IGOAuthAPI(){
        service = (IGOAuthService) HttpConfig.getService(IGOAuthService.class);
    }
    static class SingletonHolder {
        private static IGOAuthAPI INSTANCE = new IGOAuthAPI();
    }
    public static IGOAuthAPI getInstance(){
        return SingletonHolder.INSTANCE;
    }
    public void getIGUserInfo(IGConfig igClientInfo, Subscriber<IGOAuthUserData> subscriber) {
        service.getIGUserInfo(igClientInfo.clientId
                , igClientInfo.clientSecret
                , igClientInfo.grantType
                , igClientInfo.redirectUrl
                , igClientInfo.code)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
}

package com.start.bussiness.oauth;

import com.start.entity.response.IGOAuthUserData;
import com.start.utils.IGConfig;
import rx.Subscriber;


public class IGOAuthBussiness {
    public static void getIGUserInfo(Subscriber<IGOAuthUserData> subscriber) {
        IGConfig igClientInfo = IGConfig.getClientConfig();
        IGOAuthAPI.getInstance().getIGUserInfo(igClientInfo, subscriber);
    }
}

package com.start.bussiness.oauth;


import com.start.entity.response.IGOAuthUserData;

import rx.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;


interface IGOAuthService {
    String OAUTH_ACCESS_TOKEN_PATH = "oauth/access_token";

    @FormUrlEncoded
    @POST(OAUTH_ACCESS_TOKEN_PATH)
    Observable<IGOAuthUserData> getIGUserInfo(@Field("client_id") String clientId
            , @Field("client_secret") String clientSecret
            , @Field("grant_type") String grantType
            , @Field("redirect_uri") String redirectUrl
            , @Field("code") String code);
}

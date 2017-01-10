package com.start.bussiness.likes;

import com.start.bussiness.helper.HttpConfig;
import com.start.bussiness.helper.HttpSubscriber;
import com.start.entity.common.IGBaseMeta;
import com.start.entity.response.IGLikeUsersData;
import com.start.entity.response.IGMeta;
import com.start.utils.IGConfig;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class IGLikesAPI {
    private static IGLikesService service;
    private IGLikesAPI(){
        service = (IGLikesService) HttpConfig.getService(IGLikesService.class);
    }
    static class SingletonHolder {
        private static IGLikesAPI INSTANCE = new IGLikesAPI();
    }
    public static IGLikesAPI getInstance(){
        return IGLikesAPI.SingletonHolder.INSTANCE;
    }
    public void likeUsers(String media_id, HttpSubscriber<IGLikeUsersData> subscriber){
        service.likeUsers(media_id, IGConfig.getIGOAuthUserData().access_token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
    public void likeMedia(String media_id, HttpSubscriber<IGMeta> subscriber){
        service.likeMedia(media_id, IGConfig.getIGOAuthUserData().access_token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
    public void deleteLike(String media_id, HttpSubscriber<IGMeta> subscriber){
        service.deleteLike(media_id, IGConfig.getIGOAuthUserData().access_token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
}

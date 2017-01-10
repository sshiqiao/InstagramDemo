package com.start.bussiness.comments;

import com.start.bussiness.helper.HttpConfig;
import com.start.bussiness.helper.HttpSubscriber;
import com.start.entity.response.IGComentsData;
import com.start.entity.response.IGMeta;
import com.start.utils.IGConfig;


import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class IGCommentsAPI {
    private static IGCommentsService service;
    private IGCommentsAPI(){
        service = (IGCommentsService) HttpConfig.getService(IGCommentsService.class);
    }
    static class SingletonHolder {
        private static IGCommentsAPI INSTANCE = new IGCommentsAPI();
    }
    public static IGCommentsAPI getInstance(){
        return IGCommentsAPI.SingletonHolder.INSTANCE;
    }

    public void comments(String media_id, HttpSubscriber<IGComentsData> subscriber){
        service.comments(media_id, IGConfig.getIGOAuthUserData().access_token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void commentMedia(String media_id, String text, HttpSubscriber<IGMeta> subscriber){
        service.commentMedia(media_id, text, IGConfig.getIGOAuthUserData().access_token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void deleteComment(String media_id, String comment_id, HttpSubscriber<IGMeta> subscriber){
        service.deleteComment(media_id, comment_id, IGConfig.getIGOAuthUserData().access_token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
}

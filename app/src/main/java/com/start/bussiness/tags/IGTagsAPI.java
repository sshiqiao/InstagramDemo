package com.start.bussiness.tags;

import com.start.bussiness.helper.HttpConfig;
import com.start.bussiness.helper.HttpSubscriber;
import com.start.entity.media.IGTag;
import com.start.entity.response.IGMediaData;
import com.start.entity.response.IGTagsData;
import com.start.utils.IGConfig;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class IGTagsAPI {
    private static IGTagsService service;
    private IGTagsAPI(){
        service = (IGTagsService) HttpConfig.getService(IGTagsService.class);
    }
    static class SingletonHolder {
        private static IGTagsAPI INSTANCE = new IGTagsAPI();
    }
    public static IGTagsAPI getInstance(){
        return IGTagsAPI.SingletonHolder.INSTANCE;
    }
    public void tag(String tag_name, HttpSubscriber<IGTag> subscriber){
        service.tag(tag_name, IGConfig.getIGOAuthUserData().access_token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
    public void tagMedias(String tag_name, int count, HttpSubscriber<IGMediaData> subscriber){
        service.tagMedias(tag_name,count, IGConfig.getIGOAuthUserData().access_token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
    public void searchTags(String tag_name, HttpSubscriber<IGTagsData> subscriber){
        service.searchTags(tag_name, IGConfig.getIGOAuthUserData().access_token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
}

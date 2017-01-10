package com.start.bussiness.users;

import com.start.bussiness.helper.HttpConfig;
import com.start.bussiness.helper.HttpSubscriber;
import com.start.entity.response.IGMediaData;
import com.start.entity.response.IGUserData;
import com.start.entity.response.IGSearchUsersData;
import com.start.utils.IGConfig;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class IGUsersAPI {
    private static IGUsersService service;
    private IGUsersAPI(){
        service = (IGUsersService) HttpConfig.getService(IGUsersService.class);
    }
    static class SingletonHolder {
        private static IGUsersAPI INSTANCE = new IGUsersAPI();
    }
    public static IGUsersAPI getInstance(){
        return SingletonHolder.INSTANCE;
    }
    public void user(HttpSubscriber<IGUserData> subscriber){
        service.user(IGConfig.getIGOAuthUserData().access_token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
    public void userById(String userId, HttpSubscriber<IGUserData> subscriber) {
        service.userById(userId,IGConfig.getIGOAuthUserData().access_token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
    public void userRecentMedia(int count, HttpSubscriber<IGMediaData> subscriber) {
        service.userRecentMedia(count, IGConfig.getIGOAuthUserData().access_token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
    public void userRecentMedia(int count, String max_id, HttpSubscriber<IGMediaData> subscriber) {
        service.userRecentMedia(count, max_id, IGConfig.getIGOAuthUserData().access_token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
    public void userRecentMediaById(String userId, int count, HttpSubscriber<IGMediaData> subscriber) {
        service.userRecentMediaById(userId, count, IGConfig.getIGOAuthUserData().access_token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
    public void userRecentMediaById(String userId, int count, String max_id, HttpSubscriber<IGMediaData> subscriber) {
        service.userRecentMediaById(userId, count, max_id, IGConfig.getIGOAuthUserData().access_token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
    public void userLikedMedia(int count,HttpSubscriber<IGMediaData> subscriber){
        service.userLikedMedia(count,IGConfig.getIGOAuthUserData().access_token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void searchUsers(String name, int count, HttpSubscriber<IGSearchUsersData> subscriber) {
        service.searchUsers(name,count,IGConfig.getIGOAuthUserData().access_token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
    public void searchUsers(String name, int count, String max_id, HttpSubscriber<IGSearchUsersData> subscriber) {
        service.searchUsers(name,count,max_id,IGConfig.getIGOAuthUserData().access_token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
}

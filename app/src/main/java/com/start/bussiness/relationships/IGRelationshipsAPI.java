package com.start.bussiness.relationships;

import com.start.bussiness.helper.HttpConfig;
import com.start.bussiness.helper.HttpSubscriber;
import com.start.entity.common.IGBaseRelationship;
import com.start.entity.common.IGRelationship;
import com.start.entity.response.IGFollowUsersData;
import com.start.entity.response.IGRequestedUserData;
import com.start.utils.IGConfig;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class IGRelationshipsAPI {
    private static IGRelationshipsService service;
    private IGRelationshipsAPI(){
        service = (IGRelationshipsService) HttpConfig.getService(IGRelationshipsService.class);
    }
    static class SingletonHolder {
        private static IGRelationshipsAPI INSTANCE = new IGRelationshipsAPI();
    }
    public static IGRelationshipsAPI getInstance(){
        return SingletonHolder.INSTANCE;
    }
    public void userFollows(HttpSubscriber<IGFollowUsersData> subscriber) {
        service.userFollows(IGConfig.getIGOAuthUserData().access_token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
    public void userFollows(int count, HttpSubscriber<IGFollowUsersData> subscriber) {
        service.userFollows(count, IGConfig.getIGOAuthUserData().access_token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
    public void userFollows(int count, String max_id, HttpSubscriber<IGFollowUsersData> subscriber) {
        service.userFollows(count, max_id, IGConfig.getIGOAuthUserData().access_token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
    public void userFollowBy(HttpSubscriber<IGFollowUsersData> subscriber) {
        service.userFollowBy(IGConfig.getIGOAuthUserData().access_token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
    public void userFollowBy(int count, HttpSubscriber<IGFollowUsersData> subscriber) {
        service.userFollowBy(count, IGConfig.getIGOAuthUserData().access_token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
    public void userFollowBy(int count, String max_id, HttpSubscriber<IGFollowUsersData> subscriber) {
        service.userFollowBy(count, max_id, IGConfig.getIGOAuthUserData().access_token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
    public void userRequestedBy(HttpSubscriber<IGRequestedUserData> subscriber) {
        service.userRequestedBy(IGConfig.getIGOAuthUserData().access_token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
    public void userRelationshipById(String user_id, HttpSubscriber<IGRelationship> subscriber){
        service.userRelationshipById(user_id,IGConfig.getIGOAuthUserData().access_token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
    public void modifyUserRelationshipById(String user_id, String action, HttpSubscriber<IGBaseRelationship> subscriber){
        service.modifyUserRelationshipById(user_id, action, IGConfig.getIGOAuthUserData().access_token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
}

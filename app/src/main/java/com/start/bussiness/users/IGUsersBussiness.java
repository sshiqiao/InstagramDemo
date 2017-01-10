package com.start.bussiness.users;

import com.start.bussiness.helper.HttpSubscriber;
import com.start.entity.response.IGMediaData;
import com.start.entity.response.IGSearchUsersData;
import com.start.entity.response.IGUserData;


public class IGUsersBussiness {
    public static void user(HttpSubscriber<IGUserData> subscriber){
        IGUsersAPI.getInstance().user(subscriber);
    }
    public static void userById(String userId, HttpSubscriber<IGUserData> subscriber) {
        IGUsersAPI.getInstance().userById(userId,subscriber);
    }
    public static void searchUsers(String name, int count, HttpSubscriber<IGSearchUsersData> subscriber) {
        IGUsersAPI.getInstance().searchUsers(name,count,subscriber);
    }
    public static void searchUsers(String name, int count, String max_id, HttpSubscriber<IGSearchUsersData> subscriber) {
        IGUsersAPI.getInstance().searchUsers(name,count,max_id,subscriber);
    }
    public static void userRecentMedia(int count, HttpSubscriber<IGMediaData> subscriber) {
        IGUsersAPI.getInstance().userRecentMedia(count, subscriber);
    }
    public static void userRecentMedia(int count, String max_id, HttpSubscriber<IGMediaData> subscriber) {
        IGUsersAPI.getInstance().userRecentMedia(count,max_id, subscriber);
    }
    public static void userRecentMediaById(String userId, int count, HttpSubscriber<IGMediaData> subscriber) {
        IGUsersAPI.getInstance().userRecentMediaById(userId, count, subscriber);
    }
    public static void userRecentMediaById(String userId, int count, String max_id, HttpSubscriber<IGMediaData> subscriber) {
        IGUsersAPI.getInstance().userRecentMediaById(userId, count, max_id, subscriber);
    }
    public static void userLikedMedia(int count,HttpSubscriber<IGMediaData> subscriber){
        IGUsersAPI.getInstance().userLikedMedia(count,subscriber);
    }
}

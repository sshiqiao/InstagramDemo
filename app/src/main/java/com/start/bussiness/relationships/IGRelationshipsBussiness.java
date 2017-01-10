package com.start.bussiness.relationships;

import com.start.bussiness.helper.HttpSubscriber;
import com.start.entity.common.IGBaseRelationship;
import com.start.entity.common.IGRelationship;
import com.start.entity.response.IGFollowUsersData;
import com.start.entity.response.IGRequestedUserData;


public class IGRelationshipsBussiness {
    public static void userFollows(HttpSubscriber<IGFollowUsersData> subscriber) {
        IGRelationshipsAPI.getInstance().userFollows(subscriber);
    }
    public static void userFollows(int count, HttpSubscriber<IGFollowUsersData> subscriber) {
        IGRelationshipsAPI.getInstance().userFollows(count, subscriber);
    }
    public static void userFollows(int count, String max_id, HttpSubscriber<IGFollowUsersData> subscriber) {
        IGRelationshipsAPI.getInstance().userFollows(count, max_id, subscriber);
    }
    public static void userFollowBy(HttpSubscriber<IGFollowUsersData> subscriber) {
        IGRelationshipsAPI.getInstance().userFollowBy(subscriber);
    }
    public static void userFollowBy(int count, HttpSubscriber<IGFollowUsersData> subscriber) {
        IGRelationshipsAPI.getInstance().userFollowBy(count, subscriber);
    }
    public static void userFollowBy(int count, String max_id, HttpSubscriber<IGFollowUsersData> subscriber) {
        IGRelationshipsAPI.getInstance().userFollowBy(count, max_id, subscriber);
    }
    public static void userRequestedBy(HttpSubscriber<IGRequestedUserData> subscriber) {
        IGRelationshipsAPI.getInstance().userRequestedBy(subscriber);
    }
    public void userRelationshipById(String user_id, HttpSubscriber<IGRelationship> subscriber){
        IGRelationshipsAPI.getInstance().userRelationshipById(user_id, subscriber);
    }
    public void modifyUserRelationshipById(String user_id, String action, HttpSubscriber<IGBaseRelationship> subscriber){
        IGRelationshipsAPI.getInstance().modifyUserRelationshipById(user_id, action, subscriber);
    }
}

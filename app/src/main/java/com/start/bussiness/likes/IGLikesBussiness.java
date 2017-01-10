package com.start.bussiness.likes;

import com.start.bussiness.helper.HttpSubscriber;
import com.start.entity.common.IGBaseMeta;
import com.start.entity.response.IGLikeUsersData;
import com.start.entity.response.IGMeta;


public class IGLikesBussiness {
    public static void likeUsers(String media_id, HttpSubscriber<IGLikeUsersData> subscriber){
        IGLikesAPI.getInstance().likeUsers(media_id, subscriber);
    }
    public static void likeMedia(String media_id, HttpSubscriber<IGMeta> subscriber){
        IGLikesAPI.getInstance().likeMedia(media_id, subscriber);
    }
    public static void deleteLike(String media_id, HttpSubscriber<IGMeta> subscriber){
        IGLikesAPI.getInstance().deleteLike(media_id, subscriber);
    }
}

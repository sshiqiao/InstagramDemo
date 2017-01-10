package com.start.bussiness.comments;

import com.start.bussiness.helper.HttpSubscriber;
import com.start.entity.response.IGComentsData;
import com.start.entity.response.IGMeta;


public class IGCommentsBussiness {
    public static void comments(String media_id, HttpSubscriber<IGComentsData> subscriber){
        IGCommentsAPI.getInstance().comments(media_id, subscriber);
    }

    public static void commentMedia(String media_id, String text, HttpSubscriber<IGMeta> subscriber){
        IGCommentsAPI.getInstance().commentMedia(media_id, text, subscriber);
    }

    public static void deleteComment(String media_id, String comment_id, HttpSubscriber<IGMeta> subscriber){
        IGCommentsAPI.getInstance().deleteComment(media_id, comment_id, subscriber);
    }
}

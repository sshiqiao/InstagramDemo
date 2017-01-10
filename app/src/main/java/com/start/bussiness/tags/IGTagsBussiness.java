package com.start.bussiness.tags;

import com.start.bussiness.helper.HttpSubscriber;
import com.start.entity.media.IGTag;
import com.start.entity.response.IGMediaData;
import com.start.entity.response.IGTagsData;


public class IGTagsBussiness {
    public static void tag(String tag_name, HttpSubscriber<IGTag> subscriber){
        IGTagsAPI.getInstance().tag(tag_name, subscriber);
    }
    public static void tagMedias(String tag_name, int count, HttpSubscriber<IGMediaData> subscriber){
        IGTagsAPI.getInstance().tagMedias(tag_name,count,subscriber);
    }
    public static void searchTags(String tag_name, HttpSubscriber<IGTagsData> subscriber){
        IGTagsAPI.getInstance().searchTags(tag_name,subscriber);
    }
}

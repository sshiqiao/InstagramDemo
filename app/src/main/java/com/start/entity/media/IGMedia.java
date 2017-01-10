package com.start.entity.media;

import com.start.entity.common.IGCaption;
import com.start.entity.common.IGLocation;
import com.start.entity.common.IGPosition;
import com.start.entity.user.IGCommonUser;

import java.io.Serializable;
import java.util.List;


public class IGMedia implements Serializable {

    public IGVideos videos;

    public Comments comments;

    public IGCaption caption;

    public Likes likes;

    public String link;

    public String created_time;

    public IGImages images;

    public String type;

    public List<UserInPhoto> users_in_photo;

    public String filter;

    public List<String> tags;

    public String id;

    public IGCommonUser user;

    public IGLocation location;


    public class Comments implements Serializable {
        public int count;
    }

    public class Likes implements Serializable {
        public int count;
    }

    public class UserInPhoto implements Serializable {

        public IGCommonUser user;

        public IGPosition position;

    }
}

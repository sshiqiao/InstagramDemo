package com.start.entity.media;

import com.start.entity.user.IGCommonUser;

import java.io.Serializable;


public class IGComment implements Serializable {

    public String id;

    public String text;

    public String created_time;

    public IGCommonUser from;

}

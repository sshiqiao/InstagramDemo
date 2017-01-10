package com.start.entity.common;

import com.start.entity.user.IGCommonUser;

import java.io.Serializable;


public class IGCaption implements Serializable {

    public String id;

    public String text;

    public String created_time;

    public IGCommonUser from;

}

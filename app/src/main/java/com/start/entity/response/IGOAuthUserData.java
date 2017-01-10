package com.start.entity.response;

import com.start.entity.user.IGNormalUser;

import java.io.Serializable;


public class IGOAuthUserData implements Serializable {

    public String access_token;

    public IGNormalUser user;

}

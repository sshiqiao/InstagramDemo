package com.start.entity.user;


public class IGStandardUser extends IGNormalUser {

    public Counts counts;

    public class Counts {

        public String media;

        public String follows;

        public String followed_by;

    }
}

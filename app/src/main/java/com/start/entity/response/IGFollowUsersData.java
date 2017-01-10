package com.start.entity.response;

import com.start.entity.common.IGPagination;
import com.start.entity.user.IGCommonUser;

import java.io.Serializable;
import java.util.List;



public class IGFollowUsersData implements Serializable {

    public IGPagination pagination;

    public List<IGCommonUser> data;

}

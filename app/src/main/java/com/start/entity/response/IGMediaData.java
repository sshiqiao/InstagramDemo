package com.start.entity.response;

import com.start.entity.common.IGPagination;
import com.start.entity.media.IGMedia;

import java.io.Serializable;
import java.util.List;


public class IGMediaData implements Serializable {

    public IGPagination pagination;

    public List<IGMedia> data;

}

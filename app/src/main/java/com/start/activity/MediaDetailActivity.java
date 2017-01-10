package com.start.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.start.entity.media.IGMedia;
import com.start.fragment.MediaDetailFragment;

public class MediaDetailActivity extends FragmentActivity {
    private MediaDetailFragment mediaDetailFragment;

    private IGMedia media;
    private int itemX;
    private int itemY;
    private boolean isFromVerticalList;
    private boolean isCommentListShow;
    private int columnNum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_detail);

        media = (IGMedia)getIntent().getExtras().getSerializable("IGMedia");
        itemX = getIntent().getExtras().getInt("x",0);
        itemY = getIntent().getExtras().getInt("y",0);
        isFromVerticalList = getIntent().getExtras().getBoolean("isFromVerticalList",false);
        isCommentListShow = getIntent().getExtras().getBoolean("isCommentListShow",false);
        columnNum = getIntent().getExtras().getInt("columnNum",3);

        mediaDetailFragment = (MediaDetailFragment)getSupportFragmentManager().findFragmentById(R.id.media_detail_fragment);
        mediaDetailFragment.setMediaDetailData(media, itemX, itemY, isFromVerticalList,isCommentListShow, columnNum);
    }
    @Override
    public void onBackPressed() {
        if(!mediaDetailFragment.isUserListFragmentVisible()&&!mediaDetailFragment.isCommentListFragmentVisible()){
            if(mediaDetailFragment.isFromVerticalList()){
                mediaDetailFragment.hideVerticalFragmentAnim();
            }else{
                mediaDetailFragment.hideHorizontalFragmentAnim();
            }
        }
    }
}

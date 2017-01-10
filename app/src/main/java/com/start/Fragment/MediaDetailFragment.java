package com.start.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.start.activity.R;
import com.start.bussiness.helper.HttpSubscriber;
import com.start.bussiness.helper.RxException;
import com.start.bussiness.likes.IGLikesBussiness;
import com.start.entity.media.IGMedia;
import com.start.entity.response.IGMeta;
import com.start.utils.IGConfig;
import com.start.utils.Utils;
import com.start.view.IGMediaView;

public class MediaDetailFragment extends Fragment implements View.OnClickListener, IGMediaView.OnClickImageListener {

    private View rootView;
    private FrameLayout mediaLayout;
    private IGMediaView videoView;
    private IGMediaView imageView;
    private SimpleDraweeView headPortrait;
    private TextView userName;
    private TextView text;
    private TextView time;
    private LinearLayout locationLayout;
    private TextView location;
    private TextView likeNum;
    private TextView commentNum;
    private UserListFragment userListFragment;
    private CommentListFragment commentListFragment;

    private IGMedia media;
    private int itemX;
    private int itemY;
    private int deltaY;
    private boolean isFromVerticalList;
    private boolean isCommentListShow;
    private int columnNum;

    private float scaleRatio;
    private boolean isLikeMedia;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_media_detail, container, false);

        mediaLayout = (FrameLayout)rootView.findViewById(R.id.media_layout);
        videoView = (IGMediaView)mediaLayout.findViewById(R.id.video_view);
        imageView = (IGMediaView)mediaLayout.findViewById(R.id.image_view);

        headPortrait = (SimpleDraweeView)rootView.findViewById(R.id.head_portrait);
        userName = (TextView)rootView.findViewById(R.id.user_name);
        text = (TextView)rootView.findViewById(R.id.text);
        time = (TextView)rootView.findViewById(R.id.time);
        locationLayout = (LinearLayout)rootView.findViewById(R.id.location_layout);
        location = (TextView)rootView.findViewById(R.id.location);
        likeNum = (TextView)rootView.findViewById(R.id.like_num);
        commentNum = (TextView)rootView.findViewById(R.id.comment_num);

        likeNum.setOnClickListener(this);
        commentNum.setOnClickListener(this);

        userListFragment = (UserListFragment)getChildFragmentManager().findFragmentByTag("fragment_user_list");
        commentListFragment = (CommentListFragment)getChildFragmentManager().findFragmentByTag("fragment_comment_list");
        return rootView;
    }

    public void setMediaDetailData(IGMedia media, int itemX, int itemY, boolean isFromVerticalList, boolean isCommentListShow, int columnNum) {
        this.itemX = itemX;
        this.itemY = itemY;
        this.media = media;
        this.isFromVerticalList = isFromVerticalList;
        this.isCommentListShow = isCommentListShow;
        this.columnNum = columnNum;
        likeNum.setText(media.likes.count+"次赞");
        commentNum.setText("全部"+media.comments.count+"条评论");

        scaleRatio = media.images.low_resolution.height/media.images.low_resolution.width;

        FrameLayout.LayoutParams frameLayoutParams =(FrameLayout.LayoutParams)(media.type.equals(Utils.VIDEO_TYPE)?videoView:imageView).getLayoutParams();
        frameLayoutParams.height = (int)(Utils.screenWidth()*scaleRatio);
        if(media.type.equals(Utils.VIDEO_TYPE)){
            videoView.setVideoURI(Uri.parse(media.videos.low_resolution.url));
            videoView.setVisibility(View.VISIBLE);
            videoView.setOnClickImageListener(this);
            if(isLikeMedia=IGConfig.isLikeMediaDataContained(media.id)){
                videoView.setLikeImageViewResource(R.drawable.ic_favorite_border_red_24dp);
            }
            imageView.setVisibility(View.GONE);
        }else {
            imageView.setImageURI(Uri.parse(media.images.standard_resolution.url));
            imageView.setVisibility(View.VISIBLE);
            imageView.setOnClickImageListener(this);
            if(isLikeMedia=IGConfig.isLikeMediaDataContained(media.id)){
                imageView.setLikeImageViewResource(R.drawable.ic_favorite_border_red_24dp);
            }
            videoView.setVisibility(View.GONE);
        }

        headPortrait.setImageURI(Uri.parse(media.user.profile_picture));
        time.setText(Utils.timeInterval(media.created_time));
        userName.setText(media.user.username);
        if(media.caption!=null) {
            text.setText(media.caption.text + "");
        }else{
            text.setText("");
        }
        if(media.location!=null){
            location.setText(media.location.name + "");
            locationLayout.setVisibility(View.VISIBLE);
        }else{
            locationLayout.setVisibility(View.GONE);
        }
        int[] location = new int[2];
        rootView.getLocationOnScreen(location);
        deltaY = itemY - Utils.statusHeight() - ((int)(Utils.screenWidth()*scaleRatio)+location[1]);
        if(isFromVerticalList) {
            showVerticalFragmentAnim();
        }else{
            showHorizontalFragmentAnim();
        }
    }
    public void likeMedia(){
        setLikeImageRes(true);
        IGLikesBussiness.likeMedia(media.id, new HttpSubscriber<IGMeta>() {
            @Override
            public void onSuccessed(IGMeta igMeta) {
                if(igMeta.meta.code==200) {
                    IGConfig.addLikeMediaData(media.id);
                }
            }
            @Override
            public void onFailed(RxException e) {
                setLikeImageRes(false);
            }
        });
    }

    public void deleteLike(){
        setLikeImageRes(false);
        IGLikesBussiness.deleteLike(media.id, new HttpSubscriber<IGMeta>() {
            @Override
            public void onSuccessed(IGMeta igMeta) {
                if(igMeta.meta.code==200) {
                    IGConfig.deleteLikeMediaData(media.id);
                }
            }
            @Override
            public void onFailed(RxException e) {
                setLikeImageRes(true);
            }
        });
    }

    public void setLikeImageRes(boolean isLikeMedia){
        this.isLikeMedia = isLikeMedia;
        if(isLikeMedia){
            if(media.type.equals(Utils.VIDEO_TYPE)){
                videoView.setLikeImageViewResource(R.drawable.ic_favorite_border_red_24dp);
            }else {
                imageView.setLikeImageViewResource(R.drawable.ic_favorite_border_red_24dp);
            }
        }else {
            if(media.type.equals(Utils.VIDEO_TYPE)){
                videoView.setLikeImageViewResource(R.drawable.ic_favorite_border_white_24dp);
            }else {
                imageView.setLikeImageViewResource(R.drawable.ic_favorite_border_white_24dp);
            }
        }
    }
    public void showVerticalFragmentAnim() {
        TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, deltaY, 0);
        translateAnimation.setDuration(250);
        translateAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        translateAnimation.setAnimationListener(new Utils.AnimationListener(){
            @Override
            public void onAnimationEnd(Animation animation) {
                if(isCommentListShow){
                    commentListFragment.comments(media.id);
                }
            }
        });
        rootView.startAnimation(translateAnimation);
    }

    public void hideVerticalFragmentAnim() {
        TranslateAnimation translateAnimation = Utils.translateAnimation(0, 0, 0, deltaY, 250);
        translateAnimation.setAnimationListener(new Utils.AnimationListener(){
            @Override
            public void onAnimationEnd(Animation animation) {
                getActivity().finish();
                getActivity().overridePendingTransition(0, 0);
            }
        });
        rootView.startAnimation(translateAnimation);
    }

    public void showHorizontalFragmentAnim() {
        AnimationSet set = new AnimationSet(true);
        TranslateAnimation translateAnimation = Utils.translateAnimation((itemX-Utils.screenWidth()/columnNum)*columnNum, 0,(itemY-Utils.statusHeight()-(Utils.screenHeight()-Utils.statusHeight())/columnNum)*columnNum, 0 , 350);
        set.addAnimation(translateAnimation);
        ScaleAnimation scaleAnimation = Utils.scaleAnimation(1.0f/columnNum, 1.0f/columnNum, 1.0f/columnNum, 1.0f/columnNum, 350);
        set.addAnimation(scaleAnimation);
        set.setAnimationListener(new Utils.AnimationListener(){
            @Override
            public void onAnimationEnd(Animation animation) {
                ScaleAnimation scaleAnimation = Utils.scaleAnimation(1.0f/columnNum, 1.0f, 1.0f/columnNum, 1.0f, 150);
                rootView.startAnimation(scaleAnimation);
            }
        });
        rootView.startAnimation(set);
    }

    public void hideHorizontalFragmentAnim() {
        ScaleAnimation scaleAnimation = Utils.scaleAnimation(1.0f, 1.0f/columnNum, 1.0f, 1.0f/columnNum, 150);
        scaleAnimation.setAnimationListener(new Utils.AnimationListener(){
            @Override
            public void onAnimationEnd(Animation animation) {
                AnimationSet set = new AnimationSet(true);
                TranslateAnimation translateAnimation = Utils.translateAnimation(0, (itemX-Utils.screenWidth()/columnNum)*columnNum, 0, (itemY-Utils.statusHeight()-(Utils.screenHeight()-Utils.statusHeight())/columnNum)*columnNum , 350);
                set.addAnimation(translateAnimation);
                ScaleAnimation scaleAnimation = Utils.scaleAnimation(1.0f/columnNum, 1.0f/columnNum, 1.0f/columnNum, 1.0f/columnNum, 350);
                set.addAnimation(scaleAnimation);
                set.setAnimationListener(new Utils.AnimationListener(){
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        getActivity().finish();
                        getActivity().overridePendingTransition(0, 0);
                    }
                });
                rootView.startAnimation(set);
            }
        });
        rootView.startAnimation(scaleAnimation);
    }

    public boolean isUserListFragmentVisible(){
        if(userListFragment.isRootViewVisible()){
            userListFragment.setRootViewVisible(false);
            return true;
        }else{
            return false;
        }
    }

    public boolean isCommentListFragmentVisible(){
        if(commentListFragment.isRootViewVisible()){
            commentListFragment.setRootViewVisible(false);
            return true;
        }else{
            return false;
        }
    }

    public boolean isFromVerticalList(){
        return isFromVerticalList;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.like_num:
                userListFragment.likeUsers(media.id);
                break;
            case R.id.comment_num:
                commentListFragment.comments(media.id);
                break;
        }
    }

    @Override
    public void OnClickImage(View v) {
        switch ((int)v.getTag()){
            case Utils.MEDIAVIEW_COMMENT_IMAGE_TAG:
                commentListFragment.comments(media.id);
                break;
            case Utils.MEDIAVIEW_LIKE_IMAGE_TAG:
                if(isLikeMedia){
                    deleteLike();
                }else {
                    likeMedia();
                }
                break;
        }
    }
}

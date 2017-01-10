package com.start.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.TranslateAnimation;

import com.start.activity.MediaDetailActivity;
import com.start.activity.R;
import com.start.activity.UserMessageActivity;
import com.start.adapter.FollowUsersMediaRecyclerViewAdapter;
import com.start.bussiness.helper.HttpSubscriber;
import com.start.bussiness.helper.RxException;
import com.start.bussiness.relationships.IGRelationshipsBussiness;
import com.start.bussiness.users.IGUsersBussiness;
import com.start.entity.media.IGMedia;
import com.start.entity.response.IGFollowUsersData;
import com.start.entity.response.IGMediaData;
import com.start.entity.user.IGCommonUser;
import com.start.utils.Utils;

import java.util.ArrayList;
import java.util.List;


public class FollowUsersMediaFragment extends Fragment implements FollowUsersMediaRecyclerViewAdapter.OnItemClickListenter {
    private View rootView;
    private RecyclerView mRecyclerView;
    private boolean isLoading;

    private List<FollowUsersMediaRecyclerViewAdapter.FollowUsersMedia> data = new ArrayList<>();
    private int count = 10;
    private String maxId;
    private boolean isFollowBy;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isFollowBy = getArguments().getBoolean("isFollowBy");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_follow_users_media, container, false);
        if(isFollowBy){
            userFollowBy();
        }else {
            userFollows();
        }
        showFragmentAnim();
        return rootView;
    }
    public void initRecyclerView() {
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.follow_users_media_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        FollowUsersMediaRecyclerViewAdapter mediaRecyclerViewAdapter = new FollowUsersMediaRecyclerViewAdapter(getActivity(), data);
        mediaRecyclerViewAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(mediaRecyclerViewAdapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int totalItemCount = layoutManager.getItemCount();
                int lastVisibleItem = layoutManager.findLastVisibleItemPosition();
                if (!isLoading && totalItemCount < (lastVisibleItem + Utils.INVISIBLE_ITEM_COUNT)) {
                    if (!Utils.NO_MORE_DATA.equals(maxId)) {
                        if(isFollowBy){
                            loadMoreUserFollowBy();
                        }else {
                            loadMoreUserFollows();
                        }
                        isLoading = true;
                    }
                }
            }
        });
    }

    public void addDataUser(IGFollowUsersData igFollowUsersData){
        for(IGCommonUser user : igFollowUsersData.data){
            FollowUsersMediaRecyclerViewAdapter.FollowUsersMedia followUsersMedia = new FollowUsersMediaRecyclerViewAdapter.FollowUsersMedia();
            followUsersMedia.user = user;
            data.add(followUsersMedia);
            loadUserRecentMedia(user);
        }
    }
    public void addDataMediaList(IGCommonUser user, IGMediaData igMediaData){
        for(FollowUsersMediaRecyclerViewAdapter.FollowUsersMedia followUsersMedia : data){
            if(followUsersMedia.user.id.equals(user.id)) {
                followUsersMedia.mediaList = igMediaData.data;
                break;
            }
        }
    }
    public void setMaxId(IGFollowUsersData igFollowUsersData) {
        if (igFollowUsersData.pagination != null) {
            if (igFollowUsersData.pagination.next_max_id != null) {
                maxId = igFollowUsersData.pagination.next_max_id;
            } else {
                maxId = Utils.NO_MORE_DATA;
            }
        } else {
            maxId = Utils.NO_MORE_DATA;
        }
    }
    public void userFollows(){
        IGRelationshipsBussiness.userFollows(count, new HttpSubscriber<IGFollowUsersData>() {
            @Override
            public void onSuccessed(IGFollowUsersData igFollowUsersData) {
                addDataUser(igFollowUsersData);
                setMaxId(igFollowUsersData);
                initRecyclerView();
            }
            @Override
            public void onFailed(RxException e) {

            }
        });
    }
    public void loadMoreUserFollows(){
        IGRelationshipsBussiness.userFollows(count, maxId, new HttpSubscriber<IGFollowUsersData>() {
            @Override
            public void onSuccessed(IGFollowUsersData igFollowUsersData) {
                addDataUser(igFollowUsersData);
                setMaxId(igFollowUsersData);
                mRecyclerView.getAdapter().notifyDataSetChanged();
            }
            @Override
            public void onFailed(RxException e) {}
        });
    }
    public void userFollowBy(){
        IGRelationshipsBussiness.userFollowBy(count, new HttpSubscriber<IGFollowUsersData>() {
            @Override
            public void onSuccessed(IGFollowUsersData igFollowUsersData) {
                addDataUser(igFollowUsersData);
                setMaxId(igFollowUsersData);
                initRecyclerView();
            }
            @Override
            public void onFailed(RxException e) {

            }
        });
    }
    public void loadMoreUserFollowBy(){
        IGRelationshipsBussiness.userFollowBy(count, maxId, new HttpSubscriber<IGFollowUsersData>() {
            @Override
            public void onSuccessed(IGFollowUsersData igFollowUsersData) {
                addDataUser(igFollowUsersData);
                setMaxId(igFollowUsersData);
                mRecyclerView.getAdapter().notifyDataSetChanged();
            }
            @Override
            public void onFailed(RxException e) {

            }
        });
    }
    public void loadUserRecentMedia(final IGCommonUser user) {
        IGUsersBussiness.userRecentMediaById(user.id, count, new HttpSubscriber<IGMediaData>() {
            @Override
            public void onSuccessed(IGMediaData igMediaData) {
                addDataMediaList(user, igMediaData);
                mRecyclerView.getAdapter().notifyDataSetChanged();
            }
            @Override
            public void onFailed(RxException e) {
            }
        });
    }
    public void showFragmentAnim() {
        TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, Utils.screenHeight(), 0);
        translateAnimation.setDuration(250);
        translateAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        rootView.startAnimation(translateAnimation);
    }
    public void hideFragmentAnim() {
        TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, 0, Utils.screenHeight());
        translateAnimation.setDuration(250);
        translateAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        rootView.startAnimation(translateAnimation);
    }

    @Override
    public void OnItemClick(View view, int userPosition, int mediaPosition) {
        if(mediaPosition==-1) {
            IGCommonUser user = data.get(userPosition).user;
            Intent toUserMessageActivity = new Intent(getActivity(), UserMessageActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("IGCommonUser", user);
            toUserMessageActivity.putExtras(bundle);
            startActivity(toUserMessageActivity);
        }else{
            IGMedia media = data.get(userPosition).mediaList.get(mediaPosition);
            int[] location = new int[2];
            view.getLocationOnScreen(location);
            Intent toMediaDetailActivityIntent = new Intent(getActivity(), MediaDetailActivity.class);
            toMediaDetailActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            Bundle bundle = new Bundle();
            bundle.putInt("x", location[0]);
            bundle.putInt("y", location[1]);
            bundle.putSerializable("IGMedia", media);
            bundle.putBoolean("isFromVerticalList",false);
            bundle.putBoolean("isCommentListShow",false);
//        bundle.putInt("columnNum",4);
            toMediaDetailActivityIntent.putExtras(bundle);
            startActivity(toMediaDetailActivityIntent);
        }
    }
}

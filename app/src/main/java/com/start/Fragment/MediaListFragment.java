package com.start.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.start.activity.MediaDetailActivity;
import com.start.activity.R;
import com.start.adapter.MediaHorizontalRecyclerViewAdapter;
import com.start.adapter.MediaVerticalRecyclerViewAdapter;
import com.start.bussiness.helper.HttpSubscriber;
import com.start.bussiness.helper.RxException;
import com.start.bussiness.likes.IGLikesBussiness;
import com.start.bussiness.users.IGUsersBussiness;
import com.start.entity.media.IGMedia;
import com.start.entity.response.IGMediaData;
import com.start.entity.response.IGMeta;
import com.start.utils.IGConfig;
import com.start.utils.Utils;

import java.util.List;

public class MediaListFragment extends Fragment implements MediaVerticalRecyclerViewAdapter.OnItemClickListenter,MediaHorizontalRecyclerViewAdapter.OnItemClickListenter{
    private View rootView;

    private RecyclerView mRecyclerView;
    private boolean isLoading;

    private List<IGMedia> data;
    private int count = 10;
    private String maxId;
    private String userId;
    private boolean isFromVerticalList;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_media_list, container, false);
        return rootView;
    }
    public void setMaxId(IGMediaData igMediaData) {
        if (igMediaData.pagination != null) {
            if (igMediaData.pagination.next_max_id != null) {
                maxId = igMediaData.pagination.next_max_id;
            } else {
                maxId = Utils.NO_MORE_DATA;
            }
        } else {
            maxId = Utils.NO_MORE_DATA;
        }
    }

    public void initRecyclerView() {
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.media_recycler_view);
        setHorizontalAdapter();
        mRecyclerView.addOnScrollListener(
                new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int totalItemCount = layoutManager.getItemCount();
                int lastVisibleItem = layoutManager.findLastVisibleItemPosition();
                if (!isLoading && totalItemCount < (lastVisibleItem + Utils.INVISIBLE_ITEM_COUNT)) {
                    if (!Utils.NO_MORE_DATA.equals(maxId)) {
                        loadMoreUserRecentMedia();
                        isLoading = true;
                    }
                }
            }
        });
    }
    public void setVerticalAdapter() {
        isFromVerticalList = true;
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        MediaVerticalRecyclerViewAdapter mediaRecyclerViewAdapter = new MediaVerticalRecyclerViewAdapter(getActivity(), data);
        mRecyclerView.setAdapter(mediaRecyclerViewAdapter);
        mediaRecyclerViewAdapter.setOnItemClickListener(this);
    }
    public void setHorizontalAdapter() {
        isFromVerticalList = false;
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),3));
        MediaHorizontalRecyclerViewAdapter mediaHorizontalRecyclerViewAdapter = new MediaHorizontalRecyclerViewAdapter(getActivity(),data,3);
        mRecyclerView.setAdapter(mediaHorizontalRecyclerViewAdapter);
        mediaHorizontalRecyclerViewAdapter.setOnItemClickListener(this);
    }
    public void userRecentMedia(String userId) {
        this.userId = userId;
        IGUsersBussiness.userRecentMediaById(userId, count, new HttpSubscriber<IGMediaData>() {
            @Override
            public void onSuccessed(IGMediaData igMediaData) {
                data = igMediaData.data;
                setMaxId(igMediaData);
                initRecyclerView();
            }
            @Override
            public void onFailed(RxException e) {
            }
        });
    }

    public void loadMoreUserRecentMedia() {
        IGUsersBussiness.userRecentMediaById(userId, count, maxId, new HttpSubscriber<IGMediaData>() {
            @Override
            public void onSuccessed(IGMediaData igMediaData) {
                data.addAll(igMediaData.data);
                setMaxId(igMediaData);
                mRecyclerView.getAdapter().notifyDataSetChanged();
                isLoading = false;
            }
            @Override
            public void onFailed(RxException e) {}
        });
    }
    public void toMediaDetailActivity(View view, int position, boolean isFromVerticalList, boolean isCommentListShow){
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        Intent toMediaDetailActivityIntent = new Intent(getActivity(), MediaDetailActivity.class);
        toMediaDetailActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        Bundle bundle = new Bundle();
        bundle.putInt("x", location[0]);
        bundle.putInt("y", location[1]);
        bundle.putSerializable("IGMedia", data.get(position));
        bundle.putBoolean("isFromVerticalList",isFromVerticalList);
        bundle.putBoolean("isCommentListShow",isCommentListShow);
        toMediaDetailActivityIntent.putExtras(bundle);
        startActivity(toMediaDetailActivityIntent);
    }
    public void likeMedia(final IGMedia media){
        IGConfig.addLikeMediaData(media.id);
        mRecyclerView.getAdapter().notifyDataSetChanged();
        IGLikesBussiness.likeMedia(media.id, new HttpSubscriber<IGMeta>() {
            @Override
            public void onSuccessed(IGMeta igMeta) {
            }
            @Override
            public void onFailed(RxException e) {
                IGConfig.deleteLikeMediaData(media.id);
                mRecyclerView.getAdapter().notifyDataSetChanged();
            }
        });
    }
    public void deleteLike(final IGMedia media){
        IGConfig.deleteLikeMediaData(media.id);
        mRecyclerView.getAdapter().notifyDataSetChanged();
        IGLikesBussiness.deleteLike(media.id, new HttpSubscriber<IGMeta>() {
            @Override
            public void onSuccessed(IGMeta igMeta) {
            }
            @Override
            public void onFailed(RxException e) {
                IGConfig.addLikeMediaData(media.id);
                mRecyclerView.getAdapter().notifyDataSetChanged();
            }
        });
    }

    @Override
    public void OnItemClick(View view, int position) {
        IGMedia media = data.get(position);
        if(view.getTag()==null){
            toMediaDetailActivity(view, position, isFromVerticalList, false);
        }else {
            switch ((int) view.getTag()) {
                case Utils.MEDIAVIEW_COMMENT_IMAGE_TAG:
                    toMediaDetailActivity(view, position, isFromVerticalList, true);
                    break;
                case Utils.MEDIAVIEW_LIKE_IMAGE_TAG:
                    if(IGConfig.isLikeMediaDataContained(media.id)){
                        deleteLike(media);
                    }else{
                        likeMedia(media);
                    }
                    break;
            }
        }
    }
}


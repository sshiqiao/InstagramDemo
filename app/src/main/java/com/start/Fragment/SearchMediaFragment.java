package com.start.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.start.bussiness.helper.HttpSubscriber;
import com.start.bussiness.helper.RxException;
import com.start.bussiness.locations.IGLocationsBussiness;
import com.start.bussiness.tags.IGTagsBussiness;
import com.start.entity.media.IGMedia;
import com.start.entity.response.IGMediaData;
import com.start.utils.Utils;

import java.util.List;


public class SearchMediaFragment extends Fragment implements MediaHorizontalRecyclerViewAdapter.OnItemClickListenter {
    private View rootView;
    private RecyclerView mRecyclerView;
    private boolean isLoading;
    private List<IGMedia> data;
    private String tagName;
    private String locationId;
    private int searchType;
    private String maxId;
    private int count = 10;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tagName = getArguments().getString("tagName");
        locationId = getArguments().getString("locationId");
        searchType = getArguments().getInt("searchType");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_search_media, container, false);

        switch (searchType){
            case Utils.SEARCH_TAG:
                tagMedias();
                break;
            case Utils.SEARCH_LOCATION:
                locationMedias();
                break;
        }
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
        mRecyclerView = (RecyclerView)rootView.findViewById(R.id.search_media_recycler_view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),3));
        MediaHorizontalRecyclerViewAdapter mediaHorizontalRecyclerViewAdapter = new MediaHorizontalRecyclerViewAdapter(getActivity(),data,3);
        mRecyclerView.setAdapter(mediaHorizontalRecyclerViewAdapter);
        mediaHorizontalRecyclerViewAdapter.setOnItemClickListener(this);
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
//                                loadMoreMedia();
                                isLoading = true;
                            }
                        }
                    }
                });
    }
    public void tagMedias(){
        IGTagsBussiness.tagMedias(tagName, count, new HttpSubscriber<IGMediaData>() {
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
    public void locationMedias(){
        IGLocationsBussiness.locationMedias(locationId, new HttpSubscriber<IGMediaData>() {
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

    @Override
    public void OnItemClick(View view, int position) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        Intent toMediaDetailActivityIntent = new Intent(getActivity(), MediaDetailActivity.class);
        toMediaDetailActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        Bundle bundle = new Bundle();
        bundle.putInt("x", location[0]);
        bundle.putInt("y", location[1]);
        bundle.putSerializable("IGMedia", data.get(position));
        bundle.putBoolean("isFromVerticalList",false);
        bundle.putBoolean("isCommentListShow",false);
//        bundle.putInt("columnNum",4);
        toMediaDetailActivityIntent.putExtras(bundle);
        startActivity(toMediaDetailActivityIntent);
    }
}

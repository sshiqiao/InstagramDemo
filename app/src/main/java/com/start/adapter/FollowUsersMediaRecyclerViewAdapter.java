package com.start.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.start.activity.R;
import com.start.entity.media.IGMedia;
import com.start.entity.user.IGCommonUser;
import com.start.utils.Utils;

import java.util.List;

/**
 * Created by qiao on 2017/1/5.
 */

public class FollowUsersMediaRecyclerViewAdapter extends RecyclerView.Adapter<FollowUsersMediaRecyclerViewAdapter.MediaHolder>{
    private final LayoutInflater mLayoutInflater;
    private final Context mContext;
    private List<FollowUsersMedia> data;

    private int itemPadding;
    private int itemWidth;
    private int itemCount = 3;
    private OnItemClickListenter onItemClickListenter;

    public FollowUsersMediaRecyclerViewAdapter(Context context, List<FollowUsersMedia> data) {
        this.data = data;
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        itemPadding = Utils.dp2px(0);
        itemWidth = (Utils.screenWidth()-itemPadding*(itemCount-1))/itemCount;
    }

    @Override
    public FollowUsersMediaRecyclerViewAdapter.MediaHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FollowUsersMediaRecyclerViewAdapter.MediaHolder(mContext, mLayoutInflater.inflate(R.layout.item_follow_users_media, parent, false));
    }

    @Override
    public void onBindViewHolder(final FollowUsersMediaRecyclerViewAdapter.MediaHolder holder, final int position) {
        FollowUsersMedia followUsersMedia = data.get(position);
        final IGCommonUser user = followUsersMedia.user;
        List<IGMedia> mediaList = followUsersMedia.mediaList;
        holder.headPortrait.setImageURI(Uri.parse(user.profile_picture));
        holder.userName.setText(user.username);
        if(mediaList!=null){
            if(mediaList.size()!=0) {
                if (mediaList.get(0).caption != null) {
                    holder.recentText.setText(mediaList.get(0).caption.text);
                } else {
                    holder.recentText.setText("");
                }
                holder.data= mediaList;
                holder.userRecentMediaRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
                MediaHorizontalRecyclerViewAdapter mediaHorizontalRecyclerViewAdapter = new MediaHorizontalRecyclerViewAdapter(mContext,holder.data,3);
                holder.userRecentMediaRecyclerView.setAdapter(mediaHorizontalRecyclerViewAdapter);
                mediaHorizontalRecyclerViewAdapter.setOnItemClickListener(new MediaHorizontalRecyclerViewAdapter.OnItemClickListenter() {
                    @Override
                    public void OnItemClick(View view, int mediaPosition) {
                        if(onItemClickListenter!=null){
                            onItemClickListenter.OnItemClick(view, position, mediaPosition);
                        }
                    }
                });
                holder.userRecentMediaRecyclerView.setVisibility(View.VISIBLE);
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)holder.userRecentMediaRecyclerView.getLayoutParams();
                params.height = itemWidth+Utils.dp2px(10);
            }else {
                holder.recentText.setText(R.string.no_update);
                holder.userRecentMediaRecyclerView.setVisibility(View.GONE);
            }
        } else{
            holder.recentText.setText("");
            holder.userRecentMediaRecyclerView.setVisibility(View.GONE);
        }
        holder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListenter!=null){
                    onItemClickListenter.OnItemClick(v, position, -1);
                }
            }
        });
    }
    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public void setOnItemClickListener(OnItemClickListenter onItemClickListener){
        this.onItemClickListenter = onItemClickListener;
    }
    public interface OnItemClickListenter {
        void OnItemClick(View view, int userPosition, int mediaPosition);
    }
    public static class MediaHolder extends RecyclerView.ViewHolder {
        LinearLayout itemLayout;
        SimpleDraweeView headPortrait;
        TextView userName;
        TextView recentText;
        RecyclerView userRecentMediaRecyclerView;
        List<IGMedia> data;
        MediaHolder(final Context context, View view) {
            super(view);
            itemLayout = (LinearLayout)view.findViewById(R.id.item_follow_users_media_layout);
            headPortrait = (SimpleDraweeView)view.findViewById(R.id.head_portrait);
            userName = (TextView)view.findViewById(R.id.user_name);
            recentText = (TextView)view.findViewById(R.id.recent_text);
            userRecentMediaRecyclerView = (RecyclerView)view.findViewById(R.id.user_recent_media_recycler_view);

        }
    }
    public static class FollowUsersMedia{
        public IGCommonUser user;
        public List<IGMedia> mediaList;
    }
}

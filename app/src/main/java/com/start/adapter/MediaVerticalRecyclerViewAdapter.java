package com.start.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.start.activity.R;
import com.start.entity.media.IGMedia;
import com.start.utils.IGConfig;
import com.start.utils.Utils;
import com.start.view.IGMediaView;

import java.util.List;


public class MediaVerticalRecyclerViewAdapter extends RecyclerView.Adapter<MediaVerticalRecyclerViewAdapter.MediaHolder> {
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private List<IGMedia> data;
    private OnItemClickListenter onItemClickListenter;
    public MediaVerticalRecyclerViewAdapter(Context context, List<IGMedia> data) {
        this.data = data;
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public MediaHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MediaHolder(mLayoutInflater.inflate(R.layout.item_vertical_media, parent, false));
    }

    @Override
    public void onBindViewHolder(final MediaHolder holder, final int position) {
        final IGMedia media = data.get(position);
        float scaleRatio = media.images.low_resolution.height/media.images.low_resolution.width;
        IGMediaView videoView = (IGMediaView)holder.mediaLayout.findViewById(R.id.video_view);
        IGMediaView imageView = (IGMediaView)holder.mediaLayout.findViewById(R.id.image_view);
        FrameLayout.LayoutParams frameLayoutParams =(FrameLayout.LayoutParams)(media.type.equals(Utils.VIDEO_TYPE)?videoView:imageView).getLayoutParams();
        frameLayoutParams.height = (int)(Utils.screenWidth()*scaleRatio);
        if(media.type.equals(Utils.VIDEO_TYPE)){
            videoView.setVideoURI(Uri.parse(media.videos.low_resolution.url));
            videoView.setVisibility(View.VISIBLE);
            videoView.setLikeImageViewResource(IGConfig.isLikeMediaDataContained(media.id)?R.drawable.ic_favorite_border_red_24dp:R.drawable.ic_favorite_border_white_24dp);
            videoView.setOnClickImageListener(new IGMediaView.OnClickImageListener() {
                @Override
                public void OnClickImage(View v) {
                    if(onItemClickListenter!=null){
                        onItemClickListenter.OnItemClick(v, position);
                    }
                }
            });
            imageView.setVisibility(View.GONE);
        }else {
            imageView.setImageURI(Uri.parse(media.images.standard_resolution.url));
            imageView.setVisibility(View.VISIBLE);
            imageView.setLikeImageViewResource(IGConfig.isLikeMediaDataContained(media.id)?R.drawable.ic_favorite_border_red_24dp:R.drawable.ic_favorite_border_white_24dp);
            imageView.setOnClickImageListener(new IGMediaView.OnClickImageListener() {
                @Override
                public void OnClickImage(View v) {
                    if(onItemClickListenter!=null){
                        onItemClickListenter.OnItemClick(v, position);
                    }
                }
            });
            videoView.setVisibility(View.GONE);
        }

        holder.headPortrait.setImageURI(Uri.parse(media.user.profile_picture));
        holder.time.setText(Utils.timeInterval(media.created_time));
        holder.userName.setText(media.user.username);
        holder.text.setText(media.caption!=null?media.caption.text + "":"");
        if(media.location!=null){
            holder.location.setText(media.location.name + "");
            holder.locationLayout.setVisibility(View.VISIBLE);
        }else{
            holder.locationLayout.setVisibility(View.GONE);
        }
        holder.messageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListenter!=null){
                    onItemClickListenter.OnItemClick(v, position);
                }
            }
        });
    }
    public void setOnItemClickListener(OnItemClickListenter onItemClickListener){
        this.onItemClickListenter = onItemClickListener;
    }

    public interface OnItemClickListenter {
        void OnItemClick(View view, int position);
    }
    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public static class MediaHolder extends RecyclerView.ViewHolder {
        FrameLayout mediaLayout;
        RelativeLayout messageLayout;
        SimpleDraweeView headPortrait;
        TextView userName;
        TextView text;
        TextView time;
        LinearLayout locationLayout;
        TextView location;
        MediaHolder(View view) {
            super(view);
            mediaLayout = (FrameLayout)view.findViewById(R.id.media_layout);
            messageLayout = (RelativeLayout)view.findViewById(R.id.message_layout);
            headPortrait = (SimpleDraweeView)view.findViewById(R.id.head_portrait);
            userName = (TextView)view.findViewById(R.id.user_name);
            text = (TextView)view.findViewById(R.id.text);
            time = (TextView)view.findViewById(R.id.time);
            locationLayout = (LinearLayout)view.findViewById(R.id.location_layout);
            location = (TextView)view.findViewById(R.id.location);
        }
    }

}
package com.start.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.facebook.drawee.view.SimpleDraweeView;
import com.start.activity.R;
import com.start.entity.media.IGMedia;
import com.start.utils.Utils;

import java.util.List;

/**
 * Created by qiao on 2017/1/5.
 */

public class MediaHorizontalRecyclerViewAdapter extends RecyclerView.Adapter<MediaHorizontalRecyclerViewAdapter.MediaHolder>{
    private final LayoutInflater mLayoutInflater;
    private final Context mContext;
    private List<IGMedia> data;
    private int itemPadding;
    private int itemWidth;
    private int itemCount = 4;
    private OnItemClickListenter onItemClickListenter;
    public MediaHorizontalRecyclerViewAdapter(Context context, List<IGMedia> data) {
        this.data = data;
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        itemPadding = Utils.dp2px(0);
        itemWidth = (Utils.screenWidth()-itemPadding*(itemCount-1))/itemCount;
    }
    public MediaHorizontalRecyclerViewAdapter(Context context, List<IGMedia> data, int itemCount) {
        this.data = data;
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        this.itemCount = itemCount;
        itemPadding = Utils.dp2px(0);
        itemWidth = (Utils.screenWidth()-itemPadding*(itemCount-1))/itemCount;
    }
    @Override
    public MediaHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MediaHolder(mLayoutInflater.inflate(R.layout.item_horizontal_media, parent, false));
    }

    @Override
    public void onBindViewHolder(final MediaHolder holder, final int position) {
        final IGMedia media = data.get(position);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)holder.image.getLayoutParams();
        params.width = params.height = itemWidth;
        holder.image.setImageURI(Uri.parse(media.images.thumbnail.url));
        holder.image.setOnClickListener(new View.OnClickListener() {
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
        public SimpleDraweeView image;
        MediaHolder(View view) {
            super(view);
            image = (SimpleDraweeView)view.findViewById(R.id.image);
        }
    }
}

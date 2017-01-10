package com.start.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.start.activity.R;
import com.start.utils.Utils;

import java.util.List;


public class SearchListRecyclerViewAdapter extends RecyclerView.Adapter<SearchListRecyclerViewAdapter.SearchHolder>{
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private List<SearchItem> data;
    private int searchType = 0;
    private OnItemClickListenter onItemClickListenter;
    public SearchListRecyclerViewAdapter(Context context, List<SearchItem> data){
        this.data = data;
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }
    public void setType(int searchType) {
        this.searchType = searchType;
    }
    @Override
    public SearchHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SearchHolder(mLayoutInflater.inflate(R.layout.item_search, parent, false));
    }

    @Override
    public void onBindViewHolder(SearchHolder holder, final int position) {
        SearchItem searchItem = data.get(position);
        SimpleDraweeView headerPortrait = (SimpleDraweeView)holder.imageLayout.findViewById(R.id.head_portrait);
        ImageView searchIcon = (ImageView)holder.imageLayout.findViewById(R.id.search_icon);
        holder.text1.setText(""+searchItem.text1);
        switch (searchType){
            case Utils.SEARCH_USER:
                headerPortrait.setImageURI(Uri.parse(searchItem.url));
                headerPortrait.setVisibility(View.VISIBLE);
                searchIcon.setVisibility(View.INVISIBLE);
                holder.text2.setText(""+searchItem.text2);
                holder.text2.setVisibility(View.VISIBLE);
                break;
            case Utils.SEARCH_TAG:
                searchIcon.setImageResource(R.drawable.ic_tag_blue_48dp);
                searchIcon.setVisibility(View.VISIBLE);
                headerPortrait.setVisibility(View.INVISIBLE);
                holder.text2.setVisibility(View.VISIBLE);
                holder.text2.setText(""+searchItem.text2);
                break;
            case Utils.SEARCH_LOCATION:
                searchIcon.setImageResource(R.drawable.ic_location_on_black_48dp);
                searchIcon.setVisibility(View.VISIBLE);
                headerPortrait.setVisibility(View.INVISIBLE);
                holder.text2.setVisibility(View.GONE);
                break;
        }
        holder.searchItemLayout.setOnClickListener(new View.OnClickListener() {
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

    public static class SearchHolder extends RecyclerView.ViewHolder {
        private LinearLayout searchItemLayout;
        private FrameLayout imageLayout;
        private TextView text1;
        private TextView text2;
        SearchHolder(View view) {
            super(view);
            searchItemLayout = (LinearLayout)view.findViewById(R.id.search_item_layout);
            imageLayout = (FrameLayout)view.findViewById(R.id.image_layout);
            text1 = (TextView)view.findViewById(R.id.text1);
            text2 = (TextView)view.findViewById(R.id.text2);
        }
    }
    public static class SearchItem {
        public String url;
        public String text1;
        public String text2;
    }
}

package com.start.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.start.activity.R;
import com.start.entity.user.IGCommonUser;
import com.start.entity.user.IGLikeUser;

import java.util.List;

import retrofit2.http.PUT;


public class UserListRecyclerViewAdapter extends RecyclerView.Adapter<UserListRecyclerViewAdapter.UserHolder>{
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private List<IGCommonUser> data;
    private OnItemClickListenter onItemClickListenter;
    public UserListRecyclerViewAdapter(Context context, List<IGCommonUser> data){
        this.data = data;
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }
    @Override
    public UserListRecyclerViewAdapter.UserHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new UserListRecyclerViewAdapter.UserHolder(mLayoutInflater.inflate(R.layout.item_user, parent, false));
    }

    @Override
    public void onBindViewHolder(UserListRecyclerViewAdapter.UserHolder holder, final int position) {
        IGCommonUser user = data.get(position);
        if(user.profile_picture!=null){
            holder.headProtrait.setImageURI(Uri.parse(user.profile_picture));
            holder.headProtrait.setVisibility(View.VISIBLE);
        }else {
            holder.headProtrait.setVisibility(View.INVISIBLE);
        }
        holder.text1.setText(""+user.username);
        if(user.full_name!=null) {
            holder.text2.setText("" + user.full_name);
        }else{
            holder.text2.setText("");
        }
        holder.userItemLayout.setOnClickListener(new View.OnClickListener() {
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

    public static class UserHolder extends RecyclerView.ViewHolder {
        private LinearLayout userItemLayout;
        private SimpleDraweeView headProtrait;
        private TextView text1;
        private TextView text2;
        UserHolder(View view) {
            super(view);
            userItemLayout = (LinearLayout)view.findViewById(R.id.user_item_layout);
            headProtrait = (SimpleDraweeView)view.findViewById(R.id.head_portrait);
            text1 = (TextView)view.findViewById(R.id.text1);
            text2 = (TextView)view.findViewById(R.id.text2);
        }
    }
}

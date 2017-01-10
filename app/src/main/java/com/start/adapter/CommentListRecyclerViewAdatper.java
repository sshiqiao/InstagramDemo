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
import com.start.entity.media.IGComment;
import com.start.utils.IGConfig;
import com.start.utils.Utils;

import java.util.List;



public class CommentListRecyclerViewAdatper extends RecyclerView.Adapter<CommentListRecyclerViewAdatper.CommentHolder> {
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private List<IGComment> data;
    private OnItemClickListenter onItemClickListenter;

    public CommentListRecyclerViewAdatper(Context context, List<IGComment> data){
        this.data = data;
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }
    @Override
    public CommentListRecyclerViewAdatper.CommentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CommentListRecyclerViewAdatper.CommentHolder(mLayoutInflater.inflate(R.layout.item_comment, parent, false));
    }

    @Override
    public void onBindViewHolder(CommentListRecyclerViewAdatper.CommentHolder holder, final int position) {
        IGComment comment = data.get(position);
        holder.headProtrait.setImageURI(Uri.parse(comment.from.profile_picture));
        holder.userName.setText(""+comment.from.username);
        holder.text.setText(""+comment.text);
        holder.time.setText(""+ Utils.timeInterval(comment.created_time));
        if(IGConfig.getIGOAuthUserData().user.id.equals(comment.from.id)){
            holder.delete.setVisibility(View.VISIBLE);
        }else{
            holder.delete.setVisibility(View.INVISIBLE);
        }
        holder.headProtrait.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListenter!=null){
                    onItemClickListenter.OnItemClick(v, position);
                }
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
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

    public static class CommentHolder extends RecyclerView.ViewHolder {
        private LinearLayout commentItemLayout;
        private SimpleDraweeView headProtrait;
        private TextView userName;
        private TextView text;
        private TextView time;
        private TextView delete;
        CommentHolder(View view) {
            super(view);
            commentItemLayout = (LinearLayout)view.findViewById(R.id.comment_list_layout);
            headProtrait = (SimpleDraweeView)view.findViewById(R.id.head_portrait);
            userName = (TextView)view.findViewById(R.id.user_name);
            text = (TextView)view.findViewById(R.id.text);
            time = (TextView)view.findViewById(R.id.time);
            delete = (TextView)view.findViewById(R.id.delete);
        }
    }
}

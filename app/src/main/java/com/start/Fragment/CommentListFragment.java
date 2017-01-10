package com.start.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.start.activity.R;
import com.start.activity.UserMessageActivity;
import com.start.adapter.CommentListRecyclerViewAdatper;
import com.start.bussiness.comments.IGCommentsBussiness;
import com.start.bussiness.helper.HttpSubscriber;
import com.start.bussiness.helper.RxException;
import com.start.entity.media.IGComment;
import com.start.entity.response.IGComentsData;
import com.start.entity.response.IGMeta;
import com.start.utils.Utils;
import com.start.view.IGToast;

import java.nio.BufferUnderflowException;
import java.util.ArrayList;
import java.util.List;


public class CommentListFragment extends Fragment implements CommentListRecyclerViewAdatper.OnItemClickListenter,View.OnClickListener,View.OnTouchListener{
    private View rootView;
    private RecyclerView mRecyclerView;
    private CommentListRecyclerViewAdatper commentListRecyclerViewAdatper;

    private RelativeLayout commentLayout;
    private EditText comment;
    private ImageView send;
    private List<IGComment> data = new ArrayList<>();
    private String mediaId;
    private GestureDetector mGestureDetector;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_comment_list, container, false);
        rootView.setVisibility(View.INVISIBLE);

        mRecyclerView = (RecyclerView)rootView.findViewById(R.id.comment_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        commentListRecyclerViewAdatper = new CommentListRecyclerViewAdatper(getActivity(), data);
        mRecyclerView.setAdapter(commentListRecyclerViewAdatper);
        commentListRecyclerViewAdatper.setOnItemClickListener(this);

        commentLayout = (RelativeLayout)rootView.findViewById(R.id.comment_layout);
        comment = (EditText)rootView.findViewById(R.id.comment);
        send = (ImageView)rootView.findViewById(R.id.send);
        send.setOnClickListener(this);

        mGestureDetector = new GestureDetector(getActivity(), new GestureListener());
        mRecyclerView.setOnTouchListener(this);
        return rootView;
    }
    public void comments(String mediaId){
        if(!isRootViewVisible()){
            setRootViewVisible(true);
        }
        this.mediaId = mediaId;
        IGCommentsBussiness.comments(mediaId, new HttpSubscriber<IGComentsData>() {
            @Override
            public void onSuccessed(IGComentsData igComentsData) {
                data.clear();
                data.addAll(igComentsData.data);
                mRecyclerView.getAdapter().notifyDataSetChanged();
                mRecyclerView.scrollToPosition(data.size()-1);
            }
            @Override
            public void onFailed(RxException e) {
            }
        });
    }
    public void commentMedia(){
        IGCommentsBussiness.commentMedia(mediaId, comment.getText().toString(), new HttpSubscriber<IGMeta>() {
            @Override
            public void onSuccessed(IGMeta igError) {
                if(igError.meta.code == 200) {
                    comment.setText("");
                    Utils.hideSoftInput(send);
                    comments(mediaId);
                }
                send.setEnabled(true);
                send.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.colorAccent));
            }
            @Override
            public void onFailed(RxException e) {
                send.setEnabled(true);
                send.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.colorAccent));
            }
        });
    }
    public void deleteComment(String commentId){
        IGCommentsBussiness.deleteComment(mediaId, commentId, new HttpSubscriber<IGMeta>() {
            @Override
            public void onSuccessed(IGMeta igMeta) {
                if(igMeta.meta.code==200){
                    IGToast.show(getActivity().getResources().getString(R.string.delete_comment_successed), Gravity.CENTER);
                    comments(mediaId);
                }
            }
            @Override
            public void onFailed(RxException e) {
            }
        });
    }
    public void setRootViewVisible(boolean visible){
        if(visible){
            showFragmentAnim();
        }else{
            hideFragmentAnim();
        }
    }
    public void showFragmentAnim() {
        rootView.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
        TranslateAnimation translateAnimation = Utils.translateAnimation(0, 0, Utils.dp2px(50), 0, 150);
        translateAnimation.setAnimationListener(new Utils.AnimationListener(){
            @Override
            public void onAnimationEnd(Animation animation) {
                mRecyclerView.setVisibility(View.VISIBLE);
                TranslateAnimation translateAnimation2 = Utils.translateAnimation(Utils.screenWidth(), 0, 0, 0, 250);
                mRecyclerView.startAnimation(translateAnimation2);
            }
        });
        commentLayout.startAnimation(translateAnimation);
    }
    public void hideFragmentAnim() {
        TranslateAnimation translateAnimation = Utils.translateAnimation(0, 0, 0, Utils.dp2px(50), 150);
        translateAnimation.setAnimationListener(new Utils.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                TranslateAnimation translateAnimation2 = Utils.translateAnimation(0, Utils.screenWidth(), 0, 0, 250);
                translateAnimation2.setAnimationListener(new Utils.AnimationListener() {
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        rootView.setVisibility(View.INVISIBLE);
                    }
                });
                mRecyclerView.startAnimation(translateAnimation2);
            }
        });
        commentLayout.startAnimation(translateAnimation);
    }
    public boolean isRootViewVisible(){
        return rootView.getVisibility()==View.VISIBLE?true:false;
    }
    @Override
    public void OnItemClick(View view, int position) {
        switch (view.getId()){
            case R.id.head_portrait:
                Intent toUserMessageActivity = new Intent(getActivity(), UserMessageActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("user",data.get(position).from);
                toUserMessageActivity.putExtras(bundle);
                startActivity(toUserMessageActivity);
                break;
            case R.id.delete:
                deleteComment(data.get(position).id);
                break;
        }
    }

    @Override
    public void onClick(final View v) {
        if(!comment.getText().toString().equals("")){
            send.setEnabled(false);
            send.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.colorLightGray));
            commentMedia();
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        mGestureDetector.onTouchEvent(event);
        return false;
    }
    class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if(e1!=null&&e2!=null) {
                float x = e2.getX() - e1.getX();
                if (x > 30) {
                    hideFragmentAnim();
                }
            }
            return true;
        }
    }
}

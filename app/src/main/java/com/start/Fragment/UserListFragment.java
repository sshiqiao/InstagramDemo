package com.start.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

import com.start.activity.R;
import com.start.activity.UserMessageActivity;
import com.start.adapter.UserListRecyclerViewAdapter;
import com.start.bussiness.helper.HttpSubscriber;
import com.start.bussiness.helper.RxException;
import com.start.bussiness.likes.IGLikesBussiness;
import com.start.bussiness.users.IGUsersBussiness;
import com.start.entity.response.IGLikeUsersData;
import com.start.entity.response.IGUserData;
import com.start.entity.user.IGCommonUser;
import com.start.entity.user.IGLikeUser;
import com.start.utils.Utils;
import com.start.view.IGMediaView;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


public class UserListFragment extends Fragment implements UserListRecyclerViewAdapter.OnItemClickListenter,View.OnTouchListener{
    private View rootView;
    private RecyclerView mRecyclerView;
    UserListRecyclerViewAdapter userListRecyclerViewAdapter;
    private List<IGCommonUser> data = new ArrayList<>();

    private GestureDetector mGestureDetector;
    private int deltaX;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_user_list, container, false);
        rootView.setVisibility(View.INVISIBLE);

        mRecyclerView = (RecyclerView)rootView.findViewById(R.id.search_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        userListRecyclerViewAdapter = new UserListRecyclerViewAdapter(getActivity(), data);
        mRecyclerView.setAdapter(userListRecyclerViewAdapter);
        userListRecyclerViewAdapter.setOnItemClickListener(this);

        mGestureDetector = new GestureDetector(getActivity(), new GestureListener());
        mRecyclerView.setOnTouchListener(this);

        deltaX = (int)(Utils.screenWidth()*2/3.0);
        return rootView;
    }

    public void likeUsers(String mediaId){
        setRootViewVisible(true);
        IGLikesBussiness.likeUsers(mediaId, new HttpSubscriber<IGLikeUsersData>() {
            @Override
            public void onSuccessed(IGLikeUsersData igLikeUsersData) {
                filterLikeUserData(igLikeUsersData);
            }
            @Override
            public void onFailed(RxException e) {}
        });
    }
    public void filterLikeUserData(IGLikeUsersData igLikeUsersData){
        data.clear();
        for(IGLikeUser likeUser : igLikeUsersData.data){
            userById(likeUser.id);
            IGCommonUser user = new IGCommonUser();
            user.id = likeUser.id;
            user.username = likeUser.username;
            data.add(user);
        }
    }
    public void userById(String userId){
        IGUsersBussiness.userById(userId, new HttpSubscriber<IGUserData>() {
            @Override
            public void onSuccessed(IGUserData igUserData) {
                for(IGCommonUser user :data){
                    if(user.id.equals(igUserData.data.id)) {
                        user.full_name = igUserData.data.full_name;
                        user.profile_picture = igUserData.data.profile_picture;
                        mRecyclerView.getAdapter().notifyDataSetChanged();
                        break;
                    }
                }
            }
            @Override
            public void onFailed(RxException e) {}
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
        TranslateAnimation translateAnimation = Utils.translateAnimation(-deltaX, 0, 0, 0, 250);
        mRecyclerView.startAnimation(translateAnimation);
    }
    public void hideFragmentAnim() {
        TranslateAnimation translateAnimation = Utils.translateAnimation(0, -deltaX, 0, 0, 250);
        translateAnimation.setAnimationListener(new Utils.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                AlphaAnimation alphaAnimation = Utils.alphaAnimation(1.0f, 0.0f, 250);
                alphaAnimation.setAnimationListener(new Utils.AnimationListener() {
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        rootView.setVisibility(View.INVISIBLE);
                    }
                });
                rootView.startAnimation(alphaAnimation);
            }
        });
        mRecyclerView.startAnimation(translateAnimation);
    }
    public boolean isRootViewVisible(){
        return rootView.getVisibility()==View.VISIBLE?true:false;
    }
    @Override
    public void OnItemClick(View view, int position) {
        IGCommonUser user = data.get(position);
        Intent toUserMessageActivity = new Intent(getActivity(), UserMessageActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("IGCommonUser", user);
        toUserMessageActivity.putExtras(bundle);
        startActivity(toUserMessageActivity);
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
                if (x < -10) {
                    hideFragmentAnim();
                }
            }
            return true;
        }
    }
}

package com.start.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.start.activity.R;
import com.start.bussiness.helper.HttpSubscriber;
import com.start.bussiness.helper.RxException;
import com.start.bussiness.users.IGUsersBussiness;
import com.start.entity.response.IGUserData;
import com.start.entity.user.IGCommonUser;
import com.start.entity.user.IGStandardUser;
import com.start.utils.Utils;

import java.util.ArrayList;
import java.util.List;


public class UserMessageFragment extends Fragment implements TabLayout.OnTabSelectedListener, View.OnClickListener{
    private View rootView;
    private FragmentTransaction mFragmentTransaction;
    private List<FrameLayout> tabList = new ArrayList<>();
    private IGCommonUser commonUser;
    private IGStandardUser standardUser;
    private AppBarLayout appBarLayout;
    private SimpleDraweeView headPortrait;
    private TextView userName;
    private TextView userIntroduce;
    private TextView userPublishNum;
    private TextView userFollowByNum;
    private TextView userFollowNum;
    private TabLayout mTabLayout;
    private MediaListFragment mediaListFragment;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null) {
            commonUser = (IGCommonUser) getArguments().getSerializable("IGCommonUser");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_user_message, container, false);

        appBarLayout = (AppBarLayout)rootView.findViewById(R.id.app_bar_layout);

        headPortrait = (SimpleDraweeView)rootView.findViewById(R.id.head_portrait);
        userName = (TextView)rootView.findViewById(R.id.user_name);
        userIntroduce = (TextView)rootView.findViewById(R.id.user_introduce);

        userPublishNum = (TextView)rootView.findViewById(R.id.user_publish_num);
        userFollowNum = (TextView)rootView.findViewById(R.id.user_follow_num);
        userFollowByNum = (TextView)rootView.findViewById(R.id.user_follow_by_num);
        userFollowNum.setOnClickListener(this);
        userFollowByNum.setOnClickListener(this);

        mTabLayout = (TabLayout)rootView.findViewById(R.id.tab_layout);
        FrameLayout tab1 = (FrameLayout)inflater.inflate(R.layout.item_list_tab,null);
        ImageView tabImage1 = (ImageView)tab1.findViewById(R.id.tab_image);
        tabImage1.setImageResource(R.drawable.ic_view_module_blue_48dp);
        tabList.add(tab1);
        mTabLayout.addTab(mTabLayout.newTab().setTag("tab_module").setCustomView(tab1));

        FrameLayout tab2 = (FrameLayout)inflater.inflate(R.layout.item_list_tab,null);
        ImageView tabImage2 = (ImageView)tab2.findViewById(R.id.tab_image);
        tabImage2.setImageResource(R.drawable.ic_view_list_grey_48dp);
        tabList.add(tab2);
        mTabLayout.addTab(mTabLayout.newTab().setTag("tab_list").setCustomView(tab2));
        mTabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(getActivity(), R.color.colorTransparent));
        mTabLayout.addOnTabSelectedListener(this);

        mediaListFragment = (MediaListFragment)getChildFragmentManager().findFragmentById(R.id.media_list_fragment);
        if(commonUser!=null){
            setUserMessageData(commonUser);
        }
        return rootView;
    }
    public void setUserMessageData(IGCommonUser commonUser){
        this.commonUser = commonUser;
        userName.setText(commonUser.username);
        if(commonUser.profile_picture!=null) {
            headPortrait.setImageURI(Uri.parse(commonUser.profile_picture));
        }
        if(commonUser.full_name!=null){
            userIntroduce.setText(commonUser.full_name);
        }
        getStandardUser();
        mediaListFragment.userRecentMedia(commonUser.id);
    }
    public void getStandardUser(){
        IGUsersBussiness.userById(commonUser.id, new HttpSubscriber<IGUserData>() {
            @Override
            public void onSuccessed(IGUserData igUserData) {
                standardUser = igUserData.data;
                setUserData();
            }
            @Override
            public void onFailed(RxException e) {
            }
        });
    }
    public void setUserData() {
        userPublishNum.setText(standardUser.counts.media);
        userFollowNum.setText(standardUser.counts.follows);
        userFollowByNum.setText(standardUser.counts.followed_by);
        headPortrait.setImageURI(Uri.parse(standardUser.profile_picture));
        userIntroduce.setText(standardUser.full_name+"");
    }
    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        if(tab.getTag().equals("tab_module")){
            ((ImageView) tabList.get(0).findViewById(R.id.tab_image)).setImageResource(R.drawable.ic_view_module_blue_48dp);
            ((ImageView) tabList.get(1).findViewById(R.id.tab_image)).setImageResource(R.drawable.ic_view_list_grey_48dp);
            mediaListFragment.setHorizontalAdapter();
        }else{
            ((ImageView) tabList.get(0).findViewById(R.id.tab_image)).setImageResource(R.drawable.ic_view_module_grey_48dp);
            ((ImageView) tabList.get(1).findViewById(R.id.tab_image)).setImageResource(R.drawable.ic_view_list_blue_48dp);
            mediaListFragment.setVerticalAdapter();
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {}

    @Override
    public void onTabReselected(TabLayout.Tab tab) {}

    @Override
    public void onClick(View v) {
        boolean isFollowBy = false;
        switch (v.getId()){
            case R.id.user_follow_num:
                isFollowBy = false;
                break;
            case R.id.user_follow_by_num:
                isFollowBy = true;
                break;
        }
        FollowUsersMediaFragment followUsersMediaFragment = new FollowUsersMediaFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean("isFollowBy",isFollowBy);
        followUsersMediaFragment.setArguments(bundle);
        mFragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        mFragmentTransaction.replace(R.id.user_message_layout,followUsersMediaFragment,"userMsgTofollowUsersMedia");
        mFragmentTransaction.addToBackStack(null);
        mFragmentTransaction.commit();
    }
}

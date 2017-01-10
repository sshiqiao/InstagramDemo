package com.start.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.start.fragment.FollowUsersMediaFragment;
import com.start.adapter.FragmentViewPagerAdapter;
import com.start.fragment.SearchFragment;
import com.start.fragment.UserMessageFragment;
import com.start.utils.IGConfig;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity implements TabLayout.OnTabSelectedListener {
    private List<FrameLayout> tabList = new ArrayList<>();
    private List<Fragment> fragmentList = new ArrayList<>();
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);

        UserMessageFragment userMessageFragment = new UserMessageFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("IGCommonUser", IGConfig.getIGOAuthUserData().user);
        userMessageFragment.setArguments(bundle);
        fragmentList.add(userMessageFragment);
        FrameLayout tab1 = (FrameLayout)getLayoutInflater().inflate(R.layout.item_main_tab,null);
        TextView tabText1 = (TextView)tab1.findViewById(R.id.tab_text);
        tabText1.setText(R.string.dynamic);
        tabText1.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.colorAccent));
        tabList.add(tab1);
        mTabLayout.addTab(mTabLayout.newTab().setTag("tab_dynamic").setCustomView(tab1));

        fragmentList.add(new SearchFragment());
        FrameLayout tab2 = (FrameLayout)getLayoutInflater().inflate(R.layout.item_main_tab,null);
        TextView tabText2 = (TextView)tab2.findViewById(R.id.tab_text);
        tabText2.setText(R.string.search);
        tabList.add(tab2);
        mTabLayout.addTab(mTabLayout.newTab().setTag("tab_search").setCustomView(tab2));

        mTabLayout.addOnTabSelectedListener(this);

        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mViewPager.setAdapter(new FragmentViewPagerAdapter(getSupportFragmentManager(),fragmentList));
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout){
            @Override
            public void onPageSelected(final int position) {
                for(int i=0; i<tabList.size(); i++){
                    FrameLayout tab = tabList.get(i);
                    TextView tabText = (TextView)tab.findViewById(R.id.tab_text);
                    if(position == i){
                        tabText.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.colorAccent));
                    }else{
                        tabText.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.colorWhite));
                    }
                }
            }
        });
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        if(tab.getTag().equals("tab_dynamic")){
            mViewPager.setCurrentItem(0);
        }else{
            mViewPager.setCurrentItem(1);
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count == 0) {
            super.onBackPressed();
        } else {
            Fragment fragment = null;
            if((fragment = getSupportFragmentManager().findFragmentByTag("userMsgTofollowUsersMedia"))!=null){
                ((FollowUsersMediaFragment)fragment).hideFragmentAnim();
            }
            getSupportFragmentManager().popBackStack();
        }
    }
}

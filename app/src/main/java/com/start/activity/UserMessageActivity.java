package com.start.activity;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.start.entity.user.IGCommonUser;
import com.start.fragment.UserMessageFragment;

public class UserMessageActivity extends FragmentActivity {

    private UserMessageFragment userMessageFragment;
    private IGCommonUser commonUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_message);
        commonUser = (IGCommonUser)getIntent().getExtras().getSerializable("IGCommonUser");
        userMessageFragment = (UserMessageFragment)getSupportFragmentManager().findFragmentById(R.id.user_message_fragment);
        userMessageFragment.setUserMessageData(commonUser);
    }
}

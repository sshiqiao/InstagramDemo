package com.start.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.start.activity.R;
import com.start.utils.Utils;

public class SearchFragment extends Fragment implements TextWatcher, View.OnClickListener{
    private View rootView;
    private FragmentTransaction mFragmentTransaction;

    private EditText search;
    private ImageView clear;
    private LinearLayout peripheryLocationlayout;
    private LinearLayout searchUserTagLayout;
    private LinearLayout userLayout;
    private LinearLayout tagLayout;
    private TextView userText;
    private TextView tagText;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_search, container, false);
        search = (EditText)rootView.findViewById(R.id.search);
        search.addTextChangedListener(this);
        clear = (ImageView)rootView.findViewById(R.id.clear);
        clear.setOnClickListener(this);
        peripheryLocationlayout = (LinearLayout)rootView.findViewById(R.id.periphery_location_layout);
        peripheryLocationlayout.setOnClickListener(this);
        searchUserTagLayout = (LinearLayout)rootView.findViewById(R.id.search_user_tag_layout);
        userLayout = (LinearLayout)rootView.findViewById(R.id.user_layout);
        userLayout.setOnClickListener(this);
        tagLayout = (LinearLayout)rootView.findViewById(R.id.tag_layout);
        tagLayout.setOnClickListener(this);
        userText = (TextView)rootView.findViewById(R.id.user_text);
        tagText = (TextView)rootView.findViewById(R.id.tag_text);
        return rootView;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if(searchUserTagLayout.getVisibility()!=View.VISIBLE) {
            peripheryLocationlayout.setVisibility(View.INVISIBLE);
            searchUserTagLayout.setVisibility(View.VISIBLE);
        }
        userText.setText(getActivity().getResources().getString(R.string.search_user)+" "+search.getText().toString());
        tagText.setText(getActivity().getResources().getString(R.string.search_tag)+" "+search.getText().toString().toString());
    }

    @Override
    public void afterTextChanged(Editable s) {
        if(search.getText().toString().equals("")){
            searchUserTagLayout.setVisibility(View.INVISIBLE);
            peripheryLocationlayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        int searchType = 0;
        Utils.hideSoftInput(v);
        switch (v.getId()){
            case R.id.clear:
                search.setText("");
                searchUserTagLayout.setVisibility(View.INVISIBLE);
                peripheryLocationlayout.setVisibility(View.VISIBLE);
                break;
            case R.id.user_layout:
                searchType = Utils.SEARCH_USER;
                break;
            case R.id.tag_layout:
                searchType = Utils.SEARCH_TAG;
                break;
            case R.id.periphery_location_layout:
                searchType = Utils.SEARCH_LOCATION;
                break;
        }
        if(v.getId()!=R.id.clear){
            SearchListFragment searchListFragment = new SearchListFragment();
            Bundle bundle = new Bundle();
            bundle.putString("key",search.getText().toString());
            bundle.putInt("searchType",searchType);
            searchListFragment.setArguments(bundle);
            mFragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            mFragmentTransaction.add(R.id.search_layout, searchListFragment, "searchListFragment");
            mFragmentTransaction.addToBackStack("searchListFragment");
            mFragmentTransaction.commit();
        }
    }
}

package com.start.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.start.activity.R;
import com.start.activity.UserMessageActivity;
import com.start.adapter.SearchListRecyclerViewAdapter;
import com.start.bussiness.helper.HttpSubscriber;
import com.start.bussiness.helper.RxException;
import com.start.bussiness.locations.IGLocationsBussiness;
import com.start.bussiness.tags.IGTagsBussiness;
import com.start.bussiness.users.IGUsersBussiness;
import com.start.entity.common.IGBaseLocation;
import com.start.entity.media.IGTag;
import com.start.entity.response.IGBaseLocationData;
import com.start.entity.response.IGSearchUsersData;
import com.start.entity.response.IGTagsData;
import com.start.entity.user.IGCommonUser;
import com.start.entity.user.IGNormalUser;
import com.start.utils.IGApplication;
import com.start.utils.Utils;
import com.start.view.IGToast;

import java.util.ArrayList;
import java.util.List;


public class SearchListFragment extends Fragment implements LocationListener, SearchListRecyclerViewAdapter.OnItemClickListenter{
    private View rootView;
    private FragmentTransaction mFragmentTransaction;
    private RecyclerView mRecyclerView;
    SearchListRecyclerViewAdapter searchListRecyclerViewAdapter;
    private List<IGNormalUser> userList;
    private List<IGTag> tagList;
    private List<IGBaseLocation> locationList;
    private List<SearchListRecyclerViewAdapter.SearchItem> data = new ArrayList<>();
    private String key;
    private int searchType = 0;
    private int count = 10;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        key = getArguments().getString("key");
        searchType = getArguments().getInt("searchType");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_search_list, container, false);
        mRecyclerView = (RecyclerView)rootView.findViewById(R.id.search_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        searchListRecyclerViewAdapter = new SearchListRecyclerViewAdapter(getActivity(), data);
        mRecyclerView.setAdapter(searchListRecyclerViewAdapter);
        searchListRecyclerViewAdapter.setOnItemClickListener(this);
        switch (searchType){
            case Utils.SEARCH_USER:
                searchUser();
                break;
            case Utils.SEARCH_TAG:
                searchTag();
                break;
            case Utils.SEARCH_LOCATION:
                location();
                break;
        }
        return rootView;
    }
    public void filterUserData(IGSearchUsersData igSearchUsersData){
        data.clear();
        for(IGNormalUser user : igSearchUsersData.data){
            SearchListRecyclerViewAdapter.SearchItem searchItem = new SearchListRecyclerViewAdapter.SearchItem();
            searchItem.url = user.profile_picture;
            searchItem.text1 = user.username;
            searchItem.text2 = user.full_name;
            data.add(searchItem);
        }
    }
    public void filterTagData(IGTagsData igTagsData){
        data.clear();
        for(IGTag tag : igTagsData.data){
            SearchListRecyclerViewAdapter.SearchItem searchItem = new SearchListRecyclerViewAdapter.SearchItem();
            searchItem.text1 = "#"+tag.name;
            searchItem.text2 = tag.media_count+"篇公开贴";
            data.add(searchItem);
        }
    }
    public void filterLocationData(IGBaseLocationData igBaseLocationData){
        data.clear();
        for(IGBaseLocation location : igBaseLocationData.data){
            SearchListRecyclerViewAdapter.SearchItem searchItem = new SearchListRecyclerViewAdapter.SearchItem();
            searchItem.text1 = location.name;
            searchItem.text2 = location.name;
            data.add(searchItem);
        }
    }
    public void searchUser() {
        IGUsersBussiness.searchUsers(key, count, new HttpSubscriber<IGSearchUsersData>() {
            @Override
            public void onSuccessed(IGSearchUsersData igSearchUsersData) {
                userList = igSearchUsersData.data;
                filterUserData(igSearchUsersData);
                searchListRecyclerViewAdapter.setType(0);
                searchListRecyclerViewAdapter.notifyDataSetChanged();
            }
            @Override
            public void onFailed(RxException e) {
            }
        });
    }
    public void searchTag() {
        IGTagsBussiness.searchTags(key, new HttpSubscriber<IGTagsData>() {
            @Override
            public void onSuccessed(IGTagsData igTagsData) {
                tagList = igTagsData.data;
                filterTagData(igTagsData);
                searchListRecyclerViewAdapter.setType(1);
                searchListRecyclerViewAdapter.notifyDataSetChanged();
            }
            @Override
            public void onFailed(RxException e) {}
        });
    }
    public void location(){
        LocationManager locationManager = (LocationManager) IGApplication.getGlobalContext().getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) || locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            String provider = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) ? LocationManager.NETWORK_PROVIDER : LocationManager.GPS_PROVIDER;
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                IGToast.show(getActivity().getResources().getString(R.string.failed_to_locate_targeting));
            }else {
                locationManager.requestLocationUpdates(provider, 3000, 10, this);
            }
        }else {
            IGToast.show(getActivity().getResources().getString(R.string.location_service_is_not_open));
            getActivity().getSupportFragmentManager().popBackStack();

            Intent i = new Intent();
            i.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(i);
        }
    }
    public void searchLocation(double lat, double lng) {
        IGLocationsBussiness.searchLocations(lat+"",lng+"", "750", new HttpSubscriber<IGBaseLocationData>() {
            @Override
            public void onSuccessed(IGBaseLocationData igBaseLocationData) {
                locationList = igBaseLocationData.data;
                filterLocationData(igBaseLocationData);
                searchListRecyclerViewAdapter.setType(2);
                searchListRecyclerViewAdapter.notifyDataSetChanged();
            }
            @Override
            public void onFailed(RxException e) {}
        });
    }

    @Override
    public void onLocationChanged(Location location) {
        searchLocation(location.getLatitude(),location.getLongitude());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void OnItemClick(View view, int position) {
        String tagName = null;
        String locationId = null;
        switch (searchType){
            case Utils.SEARCH_USER:
                IGCommonUser user = userList.get(position);
                Intent toUserMessageActivity = new Intent(getActivity(), UserMessageActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("IGCommonUser", user);
                toUserMessageActivity.putExtras(bundle);
                startActivity(toUserMessageActivity);
                break;
            case Utils.SEARCH_TAG:
                IGTag tag = tagList.get(position);
                tagName = tag.name;
                break;
            case Utils.SEARCH_LOCATION:
                IGBaseLocation location = locationList.get(position);
                locationId = location.id;
                break;
        }
        if(searchType!=Utils.SEARCH_USER){
            SearchMediaFragment searchMediaFragment = new SearchMediaFragment();
            Bundle bundle = new Bundle();
            bundle.putString("tagName",tagName);
            bundle.putString("locationId",locationId);
            bundle.putInt("searchType",searchType);
            searchMediaFragment.setArguments(bundle);
            mFragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            mFragmentTransaction.add(R.id.search_layout, searchMediaFragment, "searchMediaFragment");
            mFragmentTransaction.addToBackStack("searchMediaFragment");
            mFragmentTransaction.commit();
        }

    }
}

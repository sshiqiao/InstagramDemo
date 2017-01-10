package com.start.bussiness.locations;

import com.start.bussiness.helper.HttpConfig;
import com.start.bussiness.helper.HttpSubscriber;
import com.start.entity.common.IGBaseLocation;
import com.start.entity.response.IGBaseLocationData;
import com.start.entity.response.IGMediaData;
import com.start.utils.IGConfig;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class IGLocationsAPI {
    private static IGLocationsService service;
    private IGLocationsAPI(){
        service = (IGLocationsService) HttpConfig.getService(IGLocationsService.class);
    }
    static class SingletonHolder {
        private static IGLocationsAPI INSTANCE = new IGLocationsAPI();
    }
    public static IGLocationsAPI getInstance(){
        return IGLocationsAPI.SingletonHolder.INSTANCE;
    }
    public void location(String location_id, HttpSubscriber<IGBaseLocation> subscriber) {
        service.location(location_id, IGConfig.getIGOAuthUserData().access_token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
    public void locationMedias(String location_id, HttpSubscriber<IGMediaData> subscriber) {
        service.locationMedias(location_id, IGConfig.getIGOAuthUserData().access_token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
    public void searchLocations(String lat, String lng, String distance, HttpSubscriber<IGBaseLocationData> subscriber) {
        service.searchLocations(lat, lng, distance, IGConfig.getIGOAuthUserData().access_token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
}

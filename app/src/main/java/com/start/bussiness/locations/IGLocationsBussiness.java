package com.start.bussiness.locations;

import com.start.bussiness.helper.HttpSubscriber;
import com.start.entity.common.IGBaseLocation;
import com.start.entity.response.IGBaseLocationData;
import com.start.entity.response.IGMediaData;


public class IGLocationsBussiness {
    public static void location(String location_id, HttpSubscriber<IGBaseLocation> subscriber) {
        IGLocationsAPI.getInstance().location(location_id, subscriber);
    }
    public static void locationMedias(String location_id, HttpSubscriber<IGMediaData> subscriber) {
        IGLocationsAPI.getInstance().locationMedias(location_id, subscriber);
    }
    public static void searchLocations(String lat, String lng, String distance, HttpSubscriber<IGBaseLocationData> subscriber) {
        IGLocationsAPI.getInstance().searchLocations(lat, lng, distance, subscriber);
    }

}

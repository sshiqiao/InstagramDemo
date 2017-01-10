package com.start.bussiness.locations;

import com.start.entity.common.IGBaseLocation;
import com.start.entity.response.IGBaseLocationData;
import com.start.entity.response.IGMediaData;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;


public interface IGLocationsService {
    String LOCATIONS_LOCATON_ID_PATH = "v1/locations/{location-id}";
    String LOCATIONS_LOCATON_MEDIA_RECENT_PATH = "v1/locations/{location-id}/media/recent";
    String LOCATIONS_SEARCH_PATH = "v1/locations/search";

    @GET(LOCATIONS_LOCATON_ID_PATH)
    Observable<IGBaseLocation> location(@Path("location-id") String location_id, @Query("access_token") String access_token);

    @GET(LOCATIONS_LOCATON_MEDIA_RECENT_PATH)
    Observable<IGMediaData> locationMedias(@Path("location-id") String location_id, @Query("access_token") String access_token);

    @GET(LOCATIONS_SEARCH_PATH)
    Observable<IGBaseLocationData> searchLocations(@Query("lat") String lat, @Query("lng") String lng, @Query("distance") String distance, @Query("access_token") String access_token);
}

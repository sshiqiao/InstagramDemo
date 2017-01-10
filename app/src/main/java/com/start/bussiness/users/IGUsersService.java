package com.start.bussiness.users;

import com.start.entity.response.IGMediaData;
import com.start.entity.response.IGUserData;
import com.start.entity.response.IGSearchUsersData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;


public interface IGUsersService {

    String USERS_SELF_PATH = "v1/users/self";
    String USERS_USER_ID_PATH = "v1/users/{user-id}";
    String USERS_SELF_MEDIA_RECENT_PATH = "v1/users/self/media/recent";
    String USERS_USER_ID_MEDIA_RECENT_PATH = "v1/users/{user-id}/media/recent";
    String USERS_SELF_MEDIA_LIKED_PATH = "v1/users/self/media/liked";
    String USERS_SEARCH_PATH = "v1/users/search";

    @GET(USERS_SELF_PATH)
    Observable<IGUserData> user(@Query("access_token") String access_token);

    @GET(USERS_USER_ID_PATH)
    Observable<IGUserData> userById(@Path("user-id") String user_id, @Query("access_token") String access_token);

    @GET(USERS_SELF_MEDIA_RECENT_PATH)
    Observable<IGMediaData> userRecentMedia(@Query("count") int count, @Query("access_token") String access_token);

    @GET(USERS_SELF_MEDIA_RECENT_PATH)
    Observable<IGMediaData> userRecentMedia(@Query("count") int count, @Query("max_id") String max_id, @Query("access_token") String access_token);

    @GET(USERS_USER_ID_MEDIA_RECENT_PATH)
    Observable<IGMediaData> userRecentMediaById(@Path("user-id") String user_id, @Query("count") int count, @Query("access_token") String access_token);

    @GET(USERS_USER_ID_MEDIA_RECENT_PATH)
    Observable<IGMediaData> userRecentMediaById(@Path("user-id") String user_id, @Query("count") int count, @Query("max_id") String max_id, @Query("access_token") String access_token);

    @GET(USERS_SELF_MEDIA_LIKED_PATH)
    Observable<IGMediaData> userLikedMedia(@Query("count") int count, @Query("access_token") String access_token);

    @GET(USERS_SEARCH_PATH)
    Observable<IGSearchUsersData> searchUsers(@Query("q") String key, @Query("count") int count, @Query("access_token") String access_token);

    @GET(USERS_SEARCH_PATH)
    Observable<IGSearchUsersData> searchUsers(@Query("q") String key, @Query("count") int count, @Query("max_id") String max_id, @Query("access_token") String access_token);
}

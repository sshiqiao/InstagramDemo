package com.start.bussiness.relationships;

import com.start.entity.common.IGBaseRelationship;
import com.start.entity.common.IGRelationship;
import com.start.entity.response.IGRequestedUserData;
import com.start.entity.response.IGFollowUsersData;

import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;


public interface IGRelationshipsService {
    String USERS_SELF_FOLLOWS_PATH = "v1/users/self/follows";
    String USERS_SELF_FOLLOWED_BY_PATH = "v1/users/self/followed-by";
    String USERS_SELF_REQUESTED_BY_PATH = "v1/users/self/requested-by";
    String USERS_USER_ID_RELATIONSHIP_PATH = "v1/users/{user-id}/relationship";

    @GET(USERS_SELF_FOLLOWS_PATH)
    Observable<IGFollowUsersData> userFollows(@Query("access_token") String access_token);

    @GET(USERS_SELF_FOLLOWS_PATH)
    Observable<IGFollowUsersData> userFollows(@Query("count") int count, @Query("access_token") String access_token);

    @GET(USERS_SELF_FOLLOWS_PATH)
    Observable<IGFollowUsersData> userFollows(@Query("count") int count, @Query("max_id") String max_id, @Query("access_token") String access_token);

    @GET(USERS_SELF_FOLLOWED_BY_PATH)
    Observable<IGFollowUsersData> userFollowBy(@Query("access_token") String access_token);

    @GET(USERS_SELF_FOLLOWED_BY_PATH)
    Observable<IGFollowUsersData> userFollowBy(@Query("count") int count, @Query("access_token") String access_token);

    @GET(USERS_SELF_FOLLOWED_BY_PATH)
    Observable<IGFollowUsersData> userFollowBy(@Query("count") int count, @Query("max_id") String max_id,@Query("access_token") String access_token);

    @GET(USERS_SELF_REQUESTED_BY_PATH)
    Observable<IGRequestedUserData> userRequestedBy(@Query("access_token") String access_token);

    @GET(USERS_USER_ID_RELATIONSHIP_PATH)
    Observable<IGRelationship> userRelationshipById(@Path("user-id")String user_id, @Query("access_token") String access_token);

    @POST(USERS_USER_ID_RELATIONSHIP_PATH)
    Observable<IGBaseRelationship> modifyUserRelationshipById(@Path("user-id")String user_id, @Field("action") String action, @Field("access_token") String access_token);
}

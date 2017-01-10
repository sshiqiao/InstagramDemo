package com.start.bussiness.likes;

import com.start.entity.response.IGLikeUsersData;
import com.start.entity.response.IGMeta;

import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;


public interface IGLikesService {

    String MEDIA_MEDIA_ID_LIKES_PATH = "v1/media/{media-id}/likes";

    @GET(MEDIA_MEDIA_ID_LIKES_PATH)
    Observable<IGLikeUsersData> likeUsers(@Path("media-id") String media_id, @Query("access_token") String access_token);

    @POST(MEDIA_MEDIA_ID_LIKES_PATH)
    Observable<IGMeta> likeMedia(@Path("media-id") String media_id, @Query("access_token") String access_token);

    @DELETE(MEDIA_MEDIA_ID_LIKES_PATH)
    Observable<IGMeta> deleteLike(@Path("media-id") String media_id, @Query("access_token") String access_token);

}

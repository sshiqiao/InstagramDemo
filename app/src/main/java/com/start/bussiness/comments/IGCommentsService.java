package com.start.bussiness.comments;

import com.start.entity.response.IGComentsData;
import com.start.entity.response.IGMeta;

import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;


public interface IGCommentsService {
    String MEDIA_MEDIA_ID_COMMENTS_PATH = "v1/media/{media-id}/comments";
    String MEDIA_MEDIA_ID_COMMENTS_COMMENT_ID_PATH = "v1/media/{media-id}/comments/{comment-id}";

    @GET(MEDIA_MEDIA_ID_COMMENTS_PATH)
    Observable<IGComentsData> comments(@Path("media-id") String media_id, @Query("access_token") String access_token);

    @FormUrlEncoded
    @POST(MEDIA_MEDIA_ID_COMMENTS_PATH)
    Observable<IGMeta> commentMedia(@Path("media-id") String media_id, @Field("text") String text, @Field("access_token") String access_token);

    @DELETE(MEDIA_MEDIA_ID_COMMENTS_COMMENT_ID_PATH)
    Observable<IGMeta> deleteComment(@Path("media-id") String media_id, @Path("comment-id") String comment_id, @Query("access_token") String access_token);
}

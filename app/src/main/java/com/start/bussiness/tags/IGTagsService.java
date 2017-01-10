package com.start.bussiness.tags;

import com.start.entity.media.IGTag;
import com.start.entity.response.IGMediaData;
import com.start.entity.response.IGTagsData;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;


public interface IGTagsService {
    String TAGS_NAME_PATH = "v1/tags/{tag-name}";
    String TAGS_TAG_NAME_MEDIA_PATH = "v1/tags/{tag-name}/media/recent";
    String TAGS_SEARCH_PATH = "v1/tags/search";

    @GET(TAGS_NAME_PATH)
    Observable<IGTag> tag(@Path("tag-name") String tag_name, @Query("access_token") String access_token);

    @GET(TAGS_TAG_NAME_MEDIA_PATH)
    Observable<IGMediaData> tagMedias(@Path("tag-name") String tag_name,@Query("count") int count, @Query("access_token") String access_token);

    @GET(TAGS_SEARCH_PATH)
    Observable<IGTagsData> searchTags(@Query("q") String tag_name, @Query("access_token") String access_token);

}

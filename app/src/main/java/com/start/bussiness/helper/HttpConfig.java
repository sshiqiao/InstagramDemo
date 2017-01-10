package com.start.bussiness.helper;

import android.util.Log;

import com.start.utils.IGApplication;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by qiao on 2016/12/26.
 */

public class HttpConfig {
    private static final String BASE_URL = "https://api.instagram.com/";
    private static final int DEFAULT_TIMEOUT = 5;
    private OkHttpClient httpClient;
    private Retrofit retrofit;
    private HttpConfig() {
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        httpClientBuilder.writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        httpClientBuilder.readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);

        File httpCacheDirectory = new File(IGApplication.getGlobalContext().getExternalCacheDir(), "IGHttpCache");
        if (!httpCacheDirectory.exists()) {
            httpCacheDirectory.mkdirs();
        }
        httpClientBuilder.cache(new Cache(httpCacheDirectory, 20 * 1024 * 1024));

        httpClient = httpClientBuilder.build();

        retrofit = new Retrofit.Builder()
                .client(httpClient)
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }
    public static HttpConfig getInstance(){
        return SingletonHolder.INSTANCE;
    }
    static class SingletonHolder {
        private static final HttpConfig INSTANCE = new HttpConfig();
    }
    public static Object getService(Class<?> c){
        return HttpConfig.getInstance().retrofit.create(c);
    }
}

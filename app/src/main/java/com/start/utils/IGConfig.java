package com.start.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.Gravity;

import com.start.activity.R;
import com.start.entity.response.IGOAuthUserData;
import com.start.view.IGToast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class IGConfig implements Serializable {
    public String clientId = "b424e99ce97a4fdb81948cdd251787d3";//"21df2915cb0c428f82d081f9d8fab863";
    public String clientSecret = "36d77e0210694df69150a1db52e929c2";//"4641e3d2bfbc48328c4e7e53f2875399";
    public String grantType = "authorization_code";
    public String redirectUrl = "http://116.62.49.210/InstagramServer/redirectUrl";
    public String code = "";
    private IGConfig(){
    }

    public static synchronized IGConfig getClientConfig() {
        return CreateIGClientInfo.igClientInfo;
    }

    static class CreateIGClientInfo {
        private static IGConfig igClientInfo = new IGConfig();
    }
    private static IGOAuthUserData OAuthUserData;
    public static IGOAuthUserData getIGOAuthUserData(){
        if(OAuthUserData==null){
            OAuthUserData = readOAuthUserData();
        }
        return OAuthUserData;
    }
    public static void writeOAuthUserData(IGOAuthUserData data){
        IGConfig.OAuthUserData = data;
        SharedPreferences sharedPreferences  = IGApplication.getGlobalContext().getSharedPreferences("IG", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("IGOAuthUserData",Utils.modelToJson(data));
        editor.commit();
    }

    public static IGOAuthUserData readOAuthUserData(){
        SharedPreferences sharedPreferences  = IGApplication.getGlobalContext().getSharedPreferences("IG", Context.MODE_PRIVATE);
        String igOAuthUserDataString = sharedPreferences.getString("IGOAuthUserData",null);
        IGOAuthUserData igoAuthUserData = (IGOAuthUserData) Utils.jsonToModel(igOAuthUserDataString,IGOAuthUserData.class);
        return igoAuthUserData;
    }

    private static List<String> likeMediaData;
    public static List getLikeMediaData(){
        if(likeMediaData == null){
            likeMediaData = IGConfig.readLikeMediaData();
            if(likeMediaData == null){
                likeMediaData = new ArrayList();
            }
        }
        return likeMediaData;
    }
    public static void writeLikeMediaData(List<String> likeMediaData){
        SharedPreferences sharedPreferences  = IGApplication.getGlobalContext().getSharedPreferences("IG", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("LikeMediaData",Utils.modelToJson(likeMediaData));
        editor.commit();
    }

    public static List<String> readLikeMediaData(){
        SharedPreferences sharedPreferences  = IGApplication.getGlobalContext().getSharedPreferences("IG", Context.MODE_PRIVATE);
        String likeMediaDataString = sharedPreferences.getString("LikeMediaData",null);
        List<String> likeMediaData = (List<String>) Utils.jsonToModel(likeMediaDataString,List.class);
        return likeMediaData;
    }
    public static boolean isLikeMediaDataContained(String mediaId){
        boolean isContained = false;
        List<String> likeMediaData = getLikeMediaData();
        for(String id:likeMediaData){
            if(mediaId.equals(id)){
                isContained = true;
                break;
            }
        }
        return isContained;
    }
    public static void addLikeMediaData(String mediaId){
        List<String> likeMediaData = getLikeMediaData();
        if(!isLikeMediaDataContained(mediaId)){
            likeMediaData.add(mediaId);
            writeLikeMediaData(likeMediaData);
        }
    }
    public static void deleteLikeMediaData(String mediaId){
        List<String> likeMediaData = getLikeMediaData();
        likeMediaData.remove(mediaId);
        writeLikeMediaData(likeMediaData);
    }
}

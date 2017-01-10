package com.start.bussiness.helper;
import com.start.entity.common.IGBaseMeta;
import com.start.entity.response.IGMeta;
import com.start.utils.Utils;

import java.io.IOException;
import retrofit2.Response;
import retrofit2.adapter.rxjava.HttpException;


public class RxException extends RuntimeException {
    public String url;
    public Response response;
    public RxExcptionType type;
    public Throwable exception;
    public IGMeta error;
    public enum RxExcptionType {
        NETWORK,
        HTTP,
        UNEXPECTED
    }
    public RxException(Response response, RxExcptionType type, Throwable exception) {
        if(response!=null) {
            this.url = response.raw().request().url().toString();
            this.response = response;
        }
        this.type = type;
        this.exception = exception;
        if (response == null || response.errorBody() == null) {
            error = new IGMeta();
            error.meta = new IGBaseMeta();
            error.meta.error_message = exception.getMessage();
            if(type==RxExcptionType.NETWORK){
                error.meta.code = -1;
                error.meta.error_type = "NetworkException";
            }else if(type==RxExcptionType.UNEXPECTED){
                error.meta.code = -2;
                error.meta.error_type = "UnexpextedException";
            }
            return ;
        }
        try {
            error = (IGMeta) Utils.jsonToModel(this.response.errorBody().string(), IGMeta.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static RxException convertToRxException(Throwable throwable){
        if (throwable instanceof HttpException) {
            HttpException httpException = (HttpException) throwable;
            Response response = httpException.response();
            return new RxException(response, RxException.RxExcptionType.HTTP, throwable);
        }
        if (throwable instanceof IOException) {
            return new RxException(null, RxException.RxExcptionType.NETWORK, throwable);
        }
        return new RxException(null, RxException.RxExcptionType.UNEXPECTED, throwable);
    }
}

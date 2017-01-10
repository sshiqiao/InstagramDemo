package com.start.bussiness.helper;

import android.widget.Toast;
import com.start.entity.common.IGBaseMeta;
import com.start.utils.IGApplication;
import com.start.view.IGToast;

import rx.Subscriber;


public abstract class HttpSubscriber<T> extends Subscriber {

    @Override
    public void onCompleted() {}

    @Override
    public void onNext(Object o) {
        onSuccessed((T)o);
    }

    @Override
    public void onError(Throwable e) {
        RxException rxException = RxException.convertToRxException(e);
        IGBaseMeta meta = rxException.error.meta;
        IGToast.show("error_code:"+meta.code+"\nerror_type:"+meta.error_type+"\nerror_message:"+meta.error_message);
        onFailed(rxException);
    }

    public abstract void onSuccessed(T t);

    public abstract void onFailed(RxException e);

}

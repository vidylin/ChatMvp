package com.chatmvp.common.basePresenter;

/**
 * Created by Administrator on 2016/10/19.
 */
public interface  Presenter<V> {

    void attachView(V view);

    void detachView();
}

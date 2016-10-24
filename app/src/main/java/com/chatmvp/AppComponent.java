package com.chatmvp;

import android.app.Application;

import com.chatmvp.common.db.ChatDBManager;
import com.chatmvp.common.db.RecentItemDBManager;
import com.chatmvp.common.utils.SharePreferenceUtil;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Administrator on 2016/10/19.
 */
@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {
    Application getApplication();
    SharePreferenceUtil getSharePreferenceUtil();
    ChatDBManager getChatDBManager();
    RecentItemDBManager getRecentItemDBManager();
}

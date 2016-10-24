package com.chatmvp;

import android.app.Application;

import com.chatmvp.common.constant.ConstantValues;
import com.chatmvp.common.db.ChatDBManager;
import com.chatmvp.common.db.RecentItemDBManager;
import com.chatmvp.common.utils.SharePreferenceUtil;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by clevo on 2015/6/9.
 */
@Module
public class AppModule {

    private Application application;

    public AppModule(Application application){
        this.application=application;
    }

    @Provides
    @Singleton
    public Application provideApplication(){
        return application;
    }

    @Provides
    SharePreferenceUtil provideSharePreferenceUtil(){
        return new SharePreferenceUtil(AppApplication.context, ConstantValues.SP_FILE_NAME);
    }

    @Provides
    @Singleton
    ChatDBManager provideChatDBManager(){
        return new ChatDBManager();
    }

    @Provides
    @Singleton
    RecentItemDBManager provideRecentDB(){
        return new RecentItemDBManager();
    }

}

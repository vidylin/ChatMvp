package com.chatmvp.module;

import com.chatmvp.ActivityScope;
import com.chatmvp.activity.MainActivity;
import com.chatmvp.common.db.ChatBeanDB;
import com.chatmvp.common.db.RecentDB;
import com.chatmvp.common.utils.SharePreferenceUtil;
import com.chatmvp.presenter.MainPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Administrator on 2016/10/19.
 */
@Module
public class MainActivityModule {
    private MainActivity mainActivity;
    public MainActivityModule(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @ActivityScope
    @Provides
    MainActivity provideMainActivity() {
        return mainActivity;
    }

    @ActivityScope
    @Provides
    MainPresenter provideMainActivityPresenter(MainActivity mainActivity, SharePreferenceUtil util, ChatBeanDB chatBeanDB, RecentDB recentDB) {
        return new MainPresenter(mainActivity,util,chatBeanDB,recentDB);
    }
}

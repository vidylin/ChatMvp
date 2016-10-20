package com.chatmvp.component;

import com.chatmvp.ActivityScope;
import com.chatmvp.AppComponent;
import com.chatmvp.activity.MainActivity;
import com.chatmvp.module.MainActivityModule;

import dagger.Component;

/**
 * Created by Administrator on 2016/10/19.
 */
@ActivityScope
@Component(modules = MainActivityModule.class,dependencies = AppComponent.class)
public interface MainActivityComponent {
    MainActivity inject(MainActivity mainActivity);
}

package com.chatmvp.View;

import com.chatmvp.common.baseView.BaseView;
import com.chatmvp.common.entity.ChatBean;

/**
 * Created by Administrator on 2016/10/19.
 */
public interface MainView extends BaseView {
    void sendMsgResult(ChatBean mChatBean);
}

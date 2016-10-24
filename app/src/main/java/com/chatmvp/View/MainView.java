package com.chatmvp.View;

import com.chatmvp.common.GreenDAOBean.ChatBean;
import com.chatmvp.common.baseView.BaseView;

/**
 * Created by Administrator on 2016/10/19.
 */
public interface MainView extends BaseView {
    void sendMsgResult(ChatBean mChatBean);
}

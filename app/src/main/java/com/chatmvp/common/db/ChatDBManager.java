package com.chatmvp.common.db;

import com.chatmvp.common.GreenDAOBean.ChatBean;
import com.chatmvp.common.db.base.BaseManager;

import org.greenrobot.greendao.AbstractDao;

/**
 * Created by Administrator on 2016/10/21.
 */
public class ChatDBManager extends BaseManager<ChatBean,Long>{
    @Override
    public AbstractDao<ChatBean, Long> getAbstractDao() {
        return daoSession.getChatBeanDao();
    }
}

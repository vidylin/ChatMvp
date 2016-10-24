package com.chatmvp.common.db;

import com.chatmvp.common.GreenDAOBean.RecentItem;
import com.chatmvp.common.db.base.BaseManager;

import org.greenrobot.greendao.AbstractDao;

/**
 * Created by Administrator on 2016/10/21.
 */
public class RecentItemDBManager extends BaseManager<RecentItem,Long>{
    @Override
    public AbstractDao<RecentItem, Long> getAbstractDao() {
        return daoSession.getRecentItemDao();
    }
}

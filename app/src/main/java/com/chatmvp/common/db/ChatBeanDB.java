package com.chatmvp.common.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.chatmvp.common.entity.ChatBean;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/20.
 */
public class ChatBeanDB {
    public static final String MSG_DBNAME = "message.db";
    private SQLiteDatabase db;

    public ChatBeanDB(Context context) {
        db = context.openOrCreateDatabase(MSG_DBNAME, Context.MODE_PRIVATE,
                null);
    }

    public void saveMsg(String id, ChatBean entity) {
        db.execSQL("CREATE table IF NOT EXISTS _"
                + id
                + " (_id INTEGER PRIMARY KEY AUTOINCREMENT,userId TEXT," +
                "name TEXT,userHeadIcon TEXT,content TEXT,isCome TEXT," +
                "messageType INTEGER,type INTEGER,userVoiceTime FLOAT,userVoicePath TEXT," +
                "userVoiceUrl TEXT,sendState INTEGER,imageUrl TEXT,imageIconUrl TEXT,imageLocal TEXT,isNew INTEGER,time TEXT)");
        int isCome = 0;
        if (entity.isComMeg()) {// 如果是收到的消息，保存在数据库的值为1
            isCome = 1;
        }
        db.execSQL(
                "insert into _"
                        + id
                        + " (userId,name,userHeadIcon,content,isCome,messageType,type,userVoiceTime" +
                        ",userVoicePath,userVoiceUrl,sendState,imageUrl,imageIconUrl,imageLocal,isNew,time) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
                new Object[] { entity.getUserId(), entity.getUserName(),entity.getUserHeadIcon(),
                        entity.getUserContent(), isCome, entity.getMessagetype(),
                        entity.getType(), entity.getUserVoiceTime(),entity.getUserVoicePath(),
                        entity.getUserVoiceUrl(),entity.getSendState(),entity.getImageUrl(),entity.getImageIconUrl(),
                entity.getImageLocal(),entity.getIsNew(),entity.getTime()});
    }

    public List<ChatBean> getMsg(String id, int pager,String userId) {
        List<ChatBean> list = new LinkedList<>();
        int num = 10 * (pager + 1);// 本来是准备做滚动到顶端自动加载数据
        db.execSQL("CREATE table IF NOT EXISTS _"
                + id
                + " (_id INTEGER PRIMARY KEY AUTOINCREMENT,userId TEXT," +
                "name TEXT,userHeadIcon TEXT,content TEXT,isCome TEXT," +
                "messageType INTEGER,type INTEGER,userVoiceTime FLOAT,userVoicePath TEXT," +
                "userVoiceUrl TEXT,sendState INTEGER,imageUrl TEXT,imageIconUrl TEXT,imageLocal TEXT,isNew INTEGER,time TEXT)");
        Cursor c = db.rawQuery("SELECT * from _" + id+" WHERE userId = "+ userId
                + " ORDER BY _id DESC LIMIT " + num, null);
        while (c.moveToNext()) {
            ChatBean entity = new ChatBean();
            entity.setUserId(c.getString(c.getColumnIndex("userId")));
            entity.setUserName(c.getString(c.getColumnIndex("name")));
            entity.setUserHeadIcon(c.getString(c.getColumnIndex("userHeadIcon")));
            entity.setUserContent(c.getString(c.getColumnIndex("content")));
            int isCome = c.getInt(c.getColumnIndex("isCome"));
            boolean isComMsg = false;
            if (isCome == 1) {
                isComMsg = true;
            }
            entity.setComMeg(isComMsg);
            entity.setMessagetype(c.getInt(c.getColumnIndex("messageType")));
            entity.setType(c.getInt(c.getColumnIndex("type")));
            entity.setUserVoiceTime(c.getFloat(c.getColumnIndex("userVoiceTime")));
            entity.setUserVoicePath(c.getString(c.getColumnIndex("userVoicePath")));
            entity.setUserVoiceUrl(c.getString(c.getColumnIndex("userVoiceUrl")));
            entity.setSendState(c.getInt(c.getColumnIndex("sendState")));
            entity.setImageUrl(c.getString(c.getColumnIndex("imageUrl")));
            entity.setImageIconUrl(c.getString(c.getColumnIndex("imageIconUrl")));
            entity.setImageLocal(c.getString(c.getColumnIndex("imageLocal")));
            entity.setIsNew(c.getInt(c.getColumnIndex("isNew")));
            entity.setTime(c.getString(c.getColumnIndex("time")));
            list.add(entity);
        }
        c.close();
        Collections.reverse(list);// 前后反转一下消息记录
        return list;
    }
}

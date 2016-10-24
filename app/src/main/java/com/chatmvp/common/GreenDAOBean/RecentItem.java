package com.chatmvp.common.greenDAOBean;

import com.chatmvp.R;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Administrator on 2016/10/21.
 */
@Entity
public class RecentItem {
    @Id
    private Long id;
    private String userId;
    private int headImg;// 头像
    private String name;// 消息来自
    private String message;// 消息内容
    private int newNum;// 新消息数目
    private long time;// 消息日期
    private int msgType;// 消息类型
    private int voiceTime;// 语音时长
    // ===头像===
    @Transient
    public static final int[] heads = { R.mipmap.h0 };
    @Generated(hash = 30525189)
    public RecentItem(Long id, String userId, int headImg, String name,
            String message, int newNum, long time, int msgType, int voiceTime) {
        this.id = id;
        this.userId = userId;
        this.headImg = headImg;
        this.name = name;
        this.message = message;
        this.newNum = newNum;
        this.time = time;
        this.msgType = msgType;
        this.voiceTime = voiceTime;
    }
    @Generated(hash = 1983251930)
    public RecentItem() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getUserId() {
        return this.userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public int getHeadImg() {
        return this.headImg;
    }
    public void setHeadImg(int headImg) {
        this.headImg = headImg;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getMessage() {
        return this.message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public int getNewNum() {
        return this.newNum;
    }
    public void setNewNum(int newNum) {
        this.newNum = newNum;
    }
    public long getTime() {
        return this.time;
    }
    public void setTime(long time) {
        this.time = time;
    }
    public int getMsgType() {
        return this.msgType;
    }
    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }
    public int getVoiceTime() {
        return this.voiceTime;
    }
    public void setVoiceTime(int voiceTime) {
        this.voiceTime = voiceTime;
    }
   
}

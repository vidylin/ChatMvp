package com.chatmvp.common.GreenDAOBean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Administrator on 2016/10/21.
 */
@Entity
public class ChatBean {
    @Id
    private Long id;
    private String UserId;
    private String UserHeadIcon;
    private String UserName;
    private String UserContent;
    private String time;
    private int type;
    private int messagetype;
    private float UserVoiceTime;
    private String UserVoicePath;
    private String UserVoiceUrl;
    private int sendState;
    private String imageUrl;
    private String imageIconUrl;
    private String imageLocal;
    private int isNew;
    private boolean isComMeg;
    @Generated(hash = 517373904)
    public ChatBean(Long id, String UserId, String UserHeadIcon, String UserName,
            String UserContent, String time, int type, int messagetype,
            float UserVoiceTime, String UserVoicePath, String UserVoiceUrl,
            int sendState, String imageUrl, String imageIconUrl, String imageLocal,
            int isNew, boolean isComMeg) {
        this.id = id;
        this.UserId = UserId;
        this.UserHeadIcon = UserHeadIcon;
        this.UserName = UserName;
        this.UserContent = UserContent;
        this.time = time;
        this.type = type;
        this.messagetype = messagetype;
        this.UserVoiceTime = UserVoiceTime;
        this.UserVoicePath = UserVoicePath;
        this.UserVoiceUrl = UserVoiceUrl;
        this.sendState = sendState;
        this.imageUrl = imageUrl;
        this.imageIconUrl = imageIconUrl;
        this.imageLocal = imageLocal;
        this.isNew = isNew;
        this.isComMeg = isComMeg;
    }
    @Generated(hash = 1872716502)
    public ChatBean() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getUserId() {
        return this.UserId;
    }
    public void setUserId(String UserId) {
        this.UserId = UserId;
    }
    public String getUserHeadIcon() {
        return this.UserHeadIcon;
    }
    public void setUserHeadIcon(String UserHeadIcon) {
        this.UserHeadIcon = UserHeadIcon;
    }
    public String getUserName() {
        return this.UserName;
    }
    public void setUserName(String UserName) {
        this.UserName = UserName;
    }
    public String getUserContent() {
        return this.UserContent;
    }
    public void setUserContent(String UserContent) {
        this.UserContent = UserContent;
    }
    public String getTime() {
        return this.time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public int getType() {
        return this.type;
    }
    public void setType(int type) {
        this.type = type;
    }
    public int getMessagetype() {
        return this.messagetype;
    }
    public void setMessagetype(int messagetype) {
        this.messagetype = messagetype;
    }
    public float getUserVoiceTime() {
        return this.UserVoiceTime;
    }
    public void setUserVoiceTime(float UserVoiceTime) {
        this.UserVoiceTime = UserVoiceTime;
    }
    public String getUserVoicePath() {
        return this.UserVoicePath;
    }
    public void setUserVoicePath(String UserVoicePath) {
        this.UserVoicePath = UserVoicePath;
    }
    public String getUserVoiceUrl() {
        return this.UserVoiceUrl;
    }
    public void setUserVoiceUrl(String UserVoiceUrl) {
        this.UserVoiceUrl = UserVoiceUrl;
    }
    public int getSendState() {
        return this.sendState;
    }
    public void setSendState(int sendState) {
        this.sendState = sendState;
    }
    public String getImageUrl() {
        return this.imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    public String getImageIconUrl() {
        return this.imageIconUrl;
    }
    public void setImageIconUrl(String imageIconUrl) {
        this.imageIconUrl = imageIconUrl;
    }
    public String getImageLocal() {
        return this.imageLocal;
    }
    public void setImageLocal(String imageLocal) {
        this.imageLocal = imageLocal;
    }
    public int getIsNew() {
        return this.isNew;
    }
    public void setIsNew(int isNew) {
        this.isNew = isNew;
    }
    public boolean getIsComMeg() {
        return this.isComMeg;
    }
    public void setIsComMeg(boolean isComMeg) {
        this.isComMeg = isComMeg;
    }

}

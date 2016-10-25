package com.chatmvp.common.constant;

/**
 * Created by Administrator on 2016/10/20.
 */
public class ConstantValues {
    public static final String SP_FILE_NAME = "push_msg_sp";
    public static final int FROM_USER_MSG = 0;//接收消息类型
    public static final int TO_USER_MSG = 1;//发送消息类型
    public static final int FROM_USER_IMG = 2;//接收消息类型
    public static final int TO_USER_IMG = 3;//发送消息类型
    public static final int FROM_USER_VOICE = 4;//接收消息类型
    public static final int TO_USER_VOICE = 5;//发送消息类型
    // Text
    public static final int MESSAGE_TYPE_TEXT = 1;
    // image
    public static final int MESSAGE_TYPE_IMG = 2;
    // file
    public static final int MESSAGE_TYPE_FILE = 3;
    // Record
    public static final int MESSAGE_TYPE_RECORD = 4;
    public static final int SENDING = 0;
    public static final int COMPLETED = 1;
    public static final int SENDERROR = 2;
    public static final String SERVER_URL = "http://192.168.4.120:8080/UploadServlet/";
    public static final String SERVER_DOWN_URL = "http://192.168.4.120:8080/UploadServlet/";
}

package com.chatmvp.presenter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

import com.chatmvp.View.MainView;
import com.chatmvp.activity.MainActivity;
import com.chatmvp.common.basePresenter.BasePresenter;
import com.chatmvp.common.constant.ConstantValues;
import com.chatmvp.common.db.ChatBeanDB;
import com.chatmvp.common.db.RecentDB;
import com.chatmvp.common.entity.ChatBean;
import com.chatmvp.common.entity.MessageItem;
import com.chatmvp.common.entity.RecentItem;
import com.chatmvp.common.utils.FileSaveUtil;
import com.chatmvp.common.utils.GetCurrentTime;
import com.chatmvp.common.utils.PictureUtil;
import com.chatmvp.common.utils.SharePreferenceUtil;
import com.chatmvp.common.widget.ChatBottomView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/19.
 */
public class MainPresenter extends BasePresenter<MainView>{
    private MainActivity mainActivity;
    private SharePreferenceUtil util;
    private ChatBeanDB chatBeanDB;
    private RecentDB recentDB;

    public MainPresenter(MainActivity mainActivity,SharePreferenceUtil util,ChatBeanDB chatBeanDB,RecentDB recentDB){
        this.mainActivity = mainActivity;
        this.util = util;
        this.chatBeanDB = chatBeanDB;
        this.recentDB = recentDB;
        attachView(mainActivity);
    }

    public void to(){
        mvpView.getDataFailed("ddddd");
    }

    public void openCamera(String camPicPath){
        Intent openCameraIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        Uri uri = Uri.fromFile(new File(camPicPath));
        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        mainActivity.startActivityForResult(openCameraIntent,
                ChatBottomView.FROM_CAMERA);
    }

    public void openGallery(){
        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            intent.setAction(Intent.ACTION_GET_CONTENT);
        } else {
            intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.putExtra("crop", "true");
            intent.putExtra("scale", "true");
            intent.putExtra("scaleUpIfNeeded", true);
        }
        intent.setType("image/*");
        mainActivity.startActivityForResult(intent,
                ChatBottomView.FROM_GALLERY);
    }

    public String getSavePicPath() {
        final String dir = FileSaveUtil.SD_CARD_PATH + "image_data/";
        try {
            FileSaveUtil.createSDDirectory(dir);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String fileName = String.valueOf(System.currentTimeMillis() + ".png");
        return dir + fileName;
    }

    /**
     * 发送语音
     */
    public void upVoice(float seconds, String filePath) {
        ChatBean mChatBean = getTbub("13622215086","vidy", ConstantValues.TO_USER_VOICE,
                null, null, null,
                null, filePath, null,seconds, 0);
        chatBeanDB.saveMsg("13622215085",mChatBean);
        mvpView.sendMsgResult(mChatBean);
    }
    /**
     * 发送文字
     */
    public void sendMsgText(String content) {
        ChatBean mChatBean = getTbub("13622215086","vidy", ConstantValues.TO_USER_MSG,
                content, null, null,
                null, null, null,0, 0);
        chatBeanDB.saveMsg("13622215085",mChatBean);
        //发送消息到服务器
        //...
        //保存最近一条消息
        RecentItem recentItem = new RecentItem(
                MessageItem.MESSAGE_TYPE_TEXT, "13622215085",
                0, "vidy_lin", content, 0,
                System.currentTimeMillis(), 0);
        recentDB.saveRecent(recentItem);
        mvpView.sendMsgResult(mChatBean);
    }

    /**
     * 发送图片
     */
    int i = 0;
    public void upImage(String filePath) {
        ChatBean mChatBean = getTbub("13622215086","vidy", ConstantValues.TO_USER_IMG,
                null, filePath, null,
                null, null, null,0f, 0);
        chatBeanDB.saveMsg("13622215085",mChatBean);
        mvpView.sendMsgResult(mChatBean);
    }

    /**
     * 加载消息历史，从数据库中读出
     */
    public List<ChatBean> initMsgData() {
        String ss = "13622215086";
        List<ChatBean> list = chatBeanDB.getMsg("13622215085", 0,ss);
        List<ChatBean> msgList = new ArrayList<>();// 消息对象数组
        if (list.size() > 0) {
            for (ChatBean entity : list) {
                if (entity.getUserName().equals("")) {
                    entity.setUserName("vvvv");
                }
                if (entity.getUserHeadIcon() ==null) {
                    entity.setUserHeadIcon("");
                }
                msgList.add(entity);
            }
        }
        return msgList;
    }

    private ChatBean getTbub(String userId,String username, int type,
                             String Content, String imageIconUrl, String imageUrl,
                             String imageLocal, String userVoicePath, String userVoiceUrl,
                             float userVoiceTime, int sendState) {
        ChatBean tbub = new ChatBean();
        tbub.setUserId(userId);
        tbub.setUserName(username);
        String time = GetCurrentTime.returnTime();
        tbub.setTime(time);
        tbub.setType(type);
        tbub.setUserContent(Content);
        tbub.setImageIconUrl(imageIconUrl);
        tbub.setImageUrl(imageUrl);
        tbub.setUserVoicePath(userVoicePath);
        tbub.setUserVoiceUrl(userVoiceUrl);
        tbub.setUserVoiceTime(userVoiceTime);
        tbub.setSendState(sendState);
        tbub.setImageLocal(imageLocal);
        return tbub;
    }

    public void showDialog(final String path) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // // TODO Auto-generated method stub
                try {
                    String GalPicPath = getSavePicPath();
                    Bitmap bitmap = PictureUtil.compressSizeImage(path);
                    boolean isSave = FileSaveUtil.saveBitmap(
                            PictureUtil.reviewPicRotate(bitmap, GalPicPath),
                            GalPicPath);
                    File file = new File(GalPicPath);
                    if (file.exists() && isSave) {
                        upImage(GalPicPath);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}

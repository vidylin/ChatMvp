package com.chatmvp.presenter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.webkit.MimeTypeMap;

import com.chatmvp.View.MainView;
import com.chatmvp.activity.MainActivity;
import com.chatmvp.api.ApiService;
import com.chatmvp.common.basePresenter.BasePresenter;
import com.chatmvp.common.constant.ConstantValues;
import com.chatmvp.common.db.ChatDBManager;
import com.chatmvp.common.db.RecentItemDBManager;
import com.chatmvp.common.greenDAOBean.ChatBean;
import com.chatmvp.common.greenDAOBean.RecentItem;
import com.chatmvp.common.utils.FileSaveUtil;
import com.chatmvp.common.utils.GetCurrentTime;
import com.chatmvp.common.utils.PictureUtil;
import com.chatmvp.common.utils.SharePreferenceUtil;
import com.chatmvp.common.widget.ChatBottomView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by Administrator on 2016/10/19.
 */
public class MainPresenter extends BasePresenter<MainView>{
    private MainActivity mainActivity;
    private SharePreferenceUtil util;
    private ChatDBManager chatDBManager;
    private RecentItemDBManager recentItemDBManager;

    public MainPresenter(MainActivity mainActivity,SharePreferenceUtil util,ChatDBManager chatDBManager,RecentItemDBManager recentItemDBManager){
        this.mainActivity = mainActivity;
        this.util = util;
        this.chatDBManager = chatDBManager;
        this.recentItemDBManager = recentItemDBManager;
        attachView(mainActivity);
    }

    public void to(){
        downloadFile("1476953507118.png");
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
        chatDBManager.insert(mChatBean);
        mvpView.sendMsgResult(mChatBean);
        uploadTest(filePath);
    }
    /**
     * 发送文字
     */
    public void sendMsgText(String content) {
        ChatBean mChatBean = getTbub("13622215086","vidy", ConstantValues.TO_USER_MSG,
                content, null, null,
                null, null, null,0, 0);
        boolean result = chatDBManager.insert(mChatBean);
        //发送消息到服务器
        //... RecentItem(long id, String userId, int headImg, String name,String message, int newNum, long time, int msgType, int voiceTime)
        //保存最近一条消息
        RecentItem recentItem = new RecentItem();
        recentItem.setUserId("13622215085");
        recentItem.setName("vidy_lin");
        recentItem.setMsgType(ConstantValues.MESSAGE_TYPE_TEXT);
        recentItem.setMessage(content);
        recentItem.setTime(System.currentTimeMillis());
        recentItemDBManager.insert(recentItem);
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
        chatDBManager.insert(mChatBean);
        mvpView.sendMsgResult(mChatBean);
        uploadTest(filePath);
    }

    public int loadRecords(int number){
        int page = (int) chatDBManager.getPages(number);
        return  page;
    }

    public List<ChatBean> loadPages(int page, int number){
       return chatDBManager.loadPages(page, number);
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

    private void uploadTest(String fileUrl) {
        File file1 = new File(fileUrl);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ConstantValues.SERVER_URL)
//                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiService apiService = retrofit.create(ApiService.class);
        // 获取文件真实的minetype
        Map<String, RequestBody> params = new HashMap<>();
        String mimeType = MimeTypeMap.getSingleton()
                .getMimeTypeFromExtension(
                        MimeTypeMap.getFileExtensionFromUrl(file1.getPath()));
//        String mimeType2 = MimeTypeMap.getSingleton()
//                .getMimeTypeFromExtension(
//                        MimeTypeMap.getFileExtensionFromUrl(file2.getPath()));
        // 网上看了大量的都是传的image/png,或者image/jpg 啥的，这个参数还不是很明白，需要跟下源码在研究下。用这个minetype文件能上传成功。
        RequestBody fileBody = RequestBody.create(MediaType.parse(mimeType), file1);
//        RequestBody fileBody2 = RequestBody.create(MediaType.parse(mimeType2), file2);
        params.put("file\"; filename=\"" + file1.getName() + "", fileBody);
//        params.put("file\"; filename=\"" + file2.getName() + "", fileBody2);
        Call<ResponseBody> call = apiService.upload(params);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String jsonString = new String(response.body().bytes()); // 这就是返回的json字符串了。
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {

            }
        });
    }

    public void downloadFile(String fileUrl){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ConstantValues.SERVER_DOWN_URL)
                .build();
        ApiService downloadService = retrofit.create(ApiService.class);
        Call<ResponseBody> call = downloadService.downloadFileWithDynamicUrlSync(fileUrl);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccess()) {
                    boolean writtenToDisk = writeResponseBodyToDisk(response.body());
                } else {
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
    }

    private boolean writeResponseBodyToDisk(ResponseBody body) {
        try {
            // todo change the file location/name according to your needs
            File futureStudioIconFile = new File(Environment.getExternalStorageDirectory().getCanonicalFile()
                    + File.separator + "Future Studio Icon.png");
            InputStream inputStream = null;
            OutputStream outputStream = null;
            try {
                byte[] fileReader = new byte[4096];
                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;
                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);
                while (true) {
                    int read = inputStream.read(fileReader);
                    if (read == -1) {
                        break;
                    }
                    outputStream.write(fileReader, 0, read);
                    fileSizeDownloaded += read;
                }
                outputStream.flush();
                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }

}

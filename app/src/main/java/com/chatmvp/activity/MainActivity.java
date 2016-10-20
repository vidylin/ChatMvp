package com.chatmvp.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chatmvp.AppComponent;
import com.chatmvp.R;
import com.chatmvp.View.MainView;
import com.chatmvp.common.adapter.ChatListViewAdapter;
import com.chatmvp.common.adapter.ChatRecyclerAdapter;
import com.chatmvp.common.adapter.DataAdapter;
import com.chatmvp.common.animator.SlideInOutBottomItemAnimator;
import com.chatmvp.common.baseActivity.BaseActivity;
import com.chatmvp.common.entity.ChatBean;
import com.chatmvp.common.utils.FileSaveUtil;
import com.chatmvp.common.utils.ImageCheckoutUtil;
import com.chatmvp.common.utils.KeyBoardUtils;
import com.chatmvp.common.widget.AudioRecordButton;
import com.chatmvp.common.widget.ChatBottomView;
import com.chatmvp.common.widget.HeadIconSelectorView;
import com.chatmvp.common.widget.PullToRefreshLayout;
import com.chatmvp.common.widget.PullToRefreshRecyclerView;
import com.chatmvp.common.widget.PullToRefreshView;
import com.chatmvp.common.widget.WrapContentLinearLayoutManager;
import com.chatmvp.component.DaggerMainActivityComponent;
import com.chatmvp.module.MainActivityModule;
import com.chatmvp.presenter.MainPresenter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

public class MainActivity extends BaseActivity implements MainView {
    @Inject
    MainPresenter mainPresenter;
    @Bind(R.id.content_lv)
    PullToRefreshLayout contentLv;
    @Bind(R.id.voice_iv)
    ImageView voiceIv;
    @Bind(R.id.mess_et)
    EditText messEt;
    @Bind(R.id.voice_btn)
    AudioRecordButton voiceBtn;
    @Bind(R.id.emoji)
    ImageView emoji;
    @Bind(R.id.mess_iv)
    ImageView messIv;
    @Bind(R.id.tongbao_utils)
    LinearLayout tongbaoUtils;
    @Bind(R.id.vPager)
    ViewPager vPager;
    @Bind(R.id.send_emoji_icon)
    TextView sendEmojiIcon;
    @Bind(R.id.emoji_group)
    LinearLayout emojiGroup;
    @Bind(R.id.other_lv)
    ChatBottomView otherLv;
    @Bind(R.id.mess_lv)
    ListView messLv;
    @Bind(R.id.bottom_container_ll)
    LinearLayout bottomContainerLl;
    @Bind(R.id.layout_tongbao_rl)
    RelativeLayout layoutTongbaoRl;
    private PullToRefreshRecyclerView myList;
    private String item[] = {"你好!", "我正忙着呢,等等", "有啥事吗？", "有时间聊聊吗", "再见！"};
    private DataAdapter adapter;
    private ChatRecyclerAdapter tbAdapter;
    private boolean CAN_WRITE_EXTERNAL_STORAGE = true;
    private String camPicPath;
    private List<ChatBean> tblist = new ArrayList<>();
    private WrapContentLinearLayoutManager wcLinearLayoutManger;
    private static final int IMAGE_SIZE = 100 * 1024;
    private File mCurrentPhotoFile;

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerMainActivityComponent.builder()
                .appComponent(appComponent)
                .mainActivityModule(new MainActivityModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);
        tblist = mainPresenter.initMsgData();
        init();
    }

    private void init(){
        contentLv.setSlideView(new PullToRefreshView(this).getSlideView(PullToRefreshView.RECYCLERVIEW));
        assert contentLv != null;
        myList = (PullToRefreshRecyclerView) contentLv.returnMylist();
        adapter = new DataAdapter(this, item);
        messLv.setAdapter(adapter);
        tbAdapter = new ChatRecyclerAdapter(this, tblist);

        wcLinearLayoutManger = new WrapContentLinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        myList.setLayoutManager(wcLinearLayoutManger);
        myList.setItemAnimator(new SlideInOutBottomItemAnimator(myList));
        myList.setAdapter(tbAdapter);

        messEt.setOnKeyListener(onKeyListener);
        voiceBtn.setAudioFinishRecorderListener(new AudioRecordButton.AudioFinishRecorderListener() {
            @Override
            public void onStart() {
                tbAdapter.stopPlayVoice();
            }

            @Override
            public void onFinished(float seconds, String filePath) {
                mainPresenter.upVoice(seconds, filePath);
            }
        });
        otherLv.setOnHeadIconClickListener(new HeadIconSelectorView.OnHeadIconClickListener() {
            @Override
            public void onClick(int from) {
                switch (from){
                    case ChatBottomView.FROM_CAMERA:
                        if(!CAN_WRITE_EXTERNAL_STORAGE){
                            Toast.makeText(MainActivity.this,"权限未开通\n请到设置中开通相册权限",Toast.LENGTH_SHORT).show();
                        }else {
                            final String state = Environment.getExternalStorageState();
                            if (Environment.MEDIA_MOUNTED.equals(state)) {
                                camPicPath = mainPresenter.getSavePicPath();
                                mainPresenter.openCamera(camPicPath);
                            } else {
                                Toast.makeText(mActivity,"请检查内存卡",Toast.LENGTH_SHORT).show();
                            }
                        }
                        break;
                    case ChatBottomView.FROM_GALLERY:
                        if(!CAN_WRITE_EXTERNAL_STORAGE){
                            Toast.makeText(MainActivity.this,"权限未开通\n请到设置中开通相册权限",Toast.LENGTH_SHORT).show();
                        }else {
                            String status = Environment.getExternalStorageState();
                            if (status.equals(Environment.MEDIA_MOUNTED)) {// 判断是否有SD卡
                                mainPresenter.openGallery();
                            } else {
                                Toast.makeText(mActivity,"没有SD卡",Toast.LENGTH_SHORT).show();
                            }
                        }
                        break;
                    case ChatBottomView.FROM_PHRASE:
                        if (messLv.getVisibility() == View.GONE) {
                            otherLv.setVisibility(View.GONE);
                            emoji.setBackgroundResource(R.mipmap.emoji);
                            voiceIv.setBackgroundResource(R.mipmap.voice_btn_normal);
                            messLv.setVisibility(View.VISIBLE);
                            KeyBoardUtils.hideKeyBoard(MainActivity.this,
                                    messEt);
                            messIv.setBackgroundResource(R.mipmap.chatting_setmode_keyboard_btn_normal);
                        }
                }
            }
        });
    }

    @OnItemClick({R.id.mess_lv})
    public void onItemClick(AdapterView<?> arg0, View view, int arg2,
                        long arg3){
        String msg = item[arg2];
        mainPresenter.sendMsgText(msg);
    }

    @OnClick({R.id.mess_iv,R.id.voice_iv,R.id.mess_et})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.mess_iv:
                emojiGroup.setVisibility(View.GONE);
                if (otherLv.getVisibility() == View.GONE
                        && messLv.getVisibility() == View.GONE) {
                    messEt.setVisibility(View.VISIBLE);
                    messIv.setFocusable(true);
                    voiceBtn.setVisibility(View.GONE);
                    emoji.setBackgroundResource(R.mipmap.emoji);
                    voiceIv.setBackgroundResource(R.mipmap.voice_btn_normal);
                    otherLv.setVisibility(View.VISIBLE);
                    KeyBoardUtils.hideKeyBoard(MainActivity.this,
                            messEt);
                    messIv.setBackgroundResource(R.mipmap.chatting_setmode_keyboard_btn_normal);
                } else {
                    otherLv.setVisibility(View.GONE);
                    KeyBoardUtils.showKeyBoard(MainActivity.this, messEt);
                    messIv.setBackgroundResource(R.mipmap.tb_more);
                    if (messIv.getVisibility() != View.GONE) {
//                        messIv.setVisibility(View.GONE);
                        KeyBoardUtils.showKeyBoard(MainActivity.this, messEt);
                        messIv.setBackgroundResource(R.mipmap.tb_more);
                    }
                }
                break;
            case R.id.voice_iv:
                if (voiceBtn.getVisibility() == View.GONE) {
                    emoji.setBackgroundResource(R.mipmap.emoji);
                    messIv.setBackgroundResource(R.mipmap.tb_more);
                    messEt.setVisibility(View.GONE);
                    emojiGroup.setVisibility(View.GONE);
                    otherLv.setVisibility(View.GONE);
                    messLv.setVisibility(View.GONE);
                    voiceBtn.setVisibility(View.VISIBLE);
                    KeyBoardUtils.hideKeyBoard(MainActivity.this,
                            messEt);
                    voiceIv.setBackgroundResource(R.mipmap.chatting_setmode_keyboard_btn_normal);
                } else {
                    messEt.setVisibility(View.VISIBLE);
                    voiceBtn.setVisibility(View.GONE);
                    voiceIv.setBackgroundResource(R.mipmap.voice_btn_normal);
                    KeyBoardUtils.showKeyBoard(MainActivity.this, messEt);
                }
                break;
            case R.id.mess_et:
                otherLv.setVisibility(View.GONE);
                messLv.setVisibility(View.GONE);
                messIv.setBackgroundResource(R.mipmap.tb_more);
                voiceIv.setBackgroundResource(R.mipmap.voice_btn_normal);
                break;
            default:
                break;
        }
    }

    //发送文字
    private View.OnKeyListener onKeyListener = new View.OnKeyListener() {

        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_ENTER
                    && event.getAction() == KeyEvent.ACTION_DOWN) {
                String content = messEt.getText().toString();
                mainPresenter.sendMsgText(content);
                return true;
            }
            return false;
        }
    };

    @Override
    public void sendMsgResult(ChatBean mChatBean) {
        tblist.add(mChatBean);
        messEt.setText("");
        tbAdapter.isPicRefresh = true;
        tbAdapter.notifyItemInserted(tblist.size() - 1);
        myList.smoothScrollToPosition(tbAdapter.getItemCount() - 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            otherLv.setVisibility(View.GONE);
            messIv.setBackgroundResource(R.mipmap.tb_more);
            switch (requestCode) {
                case ChatBottomView.FROM_CAMERA:
                    FileInputStream is = null;
                    try {
                        is = new FileInputStream(camPicPath);
                        File camFile = new File(camPicPath); // 图片文件路径
                        if (camFile.exists()) {
                            int size = ImageCheckoutUtil
                                    .getImageSize(ChatListViewAdapter
                                            .getLoacalBitmap(camPicPath));
                            if (size > IMAGE_SIZE) {
                                mainPresenter.showDialog(camPicPath);
                            } else {
                                mainPresenter.upImage(camPicPath);
                            }
                        } else {
                           Toast.makeText(mActivity,"该文件不存在!",Toast.LENGTH_SHORT).show();
                        }
                    } catch (FileNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } finally {
                        // 关闭流
                        try {
                            is.close();
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                    break;
                case ChatBottomView.FROM_GALLERY:
                    Uri uri = data.getData();
                    String path = FileSaveUtil.getPath(getApplicationContext(), uri);
                    mCurrentPhotoFile = new File(path); // 图片文件路径
                    if (mCurrentPhotoFile.exists()) {
                        int size = ImageCheckoutUtil.getImageSize(ChatListViewAdapter
                                .getLoacalBitmap(path));
                        if (size > IMAGE_SIZE) {
                            mainPresenter.showDialog(path);
                        } else {
                            mainPresenter.upImage(path);
                        }
                    } else {
                        Toast.makeText(mActivity,"该文件不存在!",Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        } else if (resultCode == RESULT_CANCELED) {
            // Toast.makeText(this, "操作取消", Toast.LENGTH_SHORT).show();
        }
    }
}

package com.simon.appmanager;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.simon.appmanager.utils.SoundUtils;
import com.simon.appmanager.utils.XActivityStack;

import org.xutils.x;

import java.io.File;
import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.content.CustomContent;
import cn.jpush.im.android.api.content.EventNotificationContent;
import cn.jpush.im.android.api.content.ImageContent;
import cn.jpush.im.android.api.content.PromptContent;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.content.VoiceContent;
import cn.jpush.im.android.api.event.MessageEvent;
import cn.jpush.im.android.api.event.NotificationClickEvent;
import cn.jpush.im.android.api.event.OfflineMessageEvent;
import cn.jpush.im.android.api.model.Message;

@SuppressWarnings("all")
public class BaseActivity extends AppCompatActivity {

    public Handler mHandler = new Handler();
    private static final int PERMISSION_REQ_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        XActivityStack.getInstance().addActivity(this);
        x.view().inject(this);

        showPermissions();
        // 注册接收事件
        JMessageClient.registerEventReceiver(this);
    }

    //请求权限
    public void showPermissions() {
        if ( ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.CHANGE_NETWORK_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_NETWORK_STATE,
                    Manifest.permission.CHANGE_NETWORK_STATE,
            }, PERMISSION_REQ_CODE);
        } else {
            // PERMISSION_GRANTED
        }
    }

    //Android6.0申请权限的回调方法
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQ_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // PERMISSION_GRANTED
                    String saveFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/A黑识科技/";
                    File file = new File(saveFilePath);
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                }else{
                    showPermissions();
                }
                break;
            default:
                break;
        }
    }

    public void startActivity(Activity context, Class mclass) {
        Intent intent = new Intent();
        intent.setClass(context, mclass);
        context.startActivity(intent);
        context.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }

    public void startActivity(Activity context, Class mclass, Bundle bundle) {
        Intent intent = new Intent();
        intent.putExtras(bundle);
        intent.setClass(context, mclass);
        context.startActivity(intent);
        context.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }

    public void finishActivity() {
        finish();
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }


    /**
     * 接收通知栏点击事件
     *
     * @param event
     */
    public void onEvent(NotificationClickEvent event) {
        Message message = event.getMessage();
        TextContent textContent = (TextContent) message.getContent();
        Log.i("Simon", "接收通知栏点击事件：" + textContent.getText());
        SoundUtils.getInstance().releaseSoundSource();
        Intent notificationIntent = new Intent(BaseActivity.this, HomeActivity.class);
        startActivity(notificationIntent);//自定义跳转到指定页面
    }

    /**
     * 用户在线期间收到的消息都会以MessageEvent的方式上抛
     *
     * @param event
     */
    public void onEvent(MessageEvent event) {
        Message msg = event.getMessage();
        Bundle bundle=new Bundle();
        switch (msg.getContentType()) {
            case text:
                //处理文字消息
                TextContent textContent = (TextContent) msg.getContent();
                textContent.getText();
                SoundUtils.getInstance().playSoundByMedia(R.raw.new_message,75,true);
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        SoundUtils.getInstance().releaseSoundSource();
                    }
                },30000);
                Log.i("Simon", "用户在线期间收到的消息：" + textContent.getText());
                break;
            case image:
                //处理图片消息
                ImageContent imageContent = (ImageContent) msg.getContent();
                imageContent.getLocalPath();//图片本地地址
                imageContent.getLocalThumbnailPath();//图片对应缩略图的本地地址
                break;
            case voice:
                //处理语音消息
                VoiceContent voiceContent = (VoiceContent) msg.getContent();
                voiceContent.getLocalPath();//语音文件本地地址
                voiceContent.getDuration();//语音文件时长
                break;
            case custom:
                //处理自定义消息
                CustomContent customContent = (CustomContent) msg.getContent();
                customContent.getNumberValue("custom_num"); //获取自定义的值
                customContent.getBooleanValue("custom_boolean");
                customContent.getStringValue("custom_string");
                break;
            case eventNotification:
                //处理事件提醒消息
                EventNotificationContent eventNotificationContent = (EventNotificationContent) msg.getContent();
                switch (eventNotificationContent.getEventNotificationType()) {
                    case group_member_added:
                        //群成员加群事件
                        break;
                    case group_member_removed:
                        //群成员被踢事件
                        break;
                    case group_member_exit:
                        //群成员退群事件
                        break;
                    case group_info_updated://since 2.2.1
                        //群信息变更事件
                        break;
                }
                break;
            case unknown:
                // 处理未知消息，未知消息的Content为PromptContent 默认提示文本为“当前版本不支持此类型消息，请更新sdk版本”，上层可选择不处理
                PromptContent promptContent = (PromptContent) msg.getContent();
                promptContent.getPromptType();//未知消息的type是unknown_msg_type
                promptContent.getPromptText();//提示文本，“当前版本不支持此类型消息，请更新sdk版本”
                break;
        }
    }

    /**
     * 用户离线期间收到的消息会以OfflineMessageEvent的方式上抛，处理方式类似上面的
     *
     * @param event
     */
    public void onEvent(OfflineMessageEvent event) {
        List<Message> msgs = event.getOfflineMessageList();
        for (Message msg : msgs) {
            switch (msg.getContentType()) {
                case text:
                    //处理文字消息
                    TextContent textContent = (TextContent) msg.getContent();
                    textContent.getText();
                    SoundUtils.getInstance().playSoundByMedia(R.raw.new_message,75,true);
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            SoundUtils.getInstance().releaseSoundSource();
                        }
                    },30000);
                    break;
                case image:
                    //处理图片消息
                    ImageContent imageContent = (ImageContent) msg.getContent();
                    imageContent.getLocalPath();//图片本地地址
                    imageContent.getLocalThumbnailPath();//图片对应缩略图的本地地址
                    break;
                case voice:
                    //处理语音消息
                    VoiceContent voiceContent = (VoiceContent) msg.getContent();
                    voiceContent.getLocalPath();//语音文件本地地址
                    voiceContent.getDuration();//语音文件时长
                    break;
                case custom:
                    //处理自定义消息
                    CustomContent customContent = (CustomContent) msg.getContent();
                    customContent.getNumberValue("custom_num"); //获取自定义的值
                    customContent.getBooleanValue("custom_boolean");
                    customContent.getStringValue("custom_string");
                    break;
                case eventNotification:
                    //处理事件提醒消息
                    EventNotificationContent eventNotificationContent = (EventNotificationContent) msg.getContent();
                    switch (eventNotificationContent.getEventNotificationType()) {
                        case group_member_added:
                            //群成员加群事件
                            break;
                        case group_member_removed:
                            //群成员被踢事件
                            break;
                        case group_member_exit:
                            //群成员退群事件
                            break;
                        case group_info_updated://since 2.2.1
                            //群信息变更事件
                            break;
                    }
                    break;
                case unknown:
                    // 处理未知消息，未知消息的Content为PromptContent 默认提示文本为“当前版本不支持此类型消息，请更新sdk版本”，上层可选择不处理
                    PromptContent promptContent = (PromptContent) msg.getContent();
                    promptContent.getPromptType();//未知消息的type是unknown_msg_type
                    promptContent.getPromptText();//提示文本，“当前版本不支持此类型消息，请更新sdk版本”
                    break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }
}

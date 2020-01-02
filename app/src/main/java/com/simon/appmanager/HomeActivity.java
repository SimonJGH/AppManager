package com.simon.appmanager;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.simon.appmanager.fragment.ApkFragment;
import com.simon.appmanager.utils.PreferencesUtils;
import com.simon.appmanager.utils.ToastUtils;
import com.simon.appmanager.utils.XActivityStack;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.options.RegisterOptionalUserInfo;
import cn.jpush.im.api.BasicCallback;

@ContentView(R.layout.activity_main)
@SuppressWarnings("all")
public class HomeActivity extends BaseActivity {
    @ViewInject(R.id.drawerlayout)
    DrawerLayout mDrawerlayout;

    @ViewInject(R.id.ll_drawer_guide)
    LinearLayout mLl_drawer_guide;

    private String account;//极光账号
    private FragmentManager fm;
    private boolean isExit = false;// 是否退出

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ApkFragment apkFragment = new ApkFragment();
        apkFragment.setAppName("红旗", "hq_");
        ft.replace(R.id.fl_container, apkFragment);
        ft.commit();

        initNotification();
    }

    @Event(value = {R.id.tv_guide_hq, R.id.tv_guide_bt, R.id.tv_guide_bt_teacher, R.id.tv_guide_bt_manager, R.id.tv_guide_wm, R.id.tv_guide_other_manager})
    private void clickButton(View view) {
        FragmentTransaction ft = fm.beginTransaction();
        ApkFragment apkFragment = new ApkFragment();
        switch (view.getId()) {
            case R.id.tv_guide_hq:
                apkFragment.setAppName("红旗", "hq_");
                break;
            case R.id.tv_guide_bt:
                apkFragment.setAppName("奔腾苑", "bty_");
                break;
            case R.id.tv_guide_bt_teacher:
                apkFragment.setAppName("奔腾苑讲师端", "bty_teacher_");
                break;
            case R.id.tv_guide_bt_manager:
                apkFragment.setAppName("奔腾苑管理端", "bty_manager_");
                break;
            case R.id.tv_guide_wm:
                apkFragment.setAppName("维玛荟客", "wmhk_");
                break;
            case R.id.tv_guide_other_manager:
                apkFragment.setAppName("其它A p p", "other_");
                break;
        }
        ft.replace(R.id.fl_container, apkFragment);
        ft.commit();
        mDrawerlayout.closeDrawers();
    }

    @Event(value = {R.id.tv_guide_open_folder})
    private void openFolder(View view) {
        FragmentTransaction ft = fm.beginTransaction();
        ApkFragment hqFragment = new ApkFragment();
        switch (view.getId()) {
            case R.id.tv_guide_open_folder:
                startActivity(HomeActivity.this, DownloadFileActivity.class);
                mDrawerlayout.closeDrawers();
                break;
        }
    }

    /**
     * 初始化通知
     */
    private void initNotification() {
        account = PreferencesUtils.getString("ACCOUNT", "");
        if (TextUtils.isEmpty(account)) {
            Random random = new Random();
            int num = random.nextInt(200) + 100;
            account = "20200101" + num;
        }
        JMessageClient.register(account, "123456", new RegisterOptionalUserInfo(), new BasicCallback() {
            @Override
            public void gotResult(int i, String s) {
                if (i == 898001) {// 如果账户已注册 重新获取账号进行注册
                    initNotification();
                } else {
                    loginJpush();
                }
            }
        });
    }

    /**
     * 登录极光
     */
    private void loginJpush() {
        JMessageClient.login(account, "123456", new BasicCallback() {
            @Override
            public void gotResult(int i, String s) {
                if (i == 0) {
                    PreferencesUtils.putString("ACCOUNT", account);
                } else {
                    loginJpush();
                }
            }
        });
    }

    /*双击退出*/
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();// 这里也可以弹出对话框
        }
        return false;
    }

    private void exit() {
        if (!isExit) {
            isExit = true;
            ToastUtils.getInstance().showShortToastBottom("再按一次退出程序");
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false;
                }
            }, 2000);
        } else {
            // 清空activity
            XActivityStack.getInstance().finishAllActivity();
        }
    }
}

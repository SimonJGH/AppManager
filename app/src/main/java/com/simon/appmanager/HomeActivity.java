package com.simon.appmanager;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.simon.appmanager.fragment.BtFragment;
import com.simon.appmanager.fragment.BtManagerFragment;
import com.simon.appmanager.fragment.BtTeacherFragment;
import com.simon.appmanager.fragment.HqFragment;
import com.simon.appmanager.fragment.WmFragment;
import com.simon.appmanager.utils.ToastUtils;
import com.simon.appmanager.utils.XActivityStack;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.Timer;
import java.util.TimerTask;

@ContentView(R.layout.activity_main)
@SuppressWarnings("all")
public class HomeActivity extends BaseActivity {
    @ViewInject(R.id.drawerlayout)
    DrawerLayout mDrawerlayout;

    @ViewInject(R.id.ll_drawer_guide)
    LinearLayout mLl_drawer_guide;

    private FragmentManager fm;
    private boolean isExit = false;// 是否退出

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fl_container, new HqFragment());
        ft.commit();
    }

    @Event(value = {R.id.tv_guide_hq, R.id.tv_guide_bt,R.id.tv_guide_bt_teacher,R.id.tv_guide_bt_manager, R.id.tv_guide_wm})
    private void clickButton(View view) {
        FragmentTransaction ft = fm.beginTransaction();
        switch (view.getId()) {
            case R.id.tv_guide_hq:
                ft.replace(R.id.fl_container, new HqFragment());
                break;
            case R.id.tv_guide_bt:
                ft.replace(R.id.fl_container, new BtFragment());
                break;
            case R.id.tv_guide_bt_teacher:
                ft.replace(R.id.fl_container,  new BtTeacherFragment());
                break;
            case R.id.tv_guide_bt_manager:
                ft.replace(R.id.fl_container,  new BtManagerFragment());
                break;
            case R.id.tv_guide_wm:
                ft.replace(R.id.fl_container, new WmFragment());
                break;
        }
        ft.commit();
        mDrawerlayout.closeDrawers();
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

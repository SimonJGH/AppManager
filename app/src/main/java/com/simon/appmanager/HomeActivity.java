package com.simon.appmanager;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.simon.appmanager.fragment.BtFragment;
import com.simon.appmanager.fragment.HqFragment;
import com.simon.appmanager.fragment.WmFragment;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

@ContentView(R.layout.activity_main)
@SuppressWarnings("all")
public class HomeActivity extends BaseActivity {
    @ViewInject(R.id.drawerlayout)
    DrawerLayout mDrawerlayout;

    @ViewInject(R.id.ll_drawer_guide)
    LinearLayout mLl_drawer_guide;

    private FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fl_container, new HqFragment());
        ft.commit();
    }

    @Event(value = {R.id.tv_guide_hq, R.id.tv_guide_bt, R.id.tv_guide_wm})
    private void clickButton(View view) {
        FragmentTransaction ft = fm.beginTransaction();
        switch (view.getId()) {
            case R.id.tv_guide_hq:
                ft.replace(R.id.fl_container, new HqFragment());
                break;
            case R.id.tv_guide_bt:
                ft.replace(R.id.fl_container, new BtFragment());
                break;
            case R.id.tv_guide_wm:
                ft.replace(R.id.fl_container, new WmFragment());
                break;
        }
        ft.commit();
        mDrawerlayout.closeDrawers();
    }
}

package com.simon.appmanager.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.simon.appmanager.BaseFragment;
import com.simon.appmanager.R;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

/**
 * 奔腾
 */
@SuppressWarnings("all")
@ContentView(R.layout.fragment_layout_hq)
public class BtManagerFragment extends BaseFragment {
    @ViewInject(R.id.iv_qr)
    ImageView mIv_qr;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mIv_qr.setImageResource(R.mipmap.icon_app_bt);
    }
}

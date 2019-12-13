package com.simon.appmanager.update;

import android.app.Dialog;
import android.content.Context;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.simon.appmanager.R;


/**
 * 更新对话框
 */
@SuppressWarnings("all")
public class UpdateDialog extends Dialog implements View.OnClickListener {
    private UpdateDialogOperate aDialogOperate; // 操作接口
    private Context context;

    private TextView mTv_update_title;
    private TextView mTv_update_content;
    private TextView mTv_update_version;

    private Button mBt_update_now;
    private Button mBt_update_cancle;

    private UpdateBean updateBean;
    private TextView tip;

    public UpdateDialog(Context context) {
        super(context, R.style.common_dialog);
        this.context = context;
        this.setContentView(R.layout.dialog_update);

        mTv_update_title = (TextView) findViewById(R.id.tv_update_title);
        mTv_update_version = (TextView) findViewById(R.id.tv_update_version);
        mTv_update_content = (TextView) findViewById(R.id.tv_update_content);
        mBt_update_now = (Button) findViewById(R.id.bt_update_now);
        mBt_update_cancle = (Button) findViewById(R.id.bt_update_cancle);
        tip = (TextView) findViewById(R.id.update_tip);

        mBt_update_now.setOnClickListener(this);
        mBt_update_cancle.setOnClickListener(this);

    }

    public void setData(UpdateBean updateBean, boolean flag, UpdateDialogOperate aDialogOperate) {
        this.aDialogOperate = aDialogOperate;
        this.updateBean = updateBean;

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tip.setVisibility(View.GONE);
        mBt_update_now.setVisibility(View.VISIBLE);
        mBt_update_cancle.setVisibility(View.VISIBLE);

        mTv_update_title.setText(updateBean.getTitle());
        mTv_update_version.setText(updateBean.getVersionName());
        mTv_update_content.setText(updateBean.getMessage());
        this.setCancelable(false);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_update_cancle:
                aDialogOperate.executeCancel("");
                break;
            case R.id.bt_update_now:
                aDialogOperate.executeCommit("");
                break;
            default:
                break;
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }
}

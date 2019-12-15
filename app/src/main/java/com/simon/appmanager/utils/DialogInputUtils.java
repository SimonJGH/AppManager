package com.simon.appmanager.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.simon.appmanager.R;
import com.simon.appmanager.https.DbManagers;
import com.simon.appmanager.https.entity.AccountInfo;

/**
 * Dialog提示框
 */
@SuppressWarnings("all")
public class DialogInputUtils {

    private static Dialog dialog;
    private static Window window;

    /**
     * Dialog提示框消失方法
     */
    public static void dialogDismiss() {
        if (isDialogShowing()) {
            dialog.dismiss();
            dialog = null;
        }
    }

    /**
     * Dialog提示框是否正在运行
     *
     * @return Dialog提示框是否正在运行
     */
    public static boolean isDialogShowing() {
        return dialog != null && dialog.isShowing();
    }

    /**
     * 显示确认安全码提示框
     *
     * @param activity              当前Activity
     * @param dialogOnClickListener 确定按钮点击事件
     */
    public static void showInputDialog(final Activity activity,
                                       final DialogOnClickListener dialogOnClickListener, String title, String apkName, AccountInfo accountInfo, int gravity, Double scaleX, Double scaleY) {
        dialogDismiss();
        dialog = new Dialog(activity, R.style.SampleTheme);
        dialog.setContentView(R.layout.layout_dialog);
        // 点击弹窗外区域，弹窗不消失
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        // 获取窗口
        window = dialog.getWindow();
        // 设置对话框背景为透明
        window.setBackgroundDrawableResource(android.R.color.transparent);
        // 设置窗口位置
        window.setGravity(gravity);
        // 获取窗口属性
        WindowManager.LayoutParams lp = window.getAttributes();
        // 获取窗口管理者
        WindowManager windowManager = ((Activity) activity).getWindowManager();
        // 获取真机参数
        Display display = windowManager.getDefaultDisplay();
        // 设置窗口大小
        if (scaleX == 0) {
            lp.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        } else {
            lp.width = (int) (display.getWidth() * scaleX);
        }
        if (scaleY == 0) {
            lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        } else {
            lp.height = (int) (display.getHeight() * scaleY);
        }
        // 给窗口设置属性
        window.setAttributes(lp);

        TextView tv_account_title = dialog.findViewById(R.id.tv_account_title);
        tv_account_title.setText(title);

        EditText et_account_nickname = dialog.findViewById(R.id.et_account_nickname);
        EditText et_account_username = dialog.findViewById(R.id.et_account_username);
        EditText et_account_password = dialog.findViewById(R.id.et_account_password);
        if (accountInfo != null) {
            et_account_nickname.setText(accountInfo.getNickname());
            et_account_username.setText(accountInfo.getUsername());
            et_account_password.setText(accountInfo.getPassword());
        }

        TextView tv_account_submit = dialog.findViewById(R.id.tv_account_submit);
        tv_account_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nickname = et_account_nickname.getText().toString();
                String username = et_account_username.getText().toString();
                String password = et_account_password.getText().toString();

                Log.i("Simon", "account = " + nickname + username + password);
                if (TextUtils.isEmpty(nickname)) {
                    ToastUtils.getInstance().showShortToast("昵称不可为空");
                    return;
                }
                if (TextUtils.isEmpty(username)) {
                    ToastUtils.getInstance().showShortToast("帐号不可为空");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    ToastUtils.getInstance().showShortToast("密码不可为空");
                    return;
                }

                AccountInfo mAccountInfo = new AccountInfo();
                if (accountInfo != null) {
                    mAccountInfo.setAccount_id(accountInfo.getAccount_id());
                }else {
                    mAccountInfo.setAccount_id(System.currentTimeMillis());
                }
                mAccountInfo.setAppName(apkName);
                mAccountInfo.setNickname(nickname);
                mAccountInfo.setUsername(username);
                mAccountInfo.setPassword(password);
                DbManagers.getInstance().addAccountInfo(mAccountInfo);
                dialogOnClickListener.onClick();

                dialogDismiss();
            }
        });

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                // 获取焦点
                et_account_nickname.requestFocus();
                // 显示软键盘
                SoftInputUtils.showSoftInput(activity);
            }
        });

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                // 隐藏软键盘
                SoftInputUtils.hideSoftInput(activity);
            }
        });
    }

    public interface DialogOnClickListener {
        /**
         * 点击事件
         *
         * @param str 回调参数
         */
        public void onClick();
    }
}

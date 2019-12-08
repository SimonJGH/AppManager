package com.simon.appmanager.utils;

import android.view.Gravity;
import android.widget.Toast;

import com.simon.appmanager.MyApplication;


public class ToastUtils {
    private ToastUtils() {
    }

    public static ToastUtils getInstance() {
        return SafeMode.mToast;
    }

    /**
     * static final 保证了实例的唯一和不可改变
     */
    private static class SafeMode {
        private static final ToastUtils mToast = new ToastUtils();
    }

    /**
     * 吐司--短
     *
     * @param msg
     */
    public void showShortToast(String msg) {
        Toast toast = Toast.makeText(MyApplication.getInstance(), msg, Toast.LENGTH_SHORT);
        toast.show();
    }

    /**
     * 吐司--短
     *
     * @param msg
     */
    public void showShortToastBottom(String msg) {
        Toast toast = Toast.makeText(MyApplication.getInstance(), msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM, 0, 150);
        toast.show();
    }

    /**
     * 吐司--长
     *
     * @param msg
     */
    public void showLongToast(String msg) {
        Toast toast = Toast.makeText(MyApplication.getInstance(), msg, Toast.LENGTH_LONG);
//        LinearLayout layout = (LinearLayout) toast.getView();
//        layout.setBackgroundColor(Color.parseColor("#FFFFFF"));
//        TextView tv = (TextView) toast.getView().findViewById(android.R.id.message);
//        tv.setTextColor(Color.BLACK);
//        tv.setTextSize(15);
//        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}

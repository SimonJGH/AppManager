package com.simon.appmanager.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.simon.appmanager.R;


/**
 * ============================================================
 * 加载dialog
 * ============================================================
 **/
@SuppressWarnings("all")
public class LoadingDialog extends Dialog {
    private ImageView loadingImg;
    private TextView loadingTv;
    private Animation rotate;



    public LoadingDialog(@NonNull Context context) {
        this(context, R.style.loading_dialog_style);
    }

    public LoadingDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        initDialog();
    }

    private void initDialog() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_loading, null);

        loadingImg = (ImageView) view.findViewById(R.id.img);
        loadingTv = (TextView) view.findViewById(R.id.tbv);
        rotate = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_anim);
        loadingImg.setAnimation(rotate);
        Window window = getWindow();
        assert window != null;

        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(view);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(false);
        setOnKeyListener(keylistener);
    }

    public void setLoadingText(String s) {
        if (!TextUtils.isEmpty(s) && loadingTv != null)
            loadingTv.setText(s);
    }

    protected void setLoadingText(@StringRes int s) {
        loadingTv.setText(getContext().getString(s));
    }

    @Override
    public void show() {

        super.show();
        if (loadingImg != null) {
            loadingImg.startAnimation(rotate);
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (loadingImg != null && rotate != null) {
            loadingImg.clearAnimation();
        }
    }

    private OnKeyListener keylistener = (dialog, keyCode, event) -> {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            dialog.dismiss();
            if (getOwnerActivity() != null) {
                getOwnerActivity().finish();
            }
            return true;
        } else {
            return false;
        }
    };

}

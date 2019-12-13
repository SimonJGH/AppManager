package com.simon.appmanager.fragment;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.simon.appmanager.BaseFragment;
import com.simon.appmanager.MyApplication;
import com.simon.appmanager.R;
import com.simon.appmanager.https.entity.AppInfo;
import com.simon.appmanager.update.UpdateBean;
import com.simon.appmanager.update.UpdateVersion;
import com.simon.appmanager.utils.DialogUtils;
import com.simon.appmanager.utils.PopupWindowUtils;
import com.simon.appmanager.utils.QRCodeUtil;
import com.simon.appmanager.utils.ToastUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * 红旗
 */
@SuppressWarnings("all")
@ContentView(R.layout.fragment_layout_hq)
public class HqFragment extends BaseFragment {
    @ViewInject(R.id.iv_qr)
    ImageView mIv_qr;

    @ViewInject(R.id.tv_choose_version)
    TextView mTv_choose_version;

    @ViewInject(R.id.tv_account_manage)
    TextView mTv_account_manage;

    private Bitmap qrCodeWithLogo;
    private String versionType = "hq_publish";
    private String accountType = "";

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_app_hq);
        qrCodeWithLogo = QRCodeUtil.createQRCodeWithLogo("https://open.faw-hongqiacademy.com/Uploads/Website/157594530336102.apk", bitmap);
        mIv_qr.setImageBitmap(qrCodeWithLogo);

    }

    @Event(value = {R.id.iv_version_introduce, R.id.tv_choose_version, R.id.tv_install_apk, R.id.tv_download_apk, R.id.tv_download_qr, R.id.tv_account_manage})
    private void clickButton(View view) {
        switch (view.getId()) {
            case R.id.iv_version_introduce:
                updateIntroduce();
                break;
            case R.id.tv_choose_version:
                chooseVersion();
                break;
            case R.id.tv_install_apk:
                installApk("https://open.faw-hongqiacademy.com/Uploads/Website/157594530336102.apk");
                break;
            case R.id.tv_download_apk:
                mLoadingDialog.show();
                downloadApk("https://open.faw-hongqiacademy.com/Uploads/Website/157594530336102.apk", versionType);
                break;
            case R.id.tv_download_qr:
                mLoadingDialog.show();
                saveBitmapToGallery(qrCodeWithLogo, versionType);
                break;
            case R.id.tv_account_manage:
                accountManage();
                break;
        }
    }

    /**
     * 版本介绍
     */
    private void updateIntroduce() {
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.layout_dialog_hq_introduce, null);
        TextView tv_introduce_time = inflate.findViewById(R.id.tv_introduce_time);
        TextView tv_introduce_content = inflate.findViewById(R.id.tv_introduce_content);
        inflate.findViewById(R.id.iv_introduce_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUtils.getInstance().exitDialog();
            }
        });
        DialogUtils.getInstance().createDialog(getActivity(), inflate, Gravity.CENTER, 0.8, 0.0);

    }

    /**
     * 选择版本
     */
    private void chooseVersion() {
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.layout_pop_hongqi_version, null);
        inflate.findViewById(R.id.tv_version_publish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                versionType = "hq_publish";
                mTv_choose_version.setText("正式版");
                PopupWindowUtils.getInstance().closePop();
            }
        });
        inflate.findViewById(R.id.tv_version_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                versionType = "hq_test";
                mTv_choose_version.setText("测试版");
                PopupWindowUtils.getInstance().closePop();
            }
        });
        inflate.findViewById(R.id.tv_version_chat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                versionType = "hq_chat";
                mTv_choose_version.setText("聊聊版");
                PopupWindowUtils.getInstance().closePop();
            }
        });

        PopupWindowUtils.getInstance().createScalePopupWindow(getContext(), inflate, mTv_choose_version);
    }

    /**
     * 安装apk
     *
     * @param url
     */
    private void installApk(String url) {
        UpdateBean updateBean = new UpdateBean();
        updateBean.setTitle("版本更新");
        updateBean.setMessage("是否立即安装Apk?");
        updateBean.setVersionCode(100);
        updateBean.setVersionName("最新！");
        updateBean.setUrl(url);

        UpdateVersion.CheckVersion(getContext(), updateBean, false);
    }

    /**
     * 获取任意app安装包信息
     *
     * @param packageName
     */
    private void getHqAppInfo(String packageName) {
        List<PackageInfo> packages = getContext().getPackageManager().getInstalledPackages(0);
        for (int i = 0; i < packages.size(); i++) {
            PackageInfo packageInfo = packages.get(i);
            AppInfo tmpInfo = new AppInfo();
            tmpInfo.packageName = packageInfo.packageName;
            tmpInfo.versionName = packageInfo.versionName;
            tmpInfo.versionCode = packageInfo.versionCode;
        }
    }

    /**
     * 下载apk
     */
    public void downloadApk(String url, String fileName) {
        String saveFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/" + fileName + ".apk";

        String brand = android.os.Build.BRAND;
        RequestParams requestParams = new RequestParams(url);
        requestParams.setSaveFilePath(saveFilePath);
        x.http().get(requestParams, new Callback.CommonCallback<File>() {
            @Override
            public void onSuccess(File file) {
                if (file != null) {
                    mLoadingDialog.dismiss();
                    ToastUtils.getInstance().showShortToast("下载Apk成功");
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                mLoadingDialog.dismiss();
                ToastUtils.getInstance().showShortToast(ex.toString());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    /**
     * 保存bitmap到本地
     *
     * @param bmp
     * @param fileName
     */
    public void saveBitmapToGallery(Bitmap bmp, String fileName) {
        //生成路径
        String saveFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/Camera/" + "IMG_" + fileName + ".jpg";

        //获取文件
        File file = new File(saveFilePath);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            //通知系统相册刷新
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                Intent mediaScanIntent = new Intent(
                        Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri contentUri = Uri.fromFile(new File(saveFilePath));
                mediaScanIntent.setData(contentUri);
                MyApplication.getInstance().sendBroadcast(mediaScanIntent);
            } else {
                MyApplication.getInstance().sendBroadcast(new Intent(
                        Intent.ACTION_MEDIA_MOUNTED,
                        Uri.parse("file://"
                                + Environment.getExternalStorageDirectory())));
            }
            ToastUtils.getInstance().showShortToast("下载二维码成功");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            mLoadingDialog.dismiss();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            mLoadingDialog.dismiss();
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 帐号管理
     */
    private void accountManage() {
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.layout_pop_hongqi_account, null);
        inflate.findViewById(R.id.tv_account_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accountType = "hq_add";
                mTv_account_manage.setText("帐号新增");
                PopupWindowUtils.getInstance().closePop();
            }
        });
        inflate.findViewById(R.id.tv_account_edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accountType = "hq_edit";
                mTv_account_manage.setText("帐号编辑");
                PopupWindowUtils.getInstance().closePop();
            }
        });
        inflate.findViewById(R.id.tv_account_del).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accountType = "hq_del";
                mTv_account_manage.setText("帐号删除");
                PopupWindowUtils.getInstance().closePop();
            }
        });

        PopupWindowUtils.getInstance().createScalePopupWindow(getContext(), inflate, mTv_account_manage);
    }
}

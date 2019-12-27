package com.simon.appmanager.fragment;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.simon.appmanager.BaseFragment;
import com.simon.appmanager.MyApplication;
import com.simon.appmanager.R;
import com.simon.appmanager.adapter.CommonAdapter;
import com.simon.appmanager.adapter.CommonViewHolder;
import com.simon.appmanager.https.ApiConstanse;
import com.simon.appmanager.https.CommonCallBack;
import com.simon.appmanager.https.DbManagers;
import com.simon.appmanager.https.HttpUtils;
import com.simon.appmanager.https.entity.AccountInfo;
import com.simon.appmanager.https.entity.ApkListOutputBean;
import com.simon.appmanager.https.entity.AppInfo;
import com.simon.appmanager.https.entity.CommonInputBean;
import com.simon.appmanager.update.UpdateBean;
import com.simon.appmanager.update.UpdateVersion;
import com.simon.appmanager.utils.DialogInputUtils;
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
import java.util.ArrayList;
import java.util.List;

/**
 * 红旗
 */
@SuppressWarnings("all")
@ContentView(R.layout.fragment_layout_apk)
public class ApkFragment extends BaseFragment {
    @ViewInject(R.id.iv_qr)
    ImageView mIv_qr;

    @ViewInject(R.id.tv_choose_apk_type)
    TextView mTv_choose_apk_type;

    @ViewInject(R.id.tv_choose_version_code)
    TextView mTv_choose_version_code;

    @ViewInject(R.id.tv_account_manage)
    TextView mTv_account_manage;

    @ViewInject(R.id.tv_app_name)
    TextView mTv_app_name;

    @ViewInject(R.id.recycler_view)
    RecyclerView mRecyclerView;

    private Bitmap qrCodeWithLogo;//二维码
    private String appName = "";//app名称
    private String apkName = "";//apk名称
    private String apkType = "";//apk版本
    private List<ApkListOutputBean.DataBean> mDataBeanList = new ArrayList<>();
    private String introduce = "";//版本介绍
    private String date = "";//版本更新时间
    private String fileUrl = "";//文件下载地址
    private String apkCode = "";//apk版本号
    private int accountEditType = 0;//帐号编辑类型 0-add 1-edit 2-del

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mTv_app_name.setText(appName);
        initAccount();
    }

    /**
     * 传入apkName
     *
     * @param name
     */
    public void setAppName(String appName, String apkName) {
        this.appName = appName;
        this.apkName = apkName;
    }

    @Event(value = {R.id.iv_version_introduce, R.id.tv_choose_apk_type, R.id.tv_choose_version_code,
            R.id.tv_install_apk, R.id.tv_download_apk, R.id.tv_download_qr, R.id.tv_account_manage})
    private void clickButton(View view) {
        switch (view.getId()) {
            case R.id.iv_version_introduce:
                if (TextUtils.isEmpty(apkCode)) {
                    ToastUtils.getInstance().showShortToast("请选择apk版本号!");
                    return;
                }
                if (TextUtils.isEmpty(introduce)) {
                    ToastUtils.getInstance().showShortToast("暂无版本更新介绍!");
                } else {
                    updateIntroduce();
                }
                break;
            case R.id.tv_choose_apk_type:
                chooseApkType();
                break;
            case R.id.tv_choose_version_code:
                if (mDataBeanList.isEmpty()) {
                    ToastUtils.getInstance().showShortToast("暂无apk版本更新!");
                } else {
                    chooseApkCode();
                }
                break;
            case R.id.tv_install_apk:
                if (TextUtils.isEmpty(apkCode)) {
                    ToastUtils.getInstance().showShortToast("请选择apk版本号!");
                } else {
                    installApk(fileUrl);
                }
                break;
            case R.id.tv_download_apk:
                if (TextUtils.isEmpty(apkCode)) {
                    ToastUtils.getInstance().showShortToast("请选择apk版本号!");
                } else {
                    mLoadingDialog.show();
                    downloadApk(fileUrl, apkName + apkType + apkCode);
                }
                break;
            case R.id.tv_download_qr:
                if (TextUtils.isEmpty(apkCode)) {
                    ToastUtils.getInstance().showShortToast("请选择apk版本号!");
                } else {
                    mLoadingDialog.show();
                    saveBitmapToGallery(qrCodeWithLogo, apkName + apkType + apkCode);
                }
                break;
            case R.id.tv_account_manage:
                accountManage();
                break;
        }
    }

    /**
     * 初始化数据
     */
    private void initData() {
        mDataBeanList.clear();
        CommonInputBean inputBean = new CommonInputBean();
        inputBean.setType(apkName + apkType);

        mLoadingDialog.show();
        HttpUtils.getInstance().postMethod(ApiConstanse.get_apk_list, inputBean, new CommonCallBack<String>() {
            @Override
            public void requestSuccess(String result) {
                ApkListOutputBean outputBean = new Gson().fromJson(result, ApkListOutputBean.class);
                List<ApkListOutputBean.DataBean> dataBeanList = outputBean.getData();
                mDataBeanList.addAll(dataBeanList);
                ToastUtils.getInstance().showShortToast("获取数据成功!");
                Log.i("Simon", "初始化数据：requestSuccess = " + result);
            }

            @Override
            public void requestError(String errorMsg) {
                mLoadingDialog.dismiss();
                ToastUtils.getInstance().showShortToast(errorMsg);
                Log.i("Simon", "初始化数据：requestError = " + errorMsg);
            }

            @Override
            public void requestFinished() {
                mLoadingDialog.dismiss();
            }
        });
    }

    /**
     * 版本介绍
     */
    private void updateIntroduce() {
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.layout_dialog_apk_introduce, null);
        TextView tv_introduce_time = inflate.findViewById(R.id.tv_introduce_time);
        tv_introduce_time.setText(date);
        TextView tv_introduce_content = inflate.findViewById(R.id.tv_introduce_content);
        tv_introduce_content.setText(introduce);
        inflate.findViewById(R.id.iv_introduce_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUtils.getInstance().exitDialog();
            }
        });
        DialogUtils.getInstance().createDialog(getActivity(), inflate, Gravity.CENTER, 0.8, 0.0);

    }

    /**
     * 选择apk类型
     */
    private void chooseApkType() {
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.layout_pop_apk_version, null);
        inflate.findViewById(R.id.tv_version_publish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                apkType = "publish";
                mTv_choose_apk_type.setText("正式版");
                initData();
                PopupWindowUtils.getInstance().closePop();
            }
        });
        inflate.findViewById(R.id.tv_version_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                apkType = "test";
                mTv_choose_apk_type.setText("测试版");
                initData();
                PopupWindowUtils.getInstance().closePop();
            }
        });
        inflate.findViewById(R.id.tv_version_chat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                apkType = "chat";
                mTv_choose_apk_type.setText("聊聊版");
                initData();
                PopupWindowUtils.getInstance().closePop();
            }
        });

        PopupWindowUtils.getInstance().createScalePopupWindow(getContext(), inflate, mTv_choose_apk_type);
    }

    /**
     * 选择apk版本号
     */
    private void chooseApkCode() {
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.layout_apk_type, null);

        RecyclerView mRecyclerView = inflate.findViewById(R.id.recyclerview);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(llm);
        // 如果Item够简单，高度是确定的，打开FixSize将提高性能
        mRecyclerView.setHasFixedSize(true);
        // 设置Item默认动画，加也行，不加也行
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setNestedScrollingEnabled(false);

        CommonAdapter mCommonAdapter = new CommonAdapter(getActivity(), R.layout.layout_apk_code_item, mDataBeanList);
        mCommonAdapter.setItemDataListener(new CommonAdapter.ItemDataListener<ApkListOutputBean.DataBean>() {
            @Override
            public void setItemData(CommonViewHolder holder, ApkListOutputBean.DataBean dataBean) {
                TextView tv_apk_code = holder.getView(R.id.tv_apk_code);
                apkCode = dataBean.getVresion();
                tv_apk_code.setText(apkCode);
            }
        });

        mCommonAdapter.setOnItemClickListener(new CommonAdapter.OnItemClickListener() {

            @Override
            public void setOnItemClickListener(View view, int position) {
                ApkListOutputBean.DataBean dataBean = mDataBeanList.get(position);
                mTv_choose_version_code.setText(dataBean.getVresion());

                //二维码
                Bitmap bitmap = null;
                switch (apkName) {
                    case "hq_":
                        bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_app_hq);
                        break;
                    case "bty_":
                        bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_app_bt);
                        break;
                    case "bty_teacher_":
                        bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_app_bt);
                        break;
                    case "bty_manager_":
                        bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_app_bt_manager);
                        break;
                    case "wmhk_":
                        bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_app_wm);
                        break;
                }
                fileUrl = dataBean.getFile();
                qrCodeWithLogo = QRCodeUtil.createQRCodeWithLogo(fileUrl, bitmap);
                mIv_qr.setImageBitmap(qrCodeWithLogo);

                //更新介绍
                date = dataBean.getDate();
                introduce = dataBean.getIntroduce();
                PopupWindowUtils.getInstance().closePop();
            }

            @Override
            public void setOnItemLongClickListener(View view, int position) {
                //  Log.i("Simon", "setOnItemLongClickListener = " + position);
            }
        });

        mCommonAdapter.setHasStableIds(true);
        mRecyclerView.setAdapter(mCommonAdapter);

        PopupWindowUtils.getInstance().createScalePopupWindow(getContext(), inflate, mTv_choose_version_code);
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
        Log.i("Simon","下载apk = "+url);
        String saveFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/" + fileName + ".apk";
        File filedd=new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/");
        Log.i("Simon","filedd = "+filedd.exists());

        String brand = android.os.Build.BRAND;
        RequestParams requestParams = new RequestParams(url);
        requestParams.setSaveFilePath(saveFilePath);
        requestParams.setReadTimeout(100000);
        requestParams.setConnectTimeout(100000);
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
                Log.i("Simon","下载apk = "+ex.toString());
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
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/Camera/";
        File folder = new File(path);
        if (!folder.exists()) {
            folder.mkdir();
        }
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
            ToastUtils.getInstance().showShortToast(e.getMessage());
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
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.layout_pop_apk_account, null);
        inflate.findViewById(R.id.tv_account_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accountEditType = 0;
                mTv_account_manage.setText("帐号新增");
                DialogInputUtils.showInputDialog(getActivity(), new DialogInputUtils.DialogOnClickListener() {
                    @Override
                    public void onClick() {
                        ToastUtils.getInstance().showShortToast("帐号新增成功!");
                        initAccount();
                    }
                }, "帐号新增", apkName, null, Gravity.BOTTOM, 1.0, 0.0);
                initAccount();
                PopupWindowUtils.getInstance().closePop();
            }
        });
        inflate.findViewById(R.id.tv_account_edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accountEditType = 1;
                mTv_account_manage.setText("帐号编辑");
                initAccount();
                PopupWindowUtils.getInstance().closePop();
            }
        });
        inflate.findViewById(R.id.tv_account_del).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accountEditType = 2;
                mTv_account_manage.setText("帐号删除");
                initAccount();
                PopupWindowUtils.getInstance().closePop();
            }
        });

        PopupWindowUtils.getInstance().createScalePopupWindow(getContext(), inflate, mTv_account_manage);
    }

    /**
     * 初始化帐号信息
     */
    private void initAccount() {
        List<AccountInfo> accountInfos = DbManagers.getInstance().queryUserInfo(apkName);
        if (accountInfos == null)
            return;

        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(llm);
        // 如果Item够简单，高度是确定的，打开FixSize将提高性能
        mRecyclerView.setHasFixedSize(true);
        // 设置Item默认动画，加也行，不加也行
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setNestedScrollingEnabled(false);

        CommonAdapter mCommonAdapter = new CommonAdapter(getContext(), R.layout.lauout_account_item, accountInfos);
        mCommonAdapter.setItemDataListener(new CommonAdapter.ItemDataListener<AccountInfo>() {
            @Override
            public void setItemData(CommonViewHolder holder, AccountInfo dataBean) {
                TextView tv_accouint_name = holder.getView(R.id.tv_accouint_name);
                ImageView iv_account_edit = holder.getView(R.id.iv_account_edit);

                if (accountEditType == 0) {
                    iv_account_edit.setVisibility(View.INVISIBLE);
                } else if (accountEditType == 1) {
                    iv_account_edit.setVisibility(View.VISIBLE);
                    iv_account_edit.setImageResource(R.mipmap.ic_edit);
                    iv_account_edit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DialogInputUtils.showInputDialog(getActivity(), new DialogInputUtils.DialogOnClickListener() {
                                @Override
                                public void onClick() {
                                    ToastUtils.getInstance().showShortToast("帐号编辑成功!");
                                    initAccount();
                                }
                            }, "帐号编辑", apkName, dataBean, Gravity.BOTTOM, 1.0, 0.0);
                        }
                    });
                } else if (accountEditType == 2) {
                    iv_account_edit.setVisibility(View.VISIBLE);
                    iv_account_edit.setImageResource(R.mipmap.ic_delete);
                    iv_account_edit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DbManagers.getInstance().delUserInfo(dataBean.getAccount_id());
                            initAccount();
                        }
                    });
                }
                tv_accouint_name.setText(dataBean.getNickname());
            }
        });

        mCommonAdapter.setOnItemClickListener(new CommonAdapter.OnItemClickListener() {

            @Override
            public void setOnItemClickListener(View view, int position) {
                AccountInfo accountInfo = accountInfos.get(position);
                Intent intent = new Intent();
                intent.setAction("app.intent.action.VIEW");
                intent.addCategory("android.intent.category.DEFAULT");
                Bundle bundle = new Bundle();
                bundle.putString("USERNAME", accountInfo.getUsername());
                bundle.putString("PASSWORD", accountInfo.getPassword());
                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            public void setOnItemLongClickListener(View view, int position) {
                 /*<activity android:name=".JumpTestActivity">
                    <intent-filter>
                        <!-- 自定义action -->
                        <action android:name="app.intent.action.VIEW"/>
                        <category android:name="android.intent.category.DEFAULT"/>
                    </intent-filter>
                </activity>*/

                /*Intent intent = getIntent();
                Bundle bundle = intent.getExtras();
                if (bundle != null) {
                    String data = bundle.getString("data");
                    Log.e("JumpTestActivity", data);
                }*/
            }
        });

        mCommonAdapter.setHasStableIds(true);
        mRecyclerView.setAdapter(mCommonAdapter);
    }
}

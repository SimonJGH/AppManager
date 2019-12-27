package com.simon.appmanager.update;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.widget.Toast;

import com.simon.appmanager.HomeActivity;
import com.simon.appmanager.utils.ToastUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static java.lang.Thread.sleep;

@SuppressWarnings("all")
public class UpdateVersion {

    /**
     * 检查版本
     *
     * @param context
     * @param updateBean
     * @return
     */
    public static int CheckVersion(final Context context, final UpdateBean updateBean, final boolean isToast) {
        String packageName = context.getPackageName();
        // 手机端的版本
        int nowCode = getVersionCode(context);
        int newCode = updateBean.getVersionCode();
        // 小于最新版本号
        if (nowCode < newCode) {
            checkPermission(context, updateBean);
        } else {
            if (isToast) {
                ToastUtils.getInstance().showShortToastBottom("已经是最新版本");
            }
        }
        return 0;
    }

    /**
     * 检查权限
     *
     * @param context
     * @param updateBean
     */
    public static void checkPermission(final Context context, final UpdateBean updateBean) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions((HomeActivity) context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1000);
            return;
        } else {
            showUpdateDialog(context, updateBean);
        }
    }

    /**
     * 展示更新对话框
     *
     * @param context
     * @param updateBean
     */
    public static void showUpdateDialog(final Context context, final UpdateBean updateBean) {
        final UpdateDialog updateDialog = new UpdateDialog(context);
        updateDialog.setData(updateBean, true, new UpdateDialogOperate() {
            @Override
            public void executeCancel(String text) {
                updateDialog.cancel();
            }

            @Override
            public void executeCommit(String text) {
                downFile(updateBean.getUrl(), context);
                updateDialog.cancel();
            }
        });
        updateDialog.show();
    }

    //下载进度dialog
    private static ProgressDialog progressDialog;

    /**
     * 下载apk文件
     *
     * @param url
     * @param context
     */
    public static void downFile(final String url, final Context context) {
        progressDialog = new ProgressDialog(context);    //进度条，在下载的时候实时更新进度，提高用户友好度
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("正在下载");
        // progressDialog.setMessage("请稍后...");
        progressDialog.setProgress(0);
        progressDialog.setCancelable(false);//点击返回键，禁止退出
        progressDialog.show();

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //下载失败
                downFial(context);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;//输入流
                FileOutputStream fos = null;//输出流
                try {
                    is = response.body().byteStream();//获取输入流
                    long total = response.body().contentLength();//获取文件大小
                    setMax(total, context);//为progressDialog设置大小
                    File file = null;
                    if (is != null) {
                        file = new File(Environment.getExternalStorageDirectory(), "update.apk");// 设置路径
                        fos = new FileOutputStream(file);
                        byte[] buf = new byte[1024];
                        int ch = -1;
                        int process = 0;
                        while ((ch = is.read(buf)) != -1) {
                            fos.write(buf, 0, ch);
                            process += ch;
                            // 这里就是关键的实时更新进度了！
                            downLoading(process, context);
                        }
                    }
                    fos.flush();
                    // 下载完成
                    if (fos != null) {
                        fos.close();
                    }
                    sleep(1000);
                    downSuccess(context, file);
                } catch (Exception e) {
                    downFial(context);
                } finally {
                    try {
                        if (is != null)
                            is.close();
                    } catch (IOException e) {
                    }
                    try {
                        if (fos != null)
                            fos.close();
                    } catch (IOException e) {
                    }
                }
            }
        });
    }

    /**
     * 设置进度条
     *
     * @param total
     * @param context
     */
    public static void setMax(final long total, Context context) {
        ((HomeActivity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog.setMax((int) total);
            }
        });
    }

    /**
     * 进度条实时更新
     *
     * @param i
     */
    public static void downLoading(final int i, Context context) {
        ((HomeActivity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog.setProgress(i);
            }
        });
    }

    /**
     * 下载失败
     *
     * @param context
     */
    public static void downFial(final Context context) {
        ((HomeActivity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog.cancel();
                Toast.makeText(context, "更新失败", Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * 下载成功
     *
     * @param context
     * @param file
     */
    public static void downSuccess(final Context mContext, final File file) {
        //安装
        ((HomeActivity) mContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                try {
                    sleep(1000);
                    installApk(file, mContext);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 安装apk
     *
     * @param file
     * @param context
     */
    public static void installApk(File apkfile, Context mContext) {
        Intent intent = new Intent();
        //执行动作
        intent.setAction(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT > 23) { //判读版本是否在7.0以上
            Uri contentUri = FileProvider.getUriForFile(mContext, mContext.getPackageName() + ".fileprovider", apkfile);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
            //兼容8.0
            if (android.os.Build.VERSION.SDK_INT >= 26) {
                boolean hasInstallPermission = mContext.getPackageManager().canRequestPackageInstalls();
                if (!hasInstallPermission) {
                    //请求安装未知应用来源的权限
                    ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.REQUEST_INSTALL_PACKAGES}, 6666);
                }
            }
        } else {
            intent.setDataAndType(Uri.fromFile(apkfile),
                    "application/vnd.android.package-archive");
        }
        mContext.startActivity(intent);
    }

    /**
     * 获取版本号
     *
     * @param context
     * @return
     */
    public static int getVersionCode(Context context) {
        int versionCode = 0;
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            versionCode = packageInfo.versionCode;
            return versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return 0;
        }
    }
}

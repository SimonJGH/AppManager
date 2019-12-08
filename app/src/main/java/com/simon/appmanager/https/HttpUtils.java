package com.simon.appmanager.https;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.simon.appmanager.MyApplication;
import com.simon.appmanager.https.entity.BaseOutputBean;

import org.xutils.common.Callback;
import org.xutils.common.util.KeyValue;
import org.xutils.http.RequestParams;
import org.xutils.http.body.MultipartBody;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 网络工具类
 */
@SuppressWarnings("all")
public class HttpUtils {
    //获取数据成功
    public static final int SUCCESS = 200;
    //获取数据+提示
    public static final int SUCCESS_CODE = 201;
    //暂无数据
    public static final int NO_DATA = 300;
    //非企业用户注册
    public static final int CODE400 = 400;
    //数据库异常
    public static final int CODE401 = 401;
    //Token失效
    public static final int CODE500 = 500;
    //其他
    public static final int CODE4012 = 4012;

    private HttpUtils() {
    }

    public static HttpUtils getInstance() {
        return SafeMode.mHttp;
    }

    public static class SafeMode {
        private static final HttpUtils mHttp = new HttpUtils();
    }

    /**
     * Post上传
     *
     * @param url
     * @param obj
     * @param commonCallBack
     */
    public void postMethod(String url, Object obj, CommonCallBack<String> commonCallBack) {
        RequestParams params = new RequestParams(ApiConstanse.BASE_URL + url);

        Gson gson = new Gson();
        String json = gson.toJson(obj);

        params.addHeader("X-Auth-Token", PreferencesCookie.getString(MyApplication.getInstance(), "TOKEN", ""));
        params.setAsJsonContent(true);
        params.setBodyContent(json);

        x.http().post(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
//                if (url.equals(ApiConstanse.getUserOfflineList)) {
//                    Log.i("Simon", "onSuccess = " + result);
//                }
                BaseOutputBean baseOutputBean = new Gson().fromJson(result, BaseOutputBean.class);
                switch (baseOutputBean.getCode()) {
                    case CODE500:
                       // IntentUtils.startNewTaskActivity(MyApplication.getInstance(), LoginActivity.class);
                        break;
                    case SUCCESS:
                        commonCallBack.requestSuccess(result);
                        break;
                    case SUCCESS_CODE:
                        commonCallBack.requestSuccess(result);
                        break;
                    case NO_DATA:
                        commonCallBack.requestSuccess(result);
                        break;
                    default:
                        commonCallBack.requestError(baseOutputBean.getMsg());
                        break;
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
//                if (url.equals(ApiConstanse.getUserOfflineList)) {
//                    Log.i("Simon", "onError = " + ex.getMessage());
//                }

                // ToastUtils.getInstance().showShortToast(ex.getMessage());
                commonCallBack.requestError(ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                // Log.i("Simon", "Http Request Finished");
                commonCallBack.requestFinished();
            }
        });
    }

    /**
     * 上传文件
     *
     * @param url
     * @param filePath
     * @param obj
     * @param commonCallBack
     */
    public void uploadFile(String url, String filePath, Object obj, CommonCallBack<String> commonCallBack) {
        String json = "";
        //构建RequestParams对象，传入请求的服务器地址URL
        RequestParams params = new RequestParams(ApiConstanse.BASE_URL + url);

        if (obj != null) {
            Gson gson = new Gson();
            json = gson.toJson(obj);
        }
        params.setAsJsonContent(true);
        List<KeyValue> list = new ArrayList<>();

        list.add(new KeyValue("file", new File(filePath)));
        if (!TextUtils.isEmpty(json)) {
            list.add(new KeyValue("parameters", json));
        }
        MultipartBody body = new MultipartBody(list, "UTF-8");
        params.setRequestBody(body);

        x.http().post(params, new org.xutils.common.Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                // Log.i("Simon", "上传文件：onSuccess = " + result);
                commonCallBack.requestSuccess(result);
            }

            @Override
            public void onFinished() {
                //上传完成
                commonCallBack.requestFinished();
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                // Log.i("Simon", "上传文件：onError = " + ex.toString());
                commonCallBack.requestError(ex.getMessage());
            }
        });
    }

}

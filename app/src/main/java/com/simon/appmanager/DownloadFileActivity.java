package com.simon.appmanager;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.simon.appmanager.adapter.CommonEmptyAdapter;
import com.simon.appmanager.adapter.CommonViewHolder;
import com.simon.appmanager.utils.ToastUtils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@ContentView(R.layout.activity_download_file)
@SuppressWarnings("all")
public class DownloadFileActivity extends BaseActivity {
    @ViewInject(R.id.smartrefresh)
    SmartRefreshLayout mSmartRefresh;

    @ViewInject(R.id.recyclerview)
    RecyclerView mRecyclerView;

    private CommonEmptyAdapter mCommonAdapter;
    private List<File> mList = new ArrayList<>();
    private int currentPage = 1;
    private int totalPage = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initRecyclerview();
        queryFiles();
    }

    @Event(value = {R.id.iv_download_back})
    private void clickButton(View view) {
        switch (view.getId()) {
            case R.id.iv_download_back:
                finishActivity();
                break;
        }
    }

    private void initRecyclerview() {
        mSmartRefresh.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                if (currentPage < totalPage) {
                    currentPage++;
                    queryFiles();
                } else {
                    mSmartRefresh.finishLoadMoreWithNoMoreData();
                }
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                currentPage = 1;
                mList.clear();
                queryFiles();
            }
        });

        LinearLayoutManager llm = new LinearLayoutManager(DownloadFileActivity.this);
        mRecyclerView.setLayoutManager(llm);
        // 如果Item够简单，高度是确定的，打开FixSize将提高性能
        mRecyclerView.setHasFixedSize(true);
        // 设置Item默认动画，加也行，不加也行
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setNestedScrollingEnabled(false);

        mCommonAdapter = new CommonEmptyAdapter(DownloadFileActivity.this, R.layout.layout_download_file_item, mList);
        mCommonAdapter.setItemDataListener(new CommonEmptyAdapter.ItemDataListener<File>() {
            @Override
            public void setItemData(CommonViewHolder holder, File file) {
                TextView tv_download_name = holder.getView(R.id.tv_download_name);
                TextView tv_download_time = holder.getView(R.id.tv_download_time);

                if (file.exists()) {
                    tv_download_name.setText("文件名称：" + file.getName());
                    FileInputStream fis = null;
                    try {
                        fis = new FileInputStream(file);
                        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                                .format(new Date(file.lastModified()));
                        tv_download_time.setText("创建时间：" + time);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        mCommonAdapter.setOnItemEmptyClickListener(new CommonEmptyAdapter.OnItemEmptyClickListener() {
            @Override
            public void setOnEmptyClickListener() {
                queryFiles();
            }

            @Override
            public void setOnItemClickListener(View view, int position) {
               File file = mList.get(position);
//                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//                Uri uri = FileProvider.getUriForFile(DownloadFileActivity.this, "com.simon.appmanager.fileprovider", file);
//                intent.setData(uri);
//                intent.addCategory(Intent.CATEGORY_OPENABLE);
//                startActivity(intent);
//                Uri uri;
//                if (Build.VERSION.SDK_INT >= 24) {
//                    uri = FileProvider.getUriForFile(DownloadFileActivity.this, "com.simon.appmanager.fileprovider", file);
//                } else {
//                    uri = Uri.fromFile(file);
//                }
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_TEXT, "文件所在路径：打开文件管理App-选择储存类型（手机）-A黑识科技"+file.getName());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(Intent.createChooser(intent, "分享"));
            }

            @Override
            public void setOnItemLongClickListener(View view, int position) {
                File file = mList.get(position);
                if (file.exists()) {
                    file.delete();
                    ToastUtils.getInstance().showShortToast("文件删除成功");
                    mCommonAdapter.notifyItemRemoved(position);
                    mList.remove(position);
                }
            }
        });

        mCommonAdapter.setHasStableIds(true);
        mRecyclerView.setAdapter(mCommonAdapter);
    }

    /**
     * 查找文件
     */
    private void queryFiles() {
        File folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/A黑识科技/");
        File[] files = folder.listFiles();
        List<File> asList = Arrays.asList(files);
        for (File file : asList) {
            if (file.isFile()) {
                mList.add(file);
            }
        }

        mCommonAdapter.notifyDataSetChanged();
        mSmartRefresh.finishRefresh();
        mSmartRefresh.finishLoadMore();
    }
}

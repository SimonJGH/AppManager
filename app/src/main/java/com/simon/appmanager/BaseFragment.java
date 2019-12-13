package com.simon.appmanager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.simon.appmanager.view.LoadingDialog;

import org.xutils.x;

/**
 * Created by yds
 */
public class BaseFragment extends Fragment {

    private boolean injected = false;
    public Handler mHandler = new Handler();
    public LoadingDialog mLoadingDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        injected = true;
        return x.view().inject(this, inflater, container);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!injected) {
            x.view().inject(this, this.getView());
        }

        mLoadingDialog = new LoadingDialog(getContext());
    }

    public void startActivity(Context context, Class mclass) {
        Intent intent = new Intent();
        intent.setClass(context, mclass);
        context.startActivity(intent);
        getActivity().overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }

    public void startNewTaskActivity(Context context, Class mclass) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setClass(context, mclass);
        context.startActivity(intent);
        getActivity().overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }

    public void startActivity(Context context, Class mclass, Bundle bundle) {
        Intent intent = new Intent();
        intent.putExtras(bundle);
        intent.setClass(context, mclass);
        context.startActivity(intent);
        getActivity().overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }
}

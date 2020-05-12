package com.nexwise.pointscan.base;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * 基础交互
 * Created by adolf_dong on 2016/5/5.
 */
public abstract class BaseFragment extends Fragment {
    public static final int ONE = 1;
    public static final int TWO = 2;
    private ProgressDialog progressDialog;
    private Toast mToast;

    public void showToat(String content) {
        if (mToast == null) {
            mToast = Toast.makeText(getActivity(), content, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(content);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.show();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findView(view, savedInstanceState);
        onCloudMode();
    }

    /**
     * 处于服务器模式的操作
     */
    public abstract void onCloudMode();

    /**
     * 实例化页面控件
     *
     * @param view               ui实例
     * @param savedInstanceState bundle
     */
    protected abstract void findView(View view, Bundle savedInstanceState);

    /**
     * ProgressDialog交互
     *
     * @param title   标题
     * @param message 内容
     * @param cancel  是否可被取消
     */
    public void showProgressDialog(String title, String message, boolean cancel) {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(cancel);
        progressDialog.show();

    }

    /**
     * 改变ProgressDialog展示的内容
     *
     * @param message 想要改变的内容
     */
    public void setDialogMessage(String message) {
        if (progressDialog != null) {
            progressDialog.setMessage(message);
        }
    }

    /**
     * 设置dialog是否可被点击取消
     *
     * @param cancelAble 可以取消设置为true
     */
    public void setDialogCancelAble(boolean cancelAble) {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.setCancelable(cancelAble);
        }
    }

    /**
     * 取消当前展示的ProgressDialog
     */
    public void disMissProgressDialog() {
        if (progressDialog != null) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
    }

    /**
     * 显示一般的alertdialog
     *
     * @param mode            ONE,单个按钮，TWO,两个按钮
     * @param title           标题
     * @param msg             显示信息
     * @param pString         确认按钮文字
     * @param nString         取消按钮文字
     * @param simpleDialogLSN 确认和取消的两个点击事件
     */
    public void showSimpleDialog(int mode, String title, String msg, String pString, String nString, final SimpleDialogLSN simpleDialogLSN) {
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setTitle(title);
        alert.setMessage(msg);
        switch (mode) {
            case TWO:
                alert.setNegativeButton(nString, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        simpleDialogLSN.nOnClick();
                    }
                });
            case ONE:
                alert.setPositiveButton(pString, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        simpleDialogLSN.pOnClick();
                    }
                });
                break;
        }
        alert.show();

    }

    /**
     * 显示ItemList类型alertdialog
     *
     * @param title         标题
     * @param items         显示数据String[]
     * @param itemDialogLSN 点击事件
     */
    public void showItemDialog(String title, String[] items, final ItemDialogLSN itemDialogLSN) {
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        if (title != null) {
            alert.setTitle(title);
        }
        alert.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                itemDialogLSN.onItemDialogClick(which);
            }
        });
        alert.show();
    }

    /**
     * 可点击dialog
     *
     * @param title           标题
     * @param message         显示信息
     * @param cancel          是否可按返回撤销
     * @param btnStr          按钮显示文字
     * @param onClickListener 按钮点击事件
     */
    public void showProgressDialogCanClick(String title, String message, boolean cancel, String btnStr, DialogInterface.OnClickListener onClickListener) {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
        progressDialog.setButton(DialogInterface.BUTTON_POSITIVE, btnStr, onClickListener);
        progressDialog.setCancelable(cancel);
        progressDialog.show();
    }

    /**
     * alert点击事件监听器
     */
    public interface SimpleDialogLSN {
        /**
         * 确认按钮点击执行
         */
        void pOnClick();

        /**
         * 取消按钮点击执行
         */
        void nOnClick();
    }

    public interface ItemDialogLSN {
        void onItemDialogClick(int which);
    }
}

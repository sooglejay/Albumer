package sooglejay.youtu.utils;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import sooglejay.youtu.R;
import sooglejay.youtu.constant.PreferenceConstant;

public class ProgressDialogUtil {
    private AlertDialog dialog;
    private TextView tv;
    private Context context;
    boolean isCanceledOnTouchOutside = true;

    public ProgressDialogUtil(Context context) {
        initDialog(context);
        this.context = context;
    }

    private void initDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.view_progress_bar_dialog, null, false);
        tv = (TextView) view.findViewById(R.id.tv);
        builder.setView(view);
        dialog = builder.create();
        isCanceledOnTouchOutside = !PreferenceUtil.load(context, PreferenceConstant.SWITCH_DIALOG_PROGRESS_CANCELED_ON_TOUCH_OUTSIDE, false);
//        dialog = new ProgressDialog(context);
        dialog.setCanceledOnTouchOutside(isCanceledOnTouchOutside);
        dialog.setCancelable(isCanceledOnTouchOutside);
    }


    /**
     * 显示
     */
    public void show(String text) {
        try {
            isCanceledOnTouchOutside = PreferenceUtil.load(context, PreferenceConstant.SWITCH_DIALOG_PROGRESS_CANCELED_ON_TOUCH_OUTSIDE, true);

            Log.e("jwjw",isCanceledOnTouchOutside+"");
//        dialog = new ProgressDialog(context);
            dialog.setCanceledOnTouchOutside(isCanceledOnTouchOutside);
            dialog.setCancelable(isCanceledOnTouchOutside);
            if (!((Activity) context).isFinishing())
                if (dialog != null) {
                    tv.setText(text);
                    dialog.show();
                }
        } catch (Exception e) {
            Log.e("jwjw", "catch show dialog error:" + e.toString());
        }

    }

    /**
     * 隐藏
     */
    public void hide() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}

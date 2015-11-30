package sooglejay.youtu.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.lang.reflect.AccessibleObject;
import java.util.ArrayList;

import sooglejay.youtu.R;
import sooglejay.youtu.api.faceidentify.IdentifyItem;
import sooglejay.youtu.utils.GetTagUtil;
import sooglejay.youtu.utils.UIUtil;


/**
 * Created by Administrator on 2015/10/18.
 */
public class DialogFragmentCreater extends DialogFragment {
    public static final int DIALOG_FACE_OPERATION = 1000;//after detect and recognize face , do some operation

    public final static String dialog_fragment_key = "fragment_id";
    public final static String dialog_fragment_tag = "dialog";

    private Context mContext;

    private FragmentManager fragmentManager;

    public void setDialogContext(Activity mContext, FragmentManager fragmentManager) {
        this.mContext = mContext;
        this.fragmentManager = fragmentManager;
    }

    private Dialog outerDialog;


    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }


    @Override
    public void show(FragmentManager manager, String tag) {
        super.show(manager, tag);
    }

    /**
     * 调用Dialog的地方
     * * @param fragment_id
     */
    public void showDialog(Context mContext, int fragment_id) {
        this.mContext = mContext;
        try {
            Bundle args = new Bundle();
            args.putInt(dialog_fragment_key, fragment_id);
            this.setArguments(args);
            this.show(fragmentManager, dialog_fragment_tag);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void dismiss() {
        // TODO Auto-generated method stub
        super.dismiss();
    }

    @Override
    public Dialog getDialog() {
        // TODO Auto-generated method stub
        return super.getDialog();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            int fragment_id = getArguments().getInt(dialog_fragment_key);
            switch (fragment_id) {
                case DIALOG_FACE_OPERATION:
                    return showFaceOperationDialog(mContext);
            }
        }
        return super.onCreateDialog(savedInstanceState);
    }

    /**
     * 自定义Dialog
     *
     * @param mContext
     * @return
     */
    private Dialog showFaceOperationDialog(final Context mContext) {
        View convertView = LayoutInflater.from(mContext).inflate(R.layout.dialog_face_operation, null);
        TextView tv_edit_info = (TextView) convertView.findViewById(R.id.tv_edit_info);
        TextView tv_call = (TextView) convertView.findViewById(R.id.tv_call);
        TextView tv_send_message = (TextView) convertView.findViewById(R.id.tv_send_message);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCallBack.onClick(view);
                dismiss();
            }
        };

        tv_call.setOnClickListener(listener);
        tv_edit_info.setOnClickListener(listener);
        tv_send_message.setOnClickListener(listener);



        final Dialog dialog = new Dialog(mContext, R.style.CustomDialog);
//        dialog.setCanceledOnTouchOutside(false);//要求触碰到外面能够消失
        dialog.setContentView(convertView);

        dialog.getWindow().setWindowAnimations(R.style.dialog_right_control_style);
        //当dialog 显示的时候，弹出键盘
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
            }
        });

        outerDialog = dialog;
        return dialog;
    }


    public void setOnCallBack(OnClickCallBack onCallBack) {
        this.onCallBack = onCallBack;
    }

    private OnClickCallBack onCallBack;

    public interface OnClickCallBack {
       public void onClick(View view);
    }
}

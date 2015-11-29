package sooglejay.youtu.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import sooglejay.youtu.R;
import sooglejay.youtu.api.faceidentify.IdentifyItem;
import sooglejay.youtu.ui.EditFaceUserInfoActivity;
import sooglejay.youtu.utils.GetTagUtil;
import sooglejay.youtu.utils.ImageUtils;
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


    public void setFaceDatas(Bitmap faceBitmap,ArrayList<IdentifyItem> identifyItems) {
        this.faceBitmap = faceBitmap;
        this.identifyItems = identifyItems;
    }

    private Bitmap faceBitmap;//人脸部位的bitmap
    private ArrayList<IdentifyItem> identifyItems;//人脸识别 置信度 top5列表

    public void setDialogContext(Context mContext, FragmentManager fragmentManager) {
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
        tv_edit_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (faceBitmap != null) {
                    try {
                        byte[] faceByteArray = ImageUtils.Bitmap2Bytes(faceBitmap);
                        EditFaceUserInfoActivity.startActivity(mContext, faceByteArray,identifyItems);
                    } catch (NullPointerException npe) {
                        Log.e("jwjw", "图片转换失败" + npe.toString());
                        Toast.makeText(mContext, "图片转换失败,无法跳转到编辑页面", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        tv_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(identifyItems!=null && identifyItems.size()>0) {
                    String phoneStr = GetTagUtil.getPhoneNumber(identifyItems.get(0).getTag());
                    UIUtil.takePhoneCall(getActivity(), phoneStr,0);
                }
            }
        });

        tv_send_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(identifyItems!=null && identifyItems.size()>0)
                {
                    String phoneStr = GetTagUtil.getPhoneNumber(identifyItems.get(0).getTag());
                    UIUtil.sendMessage(getActivity(),phoneStr,null,0);
                }

            }
        });
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
}

package sooglejay.youtu.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import sooglejay.youtu.R;
import sooglejay.youtu.adapter.ChooseFaceAdapter;
import sooglejay.youtu.api.faceidentify.IdentifyItem;
import sooglejay.youtu.constant.PreferenceConstant;
import sooglejay.youtu.utils.PreferenceUtil;
import sooglejay.youtu.utils.ScreenUtils;
import sooglejay.youtu.utils.SpannableStringUtil;


/**
 * Created by Administrator on 2015/10/18.
 */
public class DialogFragmentCreater extends DialogFragment {
    public static final int DIALOG_FACE_OPERATION = 1000;//after detect and recognize face , do some operation
    public static final int DIALOG_CHOOSE_FACE = 1001;//top 5 faces available to choose
    public static final int DIALOG_showCreateNewGroupDialog = 1002;//create new group

    public final static String dialog_fragment_key = "fragment_id";
    public final static String dialog_fragment_tag = "dialog";

    private Context mContext;

    private FragmentManager fragmentManager;

    public void initDialogFragment(Activity mContext, FragmentManager fragmentManager) {
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
                case DIALOG_CHOOSE_FACE:
                    return showChooseFaceDialog(mContext);
                case DIALOG_showCreateNewGroupDialog:
                    return showCreateNewGroupDialog(mContext);
            }
        }
        return super.onCreateDialog(savedInstanceState);
    }


    private Dialog showFaceOperationDialog(final Context mContext) {
        View convertView = LayoutInflater.from(mContext).inflate(R.layout.dialog_face_operation, null);
        TextView tv_edit_info = (TextView) convertView.findViewById(R.id.tv_edit_info);
        TextView tv_call = (TextView) convertView.findViewById(R.id.tv_call);
        TextView tv_send_message = (TextView) convertView.findViewById(R.id.tv_send_message);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onFaceOperationCallBack.onClick(view);
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

    private Dialog showCreateNewGroupDialog(final Context mContext) {
        View convertView = LayoutInflater.from(mContext).inflate(R.layout.dialog_create_new_group, null);
        final EditText etName = (EditText) convertView.findViewById(R.id.et_name);
        TextView tvCancel = (TextView) convertView.findViewById(R.id.tv_cancel);
        TextView tvConfirm = (TextView) convertView.findViewById(R.id.tv_confirm);
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCreateNewGroupCallBack.onClick(view,etName);
                dismiss();
            }
        };

        tvCancel.setOnClickListener(listener);
        tvConfirm.setOnClickListener(listener);


        final Dialog dialog = new Dialog(mContext,R.style.CreateNewGroupDialog);
//        dialog.setCanceledOnTouchOutside(false);//要求触碰到外面能够消失
        dialog.setContentView(convertView);

        dialog.getWindow().setWindowAnimations(R.style.create_new_group_style);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = ScreenUtils.getScreenWidth(mContext);
        lp.height = ScreenUtils.getScreenHeight(mContext);
        dialog.getWindow().setAttributes(lp);


        //当dialog 显示的时候，弹出键盘
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                new AsyncTask<Integer, Void, Void>() {
                    @Override
                    protected Void doInBackground(Integer... params) {
                        try {
                            Thread.sleep(params[0]);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void value) {
                        super.onPostExecute(value);
                        if (outerDialog.isShowing())
                            if (etName != null) {
                                etName.requestFocus();
                                InputMethodManager imm = (InputMethodManager) etName.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
                            }
                    }
                }.execute(500);
            }
        });

        outerDialog = dialog;
        return dialog;
    }


    public void setIdentifyItems(ArrayList<IdentifyItem> identifyItems) {
        this.identifyItems = identifyItems;
    }

    private ArrayList<IdentifyItem> identifyItems = new ArrayList<>();

    private Dialog showChooseFaceDialog(final Context mContext) {
        View convertView = LayoutInflater.from(mContext).inflate(R.layout.dialog_choose_face, null);
        TextView tv_title = (TextView) convertView.findViewById(R.id.tv_title);
        TextView tv_from_group_name = (TextView) convertView.findViewById(R.id.tv_from_group_name);
        ListView listView = (ListView) convertView.findViewById(R.id.list_view);
        TextView tv_add_new_person = (TextView) convertView.findViewById(R.id.tv_add_new_person);
        ChooseFaceAdapter adapter = new ChooseFaceAdapter(mContext, identifyItems);
        listView.setAdapter(adapter);
        tv_title.setText("相似度Top"+identifyItems.size());
        String groupNameStr = PreferenceUtil.load(mContext, PreferenceConstant.IDENTIFY_GROUP_NAME,"1");

        tv_from_group_name.setText("");
        tv_from_group_name.append(SpannableStringUtil.addUnderLineSpan("人脸数据源来自群组："+groupNameStr));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                onChooseFaceCallBack.onItemClickListener(adapterView, view, i, l);
                dismiss();

            }
        });
        tv_add_new_person.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onChooseFaceCallBack.onClick(view);
                dismiss();

            }
        });

        tv_from_group_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onChooseFaceCallBack.onClick(view);
                dismiss();
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


    //用户选择完最相似的一个人脸后，弹出操作对话框
    public void setOnFaceOperationCallBack(OnFaceOperationCallBack onFaceOperationCallBack) {
        this.onFaceOperationCallBack = onFaceOperationCallBack;
    }
    private OnFaceOperationCallBack onFaceOperationCallBack;
    public interface OnFaceOperationCallBack {
        public void onClick(View view);
    }


    //人脸识别后，由用户自己选择top5中最相似的一个
    public void setOnChooseFaceCallBack(OnChooseFaceCallBack onChooseFaceCallBack) {
        this.onChooseFaceCallBack = onChooseFaceCallBack;
    }
    private OnChooseFaceCallBack onChooseFaceCallBack;
    public interface OnChooseFaceCallBack {
        public void onItemClickListener(AdapterView<?> adapterView, View view, int i, long l);
        public void onClick(View view);
    }


    //创建新群组
    public void setOnCreateNewGroupCallBack(OnCreateNewGroupCallBack onCreateNewGroupCallBack) {
        this.onCreateNewGroupCallBack = onCreateNewGroupCallBack;
    }
    private OnCreateNewGroupCallBack onCreateNewGroupCallBack;
    public interface OnCreateNewGroupCallBack {
        public void onClick(View view,EditText editText);
    }


}

package sooglejay.youtu.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import retrofit.RetrofitError;
import retrofit.client.Response;
import sooglejay.youtu.R;
import sooglejay.youtu.api.faceidentify.IdentifyItem;
import sooglejay.youtu.api.newperson.NewPersonResponseBean;
import sooglejay.youtu.api.newperson.NewPersonUtil;
import sooglejay.youtu.constant.IntConstant;
import sooglejay.youtu.constant.NetWorkConstant;
import sooglejay.youtu.model.NetCallback;
import sooglejay.youtu.utils.GetGroupIdsUtil;
import sooglejay.youtu.utils.GetTagUtil;
import sooglejay.youtu.utils.ImageUtils;
import sooglejay.youtu.widgets.RoundImageView;
import sooglejay.youtu.widgets.TitleBar;
import sooglejay.youtu.widgets.imagepicker.bean.Image;
import sooglejay.youtu.widgets.youtu.sign.Base64Util;

/**
 * Created by JammyQtheLab on 2015/11/28.
 */
public class EditFaceUserInfoActivity extends BaseActivity {
    private static final String EXTRA_FACE_BITMAP= "face_bitmap";
    private static final String EXTRA_FACE_IDENTIFY_DATAS= "identifyItems";
    private Activity activity;
    private Bitmap faceBitmap;
    private  byte[]faceByteArray;
    private ArrayList<IdentifyItem> identifyItems;//人脸识别 置信度 top5列表

    public static void startActivity(Context context,byte[]faceByte,ArrayList<IdentifyItem> identifyItems)
    {
        Intent intent = new Intent(context,EditFaceUserInfoActivity.class);
        intent.putExtra(EXTRA_FACE_BITMAP,faceByte);
        intent.putParcelableArrayListExtra(EXTRA_FACE_IDENTIFY_DATAS,identifyItems);
        context.startActivity(intent);
    }


    private TitleBar titleBar;
    private RoundImageView ivAvatar;
    private EditText etName;
    private EditText etPhoneNumber;

    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2015-11-29 18:30:33 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private void findViews() {
        titleBar = (TitleBar)findViewById( R.id.title_bar );
        ivAvatar = (RoundImageView)findViewById( R.id.iv_avatar );
        etName = (EditText)findViewById( R.id.et_name );
        etPhoneNumber = (EditText)findViewById( R.id.et_phone_number );
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_face_user_info);
        activity = this;
        findViews();

        titleBar = (TitleBar)findViewById(R.id.title_bar);
        titleBar.initTitleBarInfo("编辑", R.drawable.arrow_left, -1, "", "确定");
        titleBar.setOnTitleBarClickListener(new TitleBar.OnTitleBarClickListener() {
            @Override
            public void onLeftButtonClick(View v) {
                activity.finish();
            }

            @Override
            public void onRightButtonClick(View v) {
                String phoneStr = etPhoneNumber.getText().toString();
                String nameStr = etName.getText().toString();
                ArrayList<String>groupids = new ArrayList<String>();
                groupids.add(IntConstant.GROUP_ID+"");

                NewPersonUtil.newPerson(activity, NetWorkConstant.APP_ID, groupids, "1", Base64Util.encode(faceByteArray), nameStr, GetTagUtil.getTag(nameStr,phoneStr, GetGroupIdsUtil.getGroupIds(groupids)), new NetCallback<NewPersonResponseBean>(activity) {
                    @Override
                    public void onFailure(RetrofitError error, String message) {

                    }

                    @Override
                    public void success(NewPersonResponseBean newPersonResponseBean, Response response) {

                    }
                });
            }
        });

        faceByteArray = getIntent().getByteArrayExtra(EXTRA_FACE_BITMAP);
        identifyItems = getIntent().getParcelableArrayListExtra(EXTRA_FACE_IDENTIFY_DATAS);

        faceBitmap = ImageUtils.Bytes2Bimap(faceByteArray);
        ivAvatar = (RoundImageView)findViewById(R.id.iv_avatar);
        ivAvatar.setImageBitmap(faceBitmap);


        if(identifyItems!=null && identifyItems.size()>0)
        {
            String tag = identifyItems.get(0).getTag();
            etName.setText(GetTagUtil.getName(tag));
            etPhoneNumber.setText(GetTagUtil.getPhoneNumber(tag));
        }

    }
}

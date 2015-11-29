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

import sooglejay.youtu.R;
import sooglejay.youtu.utils.ImageUtils;
import sooglejay.youtu.widgets.RoundImageView;
import sooglejay.youtu.widgets.TitleBar;

/**
 * Created by JammyQtheLab on 2015/11/28.
 */
public class EditFaceUserInfoActivity extends BaseActivity {
    private static final String EXTRA_FACE_BITMAP= "face_bitmap";
    private Activity activity;
    private Bitmap faceBitmap;
    public static void startActivity(Context context,byte[]faceByte)
    {
        Intent intent = new Intent(context,EditFaceUserInfoActivity.class);
        intent.putExtra(EXTRA_FACE_BITMAP,faceByte);
        context.startActivity(intent);
    }


    private TitleBar titleBar;
    private RoundImageView ivAvatar;
    private EditText etName;
    private EditText etPhoneNumber;
    private EditText etQq;
    private EditText etWeixin;
    private TextView tvSave;

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
        etQq = (EditText)findViewById( R.id.et_qq );
        etWeixin = (EditText)findViewById( R.id.et_weixin );
        tvSave = (TextView)findViewById( R.id.tv_save );
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_face_user_info);
        activity = this;
        findViews();

        titleBar = (TitleBar)findViewById(R.id.title_bar);
        titleBar.initTitleBarInfo("编辑", R.drawable.arrow_left, -1, "", "");
        titleBar.setOnTitleBarClickListener(new TitleBar.OnTitleBarClickListener() {
            @Override
            public void onLeftButtonClick(View v) {
                activity.finish();
            }

            @Override
            public void onRightButtonClick(View v) {

            }
        });

        byte[]faceByteArray = getIntent().getByteArrayExtra(EXTRA_FACE_BITMAP);
        faceBitmap = ImageUtils.Bytes2Bimap(faceByteArray);
        ivAvatar = (RoundImageView)findViewById(R.id.iv_avatar);
        ivAvatar.setImageBitmap(faceBitmap);


        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String qqStr = etQq.getText().toString();
                String weixinStr = etWeixin.getText().toString();
                String phoneStr = etPhoneNumber.getText().toString();
                String nameStr = etName.getText().toString();



            }
        });

    }
}

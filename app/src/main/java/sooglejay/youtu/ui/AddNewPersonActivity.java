package sooglejay.youtu.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import sooglejay.youtu.R;
import sooglejay.youtu.api.faceidentify.IdentifyItem;
import sooglejay.youtu.constant.IntConstant;
import sooglejay.youtu.utils.GetTagUtil;
import sooglejay.youtu.utils.ImageUtils;
import sooglejay.youtu.widgets.RoundImageView;
import sooglejay.youtu.widgets.TitleBar;

/**
 * Created by JammyQtheLab on 2015/11/28.
 */
public class AddNewPersonActivity extends BaseActivity {
    private static final String EXTRA_FACE_IMAGE_PATH = "face_bitmap";
    private static final String EXTRA_FACE_IDENTIFY_DATAS = "identifyItems";
    private Activity activity;
    private String imageFilePath;//图片文件的地址
    private ArrayList<IdentifyItem> identifyItems;//人脸识别 置信度 top5列表

    public static void startActivity(Context context, String imageFilePath, ArrayList<IdentifyItem> identifyItems) {
        Intent intent = new Intent(context, AddNewPersonActivity.class);
        intent.putExtra(EXTRA_FACE_IMAGE_PATH, imageFilePath);
        intent.putParcelableArrayListExtra(EXTRA_FACE_IDENTIFY_DATAS, identifyItems);
        context.startActivity(intent);
    }

    private TitleBar titleBar;
    private RoundImageView ivAvatar;
    private EditText etName;
    private EditText etPhoneNumber;

    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2015-11-30 22:59:55 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private void findViews() {
        titleBar = (TitleBar) findViewById(R.id.title_bar);
        ivAvatar = (RoundImageView) findViewById(R.id.ivAvatar);
        etName = (EditText) findViewById(R.id.et_name);
        etPhoneNumber = (EditText) findViewById(R.id.et_phone_number);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_person_activity);
        activity = this;
        findViews();

        titleBar.initTitleBarInfo("添加人脸联系人", R.drawable.arrow_left, -1, "", "确定");
        titleBar.setOnTitleBarClickListener(new TitleBar.OnTitleBarClickListener() {
            @Override
            public void onLeftButtonClick(View v) {
                activity.finish();
            }

            @Override
            public void onRightButtonClick(View v) {
                String phoneStr = etPhoneNumber.getText().toString();
                String nameStr = etName.getText().toString();
                ArrayList<String> groupids = new ArrayList<String>();
                groupids.add(IntConstant.GROUP_ID + "");

//                NewPersonUtil.newPerson(activity, NetWorkConstant.APP_ID, groupids, nameStr, Base64Util.encode(faceByteArray), nameStr, GetTagUtil.getTag(nameStr, "", "", phoneStr), new NetCallback<NewPersonResponseBean>(activity) {
//                    @Override
//                    public void onFailure(RetrofitError error, String message) {
//
//                    }
//
//                    @Override
//                    public void success(NewPersonResponseBean newPersonResponseBean, Response response) {
//
//                    }
//                });
            }
        });

        imageFilePath = getIntent().getStringExtra(EXTRA_FACE_IMAGE_PATH);
        identifyItems = getIntent().getParcelableArrayListExtra(EXTRA_FACE_IDENTIFY_DATAS);

        if (imageFilePath != null) {
            ImageLoader.getInstance().displayImage("file://" + imageFilePath, ivAvatar, ImageUtils.getOptions());
        }

        if (identifyItems != null && identifyItems.size() > 0) {
            String tag = identifyItems.get(0).getTag();
            etName.setText(GetTagUtil.getName(tag));
            etPhoneNumber.setText(GetTagUtil.getPhoneNumber(tag));
        }

    }
}

package sooglejay.youtu.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import retrofit.RetrofitError;
import retrofit.client.Response;
import sooglejay.youtu.R;
import sooglejay.youtu.api.detectface.DetectFaceResponseBean;
import sooglejay.youtu.api.detectface.DetectFaceUtil;
import sooglejay.youtu.api.detectface.FaceItem;
import sooglejay.youtu.api.facecompare.FaceCompareResponseBean;
import sooglejay.youtu.api.facecompare.FaceCompareUtil;
import sooglejay.youtu.constant.ExtraConstants;
import sooglejay.youtu.constant.NetWorkConstant;
import sooglejay.youtu.model.NetCallback;
import sooglejay.youtu.utils.ImageUtils;
import sooglejay.youtu.utils.ProgressDialogUtil;
import sooglejay.youtu.widgets.FaceImageView;
import sooglejay.youtu.widgets.TitleBar;
import sooglejay.youtu.widgets.imagepicker.MultiImageSelectorActivity;
import sooglejay.youtu.widgets.imagepicker.bean.Folder;
import sooglejay.youtu.widgets.imagepicker.bean.Image;
import sooglejay.youtu.widgets.youtu.Youtu;
import sooglejay.youtu.widgets.youtu.sign.Base64Util;


public class MainActivity extends BaseActivity {

    private TitleBar titleBar;

    private TextView tv_test;
    private TextView tv_result;
    private ArrayList<String> imageList = new ArrayList<>();
    private String resultPath;//图片最终位置

    private FaceImageView iv_image;
    private Activity activity;

    private boolean hasFolderGened = false;


    // 结果数据
    private ArrayList<String> resultList = new ArrayList<>();
    // 文件夹数据
    private ArrayList<Folder> mResultFolder = new ArrayList<>();

    Youtu faceYoutu  ;
    JSONObject respose;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activity = this;
        getSupportLoaderManager().initLoader(0, null, mLoaderCallback);
        titleBar = (TitleBar) findViewById(R.id.title_bar);
        titleBar.initTitleBarInfo("相册管理", -1, -1, "", "");


        tv_test = (TextView) findViewById(R.id.tv_test);
        tv_result = (TextView) findViewById(R.id.tv_result);
        iv_image =(FaceImageView)findViewById(R.id.iv_image);
        tv_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageUtils.startPickPhoto(activity, imageList, 10, false);
                ArrayList<String> urls = new ArrayList<String>();
                urls.add("http://img4.imgtn.bdimg.com/it/u=3668099963,344011271&fm=21&gp=0.jpg");
                urls.add("http://img1.imgtn.bdimg.com/it/u=941659139,2318819624&fm=21&gp=0.jpg");
                urls.add("http://img0.imgtn.bdimg.com/it/u=2278907624,1794819904&fm=21&gp=0.jpg");
                urls.add("http://img1.imgtn.bdimg.com/it/u=2271334871,1423653824&fm=21&gp=0.jpg");
                urls.add("http://img0.imgtn.bdimg.com/it/u=3551133733,3837171453&fm=21&gp=0.jpg");
                urls.add("http://img5.imgtn.bdimg.com/it/u=2699078590,911824813&fm=21&gp=0.jpg");
                urls.add("http://img3.imgtn.bdimg.com/it/u=3926533420,3434080491&fm=21&gp=0.jpg");
                urls.add("http://img5.imgtn.bdimg.com/it/u=48598112,4255180885&fm=21&gp=0.jpg");
                urls.add("http://img4.imgtn.bdimg.com/it/u=3985515607,2668814911&fm=21&gp=0.jpg");
                urls.add("http://img2.imgtn.bdimg.com/it/u=3966248764,2016989228&fm=21&gp=0.jpg");
                urls.add("http://img1.imgtn.bdimg.com/it/u=2916976433,2101664520&fm=21&gp=0.jpg");
                urls.add("http://img3.imgtn.bdimg.com/it/u=3637056205,2636938872&fm=21&gp=0.jpg");
                urls.add("http://img1.imgtn.bdimg.com/it/u=3928700240,1674800841&fm=21&gp=0.jpg");
                urls.add("http://img0.imgtn.bdimg.com/it/u=554268179,3326490655&fm=21&gp=0.jpg");
                urls.add("http://img5.imgtn.bdimg.com/it/u=520938181,3531679252&fm=21&gp=0.jpg");
                urls.add("http://img3.imgtn.bdimg.com/it/u=29132340,1272607089&fm=21&gp=0.jpg");
                urls.add("http://img3.imgtn.bdimg.com/it/u=29132340,1272607089&fm=21&gp=0.jpg");
                urls.add("http://img1.imgtn.bdimg.com/it/u=1346204935,1833295314&fm=21&gp=0.jpg");
                urls.add("http://img4.imgtn.bdimg.com/it/u=1957294427,3132353955&fm=21&gp=0.jpg");
                urls.add("http://img4.imgtn.bdimg.com/it/u=1004874572,2884824536&fm=21&gp=0.jpg");
                Intent intent = new Intent(activity, GalleryActivity.class);
                intent.putExtra(ExtraConstants.EXTRA_POSITION, 0);
                intent.putExtra(ExtraConstants.EXTRA_URLS, (ArrayList<String>) urls);
//                activity.startActivity(intent);
            }
        });
    }

    private void faceCompareUrl() {
        final ProgressDialogUtil progressDialogUtil = new ProgressDialogUtil(this);
        progressDialogUtil.show("正在发送请求...");
        FaceCompareUtil.faceCompareUrl(this, "", "", "http://open.youtu.qq.com/content/img/slide-1.jpg", "http://open.youtu.qq.com/content/img/slide-1.jpg", NetWorkConstant.APP_ID, new NetCallback<FaceCompareResponseBean>(this) {
            @Override
            public void onFailure(RetrofitError error, String message) {
                progressDialogUtil.hide();
            }

            @Override
            public void success(FaceCompareResponseBean netWorkResultBean, Response response) {
                progressDialogUtil.hide();

            }
        });
    }

    /**
     * @param imagePath
     * @return -1 null; -2 file is bad
     */
    private void detectface(final String imagePath) {
        final  ProgressDialogUtil p = new ProgressDialogUtil(this);
        p.show("正在检测...");
        DetectFaceUtil.detectFace(this, NetWorkConstant.APP_ID, Base64Util.encode(ImageUtils.getBytes(imagePath)), 1, new NetCallback<DetectFaceResponseBean>(this) {
            @Override
            public void onFailure(RetrofitError error, String message) {
                p.hide();

            }
            @Override
            public void success(DetectFaceResponseBean detectFaceResponseBean, Response response) {
                p.hide();
                tv_result.setText(detectFaceResponseBean.toString());
                List<FaceItem> faceItem = detectFaceResponseBean.getFace();
                if(faceItem!=null&&faceItem.size()>0) {
                    FaceItem faceItem1 = faceItem.get(0);
                    iv_image.setFaceItem(faceItem1);
               }
            }
        });

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            //若是从图库选择图
            case ImageUtils.REQUEST_CODE_PICK_IMAGE:
                if (resultCode == Activity.RESULT_OK) {
                    // 获取返回的图片列表
                    List<String> paths = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                    imageList.clear();
                    imageList.addAll(paths);
                    if (imageList == null) return;
                    if (imageList.size() == 1) {

                        ImageLoader.getInstance().displayImage("file://" + imageList.get(0), iv_image, ImageUtils.getOptions());
                        resultPath = ImageUtils.getImageFolderPath(this) + File.separator + System.currentTimeMillis() + ".jpg";
//                        ImageUtils.cropImage(this, Uri.fromFile(new File(imageList.get(0))), resultPath, 1, 1);
//                        Bitmap tempBitmap = BitmapFactory.decodeFile(imageList.get(0));
//                        Bitmap bitmap = ImageUtils.compressImage(tempBitmap,600);
//                        ImageUtils.saveMyBitmap(bitmap,resultPath);
                        detectface(imageList.get(0));

//                        String test = ImageUtils.getImageFolderPath(this)+File.separator+"test.jpg";
//
//                        ImageLoader.getInstance().displayImage("drawable://" + "test.jpg", iv_image, ImageUtils.getOptions());
                    }
                }
                break;
            //裁剪图片
            case ImageUtils.REQUEST_CODE_CROP_IMAGE:
                if (resultCode == Activity.RESULT_OK) {
                    //添加图片到list并且显示出来
                    //上传图片
                    if (!TextUtils.isEmpty(resultPath)) {
                        ImageLoader.getInstance().displayImage("file://" + resultPath, iv_image, ImageUtils.getOptions());
                        ImageUtils.compressAndSave(activity, resultPath, 600);
                        detectface(resultPath);
                    }
                }
                break;
            default:
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    private LoaderManager.LoaderCallbacks<Cursor> mLoaderCallback = new LoaderManager.LoaderCallbacks<Cursor>() {

        private final String[] IMAGE_PROJECTION = {
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.DATE_ADDED,
                MediaStore.Images.Media._ID};

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            if (id == 0) {
                CursorLoader cursorLoader = new CursorLoader(activity,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION,
                        null, null, IMAGE_PROJECTION[2] + " DESC");
                return cursorLoader;
            }

            return null;
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            if (data != null) {
                List<Image> images = new ArrayList<>();
                int count = data.getCount();
                if (count > 0) {
                    data.moveToFirst();
                    do {
                        String path = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[0]));
                        String name = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[1]));
                        long dateTime = data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[2]));
                        Image image = new Image(path, name, dateTime);
                        images.add(image);
                        if (!hasFolderGened) {
                            // 获取文件夹名称
                            File imageFile = new File(path);
                            File folderFile = imageFile.getParentFile();
                            Folder folder = new Folder();
                            folder.name = folderFile.getName();
                            folder.path = folderFile.getAbsolutePath();
                            folder.cover = image;
                            if (!mResultFolder.contains(folder)) {
                                List<Image> imageList = new ArrayList<>();
                                imageList.add(image);
                                folder.images = imageList;
                                mResultFolder.add(folder);
                            } else {
                                // 更新
                                Folder f = mResultFolder.get(mResultFolder.indexOf(folder));
                                f.images.add(image);
                            }
                        }

                    } while (data.moveToNext());

                    hasFolderGened = true;

                }
            }
        }
        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    };


}

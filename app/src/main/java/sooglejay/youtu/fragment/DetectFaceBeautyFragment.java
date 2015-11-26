package sooglejay.youtu.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

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
import sooglejay.youtu.ui.GalleryActivity;
import sooglejay.youtu.utils.ImageUtils;
import sooglejay.youtu.utils.ProgressDialogUtil;
import sooglejay.youtu.widgets.FaceImageView;
import sooglejay.youtu.widgets.RoundImageView;
import sooglejay.youtu.widgets.TitleBar;
import sooglejay.youtu.widgets.decoview.decoviewlib.DecoView;
import sooglejay.youtu.widgets.decoview.decoviewlib.charts.SeriesItem;
import sooglejay.youtu.widgets.decoview.decoviewlib.charts.SeriesLabel;
import sooglejay.youtu.widgets.decoview.decoviewlib.events.DecoEvent;
import sooglejay.youtu.widgets.imagepicker.MultiImageSelectorActivity;
import sooglejay.youtu.widgets.imagepicker.bean.Folder;
import sooglejay.youtu.widgets.youtu.sign.Base64Util;

/**
 * Created by JammyQtheLab on 2015/11/24.
 */
public class DetectFaceBeautyFragment extends DecoViewBaseFragment {
    // 不同loader定义
    private static final int LOADER_ALL = 0;
    private static final float seriesMax = 100f;

    private ArrayList<String> imageList = new ArrayList<>();
    private String resultPath;//图片最终位置

    // 结果数据
    private ArrayList<String> resultList = new ArrayList<>();
    // 文件夹数据
    private ArrayList<Folder> mResultFolder = new ArrayList<>();

    private boolean hasFolderGened = false;//是否已经加载了相册
    private Activity context;

    private RoundImageView iv_avatar;
    private int mTotalScores;
    private int mYourScores;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detect_face_beauty, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        context = this.getActivity();
        setUpViews(view, savedInstanceState);
    }

    private void setUpViews(View view, Bundle savedInstanceState) {
        context = this.getActivity();
        findViews(view);
    }

    private TitleBar titleBar;
    private TextView tvTest;
    private FaceImageView ivImage;
    private TextView tvResult;
    private DecoView dynamicArcView;

    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2015-11-24 19:37:49 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private void findViews(View view) {
        titleBar = (TitleBar) view.findViewById(R.id.title_bar);
        titleBar.initTitleBarInfo("颜值工作室", -1, -1, "", "");
        dynamicArcView = (DecoView) view.findViewById(R.id.dynamicArcView);
        tvTest = (TextView) view.findViewById(R.id.tv_test);
        iv_avatar = (RoundImageView) view.findViewById(R.id.iv_avatar);
        ivImage = (FaceImageView) view.findViewById(R.id.iv_image);
        tvResult = (TextView) view.findViewById(R.id.tv_result);
        tvTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageUtils.startPickPhoto(getActivity(), DetectFaceBeautyFragment.this, imageList, 10, false);
            }
        });
    }

    private void faceCompareUrl() {
        final ProgressDialogUtil progressDialogUtil = new ProgressDialogUtil(context);
        progressDialogUtil.show("正在发送请求...");
        FaceCompareUtil.faceCompareUrl(context, "", "", "http://open.youtu.qq.com/content/img/slide-1.jpg", "http://open.youtu.qq.com/content/img/slide-1.jpg", NetWorkConstant.APP_ID, new NetCallback<FaceCompareResponseBean>(context) {
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
     * @param bitmap
     * @return -1 null; -2 file is bad
     */
    private void detectface(final Bitmap bitmap) {
        final ProgressDialogUtil p = new ProgressDialogUtil(context);
        p.show("正在检测...");
        DetectFaceUtil.detectFace(context, NetWorkConstant.APP_ID, Base64Util.encode(ImageUtils.Bitmap2Bytes(bitmap)), 1, new NetCallback<DetectFaceResponseBean>(context) {
            @Override
            public void onFailure(RetrofitError error, String message) {
                p.hide();
                ivImage.setCanvasBitmapRes(bitmap);
            }

            @Override
            public void success(DetectFaceResponseBean detectFaceResponseBean, Response response) {
                p.hide();
                tvResult.setText(detectFaceResponseBean.toString());
                List<FaceItem> faceItem = detectFaceResponseBean.getFace();
                if(faceItem!=null&&faceItem.size()>0)
                {
                    ivImage.setCanvasRes(bitmap, faceItem);
                }else {
                    ivImage.setCanvasBitmapRes(bitmap);
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
                        ImageLoader.getInstance().displayImage("file://" + imageList.get(0), ivImage, ImageUtils.getOptions());
                        ImageLoader.getInstance().displayImage("file://" + imageList.get(0), iv_avatar, ImageUtils.getOptions());
                        resultPath = imageList.get(0);
                        Bitmap bitmap =ImageUtils.getBitmapFromLocalPath(resultPath, 1);
                        Bitmap resizedBitmap =ImageUtils.getResizedBitmap(bitmap, 600, 600);
                        detectface(resizedBitmap);
                    }
                }
                break;
            //裁剪图片
            case ImageUtils.REQUEST_CODE_CROP_IMAGE:
                if (resultCode == Activity.RESULT_OK) {
                    //添加图片到list并且显示出来
                    //上传图片
                    if (!TextUtils.isEmpty(resultPath)) {
//                        ImageLoader.getInstance().displayImage("file://" + resultPath, ivImage, ImageUtils.getOptions());
//                        String dstPath = ImageUtils.getImageFolderPath(getActivity()) + System.currentTimeMillis() + File.separator + "jpg";
//                        ImageUtils.compressAndSave(getActivity(), resultPath, dstPath, 600);
//                        detectface(dstPath);
                    }
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getView() == null) {
            return;
        }
        mInitialized = true;
        createAnimation();
    }

    @Override
    protected void createTracks() {

        final DecoView decoView = getDecoView();
        final View view = getView();
        if (decoView == null || view == null) {
            return;
        }
        decoView.deleteAll();
        decoView.configureAngles(280, 0);

        decoView.addSeries(new SeriesItem.Builder(Color.argb(255, 218, 218, 218))
                .setRange(0, seriesMax, seriesMax)
                .setInitialVisibility(false)
                .setLineWidth(getDimension(32f))
                .build());

        SeriesItem seriesItem1 = new SeriesItem.Builder(Color.argb(255, 64, 196, 0))
                .setRange(0, seriesMax, 0)
                .setInitialVisibility(false)
                .setLineWidth(getDimension(32f))
                .setSeriesLabel(new SeriesLabel.Builder("Percent %.0f%%")
                        .setColorBack(Color.argb(218, 0, 0, 0))
                        .setColorText(Color.argb(255, 255, 255, 255))
                        .build())
                .build();

        mYourScores = decoView.addSeries(seriesItem1);

        SeriesItem seriesItem2 = new SeriesItem.Builder(Color.argb(255, 64, 0, 196))
                .setRange(0, seriesMax, 0)
                .setInitialVisibility(false)
                .setLineWidth(getDimension(24f))
                .setSpinDuration(3000)
                .setSeriesLabel(new SeriesLabel.Builder("Value %.0f").build())
                .build();

        mTotalScores = decoView.addSeries(seriesItem2);


        mTotalScores = decoView.addSeries(seriesItem2);

        //颜值数值 显示
        final TextView textPercent = (TextView) view.findViewById(R.id.textPercentage);
        textPercent.setVisibility(View.INVISIBLE);
    }


    @Override
    protected void setupEvents() {
        final DecoView decoView = getDecoView();
        final View view = getView();
        if (decoView == null || decoView.isEmpty() || view == null) {
            throw new IllegalStateException("致命的错误！View cannot be null !");
        }
        decoView.executeReset();

        final TextView textPercent = (TextView) view.findViewById(R.id.textPercentage);
        final View[] linkedViews = {textPercent};

        decoView.addEvent(new DecoEvent.Builder(DecoEvent.EventType.EVENT_SHOW, true)
                .setDelay(500)
                .setDuration(2000)
                .setLinkedViews(linkedViews)
                .build());

        decoView.addEvent(new DecoEvent.Builder(25).setIndex(mYourScores).setDelay(3300).build());
        decoView.addEvent(new DecoEvent.Builder(50).setIndex(mYourScores).setDelay(8000).setDuration(1000).build());
        decoView.addEvent(new DecoEvent.Builder(0).setIndex(mYourScores).setDelay(13000).setDuration(6000).build());

        decoView.addEvent(new DecoEvent.Builder(5).setIndex(mTotalScores).setDelay(4250).setDuration(2000).build());
        decoView.addEvent(new DecoEvent.Builder(30).setIndex(mTotalScores).setDelay(9000).build());
        decoView.addEvent(new DecoEvent.Builder(0)
                .setIndex(mTotalScores)
                .setDelay(13000)
                .build());

        decoView.addEvent(new DecoEvent.Builder(DecoEvent.EventType.EVENT_HIDE, false)
                .setDelay(19500)
                .setDuration(2000)
                .setLinkedViews(linkedViews)
                .setListener(new DecoEvent.ExecuteEventListener() {
                    @Override
                    public void onEventStart(DecoEvent event) {

                    }

                    @Override
                    public void onEventEnd(DecoEvent event) {

                    }
                })
                .build());
    }
}

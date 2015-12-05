package sooglejay.youtu.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import retrofit.RetrofitError;
import retrofit.client.Response;
import sooglejay.youtu.R;
import sooglejay.youtu.api.detectface.DetectFaceResponseBean;
import sooglejay.youtu.api.detectface.DetectFaceUtil;
import sooglejay.youtu.api.detectface.FaceItem;
import sooglejay.youtu.constant.IntConstant;
import sooglejay.youtu.constant.NetWorkConstant;
import sooglejay.youtu.model.NetCallback;
import sooglejay.youtu.utils.ImageUtils;
import sooglejay.youtu.widgets.PagerSlidingTabStrip;
import sooglejay.youtu.widgets.RoundImageView;
import sooglejay.youtu.widgets.TitleBar;
import sooglejay.youtu.widgets.decoview.decoviewlib.DecoView;
import sooglejay.youtu.widgets.decoview.decoviewlib.charts.SeriesItem;
import sooglejay.youtu.widgets.decoview.decoviewlib.charts.SeriesLabel;
import sooglejay.youtu.widgets.decoview.decoviewlib.events.DecoEvent;
import sooglejay.youtu.widgets.imagepicker.MultiImageSelectorActivity;
import sooglejay.youtu.widgets.youtu.sign.Base64Util;

/**
 * Created by JammyQtheLab on 2015/11/24.
 */
public class DetectFaceBeautyFragment extends DecoViewBaseFragment {
    private static final float seriesMax = 100f;
    private int mTotalScores;
    private int mYourScores;


    private String a_imageStr;
    private String b_imageStr;

    private boolean a_isSelected = false;
    private boolean b_isSelected = false;


    private ArrayList<FaceItem> a_face = new ArrayList<>();
    private ArrayList<FaceItem> b_face = new ArrayList<>();


    private ArrayList<String> a_imageList = new ArrayList<>();
    private ArrayList<String> b_imageList = new ArrayList<>();


    private TitleBar titleBar;
    private DecoView dynamicArcView;
    private RoundImageView ivA;
    private TextView tvATextPercentage;
    private TextView tvAReady;
    private RoundImageView ivB;
    private TextView tvBTextPercentage;
    private TextView tvBReady;
    private FrameLayout layoutStartPk;
    private TextView textPercentage;


    private Activity activity;

    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2015-12-05 20:52:34 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private void findViews(View view) {
        titleBar = (TitleBar) view.findViewById(R.id.title_bar);
        dynamicArcView = (DecoView) view.findViewById(R.id.dynamicArcView);
        ivA = (RoundImageView) view.findViewById(R.id.iv_a);
        tvATextPercentage = (TextView) view.findViewById(R.id.tv_a_textPercentage);
        tvAReady = (TextView) view.findViewById(R.id.tv_a_ready);
        ivB = (RoundImageView) view.findViewById(R.id.iv_b);
        tvBTextPercentage = (TextView) view.findViewById(R.id.tv_b_textPercentage);
        tvBReady = (TextView) view.findViewById(R.id.tv_b_ready);
        layoutStartPk = (FrameLayout) view.findViewById(R.id.layout_start_pk);
        textPercentage = (TextView) view.findViewById(R.id.textPercentage);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detect_face_beauty, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        activity = getActivity();
        setUpViews(view, savedInstanceState);
        setUpListener();
    }

    private void setUpViews(View view, Bundle savedInstanceState) {
        findViews(view);

        tvATextPercentage.setVisibility(View.GONE);
        tvBTextPercentage.setVisibility(View.GONE);
        titleBar.initTitleBarInfo("颜值PK", -1, -1, "", "");
    }

    private void setUpListener() {
        tvAReady.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(a_imageStr)) {
                    Toast.makeText(activity, "先选择人脸图片", Toast.LENGTH_SHORT).show();
                    return;
                }
                tvAReady.setTextColor(ContextCompat.getColor(activity, R.color.light_gray_color));
                tvAReady.setEnabled(false);
            }
        });
        tvBReady.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(b_imageStr)) {
                    Toast.makeText(activity, "先选择人脸图片", Toast.LENGTH_SHORT).show();
                    return;
                }
                tvBReady.setTextColor(ContextCompat.getColor(activity, R.color.light_gray_color));
                tvBReady.setEnabled(false);

            }
        });
        ivA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                a_isSelected = true;
                b_isSelected = false;
                ImageUtils.startPickPhoto(getActivity(), DetectFaceBeautyFragment.this, a_imageList, 1, false);
            }
        });
        ivB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b_isSelected = true;
                a_isSelected = false;
                ImageUtils.startPickPhoto(getActivity(), DetectFaceBeautyFragment.this, b_imageList, 1, false);

            }
        });


        layoutStartPk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(a_imageStr)) {
                    Toast.makeText(activity, "请选择左边的人脸图片", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(b_imageStr)) {
                    Toast.makeText(activity, "请选择右边的人脸图片", Toast.LENGTH_SHORT).show();
                    return;
                }
                //先来分析A
                //1、先把图片转换成为bitmap
                new AsyncTask<String, String, String>() {
                    @Override
                    protected void onPostExecute(String string) {
                        super.onPostExecute(string);

                        //2、然后去检测人脸
                        DetectFaceUtil.detectFace(activity, NetWorkConstant.APP_ID, string, 0, new NetCallback<DetectFaceResponseBean>(activity) {
                            @Override
                            public void onFailure(RetrofitError error, String message) {

                            }

                            @Override
                            public void success(DetectFaceResponseBean detectFaceResponseBean, Response response) {
                                if (detectFaceResponseBean.getFace() != null) {
                                    a_face.addAll(detectFaceResponseBean.getFace());
                                    compareAWithB(a_face, b_face);

                                }
                            }
                        });

                    }

                    @Override
                    protected String doInBackground(String... params) {
                        Bitmap tempBitmap = ImageUtils.getBitmapFromLocalPath(params[0], 1);
                        Bitmap bitmap = ImageUtils.getResizedBitmap(tempBitmap, IntConstant.IMAGE_SIZE, IntConstant.IMAGE_SIZE);
                        byte[] bytes = ImageUtils.Bitmap2Bytes(bitmap);
                        return Base64Util.encode(bytes);

                    }
                }.execute(a_imageStr);


                //再来分析B
                //1、先把图片转换成为bitmap
                new AsyncTask<String, String, String>() {
                    @Override
                    protected void onPostExecute(String string) {
                        super.onPostExecute(string);

                        //2、然后去检测人脸
                        DetectFaceUtil.detectFace(activity, NetWorkConstant.APP_ID, string, 0, new NetCallback<DetectFaceResponseBean>(activity) {
                            @Override
                            public void onFailure(RetrofitError error, String message) {

                            }

                            @Override
                            public void success(DetectFaceResponseBean detectFaceResponseBean, Response response) {
                                if (detectFaceResponseBean.getFace() != null) {
                                    b_face.addAll(detectFaceResponseBean.getFace());
                                    compareAWithB(a_face, b_face);


                                }
                            }
                        });

                    }

                    @Override
                    protected String doInBackground(String... params) {
                        Bitmap tempBitmap = ImageUtils.getBitmapFromLocalPath(params[0], 1);
                        Bitmap bitmap = ImageUtils.getResizedBitmap(tempBitmap, IntConstant.IMAGE_SIZE, IntConstant.IMAGE_SIZE);
                        byte[] bytes = ImageUtils.Bitmap2Bytes(bitmap);
                        return Base64Util.encode(bytes);

                    }
                }.execute(b_imageStr);
            }
        });

        titleBar.setOnTitleBarClickListener(new TitleBar.OnTitleBarClickListener() {
            @Override
            public void onLeftButtonClick(View v) {

            }

            @Override
            public void onRightButtonClick(View v) {

            }
        });
    }

    private void compareAWithB(ArrayList<FaceItem> aFaces, ArrayList<FaceItem> bFaces) {
        if (aFaces.size() > 0 && bFaces.size() > 0) {
            FaceItem af = aFaces.get(0);
            FaceItem bf = bFaces.get(0);
            tvATextPercentage.setVisibility(View.VISIBLE);
            tvATextPercentage.setText(af.getBeauty() + "");

            tvBTextPercentage.setVisibility(View.VISIBLE);
            tvBTextPercentage.setText(bf.getBeauty() + "");
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            //若是从图库选择图
            case ImageUtils.REQUEST_CODE_PICK_IMAGE:
                if (resultCode == Activity.RESULT_OK) {
                    // 获取返回的图片列表
                    if (a_isSelected) {
                        // 获取返回的图片列表
                        List<String> paths = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                        if (paths.size() > 0) {
                            a_imageStr = ImageUtils.getImageFolderPath(activity) + File.separator + System.currentTimeMillis() + ".jpg";
                            ImageUtils.cropImage(this, Uri.fromFile(new File(paths.get(0))), a_imageStr, 1, 1);
                        }
                    } else {
                        // 获取返回的图片列表
                        List<String> paths = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                        if (paths.size() > 0) {
                            b_imageStr = ImageUtils.getImageFolderPath(activity) + File.separator + System.currentTimeMillis() + ".jpg";
                            ImageUtils.cropImage(this, Uri.fromFile(new File(paths.get(0))), b_imageStr, 1, 1);
                        }
                    }
                }

                break;
            //裁剪图片
            case ImageUtils.REQUEST_CODE_CROP_IMAGE:
                if (resultCode == Activity.RESULT_OK) {
                    //添加图片到list并且显示出来
                    //上传图片
                    if (a_isSelected) {
                        a_isSelected = false;
                        if (!TextUtils.isEmpty(a_imageStr)) {
                            ImageLoader.getInstance().displayImage("file://" + a_imageStr, ivA, ImageUtils.getOptions());
                        }
                    } else {
                        b_isSelected = false;
                        if (!TextUtils.isEmpty(b_imageStr)) {
                            ImageLoader.getInstance().displayImage("file://" + b_imageStr, ivB, ImageUtils.getOptions());

                        }
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

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
import android.widget.ImageView;
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
import sooglejay.youtu.utils.ProgressDialogUtil;
import sooglejay.youtu.widgets.CircleButton;
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


    private boolean a_determined = false;
    private boolean b_determined = false;


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
    private CircleButton layoutStartPk;
    private TextView textPercentage;
    private TextView tv_start;


    private Activity activity;

    private ProgressDialogUtil progressDialogUtil;


    private ImageView iv_crown_a;
    private ImageView iv_crown_b;

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
        layoutStartPk = (CircleButton) view.findViewById(R.id.layout_start_pk);
        textPercentage = (TextView) view.findViewById(R.id.textPercentage);
        tv_start = (TextView) view.findViewById(R.id.tv_start);
        iv_crown_a = (ImageView) view.findViewById(R.id.iv_crown_a);
        iv_crown_b = (ImageView) view.findViewById(R.id.iv_crown_b);
        iv_crown_b.setVisibility(View.GONE);
        iv_crown_a.setVisibility(View.GONE);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detect_face_beauty, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        activity = getActivity();
        progressDialogUtil = new ProgressDialogUtil(activity);
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
                ivA.setEnabled(false);
                a_determined = true;
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
                ivB.setEnabled(false);
                b_determined = true;


            }
        });
        ivA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                a_isSelected = true;
                ImageUtils.startPickPhoto(getActivity(), DetectFaceBeautyFragment.this, a_imageList, 1, false);
            }
        });
        ivB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b_isSelected = true;
                ImageUtils.startPickPhoto(getActivity(), DetectFaceBeautyFragment.this, b_imageList, 1, false);

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
        setLayoutStartPkOnClickListener();
    }

    private void compareAWithB(ArrayList<FaceItem> aFaces, ArrayList<FaceItem> bFaces) {
        if (aFaces.size() > 0 && bFaces.size() > 0) {
            FaceItem af = aFaces.get(0);
            FaceItem bf = bFaces.get(0);

            int ab = af.getBeauty();
            int bb = bf.getBeauty();


            tvATextPercentage.setVisibility(View.VISIBLE);
            tvATextPercentage.setText(ab + "");

            tvBTextPercentage.setVisibility(View.VISIBLE);
            tvBTextPercentage.setText(bb + "");


            if(ab>bb)
            {
                iv_crown_a.setVisibility(View.VISIBLE);
                iv_crown_b.setVisibility(View.GONE);
            }else if(ab<bb)
            {
                iv_crown_b.setVisibility(View.VISIBLE);
                iv_crown_a.setVisibility(View.GONE);
            }else {
                iv_crown_b.setVisibility(View.VISIBLE);
                iv_crown_a.setVisibility(View.VISIBLE);
            }
        }

    }


    private void reset(boolean isFailed) {
        a_face.clear();
        b_face.clear();


        if (isFailed) {
            iv_crown_b.setVisibility(View.GONE);
            iv_crown_a.setVisibility(View.GONE);

            tvATextPercentage.setText("");
            tvAReady.setVisibility(View.VISIBLE);
            tvAReady.setTextColor(ContextCompat.getColor(activity, R.color.white_color));
            tvAReady.setEnabled(true);
            ivA.setEnabled(true);

            tvBTextPercentage.setText("");
            tvBReady.setVisibility(View.VISIBLE);
            tvBReady.setTextColor(ContextCompat.getColor(activity, R.color.white_color));
            tvBReady.setEnabled(true);
            ivB.setEnabled(true);

            tv_start.setText("开始");

        } else {
            tv_start.setText("再来一局");
            layoutStartPk.setOnClickListener(null);
            layoutStartPk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    iv_crown_b.setVisibility(View.GONE);
                    iv_crown_a.setVisibility(View.GONE);
                    tv_start.setText("开始");
                    tvATextPercentage.setText("");
                    tvAReady.setVisibility(View.VISIBLE);
                    tvAReady.setTextColor(ContextCompat.getColor(activity, R.color.white_color));
                    tvAReady.setEnabled(true);
                    ivA.setEnabled(true);

                    tvBTextPercentage.setText("");
                    tvBReady.setVisibility(View.VISIBLE);
                    tvBReady.setTextColor(ContextCompat.getColor(activity, R.color.white_color));
                    tvBReady.setEnabled(true);
                    ivB.setEnabled(true);

                    layoutStartPk.setOnClickListener(null);
                    setLayoutStartPkOnClickListener();
                }
            });
        }

    }

    private void setLayoutStartPkOnClickListener() {
        layoutStartPk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!a_determined) {
                    Toast.makeText(activity, "请左边的玩家买定离手", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!b_determined) {
                    Toast.makeText(activity, "请右边的玩家买定离手", Toast.LENGTH_SHORT).show();
                    return;
                }
                a_determined = false;
                b_determined = false;

                if (TextUtils.isEmpty(a_imageStr)) {
                    Toast.makeText(activity, "请选择左边的人脸图片", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(b_imageStr)) {
                    Toast.makeText(activity, "请选择右边的人脸图片", Toast.LENGTH_SHORT).show();
                    return;
                }
                tvAReady.setVisibility(View.GONE);
                tvBReady.setVisibility(View.GONE);


                //先来分析A
                //1、先把图片转换成为bitmap
                new AsyncTask<String, String, String>() {
                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        progressDialogUtil.show("(1/2)正在计算左边的人脸颜值...");
                    }

                    @Override
                    protected void onPostExecute(String string) {
                        super.onPostExecute(string);

                        //2、然后去检测人脸
                        DetectFaceUtil.detectFace(activity, NetWorkConstant.APP_ID, string, 0, new NetCallback<DetectFaceResponseBean>(activity) {
                            @Override
                            public void onFailure(RetrofitError error, String message) {
                                Toast.makeText(activity, "没网怎么玩！请确保网络质量杠杠滴！", Toast.LENGTH_SHORT).show();
                                progressDialogUtil.hide();
                                reset(true);
                            }

                            @Override
                            public void success(DetectFaceResponseBean detectFaceResponseBean, Response response) {
                                if (detectFaceResponseBean.getFace() != null) {
                                    a_face.addAll(detectFaceResponseBean.getFace());
                                    if (a_face.size() > 0) {
                                        tvATextPercentage.setVisibility(View.VISIBLE);
                                        tvATextPercentage.setText(a_face.get(0).getBeauty() + "");


                                        //再来分析B
                                        //1、先把图片转换成为bitmap
                                        new AsyncTask<String, String, String>() {
                                            @Override
                                            protected void onPreExecute() {
                                                super.onPreExecute();
                                                progressDialogUtil.show("(2/2)正在计算右边的人脸颜值...");
                                            }

                                            @Override
                                            protected void onPostExecute(String string) {
                                                super.onPostExecute(string);

                                                //2、然后去检测人脸
                                                DetectFaceUtil.detectFace(activity, NetWorkConstant.APP_ID, string, 0, new NetCallback<DetectFaceResponseBean>(activity) {
                                                    @Override
                                                    public void onFailure(RetrofitError error, String message) {
                                                        Toast.makeText(activity, "没网怎么玩！请确保网络质量杠杠滴！", Toast.LENGTH_SHORT).show();
                                                        progressDialogUtil.hide();
                                                        reset(true);
                                                    }

                                                    @Override
                                                    public void success(DetectFaceResponseBean detectFaceResponseBean, Response response) {
                                                        progressDialogUtil.hide();

                                                        if (detectFaceResponseBean.getFace() != null) {
                                                            b_face.addAll(detectFaceResponseBean.getFace());
                                                            if (b_face.size() > 0) {
                                                                tvBTextPercentage.setVisibility(View.VISIBLE);
                                                                tvBTextPercentage.setText(b_face.get(0).getBeauty() + "");
                                                            }
                                                            compareAWithB(a_face, b_face);
                                                            reset(false);
                                                        } else {
                                                            progressDialogUtil.hide();
                                                            Toast.makeText(activity, "右边的人脸图片没有发现人脸！游戏终止！", Toast.LENGTH_SHORT).show();
                                                            reset(true);
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

                                    } else {
                                        progressDialogUtil.hide();
                                        Toast.makeText(activity, "左边的人脸图片没有检测到人脸！游戏终止！", Toast.LENGTH_SHORT).show();
                                        reset(true);
                                    }
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
                        a_isSelected = false;//完成
                        if (!TextUtils.isEmpty(a_imageStr)) {
                            ImageLoader.getInstance().displayImage("file://" + a_imageStr, ivA, ImageUtils.getOptions());
                        }
                    } else {
                        b_isSelected = false;//完成
                        if (!TextUtils.isEmpty(b_imageStr)) {
                            ImageLoader.getInstance().displayImage("file://" + b_imageStr, ivB, ImageUtils.getOptions());
                        }
                    }
                }else {
                    if (a_isSelected) {
                        a_isSelected = false;
                        a_imageStr = "";
                    }
                    if (b_isSelected) {
                        b_imageStr = "";
                        b_isSelected = false;
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

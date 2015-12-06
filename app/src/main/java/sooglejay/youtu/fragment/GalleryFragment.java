package sooglejay.youtu.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit.RetrofitError;
import retrofit.client.Response;
import sooglejay.youtu.R;
import sooglejay.youtu.api.detectface.DetectFaceResponseBean;
import sooglejay.youtu.api.detectface.DetectFaceUtil;
import sooglejay.youtu.api.detectface.FaceItem;
import sooglejay.youtu.api.faceidentify.FaceIdentifyResponseBean;
import sooglejay.youtu.api.faceidentify.FaceIdentifyUtil;
import sooglejay.youtu.api.faceidentify.IdentifyItem;
import sooglejay.youtu.bean.FocusBean;
import sooglejay.youtu.bean.LikeBean;
import sooglejay.youtu.constant.IntConstant;
import sooglejay.youtu.constant.NetWorkConstant;
import sooglejay.youtu.constant.PreferenceConstant;
import sooglejay.youtu.db.FocusDao;
import sooglejay.youtu.db.LikeDao;
import sooglejay.youtu.event.BusEvent;
import sooglejay.youtu.model.NetCallback;
import sooglejay.youtu.ui.GalleryActivity;
import sooglejay.youtu.utils.AsyncBitmapLoader;
import sooglejay.youtu.utils.CacheUtil;
import sooglejay.youtu.utils.ImageUtils;
import sooglejay.youtu.utils.PreferenceUtil;
import sooglejay.youtu.utils.ProgressDialogUtil;
import sooglejay.youtu.utils.ShareUtils;
import sooglejay.youtu.widgets.CircleButton;
import sooglejay.youtu.widgets.FaceImageView;
import sooglejay.youtu.widgets.youtu.sign.Base64Util;

public class GalleryFragment extends BaseFragment {

    private FaceImageView imageView;
    private CircleButton iv_delete_image;
    private CircleButton iv_like_image;
    private CircleButton iv_focus_image;
    private CircleButton iv_share_image;
    private TextView tv_like;
    private TextView tv_focus;
    private FrameLayout progressContainer;
    private int position;

    private String url;

    private OnRectfChangeListener onRectfChangeListener;
    private CacheUtil cacheUtil;
    private AsyncBitmapLoader asyncBitmapLoader;

    private GalleryActivity activity;

    private FrameLayout layout_operation;

    private DialogFragmentCreater dialogFragmentCreater;
    private AsyncBitmapLoader.BitmapCallback callback;


    private LikeDao likeDao;
    private List<LikeBean> likeImageList = new ArrayList<>();
    private boolean isLiked = false;


    private FocusDao focusDao;
    private List<FocusBean> focusImageList = new ArrayList<>();
    private boolean isFocused = false;


    private boolean isDetectFace;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_gallery, container, false);
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isDetectFace) {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected void onPreExecute() {
                    super.onPreExecute();

                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                    Log.e("test","laile");
                    getImage(url);
                }

                @Override
                protected Void doInBackground(Void... voids) {
                    while (asyncBitmapLoader == null || getActivity() == null) {

                    }
                    return null;
                }
            }.execute();
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        likeDao = new LikeDao(getActivity());
        focusDao = new FocusDao(getActivity());
        likeImageList = likeDao.getAll();
        focusImageList = focusDao.getAll();

        onRectfChangeListener = (OnRectfChangeListener) this.getActivity();
//        position = getArguments().getInt("position", 0);
//        url = getArguments().getString("url", "");
        imageView = (FaceImageView) view.findViewById(R.id.iv_photo);
        imageView.setActivity(getActivity());
        imageView.setImageFilePath(url);
        layout_operation = (FrameLayout) view.findViewById(R.id.layout_operation);

        imageView.setBottomLayoutOperation(layout_operation);
        iv_delete_image = (CircleButton) view.findViewById(R.id.iv_delete_image);
        iv_like_image = (CircleButton) view.findViewById(R.id.iv_like_image);
        iv_focus_image = (CircleButton) view.findViewById(R.id.iv_focus_image);
        iv_share_image = (CircleButton) view.findViewById(R.id.iv_share_image);
        tv_like = (TextView) view.findViewById(R.id.tv_like);
        tv_focus = (TextView) view.findViewById(R.id.tv_focus);

        dialogFragmentCreater = new DialogFragmentCreater();
        dialogFragmentCreater.initDialogFragment(getActivity(), getActivity().getSupportFragmentManager());
        imageView.setDialogFragmentCreater(dialogFragmentCreater);
        imageView.setmCallback(mCallback);
        progressContainer = (FrameLayout) view.findViewById(R.id.progress_container);
        activity = (GalleryActivity) getActivity();
        cacheUtil = activity.cacheUtil;
        asyncBitmapLoader = activity.asyncBitmapLoader;


        iv_delete_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle("提示").setMessage("真的要删除这个图片么?")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (mCallback != null) {
                                    mCallback.onDeleteImagefile(url, position);
                                }
                            }
                        }).setNegativeButton("取消", null).create().show();
            }
        });


        if (likeImageList != null) {
            for (LikeBean likeBean : likeImageList) {
                if (likeBean.getImagePath().equals(url)) {
                    isLiked = true;
                    break;
                }
            }
        }
        iv_like_image.setImageResource(isLiked ? R.drawable.icon_liked : R.drawable.icon_like);
        tv_like.setText(isLiked ? "已喜欢" : "喜欢");
        iv_like_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isLiked)//如果之前是喜欢，再点击就变成不喜欢了
                {
                    likeDao.deleteByName(url);
                    Toast.makeText(activity, "取消喜欢", Toast.LENGTH_SHORT).show();
                } else {
                    LikeBean bean = new LikeBean();
                    bean.setImagePath(url);
                    likeDao.add(bean);
                    Toast.makeText(activity, "已喜欢", Toast.LENGTH_SHORT).show();
                }
                isLiked = !isLiked;
                iv_like_image.setImageResource(isLiked ? R.drawable.icon_liked : R.drawable.icon_like);
                tv_like.setText(isLiked ? "已喜欢" : "喜欢");
            }
        });


        if (focusImageList != null) {
            for (FocusBean focusBean : focusImageList) {
                if (focusBean.getImagePath().equals(url)) {
                    isFocused = true;
                    break;
                }
            }
        }
        iv_focus_image.setImageResource(isFocused ? R.drawable.icon_focused : R.drawable.icon_focus);
        tv_focus.setText(isFocused ? "已关注" : "关注");
        iv_focus_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isFocused)//如果之前是喜欢，再点击就变成不喜欢了
                {
                    focusDao.deleteByName(url);
                    Toast.makeText(activity, "取消关注", Toast.LENGTH_SHORT).show();
                } else {
                    FocusBean bean = new FocusBean();
                    bean.setImagePath(url);
                    focusDao.add(bean);
                    Toast.makeText(activity, "已关注", Toast.LENGTH_SHORT).show();
                }
                isFocused = !isFocused;
                iv_focus_image.setImageResource(isFocused ? R.drawable.icon_focused : R.drawable.icon_focus);
                tv_focus.setText(isFocused ? "已关注" : "关注");
            }
        });


        iv_share_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShareUtils shareUtils = new ShareUtils(activity,url);
                shareUtils.addCustomPlatforms(activity);
            }
        });


    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.e("jwjw", "yea");
    }


    private void closeFullScreen() {
        getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
    }

    /**
     * @param bitmap
     * @return -1 null; -2 file is bad
     */
    private void detectface(final Bitmap bitmap) {

        DetectFaceUtil.detectFace(getActivity(), NetWorkConstant.APP_ID, Base64Util.encode(ImageUtils.Bitmap2Bytes(bitmap)), 1, new NetCallback<DetectFaceResponseBean>(getActivity()) {
            @Override
            public void onFailure(RetrofitError error, String message) {
                progressContainer.setVisibility(View.GONE);
                imageView.setCanvasBitmapRes(bitmap);
                Toast.makeText(activity, "请求超时,请确保网络良好再重试", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void success(DetectFaceResponseBean detectFaceResponseBean, Response response) {
                progressContainer.setVisibility(View.GONE);
                ArrayList<FaceItem> faceItem = detectFaceResponseBean.getFace();
                if (faceItem != null && faceItem.size() > 0) {
                    imageView.setCanvasRes(bitmap, faceItem, "");
                } else {
                    imageView.setCanvasBitmapRes(bitmap);
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        if (imageView != null) {
            imageView.clearCanvasRes();
        }
        System.gc();
    }

    public void init(String s, int i, boolean isDetectFace) {
        url = s;
        position = i;
        this.isDetectFace = isDetectFace;
    }

    public interface OnRectfChangeListener {
        void onRectfChanged(RectF rectF);
    }

    /**
     * 带缓存的图片显示器
     *
     * @param imagePath
     */
    private void getImage(final String imagePath) {

        final boolean isAllowedIdentify = PreferenceUtil.load(activity, PreferenceConstant.SWITCH_IDENTIFY, true);

        final AsyncBitmapLoader.BitmapCallback callback = new AsyncBitmapLoader.BitmapCallback() {
            @Override
            public void facesLoaded(ArrayList<FaceItem> faces) {
                imageView.setCanvasFaceListRes(faces);
                imageView.setImageFilePath(imagePath);
                asyncBitmapLoader.addNewDetectedFaceToCache(imagePath, faces);
            }

            @Override
            public void bitmapLoaded(final Bitmap bitmap) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (bitmap != null) {
                            imageView.setCanvasBitmapRes(bitmap);
                        } else {
                            imageView.setImageResource(R.drawable.default_error);
                        }
                    }
                });
            }

            @Override
            public void faceidentify(Bitmap bitmap) {
                //如果已经做过人脸识别，并且成功了的，就不再做了
                if (asyncBitmapLoader.getmIdentifiedFaceBitMapCache() != null && asyncBitmapLoader.getmIdentifiedFaceBitMapCache().containsKey(imagePath)) {
                    Log.e("jwjw", 456 + "  containsKey");
                    ArrayList<IdentifyItem> identifyItems = asyncBitmapLoader.getmIdentifiedFaceBitMapCache().get(imagePath);
                    if (identifyItems != null) {
                        imageView.setIdentifyItems(identifyItems);
                    }
                } else if (isAllowedIdentify) {
                    final ProgressDialogUtil progressDialogUtil = new ProgressDialogUtil(activity);
                    progressDialogUtil.show("正在进行人脸识别...");
                    String group_id = PreferenceUtil.load(activity, PreferenceConstant.IDENTIFY_GROUP_NAME, "1");
                    FaceIdentifyUtil.faceIdentify(activity, NetWorkConstant.APP_ID, group_id, Base64Util.encode(ImageUtils.Bitmap2Bytes(bitmap)), new NetCallback<FaceIdentifyResponseBean>(activity) {
                        @Override
                        public void onFailure(RetrofitError error, String message) {
                            progressDialogUtil.hide();
                            Toast.makeText(activity, "请求超时,请确保网络良好再重试", Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void success(FaceIdentifyResponseBean faceIdentifyResponseBean, Response response) {
                            progressDialogUtil.hide();
                            if (faceIdentifyResponseBean.getCandidates() != null) {
                                imageView.setIdentifyItems(faceIdentifyResponseBean.getCandidates());
                                //新识别的人脸添加进缓存
                                asyncBitmapLoader.addNewIdentifiedFaceToCache(imagePath, faceIdentifyResponseBean.getCandidates());
                            }
                        }
                    });
                }
            }
        };

        //人脸检测缓存，如果已经检测到人脸
        final ArrayList<FaceItem> faces = asyncBitmapLoader.loadAsyncBean(activity, imagePath, callback);
        if (faces != null && faces.size() > 0) {
            new AsyncTask<String, Bitmap, Bitmap>() {
                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                }

                @Override
                protected Bitmap doInBackground(String... params) {
                    Bitmap tempBitmap = ImageUtils.getBitmapFromLocalPath(params[0], 1);
                    Bitmap bitmap = ImageUtils.getResizedBitmap(tempBitmap, IntConstant.IMAGE_SIZE, IntConstant.IMAGE_SIZE);
                    return bitmap;
                }

                @Override
                protected void onPostExecute(final Bitmap bitmap) {
                    if (bitmap != null) {
                        imageView.setCanvasBitmapRes(bitmap);
                        imageView.setCanvasFaceListRes(faces);
                        imageView.setImageFilePath(imagePath);

                        //如果已经做过人脸识别，并且成功了的，就不再做了
                        if (asyncBitmapLoader.getmIdentifiedFaceBitMapCache() != null && asyncBitmapLoader.getmIdentifiedFaceBitMapCache().containsKey(imagePath)) {
                            Log.e("jwjw", 456 + "  containsKey");
                            ArrayList<IdentifyItem> identifyItems = asyncBitmapLoader.getmIdentifiedFaceBitMapCache().get(imagePath);
                            if (identifyItems != null) {
                                imageView.setIdentifyItems(identifyItems);
                            }
                        } else if (isAllowedIdentify) {
                            final ProgressDialogUtil progressDialogUtil = new ProgressDialogUtil(activity);
                            progressDialogUtil.show("正在进行人脸识别...");
                            String group_id = PreferenceUtil.load(activity, PreferenceConstant.IDENTIFY_GROUP_NAME, "1");
                            FaceIdentifyUtil.faceIdentify(activity, NetWorkConstant.APP_ID, group_id, Base64Util.encode(ImageUtils.Bitmap2Bytes(bitmap)), new NetCallback<FaceIdentifyResponseBean>(activity) {
                                @Override
                                public void onFailure(RetrofitError error, String message) {
                                    progressDialogUtil.hide();
                                    Toast.makeText(activity, "请求超时,请确保网络良好再重试", Toast.LENGTH_SHORT).show();

                                }

                                @Override
                                public void success(FaceIdentifyResponseBean faceIdentifyResponseBean, Response response) {
                                    progressDialogUtil.hide();
                                    if (faceIdentifyResponseBean.getCandidates() != null) {
                                        imageView.setIdentifyItems(faceIdentifyResponseBean.getCandidates());
                                        //新识别的人脸添加进缓存
                                        asyncBitmapLoader.addNewIdentifiedFaceToCache(imagePath, faceIdentifyResponseBean.getCandidates());
                                    }
                                }
                            });
                        }

                    } else {
                        imageView.setImageResource(R.drawable.default_error);
                    }
                }
            }.execute(imagePath);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (Callback) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException("The Activity must implement MultiImageSelectorFragment.Callback interface...");
        }

    }

    @Override
    public void onPause() {
        super.onPause();

    }

    private  int threadCountA = 0;
    private  int threadCountB = 0;

    /**
     * EventBus 广播
     *
     * @param event
     */
    public void onEventMainThread(BusEvent event) {
        switch (event.getMsg()) {
            case BusEvent.MSG_EDIT_FACE_INFO:
                Log.e("jwjw", "jiangwei");
                threadCountA++;
                if (threadCountA == 1) {
                    if (cacheUtil != null && imageView != null) {
                        ArrayList<IdentifyItem> identifyItems = cacheUtil.getIdentifiedObjectFromFile().get(url);
                        if (identifyItems != null) {
                            Log.e("jwjw", "2");
                            imageView.setIdentifyItems(identifyItems);
                        }
                    }
                }
                break;
            case BusEvent.MSG_REFRESH:
                threadCountB++;
                if (threadCountB == 1) {
                    Log.e("test", "广播1212：进行人脸识别" + threadCountB);
                    refreshViewInThread();
                }
                break;
            case BusEvent.MSG_IS_DETECT_FACE:
                isDetectFace = PreferenceUtil.load(activity, PreferenceConstant.SWITCH_DETECT_FACE, true);
                if (!isDetectFace) {

                }
                break;
            default:
                break;
        }
    }

    public void refreshViewInThread() {
        imageView.setActivity(getActivity());
        imageView.setImageFilePath(url);

        if (dialogFragmentCreater == null) {
            Log.e("test", "DialogFragmentCreater  is  nulll");
            dialogFragmentCreater = new DialogFragmentCreater();
            dialogFragmentCreater.initDialogFragment(activity, activity.getSupportFragmentManager());
            imageView.setDialogFragmentCreater(dialogFragmentCreater);
        }

        //先读取文件结束才设置
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                asyncBitmapLoader.setmDetectedFaceBitMapCache(cacheUtil.getDetectedObjectFromFile());
                asyncBitmapLoader.setmIdentifiedFaceBitMapCache(cacheUtil.getIdentifiedObjectFromFile());
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                refreshImageView();
            }
        }.execute();
    }

    public void refreshImageView() {
        try {
            getImage(url);
        } catch (NullPointerException npe) {
            Log.e("test", "activity 已经销毁");
        }
    }


    private Callback mCallback;

    /**
     * 回调接口
     */
    public interface Callback {
        void onDeleteImagefile(String path, int position);

        void onTouchImageView();

    }


}

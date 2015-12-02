package sooglejay.youtu.fragment;

import android.graphics.Bitmap;
import android.graphics.RectF;
import android.media.FaceDetector;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.ArrayList;

import retrofit.RetrofitError;
import retrofit.client.Response;
import sooglejay.youtu.R;
import sooglejay.youtu.api.detectface.DetectFaceResponseBean;
import sooglejay.youtu.api.detectface.DetectFaceUtil;
import sooglejay.youtu.api.detectface.FaceItem;
import sooglejay.youtu.api.faceidentify.FaceIdentifyResponseBean;
import sooglejay.youtu.api.faceidentify.FaceIdentifyUtil;
import sooglejay.youtu.api.faceidentify.IdentifyItem;
import sooglejay.youtu.constant.IntConstant;
import sooglejay.youtu.constant.NetWorkConstant;
import sooglejay.youtu.constant.PreferenceConstant;
import sooglejay.youtu.model.NetCallback;
import sooglejay.youtu.ui.GalleryActivity;
import sooglejay.youtu.utils.AsyncBitmapLoader;
import sooglejay.youtu.utils.CacheUtil;
import sooglejay.youtu.utils.ImageUtils;
import sooglejay.youtu.utils.PreferenceUtil;
import sooglejay.youtu.utils.ProgressDialogUtil;
import sooglejay.youtu.widgets.FaceImageView;
import sooglejay.youtu.widgets.youtu.sign.Base64Util;

public class GalleryFragment extends BaseFragment {

    FaceDetector.Face face1;
    private FaceImageView imageView;
    private FrameLayout progressContainer;
    private int position;

    private String url;

    private OnRectfChangeListener onRectfChangeListener;
    private CacheUtil cacheUtil;
    private AsyncBitmapLoader asyncBitmapLoader;

    private GalleryActivity activity;

    private DialogFragmentCreater dialogFragmentCreater;
    private AsyncBitmapLoader.BitmapCallback callback;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_gallery, container, false);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        onRectfChangeListener = (OnRectfChangeListener) this.getActivity();
        position = getArguments().getInt("position", 0);
        url = getArguments().getString("url", "");
        imageView = (FaceImageView) view.findViewById(R.id.iv_photo);
        dialogFragmentCreater = new DialogFragmentCreater();
        dialogFragmentCreater.initDialogFragment(getActivity(), getActivity().getSupportFragmentManager());
        imageView.setDialogFragmentCreater(dialogFragmentCreater);
        progressContainer = (FrameLayout) view.findViewById(R.id.progress_container);
        activity = (GalleryActivity) getActivity();
        cacheUtil = activity.cacheUtil;
        asyncBitmapLoader = activity.asyncBitmapLoader;

        getImage(url.substring(7, url.length()));

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

    public interface OnRectfChangeListener {
        void onRectfChanged(RectF rectF);
    }

    /**
     * 带缓存的图片显示器
     *
     * @param imagePath
     */
    private void getImage(final String imagePath) {
        final AsyncBitmapLoader.BitmapCallback callback = new AsyncBitmapLoader.BitmapCallback() {
            @Override
            public void facesLoaded(ArrayList<FaceItem> faces) {
                if (faces != null && faces.size() > 0) {
                    imageView.setCanvasFaceListRes(faces);
                    imageView.setImageFilePath(imagePath);
                    asyncBitmapLoader.addNewDetectedFaceToCache(imagePath, faces);
                }
            }

            @Override
            public void bitmapLoaded(final Bitmap bitmap) {
                getActivity().runOnUiThread(new Runnable() {
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
                if (asyncBitmapLoader.getmIdentifiedFaceBitMapCache() !=null&& asyncBitmapLoader.getmIdentifiedFaceBitMapCache().containsKey(imagePath)) {
                    Log.e("jwjw", 456 + "  containsKey");
                    ArrayList<IdentifyItem> identifyItems = asyncBitmapLoader.getmIdentifiedFaceBitMapCache().get(imagePath);
                    if (identifyItems != null) {
                        imageView.setIdentifyItems(identifyItems);
                    }
                }else {
                    final ProgressDialogUtil progressDialogUtil = new ProgressDialogUtil(activity);
                    progressDialogUtil.show("正在进行人脸识别...");
                    String group_id = PreferenceUtil.load(activity, PreferenceConstant.IDENTIFY_GROUP_NAME,"1");
                    FaceIdentifyUtil.faceIdentify(activity, NetWorkConstant.APP_ID, group_id, Base64Util.encode(ImageUtils.Bitmap2Bytes(bitmap)), new NetCallback<FaceIdentifyResponseBean>(activity) {
                        @Override
                        public void onFailure(RetrofitError error, String message) {
                            progressDialogUtil.hide();
                        }

                        @Override
                        public void success(FaceIdentifyResponseBean faceIdentifyResponseBean, Response response) {
                            progressDialogUtil.hide();
                            if (faceIdentifyResponseBean.getCandidates() != null) {
                                imageView.setIdentifyItems(faceIdentifyResponseBean.getCandidates());
                                //新识别的人脸添加进缓存
                                asyncBitmapLoader.addNewIdentifiedFaceToCache(imagePath,faceIdentifyResponseBean.getCandidates());
                            }
                        }
                    });
                }
            }
        };
        final ArrayList<FaceItem> faces = asyncBitmapLoader.loadAsyncBean(getActivity(), imagePath, callback);
        if (faces != null && faces.size() > 0) {
            new AsyncTask<String, Bitmap, Bitmap>() {
                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                }

                @Override
                protected Bitmap doInBackground(String... params) {
                    Bitmap tempBitmap = ImageUtils.getBitmapFromLocalPath(params[0], 1);
                    Bitmap bitmap = ImageUtils.getResizedBitmap(tempBitmap, IntConstant.IMAGE_SIZE,  IntConstant.IMAGE_SIZE);
                    return bitmap;
                }

                @Override
                protected void onPostExecute(final Bitmap bitmap) {
                    asyncBitmapLoader.addNewDetectedFaceToCache(imagePath, faces);
                    if (bitmap != null) {
                        imageView.setCanvasBitmapRes(bitmap);
                        imageView.setCanvasFaceListRes(faces);
                        imageView.setImageFilePath(imagePath);

                        //如果已经做过人脸识别，并且成功了的，就不再做了
                        if (asyncBitmapLoader.getmIdentifiedFaceBitMapCache() !=null&& asyncBitmapLoader.getmIdentifiedFaceBitMapCache().containsKey(imagePath)) {
                            Log.e("jwjw", 456 + "  containsKey");
                            ArrayList<IdentifyItem> identifyItems = asyncBitmapLoader.getmIdentifiedFaceBitMapCache().get(imagePath);
                            if (identifyItems != null) {
                                imageView.setIdentifyItems(identifyItems);
                            }
                        }else {
                            final ProgressDialogUtil progressDialogUtil = new ProgressDialogUtil(activity);
                            progressDialogUtil.show("正在进行人脸识别...");
                            String group_id = PreferenceUtil.load(activity, PreferenceConstant.IDENTIFY_GROUP_NAME,"1");
                            FaceIdentifyUtil.faceIdentify(activity, NetWorkConstant.APP_ID, group_id, Base64Util.encode(ImageUtils.Bitmap2Bytes(bitmap)), new NetCallback<FaceIdentifyResponseBean>(activity) {
                                @Override
                                public void onFailure(RetrofitError error, String message) {
                                    progressDialogUtil.hide();
                                }
                                @Override
                                public void success(FaceIdentifyResponseBean faceIdentifyResponseBean, Response response) {
                                    progressDialogUtil.hide();
                                    if (faceIdentifyResponseBean.getCandidates() != null) {
                                        imageView.setIdentifyItems(faceIdentifyResponseBean.getCandidates());
                                        //新识别的人脸添加进缓存
                                        asyncBitmapLoader.addNewIdentifiedFaceToCache(imagePath,faceIdentifyResponseBean.getCandidates());
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
    public void onPause() {
        super.onPause();
        if(dialogFragmentCreater!=null)
        {
            dialogFragmentCreater = null;
        }
    }
}

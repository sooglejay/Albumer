package sooglejay.youtu.fragment;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.RectF;
import android.media.FaceDetector;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;

import retrofit.RetrofitError;
import retrofit.client.Response;
import sooglejay.youtu.R;
import sooglejay.youtu.api.detectface.DetectFaceResponseBean;
import sooglejay.youtu.api.detectface.DetectFaceUtil;
import sooglejay.youtu.api.detectface.FaceItem;
import sooglejay.youtu.constant.NetWorkConstant;
import sooglejay.youtu.model.NetCallback;
import sooglejay.youtu.utils.ImageUtils;
import sooglejay.youtu.widgets.FaceImageView;
import sooglejay.youtu.widgets.youtu.sign.Base64Util;
import uk.co.senab.photoview.PhotoViewAttacher.OnMatrixChangedListener;
import uk.co.senab.photoview.PhotoViewAttacher.OnPhotoTapListener;

public class GalleryFragment extends BaseFragment {

    FaceDetector.Face face1;
    private FaceImageView imageView;
    private FrameLayout progressContainer;
    private int position;
    private String url;

    private OnRectfChangeListener onRectfChangeListener;


    private Activity activity;

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
        activity = this.getActivity();
        imageView = (FaceImageView) view.findViewById(R.id.iv_photo);
        progressContainer = (FrameLayout) view.findViewById(R.id.progress_container);
        setPhotoView();

    }
    private void setPhotoView() {
        imageView.setOnMatrixChangeListener(new OnMatrixChangedListener() {

            @Override
            public void onMatrixChanged(RectF rectF) {
                // TODO Auto-generated method stub
                onRectfChangeListener.onRectfChanged(rectF);
            }
        });
        ImageLoader.getInstance().loadImage(url, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                Log.e("Retrofit", "onLoadingStarted  " + imageUri);
                progressContainer.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                progressContainer.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                Log.e("Retrofit", "_onLoadingComplete_" + imageUri);
                try {
                    Bitmap resizedBitmap = ImageUtils.getResizedBitmap(loadedImage, 800, 800);
                    detectface(resizedBitmap);
                } catch (OutOfMemoryError oom) {
                    Toast.makeText(getActivity(), " >_< ! 内存不足 ", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                Log.e("Retrofit", "onLoadingCancelled" + imageUri);
                progressContainer.setVisibility(View.GONE);
            }
        });
//        ImageLoader.getInstance().displayImage(url, imageView, ImageUtils.getOptions(), new ImageLoadingListener() {
//            @Override
//            public void onLoadingStarted(String s, View view) {
//                progressContainer.setVisibility(View.VISIBLE);
//            }
//
//            @Override
//            public void onLoadingFailed(String s, View view, FailReason failReason) {
//                progressContainer.setVisibility(View.GONE);
//
//            }
//
//            @Override
//            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
//                if (bitmap.getWidth() >= 1020 || bitmap.getHeight() >= 1920) {
//                    imageView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
//                }
//                progressContainer.setVisibility(View.GONE);
//                Bitmap resizedBitmap =ImageUtils.getResizedBitmap(bitmap, 800, 800);
//                detectface(resizedBitmap);
//            }
//
//            @Override
//            public void onLoadingCancelled(String s, View view) {
//            }
//        });

        imageView.setOnPhotoTapListener(new OnPhotoTapListener() {

            @Override
            public void onPhotoTap(View arg0, float arg1, float arg2) {
                // TODO Auto-generated method stub

            }
        });
        imageView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
            }
        });
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
                List<FaceItem> faceItem = detectFaceResponseBean.getFace();
                if (faceItem != null && faceItem.size() > 0) {
                    imageView.setCanvasRes(bitmap, faceItem);
                } else {
                    imageView.setCanvasBitmapRes(bitmap);
                }
            }
        });
    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        ImageLoader.getInstance().clearMemoryCache();
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        System.gc();
    }
    public interface OnRectfChangeListener {
        void onRectfChanged(RectF rectF);
    }
}
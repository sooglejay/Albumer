package sooglejay.youtu.fragment;

import android.graphics.Bitmap;
import android.graphics.RectF;
import android.media.FaceDetector;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.lang.ref.SoftReference;
import java.util.List;

import retrofit.RetrofitError;
import retrofit.client.Response;
import sooglejay.youtu.R;
import sooglejay.youtu.api.detectface.AsyncBean;
import sooglejay.youtu.api.detectface.DetectFaceResponseBean;
import sooglejay.youtu.api.detectface.DetectFaceUtil;
import sooglejay.youtu.api.detectface.FaceItem;
import sooglejay.youtu.constant.NetWorkConstant;
import sooglejay.youtu.model.NetCallback;
import sooglejay.youtu.utils.AsyncBitmapLoader;
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
        progressContainer = (FrameLayout) view.findViewById(R.id.progress_container);
//        setPhotoView();
        getImage(url.substring(7, url.length()));
    }

    private void setPhotoView() {
        imageView.setOnMatrixChangeListener(new OnMatrixChangedListener() {

            @Override
            public void onMatrixChanged(RectF rectF) {
                // TODO Auto-generated method stub
                onRectfChangeListener.onRectfChanged(rectF);
            }
        });
//        ImageLoader.getInstance().displayImage(url, imageView, ImageUtils.getOptions(), new ImageLoadingListener() {
//            @Override
//            public void onLoadingStarted(String imageUri, View view) {
//                Log.e("Retrofit", "onLoadingStarted url:: " + url);
//                progressContainer.setVisibility(View.VISIBLE);
//            }
//
//            @Override
//            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
//                progressContainer.setVisibility(View.GONE);
//            }
//
//            @Override
//            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//                Log.e("Retrofit", "_onLoadingComplete_" + imageUri);
//                try {
//                    Bitmap resizedBitmap = ImageUtils.getResizedBitmap(loadedImage, 800, 800);
//                    imageView.setCanvasBitmapRes(resizedBitmap);
//                    detectface(resizedBitmap);
//                } catch (OutOfMemoryError oom) {
//                    Toast.makeText(getActivity(), " >_< ! 内存不足 ", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onLoadingCancelled(String imageUri, View view) {
//                Log.e("Retrofit", "onLoadingCancelled" + imageUri);
//                progressContainer.setVisibility(View.GONE);
//            }
//        });
//

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
                    imageView.clearCanvasRes();
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


    private AsyncBitmapLoader asyncBitmapLoader = new AsyncBitmapLoader();

    private void getImage(final String imagePath) {
        final AsyncBitmapLoader.BitmapCallback callback = new AsyncBitmapLoader.BitmapCallback() {
            @Override
            public void facesLoaded(List<FaceItem> faces) {
                if(faces!=null&&faces.size()>0)
                {
                    imageView.setCanvasFaceListRes(faces);
                    asyncBitmapLoader.addKey(imagePath,faces);
                }
            }

            @Override
            public void bitmapLoaded(final Bitmap bitmap) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(bitmap!=null) {
                                imageView.setCanvasBitmapRes(bitmap);
                            }else {
                                imageView.setImageResource(R.drawable.default_error);
                            }
                        }
                    });
                }
        };
        final List<FaceItem>faces = asyncBitmapLoader.loadAsyncBean(getActivity(),imagePath,callback);
        if(faces!=null&&faces.size()>0)
        {
            new AsyncTask<String, Bitmap, Bitmap>() {
                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                }

                @Override
                protected Bitmap doInBackground(String... params) {
                    Bitmap tempBitmap = ImageUtils.getBitmapFromLocalPath(params[0], 1);
                    Bitmap bitmap =  ImageUtils.getResizedBitmap(tempBitmap, 600, 600);
                    return bitmap;
                }

                @Override
                protected void onPostExecute(final Bitmap bitmap) {
                    asyncBitmapLoader.addKey(imagePath,faces);
                    if (bitmap != null) {
                        imageView.setCanvasBitmapRes(bitmap);
                        imageView.setCanvasFaceListRes(faces);
                    }else {
                        imageView.setImageResource(R.drawable.default_error);
                    }
                }
            }.execute(imagePath);
        }
    }
}
//123
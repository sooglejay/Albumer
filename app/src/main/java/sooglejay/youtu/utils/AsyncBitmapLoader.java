/**
 * AsyncImageLoader.java
 *
 * @version 0.9
 * @author nicky.han
 */
package sooglejay.youtu.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.List;

import retrofit.RetrofitError;
import retrofit.client.Response;
import sooglejay.youtu.api.detectface.DetectFaceResponseBean;
import sooglejay.youtu.api.detectface.DetectFaceUtil;
import sooglejay.youtu.api.detectface.FaceItem;
import sooglejay.youtu.constant.NetWorkConstant;
import sooglejay.youtu.model.NetCallback;
import sooglejay.youtu.widgets.youtu.sign.Base64Util;

/**
 * Thread sued to load image
 */
public class AsyncBitmapLoader {

    private static final int SUCCESS_DETECT_FACE = 0;
    private static final int ERROR_DETECT_FACE = 1;//网络请求失败
    private static final int ERROR_RESIZE_IMAGE = 2;//bad  picture

    /**
     * Used to save image by soft reference. It make image easy to release.
     */
    private HashMap<String, SoftReference<List<FaceItem>>> mBitMapCache = null;



    public void addKey(String imagePath,List<FaceItem>faceItems)
    {
        if(mBitMapCache!=null)
        {
            if(!mBitMapCache.containsKey(imagePath))
            {
                mBitMapCache.put(imagePath,new SoftReference<List<FaceItem>>(faceItems));
            }
        }
    }
    /**
     * Constructor method
     */
    public AsyncBitmapLoader() {
        mBitMapCache = new HashMap<String, SoftReference<List<FaceItem>>>();
    }


    /**
     * Load image by path
     *
     * @param context
     * @param imagePath
     * @param callback
     * @return if return not null, it states there has cache
     */
    public List<FaceItem> loadAsyncBean(final Context context, final String imagePath, final BitmapCallback callback) {
        Log.e("jwjw", 123 + "loadAsyncBean");
        if (mBitMapCache.containsKey(imagePath)) {
            Log.e("jwjw", 456 + "  containsKey");

            SoftReference<List<FaceItem>> softReference = mBitMapCache.get(imagePath);
            List<FaceItem> faces = softReference.get();
            if (faces != null) {
                return faces;
            }
        }
        Log.e("jwjw", 123 + "  没有  containsKey");
        final Handler handler = new Handler() {
            public void handleMessage(Message message) {
                List<FaceItem> faces = (List<FaceItem>) message.obj;
                callback.facesLoaded(faces);
            }
        };

        final ProgressDialogUtil progressDialogUtil = new ProgressDialogUtil(context);
        new AsyncTask<String, Bitmap, Bitmap>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialogUtil.show("正在检测图片...");
            }

            @Override
            protected Bitmap doInBackground(String... params) {
                Bitmap tempBitmap = ImageUtils.getBitmapFromLocalPath(params[0], 1);
                Bitmap bitmap =  ImageUtils.getResizedBitmap(tempBitmap, 600, 600);
                callback.bitmapLoaded(bitmap);
                return bitmap;
            }

            @Override
            protected void onPostExecute(final Bitmap bitmap) {
                if (bitmap != null) {
                    DetectFaceUtil.detectFace(context, NetWorkConstant.APP_ID, Base64Util.encode(ImageUtils.Bitmap2Bytes(bitmap)), 1, new NetCallback<DetectFaceResponseBean>(context) {
                        @Override
                        public void onFailure(RetrofitError error, String message) {
                            progressDialogUtil.hide();
                        }

                        @Override
                        public void success(DetectFaceResponseBean detectFaceResponseBean, Response response) {
                            progressDialogUtil.hide();
                            List<FaceItem> faceItem = detectFaceResponseBean.getFace();
                            mBitMapCache.put(imagePath, new SoftReference<List<FaceItem>>(faceItem));
                            Message successMessage = handler.obtainMessage(SUCCESS_DETECT_FACE, faceItem);
                            handler.sendMessage(successMessage);
                        }
                    });
                } else {
                    progressDialogUtil.hide();
                }
            }

        }.execute(imagePath);

        return null;
    }

    public BitmapCallback callback;

    public interface BitmapCallback {
        /**
         * @param faces
         */
        public void facesLoaded(List<FaceItem> faces);
        public void bitmapLoaded(Bitmap bitmap);
    }
}

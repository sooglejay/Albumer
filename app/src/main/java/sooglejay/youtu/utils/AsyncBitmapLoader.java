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
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit.RetrofitError;
import retrofit.client.Response;
import sooglejay.youtu.api.detectface.DetectFaceResponseBean;
import sooglejay.youtu.api.detectface.DetectFaceUtil;
import sooglejay.youtu.api.detectface.FaceItem;
import sooglejay.youtu.api.faceidentify.IdentifyItem;
import sooglejay.youtu.constant.IntConstant;
import sooglejay.youtu.constant.NetWorkConstant;
import sooglejay.youtu.model.NetCallback;
import sooglejay.youtu.widgets.imagepicker.bean.Image;
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
    private HashMap<String,ArrayList<FaceItem>> mDetectedFaceBitMapCache = null;


    private HashMap<String,ArrayList<IdentifyItem>> mIdentifiedFaceBitMapCache = null;


    public void addNewDetectedFaceToCache(String imagePath, ArrayList<FaceItem> faceItems) {
        if (mDetectedFaceBitMapCache != null) {
            if (!mDetectedFaceBitMapCache.containsKey(imagePath)) {
                mDetectedFaceBitMapCache.put(imagePath, faceItems);
            }
        }
    }

    public void addNewIdentifiedFaceToCache(String imagePath, ArrayList<IdentifyItem> faceItems) {
        if (mIdentifiedFaceBitMapCache != null) {
            if (!mIdentifiedFaceBitMapCache.containsKey(imagePath)) {
                mIdentifiedFaceBitMapCache.put(imagePath, faceItems);
            }
        }
    }

    /**
     * Constructor method
     */
    public AsyncBitmapLoader() {
    }

    public void setmDetectedFaceBitMapCache(HashMap<String, ArrayList<FaceItem>> cache) {
        if (cache == null) {
            mDetectedFaceBitMapCache = new HashMap<String,ArrayList<FaceItem>>();
        } else {
            mDetectedFaceBitMapCache = cache;
        }
    }

    public void setmIdentifiedFaceBitMapCache(HashMap<String, ArrayList<IdentifyItem>> cache) {
        if (cache == null) {
            mIdentifiedFaceBitMapCache = new HashMap<String,ArrayList<IdentifyItem>>();
        } else {
            mIdentifiedFaceBitMapCache = cache;
        }

    }

    public HashMap<String, ArrayList<IdentifyItem>> getmIdentifiedFaceBitMapCache() {
        return mIdentifiedFaceBitMapCache;
    }

    public HashMap<String, ArrayList<FaceItem>> getmDetectedFaceBitMapCache() {
        return mDetectedFaceBitMapCache;
    }

    /**
     * Load image by path
     *
     * @param context
     * @param imagePath
     * @param callback
     * @return if return not null, it states there has cache
     */
    public ArrayList<FaceItem> loadAsyncBean(final Context context, final String imagePath, final BitmapCallback callback) {
        Log.e("jwjw", 123 + "loadAsyncBean");
        if (mDetectedFaceBitMapCache !=null&& mDetectedFaceBitMapCache.containsKey(imagePath)) {
            Log.e("jwjw", 456 + "  containsKey");
            ArrayList<FaceItem> faces = mDetectedFaceBitMapCache.get(imagePath);
            if (faces != null) {
                return faces;
            }
        }
        Log.e("jwjw", 123 + "  没有  containsKey");

        final ProgressDialogUtil progressDialogUtil = new ProgressDialogUtil(context);
        new AsyncTask<String, Bitmap, Bitmap>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialogUtil.show("正在检测图片...");
            }

            @Override
            protected Bitmap doInBackground(String... params)
            {
                Bitmap tempBitmap = ImageUtils.getBitmapFromLocalPath(params[0], 1);
                Bitmap bitmap = ImageUtils.getResizedBitmap(tempBitmap, IntConstant.IMAGE_SIZE, IntConstant.IMAGE_SIZE);
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
                            ArrayList<FaceItem> faceItem = detectFaceResponseBean.getFace();
                            mDetectedFaceBitMapCache.put(imagePath, faceItem);
                            callback.facesLoaded(faceItem);
                            callback.faceidentify(bitmap);
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
        public void facesLoaded(ArrayList<FaceItem> faces);
        public void bitmapLoaded(Bitmap bitmap);
        public void faceidentify(Bitmap bitmap);
    }
}

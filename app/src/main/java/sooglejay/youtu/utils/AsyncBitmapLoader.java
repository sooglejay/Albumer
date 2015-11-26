/**
 * AsyncImageLoader.java
 *
 * @version 0.9
 * @author nicky.han
 */
package sooglejay.youtu.utils;

import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.widget.FrameLayout;

import retrofit.RetrofitError;
import retrofit.client.Response;
import sooglejay.youtu.api.detectface.AsyncBean;
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
    /**
     * Used to save image by soft reference. It make image easy to release.
     */
    private HashMap<String, SoftReference<AsyncBean>> mBitMapCache = null;

    /**
     * Constructor method
     */
    public AsyncBitmapLoader() {
        mBitMapCache = new HashMap<String, SoftReference<AsyncBean>>();
        handler = new MyHandler(this);
    }

    private MyHandler handler;
    private static class MyHandler extends Handler {
        private final WeakReference<AsyncBitmapLoader> mTarget;
        MyHandler(AsyncBitmapLoader target) {
            mTarget = new WeakReference<>(target);
        }
        public void handleMessage(Message message) {
            AsyncBitmapLoader target = mTarget.get();
            if (target != null) {
                callback.imageLoaded((AsyncBean) message.obj);
            }
        }

    }


    /**
     * Load image by path
     *
     * @param context
     * @param imagePath
     * @param callback
     * @return if return not null, it states there has cache
     */
    public AsyncBean loadAsyncBean(final Context context, final String imagePath, final BitmapCallback callback) {
         AsyncBitmapLoader.callback = callback;
        if (mBitMapCache.containsKey(imagePath)) {
            SoftReference<AsyncBean> softReference = mBitMapCache.get(imagePath);
            AsyncBean bean = softReference.get();
            if (bean != null) {
                return bean;
            }
        }
        final ProgressDialogUtil progressDialogUtil = new ProgressDialogUtil(context);
        new AsyncTask<String, Bitmap, Bitmap>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialogUtil.show("正在检测图片...");
            }

            @Override
            protected Bitmap doInBackground(String... params) {

                Bitmap bitmap;
                Bitmap tempBitmap = ImageUtils.getBitmapFromLocalPath(params[0], 1);
                bitmap = ImageUtils.getResizedBitmap(tempBitmap, 600, 600);
                return bitmap;

            }

            @Override
            protected void onPostExecute(final Bitmap bitmap) {
                final AsyncBean asyncBean = new AsyncBean();
                asyncBean.setBitmap(bitmap);
                DetectFaceUtil.detectFace(context, NetWorkConstant.APP_ID, Base64Util.encode(ImageUtils.Bitmap2Bytes(bitmap)), 1, new NetCallback<DetectFaceResponseBean>(context) {
                    @Override
                    public void onFailure(RetrofitError error, String message) {
                        progressDialogUtil.hide();
                    }

                    @Override
                    public void success(DetectFaceResponseBean detectFaceResponseBean, Response response) {
                        progressDialogUtil.hide();

                        List<FaceItem> faceItem = detectFaceResponseBean.getFace();
                        asyncBean.setFaces(faceItem);
                        mBitMapCache.put(imagePath, new SoftReference<>(asyncBean));
                        Message message = handler.obtainMessage(0, asyncBean);
                        handler.sendMessage(message);
                    }
                });
            }
        }.execute(imagePath);
        return null;
    }

    private static BitmapCallback callback;

    public interface BitmapCallback {
        /**
         * @param asyncBean
         */
        public void imageLoaded(AsyncBean asyncBean);
    }
}

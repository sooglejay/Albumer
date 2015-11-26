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

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.List;

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
        if (mBitMapCache.containsKey(imagePath)) {
            SoftReference<AsyncBean> softReference = mBitMapCache.get(imagePath);
            AsyncBean bean = softReference.get();
            if (bean != null) {
                return bean;
            }
        }

        final Handler handler = new Handler()
        {
            public void handleMessage(Message message)
            {
                callback.imageLoaded((AsyncBean) message.obj);
            }
        };

            final ProgressDialogUtil progressDialogUtil = new ProgressDialogUtil(context);
            new AsyncTask<String, AsyncBean, AsyncBean>() {
                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    progressDialogUtil.show("正在检测图片...");
                }
                @Override
                protected AsyncBean doInBackground(String... params) {
                    final AsyncBean asyncBean = new AsyncBean();

                    Bitmap tempBitmap = ImageUtils.getBitmapFromLocalPath(params[0], 1);
                    Bitmap  bitmap = ImageUtils.getResizedBitmap(tempBitmap, 600, 600);
                    asyncBean.setBitmap(bitmap);
                    return asyncBean;
                }
                @Override
                protected void onPostExecute(final AsyncBean asyncBean) {
                    DetectFaceUtil.detectFace(context, NetWorkConstant.APP_ID, Base64Util.encode(ImageUtils.Bitmap2Bytes(asyncBean.getBitmap())), 1, new NetCallback<DetectFaceResponseBean>(context) {
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

    public BitmapCallback callback;

    public interface BitmapCallback {
        /**
         * @param asyncBean
         */
        public void imageLoaded(AsyncBean asyncBean);
    }
}

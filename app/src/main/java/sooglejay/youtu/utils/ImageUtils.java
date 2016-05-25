package sooglejay.youtu.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.hardware.Camera.Size;
import android.net.Uri;
import android.os.Environment;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.CRC32;

import sooglejay.youtu.R;
import sooglejay.youtu.constant.IntConstant;
import sooglejay.youtu.widgets.imagepicker.MultiImageSelectorActivity;


public class ImageUtils implements Parcelable {

    /**
     * 选择图片
     */
    public static final int REQUEST_CODE_PICK_IMAGE = 1991;
    /**
     * 拍照
     */
    public static final int REQUEST_CODE_CAPTURE_IMAGE = 1992;
    /**
     * 裁剪图片
     */
    public static final int REQUEST_CODE_CROP_IMAGE = 1993;

    /**
     * 取得图片展示选项
     *
     * @param imgs
     * @return
     */
    public static DisplayImageOptions getOptions(int... imgs) {
        int loadingImg = R.drawable.default_error;
        int emptyImg = R.drawable.default_error;
        int failImg = R.drawable.default_error;
        if (imgs.length == 3) {
            loadingImg = imgs[0];
            emptyImg = imgs[1];
            failImg = imgs[2];
        }
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(loadingImg)
                .showImageForEmptyUri(emptyImg)
                .showImageOnFail(failImg)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .considerExifParams(true)
                .displayer(new SimpleBitmapDisplayer()).build();
        return options;
    }

    /**
     * 是否可写
     * @return
     */
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /**
     * 是否可读
     * @return
     */
    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    /**
     * 取得图片文件夹位置
     *
     * @param context
     * @return
     */
    public static String getImageFolderPath(Context context) {
        String packPath = "";
        try {
            packPath = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath();
        }catch (OutOfMemoryError oom)
        {
            Log.e("jwjw","oom");
        } catch (Exception e) {
            packPath = context.getFilesDir() + File.separator + "Pictures";
        } finally {
            return packPath;
        }
    }

    /**
     * 裁剪图片
     *
     * @param activity
     * @param imageUri
     * @return
     */
    public static void cropImage(Activity activity, Uri imageUri, String resultPath, int aspectX, int aspectY) {
        Intent intent = getCropImageIntent(imageUri, resultPath, aspectX, aspectY);
        activity.startActivityForResult(intent, REQUEST_CODE_CROP_IMAGE);
    }

    public static void cropImage(Fragment fragment, Uri imageUri, String resultPath, int aspectX, int aspectY) {
        Intent intent = getCropImageIntent(imageUri, resultPath, aspectX, aspectY);
        fragment.startActivityForResult(intent, REQUEST_CODE_CROP_IMAGE);
    }


    @NonNull
    private static Intent getCropImageIntent(Uri imageUri, String resultPath, int aspectX, int aspectY) {
        //创建图片文件
        File resultFile = new File(resultPath);
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(imageUri, "image/*");
        //下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        if (aspectX != 0) {
            intent.putExtra("aspectX", aspectX);
        }
        if (aspectY != 0) {
            intent.putExtra("aspectY", aspectY);
        }
        intent.putExtra("scale", true);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", IntConstant.IMAGE_SIZE);
        intent.putExtra("outputY", IntConstant.IMAGE_SIZE);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(resultFile));
        return intent;
    }

    public static Bitmap compressImageFromPath(String path) {
        Bitmap bitmap;
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        newOpts.inPreferredConfig = Bitmap.Config.RGB_565;

        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = IntConstant.IMAGE_SIZE;//这里设置高度为600f
        float ww = IntConstant.IMAGE_SIZE;//这里设置宽度为600f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        float be = 1f;//be=1表示不缩放
        if (w >= h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0) {
            be = 1f;
        }
        newOpts.inSampleSize = (int) be;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(path, newOpts);
        if (bitmap.getHeight() > hh || bitmap.getWidth() > ww) {
            int height = bitmap.getHeight();
            int width = bitmap.getWidth();
            float scaleSize = 1f;
            if (width >= height) {
                scaleSize = ww / width;
            } else if (height > width) {
                scaleSize = ww / height;
            }
            Matrix matrix = new Matrix();
            matrix.postScale(scaleSize, scaleSize);
            Bitmap result = Bitmap.createBitmap(bitmap, 0, 0, width,
                    height, matrix, true);
            bitmap.recycle();
            return result;
        }

        return bitmap;//压缩好比例大小后再进行质量压缩
    }


    /**
     * 选择照片
     */
    public static void startPickPhoto(Activity context, ArrayList<String> selectedImages, int max_count, boolean isModeMulti) {
        Intent intent = getPickPhotoIntent(context, selectedImages, max_count, isModeMulti);
        context.startActivityForResult(intent, ImageUtils.REQUEST_CODE_PICK_IMAGE);
    }

    /**
     * 选择照片
     */
    public static void startPickPhoto(Activity context, Fragment fragment, ArrayList<String> selectedImages, int max_count, boolean isModeMulti) {
        Intent intent = getPickPhotoIntent(context, selectedImages, max_count, isModeMulti);
        fragment.startActivityForResult(intent, ImageUtils.REQUEST_CODE_PICK_IMAGE);
    }

    @NonNull
    private static Intent getPickPhotoIntent(Activity context, ArrayList<String> selectedImages, int max_count, boolean isModeMulti) {
        Intent intent = new Intent(context, MultiImageSelectorActivity.class);

        // 是否显示调用相机拍照
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);

        // 最大图片选择数量
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, max_count);

        // 设置模式 (支持 单选/MultiImageSelectorActivity.MODE_SINGLE 或者 多选/MultiImageSelectorActivity.MODE_MULTI)
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, isModeMulti ? MultiImageSelectorActivity.MODE_MULTI : MultiImageSelectorActivity.MODE_SINGLE);

        // 默认选择
        if (selectedImages != null && selectedImages.size() > 0) {
            intent.putExtra(MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST, selectedImages);
        }
        return intent;
    }


    //获得指定文件的byte数组
    public static byte[] getBytes(String filePath) {
        byte[] buffer = null;
        try {
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }

    /**
     * 获取crc32值
     *
     * @param bytes
     * @return
     */
    public static long getCrc32(byte[] bytes) {
        CRC32 crc32 = new CRC32();
        crc32.update(bytes);
        return crc32.getValue();
    }
    /**
     * 一个坐标点，以某个点为缩放中心，缩放指定倍数，求这个坐标点在缩放后的新坐标值。
     *
     * @param targetPointX 坐标点的X
     * @param targetPointY 坐标点的Y
     * @param scaleCenterX 缩放中心的X
     * @param scaleCenterY 缩放中心的Y
     * @param scale        缩放倍数
     * @return 坐标点的新坐标
     */
    protected PointF scaleByPoint(float targetPointX, float targetPointY, float scaleCenterX, float scaleCenterY, float scale) {
        Matrix matrix = new Matrix();
        // 将Matrix移到到当前圆所在的位置，
        // 然后再以某个点为中心进行缩放
        matrix.preTranslate(targetPointX, targetPointY);
        matrix.postScale(scale, scale, scaleCenterX, scaleCenterY);
        float[] values = new float[9];
        matrix.getValues(values);
        return new PointF(values[Matrix.MTRANS_X], values[Matrix.MTRANS_Y]);
    }

    public static byte[] Bitmap2Bytes(Bitmap bm){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }


    public static Bitmap Bytes2Bimap(byte[] b){
        if(b.length!=0){
            try {
                return BitmapFactory.decodeByteArray(b, 0, b.length);
            } catch (OutOfMemoryError oom)
            {
                Log.e("jwjw","oom");
            }catch (Exception e) {
                //  Logger.e(e.toString());
                Log.e("jwjw", e.toString());
            }
        }
        return null;
    }
    public static final int READ_BUFFER_SIZE = 32 * 1024;  //32KB

    /**
     * @param path
     * @param sampleSize 1 = 100%, 2 = 50%(1/2), 4 = 25%(1/4), ...
     * @return
     */
    public static Bitmap getBitmapFromLocalPath(String path, int sampleSize) {
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = sampleSize;
            return BitmapFactory.decodeFile(path, options);
        } catch (OutOfMemoryError oom)
        {
            Log.e("jwjw","oom");
        }catch (Exception e) {
            //  Logger.e(e.toString());
        }

        return null;
    }

    /**
     * @param bytes
     * @return
     */
    public static Bitmap getBitmapFromBytes(byte[] bytes) {
        try {
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        } catch (OutOfMemoryError oom)
        {
            Log.e("jwjw","oom");
        }catch (Exception e) {
            //  Logger.e(e.toString());
        }

        return null;
    }

    /**
     * @param bitmap
     * @param quality 1 ~ 100
     * @return
     */
    public static byte[] compressBitmap(Bitmap bitmap, int quality) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, quality, baos);

            return baos.toByteArray();
        }catch (OutOfMemoryError oom)
        {
            Log.e("jwjw","oom");
        } catch (Exception e) {
            //  Logger.e(e.toString());
        }

        return null;
    }

    /**
     * @param srcBitmap
     * @param newWidth
     * @param newHeight
     * @return
     */
    public static Bitmap getResizedBitmap(Bitmap srcBitmap, int newWidth, int newHeight) {
        try {
            //不
            int oW = srcBitmap.getWidth();
            int oH = srcBitmap.getHeight();
            if(oH>newHeight)
            {
                float oHf = oH;
                float scale = oHf/oW;
                Log.e("Retrofit","oH:"+oH);
                Log.e("Retrofit","oW:"+oW);
                Log.e("Retrofit","scale:"+scale);
                newWidth = (int)(newHeight/scale);
                Log.e("Retrofit","newWidth:"+newWidth);
                Log.e("Retrofit","newHeight:"+newHeight);
            }else if(oW>newWidth){
                float oHf = oH;
                float scale = oHf/oW;
                Log.e("Retrofit","oH:"+oH);
                Log.e("Retrofit","oW:"+oW);
                Log.e("Retrofit","scale:"+scale);
                newWidth = (int)(newHeight/scale);
                Log.e("Retrofit","newWidth:"+newWidth);
                Log.e("Retrofit","newHeight:"+newHeight);
            }else {
                newHeight = oH;
                newWidth = oW;
                Log.e("Retrofit","oH:"+oH);
                Log.e("Retrofit","oW:"+oW);
            }
            return Bitmap.createScaledBitmap(srcBitmap, newWidth, newHeight,true);
        } catch (Exception e) {
            //  Logger.e(e.toString());
        }catch (OutOfMemoryError oom)
        {
            Log.e("jwjw","outofmemeoty");
        }
        return null;
    }

    /**
     * captures given view and converts it to a bitmap
     *
     * @param view
     * @return
     */
    public static Bitmap captureViewToBitmap(View view) {
        Bitmap result = null;

        try {
            result = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.RGB_565);
            view.draw(new Canvas(result));
        }catch (OutOfMemoryError oom)
        {
            Log.e("jwjw","oom");
            Context context = view.getContext();
            if(context!=null)
            {
                Toast.makeText(context,"内存不足！请稍后重试！",Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            //Logger.e(e.toString());
        }

        return result;
    }

    /**
     * @param original
     * @param format
     * @param quality
     * @param outputLocation
     * @return
     */
    public static boolean saveBitmap(Bitmap original, Bitmap.CompressFormat format, int quality, String outputLocation) {
        if (original == null)
            return false;

        try {
            return original.compress(format, quality, new FileOutputStream(outputLocation));
        } catch (OutOfMemoryError oom)
        {
            Log.e("jwjw","oom");
        }catch (Exception e) {
            //  Logger.e(e.toString());
        }

        return false;
    }

    /**
     * @param filepath
     * @param widthLimit
     * @param heightLimit
     * @param totalSize
     * @return
     */
    public static Bitmap getResizedBitmap(String filepath, int widthLimit, int heightLimit, int totalSize) {
        int outWidth = 0;
        int outHeight = 0;
        int resize = 1;
        InputStream input = null;

        try {
            input = new FileInputStream(new File(filepath));

            BitmapFactory.Options getSizeOpt = new BitmapFactory.Options();
            getSizeOpt.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(input, null, getSizeOpt);
            outWidth = getSizeOpt.outWidth;
            outHeight = getSizeOpt.outHeight;

            while ((outWidth / resize) > widthLimit || (outHeight / resize) > heightLimit) {
                resize *= 2;
            }
            resize = resize * (totalSize + 15) / 15;

            BitmapFactory.Options resizeOpt = new BitmapFactory.Options();
            resizeOpt.inSampleSize = resize;

            input.close();
            input = null;

            input = new FileInputStream(new File(filepath));
            Bitmap bitmapImage = BitmapFactory.decodeStream(input, null, resizeOpt);
            return bitmapImage;
        } catch (Exception e) {
            //  Logger.e(e.toString());
        } catch (OutOfMemoryError oom)
        {
            Log.e("jwjw","oom");
        }finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    //  Logger.e(e.toString());
                }catch (OutOfMemoryError oom)
                {
                    Log.e("jwjw","oom");
                }
            }
        }
        return null;
    }


    /**
     * generate a blurred bitmap from given one
     * <p>
     * referenced: http://incubator.quasimondo.com/processing/superfastblur.pde
     *
     * @param original
     * @param radius
     * @return
     */
    public Bitmap getBlurredBitmap(Bitmap original, int radius) {
        if (radius < 1)
            return null;

        int width = original.getWidth();
        int height = original.getHeight();
        int wm = width - 1;
        int hm = height - 1;
        int wh = width * height;
        int div = radius + radius + 1;
        int r[] = new int[wh];
        int g[] = new int[wh];
        int b[] = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, p1, p2, yp, yi, yw;
        int vmin[] = new int[Math.max(width, height)];
        int vmax[] = new int[Math.max(width, height)];
        int dv[] = new int[256 * div];
        for (i = 0; i < 256 * div; i++)
            dv[i] = i / div;

        int[] blurredBitmap = new int[wh];
        original.getPixels(blurredBitmap, 0, width, 0, 0, width, height);

        yw = 0;
        yi = 0;

        for (y = 0; y < height; y++) {
            rsum = 0;
            gsum = 0;
            bsum = 0;
            for (i = -radius; i <= radius; i++) {
                p = blurredBitmap[yi + Math.min(wm, Math.max(i, 0))];
                rsum += (p & 0xff0000) >> 16;
                gsum += (p & 0x00ff00) >> 8;
                bsum += p & 0x0000ff;
            }
            for (x = 0; x < width; x++) {
                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];

                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm);
                    vmax[x] = Math.max(x - radius, 0);
                }
                p1 = blurredBitmap[yw + vmin[x]];
                p2 = blurredBitmap[yw + vmax[x]];

                rsum += ((p1 & 0xff0000) - (p2 & 0xff0000)) >> 16;
                gsum += ((p1 & 0x00ff00) - (p2 & 0x00ff00)) >> 8;
                bsum += (p1 & 0x0000ff) - (p2 & 0x0000ff);
                yi++;
            }
            yw += width;
        }

        for (x = 0; x < width; x++) {
            rsum = gsum = bsum = 0;
            yp = -radius * width;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;
                rsum += r[yi];
                gsum += g[yi];
                bsum += b[yi];
                yp += width;
            }
            yi = x;
            for (y = 0; y < height; y++) {
                blurredBitmap[yi] = 0xff000000 | (dv[rsum] << 16) | (dv[gsum] << 8) | dv[bsum];
                if (x == 0) {
                    vmin[y] = Math.min(y + radius + 1, hm) * width;
                    vmax[y] = Math.max(y - radius, 0) * width;
                }
                p1 = x + vmin[y];
                p2 = x + vmax[y];

                rsum += r[p1] - r[p2];
                gsum += g[p1] - g[p2];
                bsum += b[p1] - b[p2];

                yi += width;
            }
        }

        return Bitmap.createBitmap(blurredBitmap, width, height, Bitmap.Config.RGB_565);
    }

    /**
     * calculate optimal preview size from given parameters
     * <br>
     * referenced: http://developer.android.com/resources/samples/ApiDemos/src/com/example/android/apis/graphics/CameraPreview.html
     *
     * @param sizes obtained from camera.getParameters().getSupportedPreviewSizes()
     * @param w
     * @param h
     * @return
     */
    public static Size getOptimalPreviewSize(List<Size> sizes, int w, int h) {
        final double ASPECT_TOLERANCE = 0.05;
        double targetRatio = (double) w / h;
        if (sizes == null) return null;

        Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        int targetHeight = h;

        // Try to find an size match aspect ratio and size
        for (Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

        // Cannot find the one match the aspect ratio, ignore the requirement
        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        return optimalSize;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    public ImageUtils() {
    }

    protected ImageUtils(Parcel in) {
    }

    public static final Parcelable.Creator<ImageUtils> CREATOR = new Parcelable.Creator<ImageUtils>() {
        public ImageUtils createFromParcel(Parcel source) {
            return new ImageUtils(source);
        }

        public ImageUtils[] newArray(int size) {
            return new ImageUtils[size];
        }
    };
}

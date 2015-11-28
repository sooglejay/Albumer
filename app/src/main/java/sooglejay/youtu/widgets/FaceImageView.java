package sooglejay.youtu.widgets;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import sooglejay.youtu.R;
import sooglejay.youtu.api.detectface.FaceItem;
import uk.co.senab.photoview.PhotoView;

/**
 * Created by sooglejay on 2015/11/21.
 */
public class FaceImageView extends PhotoView {
    ArrayList<FaceItem> faceItemList = new ArrayList<>();
    Bitmap bitmap, operationBitmap;
    private int mWidth;
    private int mHeight;
    private float mAngle;


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mWidth = View.MeasureSpec.getSize(widthMeasureSpec);
        mHeight = View.MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(mWidth, mHeight);
    }

    public FaceImageView(Context context) {
        super(context);
        initView(context);

    }

    public FaceImageView(Context context, AttributeSet attr) {
        super(context, attr);
        initView(context);

    }

    public FaceImageView(Context context, AttributeSet attr, int defStyle) {
        super(context, attr, defStyle);
        initView(context);

    }

    private void initView(Context context) {
//        operationBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_more_operation).copy(Bitmap.Config.ARGB_8888, true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int cx = 0;
        int cy = 0;
        if (bitmap != null) {
            cx = (mWidth - bitmap.getWidth()) >> 1; // same as (...) / 2
            cy = (mHeight - bitmap.getHeight()) >> 1;
            if (mAngle > 0) {
                canvas.rotate(mAngle, mWidth >> 1, mHeight >> 1);
            }
            canvas.drawBitmap(bitmap, cx, cy, null);
            canvas.save();
        }
        if (faceItemList != null) {
            Paint paint = new Paint();
            paint.setColor(Color.GREEN);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(3);
            for (FaceItem faceItem : faceItemList) {
                float centerX = faceItem.getWidth() / 2;
                float centerY = faceItem.getHeight() / 2;
                float radius;
                radius = centerX > centerY ? centerX : centerY;
                if (mAngle > 0) {
                    canvas.rotate(mAngle, mWidth >> 1, mHeight >> 1);
                }
                canvas.drawCircle(centerX + faceItem.getX() + cx, centerY + faceItem.getY() + cy, radius, paint);
                canvas.save();
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 获取点击屏幕时的点的坐标
        float x = event.getX();
        float y = event.getY();
        whichCircle(x, y);//判断点击的是人的人脸
        return super.onTouchEvent(event);
    }

    private void whichCircle(float x, float y) {

    }

    public void setCanvasRes(Bitmap bitmap, ArrayList<FaceItem> faceList) {
        this.bitmap = bitmap;
        this.faceItemList = faceList;
        invalidate();
    }

    public void setCanvasBitmapRes(Bitmap bm) {
        this.bitmap = bm;
        invalidate();
    }
   public void setCanvasFaceListRes(ArrayList<FaceItem>faceListRes) {
        this.faceItemList = faceListRes;
        invalidate();
    }

    public void clearCanvasRes() {
        if (bitmap != null) {
            bitmap.recycle();
            bitmap = null;
        }
        if (faceItemList != null) {
            faceItemList.clear();
            faceItemList = null;
        }
    }
}

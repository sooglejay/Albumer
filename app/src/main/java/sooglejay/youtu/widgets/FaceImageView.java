package sooglejay.youtu.widgets;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import sooglejay.youtu.api.detectface.FaceItem;
import uk.co.senab.photoview.PhotoView;

/**
 * Created by sooglejay on 2015/11/21.
 */
public class FaceImageView extends PhotoView {
    List<FaceItem> faceItemList = new ArrayList<>();
    Bitmap bitmap;
    private int mWidth;
    private int mHeight;
    private float mAngle;

    @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        mWidth = View.MeasureSpec.getSize(widthMeasureSpec);
        mHeight = View.MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(mWidth, mHeight);
    }
    public FaceImageView(Context context) {
        super(context);
    }

    public FaceImageView(Context context, AttributeSet attr) {
        super(context, attr);
    }

    public FaceImageView(Context context, AttributeSet attr, int defStyle) {
        super(context, attr, defStyle);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int cx = 0;
        int cy = 0;
        if(bitmap!=null)
        {
             cx = (mWidth - bitmap.getWidth()) >> 1; // same as (...) / 2
             cy = (mHeight - bitmap.getHeight()) >> 1;
            if (mAngle > 0) {
                canvas.rotate(mAngle, mWidth >> 1, mHeight >> 1);
            }
            canvas.drawBitmap(bitmap, cx, cy, null);
        }
        if (faceItemList != null) {
            Paint paint = new Paint();
            paint.setColor(Color.GREEN);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(3);
            for(FaceItem faceItem :faceItemList) {
                float centerX = faceItem.getWidth() / 2;
                float centerY = faceItem.getHeight() / 2;
                float radius = centerX > centerY ? centerX : centerY;
                if (mAngle > 0) {
                    canvas.rotate(mAngle, mWidth >> 1, mHeight >> 1);
                }
                canvas.drawCircle(centerX + faceItem.getX() + cx, centerY + faceItem.getY() + cy, radius, paint);
                canvas.save();
            }
            faceItemList.clear();
            canvas.save();
        }

    }

    public void setCanvasRes(Bitmap bitmap, List<FaceItem> faceList) {
        this.bitmap = bitmap;
        this.faceItemList.addAll(faceList);
        invalidate();
    }
    public void setCanvasBitmapRes(Bitmap bm) {
        this.bitmap = bm;
    }

}

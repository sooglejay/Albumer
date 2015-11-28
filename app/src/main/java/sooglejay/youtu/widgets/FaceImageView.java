package sooglejay.youtu.widgets;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import sooglejay.youtu.api.detectface.FaceItem;
import uk.co.senab.photoview.PhotoView;

/**
 * Created by sooglejay on 2015/11/21.
 */
public class FaceImageView extends PhotoView {
    private static final int MAX_CLICK_DURATION = 300;

    private Context context;
    ArrayList<FaceItem> faceItemList = new ArrayList<>();
    Bitmap bitmap, operationBitmap;

    int cx = 0;//将图像移到中间的偏移量
    int cy = 0;
    private int mWidth;
    private int mHeight;
    private float mAngle;


    private FaceItem outerFaceItem;
    float centerX;
    float centerY;
    float radius;


    float eventX, eventY;
    boolean clickFlag = false;//点击的次数是偶数
    Long clickTime;//点击时的时间戳

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
        this.context = context;
        this.setLongClickable(true);
//        operationBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_more_operation).copy(Bitmap.Config.ARGB_8888, true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        cx = 0;
        cy = 0;

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
                outerFaceItem = faceItem;
                centerX = faceItem.getWidth() / 2;
                centerY = faceItem.getHeight() / 2;
                radius = centerX > centerY ? centerX : centerY;
                if (mAngle > 0) {
                    canvas.rotate(mAngle, mWidth >> 1, mHeight >> 1);
                }
                canvas.drawCircle(centerX + faceItem.getX() + cx, centerY + faceItem.getY() + cy, radius, paint);
                canvas.save();
            }
        }
    }

    private void whichCircle(float x, float y) {
        if (centerX + outerFaceItem.getX() + cx - radius <= x && x <= centerX + outerFaceItem.getX() + cx + radius) {
            if (centerY + outerFaceItem.getY() + cy - radius <= y && y <= centerY + outerFaceItem.getY() + cy + radius) {
                Toast.makeText(context, "点自己", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "X 符合，Y不符合", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, "X不符合", Toast.LENGTH_SHORT).show();
        }
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

    public void setCanvasFaceListRes(ArrayList<FaceItem> faceListRes) {
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


    @Override
    public void setOnTouchListener(OnTouchListener l) {
        MyOnTouchListener onTouchListener = new MyOnTouchListener();
        super.setOnTouchListener(onTouchListener);
    }

    private class MyOnTouchListener implements OnTouchListener
    {

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {

//        whichCircle(x, y);
            Log.e("test", "motionEvent.getAction()：" + motionEvent.getAction() + "");
            Log.e("test","motionEvent.getActionIndex()："+motionEvent.getActionIndex()+"");
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    Toast.makeText(context, "按下：", Toast.LENGTH_SHORT).show();
                    break;
                case MotionEvent.ACTION_UP:
                    Toast.makeText(context, "抬起：", Toast.LENGTH_SHORT).show();
                    break;
            }

            return false;
        }
    }

}

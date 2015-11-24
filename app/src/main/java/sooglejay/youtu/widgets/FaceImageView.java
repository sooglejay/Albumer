package sooglejay.youtu.widgets;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ImageView;

import sooglejay.youtu.api.detectface.FaceItem;

/**
 * Created by sooglejay on 2015/11/21.
 */
public class FaceImageView extends ImageView {
    FaceItem faceItem;

    public void setFaceItem(FaceItem faceItem) {
        this.faceItem = faceItem;
        invalidate();
    }

    public FaceImageView(Context context) {
        super(context);
    }

    public FaceImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FaceImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public FaceImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (faceItem != null) {
            Paint paint = new Paint();
            paint.setColor(Color.RED);
            canvas.drawRect(faceItem.getX(), faceItem.getY(), faceItem.getX() + faceItem.getWidth(), faceItem.getX() + faceItem.getHeight(), paint);
        }
    }
}

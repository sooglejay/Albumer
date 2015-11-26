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
import uk.co.senab.photoview.PhotoView;

/**
 * Created by sooglejay on 2015/11/21.
 */
public class FaceImageView extends PhotoView {
    FaceItem faceItem;

    public FaceImageView(Context context) {
        super(context);
    }

    public FaceImageView(Context context, AttributeSet attr) {
        super(context, attr);
    }

    public FaceImageView(Context context, AttributeSet attr, int defStyle) {
        super(context, attr, defStyle);
    }

    public void setFaceItem(FaceItem faceItem) {
        this.faceItem = faceItem;
        invalidate();
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (faceItem != null) {
            Paint paint = new Paint();
            paint.setColor(Color.GREEN);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(3);
            float centerX = faceItem.getWidth()/2;
            float centerY = faceItem.getHeight()/2;
            float radius = centerX>centerY?centerX:centerY;
            canvas.drawCircle(centerX+faceItem.getX(),centerY+faceItem.getY(),radius, paint);
            faceItem = null;
        }
    }
}

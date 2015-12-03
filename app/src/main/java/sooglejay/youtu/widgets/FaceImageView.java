package sooglejay.youtu.widgets;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.util.ArrayList;

import sooglejay.youtu.R;
import sooglejay.youtu.api.detectface.FaceItem;
import sooglejay.youtu.api.faceidentify.IdentifyItem;
import sooglejay.youtu.fragment.DialogFragmentCreater;
import sooglejay.youtu.fragment.GalleryFragment;
import sooglejay.youtu.ui.AddNewPersonActivity;
import sooglejay.youtu.ui.EditFaceUserInfoActivity;
import sooglejay.youtu.ui.SetIdentifyGroupIdActivity;
import sooglejay.youtu.utils.GetTagUtil;
import sooglejay.youtu.utils.UIUtil;
import uk.co.senab.photoview.PhotoView;

/**
 * Created by sooglejay on 2015/11/21.
 */
public class FaceImageView extends PhotoView {
    private static final int MAX_CLICK_DURATION = 300;

    public void setmCallback(GalleryFragment.Callback mCallback) {
        this.mCallback = mCallback;
    }

    private GalleryFragment.Callback mCallback;


    private Context context;
    ArrayList<FaceItem> faceItemList = new ArrayList<>();

    public void setIdentifyItems(ArrayList<IdentifyItem> identifyItems) {
        this.identifyItems = identifyItems;
        invalidate();

    }

    ArrayList<IdentifyItem> identifyItems = new ArrayList<>();
    Bitmap bitmap;
    String imageFilePath;//图片文件的源地址

    int cx = 0;//将图像移到中间的偏移量
    int cy = 0;
    private int mWidth;
    private int mHeight;
    private float mAngle;

    private FaceItem outerFaceItem;
    float centerX;
    float centerY;
    float radius;
    Long clickTime;//点击时的时间戳


    public void setBottomLayoutOperation(FrameLayout bottomLayoutOperation) {
        this.bottomLayoutOperation = bottomLayoutOperation;
    }

    private FrameLayout bottomLayoutOperation;

    private DialogFragmentCreater dialogFragmentCreater;//生成对话框，显示人脸的操作选项

    private Bitmap faceBitmap;//人脸部位的bitmap
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

                canvas.clipRect(faceItem.getX(), faceItem.getY(), faceItem.getX() + faceItem.getX() + faceItem.getWidth(), faceItem.getY() + faceItem.getHeight());
                canvas.save();

                faceBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
                canvas.restore();
            }
        }
    }

    /**
     * 点击用户的脸部
     * @param x
     * @param y
     */
    private void whichCircle(float x, float y) {
        if(outerFaceItem!=null) {
            if (centerX + outerFaceItem.getX() + cx - radius <= x && x <= centerX + outerFaceItem.getX() + cx + radius) {
                if (centerY + outerFaceItem.getY() + cy - radius <= y && y <= centerY + outerFaceItem.getY() + cy + radius) {
                    Toast.makeText(context, "点自己", Toast.LENGTH_SHORT).show();
                    if (dialogFragmentCreater != null&&identifyItems!=null&&identifyItems.size()>0) {

                        dialogFragmentCreater.setIdentifyItems(identifyItems);
                        Log.e("Retrofit","jwjw"+identifyItems.toString());
                        dialogFragmentCreater.setOnChooseFaceCallBack(new DialogFragmentCreater.OnChooseFaceCallBack() {
                            @Override
                            public void onItemClickListener(AdapterView<?> adapterView, View view, final int i, long l) {
                                dialogFragmentCreater.dismiss();
                                new AsyncTask<Void, Void, Void>() {
                                    @Override
                                    protected void onPostExecute(Void aVoid) {
                                        super.onPostExecute(aVoid);
                                        dialogFragmentCreater.setOnFaceOperationCallBack(new DialogFragmentCreater.OnFaceOperationCallBack() {
                                            @Override
                                            public void onClick(View view) {
                                                switch (view.getId()) {
                                                    case R.id.tv_call:
                                                        if (identifyItems != null && identifyItems.size() > 0) {
                                                            String phoneStr = GetTagUtil.getPhoneNumber(identifyItems.get(0).getTag());
                                                            UIUtil.takePhoneCall(context, phoneStr, 0);
                                                        }
                                                        break;
                                                    case R.id.tv_edit_info:
                                                        EditFaceUserInfoActivity.startActivity(context,imageFilePath, identifyItems,i);
                                                        break;
                                                    case R.id.tv_send_message:
                                                        if (identifyItems != null && identifyItems.size() > 0) {
                                                            String phoneStr = GetTagUtil.getPhoneNumber(identifyItems.get(0).getTag());
                                                            UIUtil.sendMessage(context, phoneStr, null, 0);
                                                        }
                                                        break;
                                                    default:
                                                        break;
                                                }
                                            }
                                        });
                                        dialogFragmentCreater.showDialog(context, DialogFragmentCreater.DIALOG_FACE_OPERATION);

                                    }

                                    @Override
                                    protected Void doInBackground(Void... voids) {

                                        try {
                                            Thread.sleep(100);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        return null;
                                    }
                                }.execute();
                            }
                            @Override
                            public void onClick(View view) {
                                switch (view.getId())
                                {
                                    case R.id.tv_from_group_name:
                                        context.startActivity(new Intent(context, SetIdentifyGroupIdActivity.class));
                                        break;
                                    case R.id.tv_add_new_person:
                                        AddNewPersonActivity.startActivity(context,imageFilePath, identifyItems);
                                        break;
                                }
                            }
                        });
                        dialogFragmentCreater.showDialog(context,DialogFragmentCreater.DIALOG_CHOOSE_FACE);
                    }else {
                        Toast.makeText(context,"没有可匹配的脸部信息",Toast.LENGTH_SHORT).show();
                    }

                } else {
                    mCallback.onTouchImageView();
                    bottomLayoutOperation.setVisibility(bottomLayoutOperation.getVisibility()==VISIBLE?GONE:VISIBLE);
                }
            } else {
                bottomLayoutOperation.setVisibility(bottomLayoutOperation.getVisibility()==VISIBLE?GONE:VISIBLE);
                mCallback.onTouchImageView();
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                clickTime = System.currentTimeMillis();
                break;
            case MotionEvent.ACTION_UP:
                if (Math.abs(clickTime - System.currentTimeMillis()) < MAX_CLICK_DURATION) {
                    whichCircle(event.getX(), event.getY());
                    return true;
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    public void setCanvasRes(Bitmap bitmap, ArrayList<FaceItem> faceList,String imageFilePath) {
        this.bitmap = bitmap;
        this.faceItemList = faceList;
        this.imageFilePath = imageFilePath;
        invalidate();
    }

    public void setImageFilePath(String imageFilePath) {
        this.imageFilePath = imageFilePath;
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

    public void setDialogFragmentCreater(DialogFragmentCreater dialogFragmentCreater)
    {
        this.dialogFragmentCreater = dialogFragmentCreater;
    }

}

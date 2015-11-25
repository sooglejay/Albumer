package sooglejay.youtu.fragment;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import sooglejay.youtu.R;
import sooglejay.youtu.utils.ImageUtils;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher.OnMatrixChangedListener;
import uk.co.senab.photoview.PhotoViewAttacher.OnPhotoTapListener;

public class GalleryFragment extends BaseFragment {

    private PhotoView imageView;
    private FrameLayout progressContainer;
    private int position;
    private String url;

    private OnRectfChangeListener onRectfChangeListener;


    private Activity activity ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return  inflater.inflate(R.layout.fragment_gallery, container, false);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        onRectfChangeListener = (OnRectfChangeListener) this.getActivity();
        position = getArguments().getInt("position", 0);
        url = getArguments().getString("url","");
        activity = this.getActivity();
        imageView = (PhotoView) view.findViewById(R.id.iv_photo);
        progressContainer = (FrameLayout) view.findViewById(R.id.progress_container);
        setPhotoView();

    }


    private void setPhotoView() {
        imageView.setOnMatrixChangeListener(new OnMatrixChangedListener() {

            @Override
            public void onMatrixChanged(RectF rectF) {
                // TODO Auto-generated method stub
                onRectfChangeListener.onRectfChanged(rectF);
            }
        });
        ImageLoader.getInstance().displayImage(url, imageView,ImageUtils.getOptions(), new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {
                progressContainer.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {
                progressContainer.setVisibility(View.GONE);

            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                progressContainer.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingCancelled(String s, View view) {
            }
        });
        imageView.setOnPhotoTapListener(new OnPhotoTapListener() {

            @Override
            public void onPhotoTap(View arg0, float arg1, float arg2) {
                // TODO Auto-generated method stub

            }
        });
        imageView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
            }
        });
    }

    private void closeFullScreen() {
        getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
    }


    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();

    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    public interface OnRectfChangeListener {
        void onRectfChanged(RectF rectF);
    }

}
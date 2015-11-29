package sooglejay.youtu.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.ListPopupWindow;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import sooglejay.youtu.R;
import sooglejay.youtu.ui.GalleryActivity;
import sooglejay.youtu.widgets.TitleBar;
import sooglejay.youtu.widgets.imagepicker.adapter.FolderAdapter;
import sooglejay.youtu.widgets.imagepicker.adapter.ImageGridAdapter;
import sooglejay.youtu.widgets.imagepicker.bean.Folder;
import sooglejay.youtu.widgets.imagepicker.bean.Image;
import sooglejay.youtu.widgets.imagepicker.utils.FileUtils;
import sooglejay.youtu.widgets.imagepicker.utils.TimeUtils;

/**
 * Created by JammyQtheLab on 2015/11/24.
 */
public class AlbumFragment extends BaseFragment {

    private static final String TAG = "AlbumFragment";

    // 不同loader定义
    private static final int LOADER_ALL = 0;
    private static final int LOADER_CATEGORY = 1;
    // 请求加载系统照相机
    private static final int REQUEST_CAMERA = 100;

    // 文件夹数据
    private ArrayList<Folder> mResultFolder = new ArrayList<>();


//    ArrayList<String> urls = new ArrayList<>();//用户点击一个图片文件的时候，需要知道该图片所在的目录

    // 图片Grid
    private GridView mGridView;

    private ImageGridAdapter mImageAdapter;
    private FolderAdapter mFolderAdapter;
    private Folder outerFolder;
    private ListPopupWindow mFolderPopupWindow;

    // 时间线
    private TextView mTimeLineText;
    // 预览按钮
    private Button mPreviewBtn;


    private boolean hasFolderGened = false;
    private boolean hasImageLoaderStarted = false;
    private boolean mIsShowCamera = false;

    private int mGridWidth, mGridHeight;

    private File mTmpFile;


    private TitleBar titleBar;
    private TextView title_tv;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_album, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mImageAdapter = new ImageGridAdapter(getActivity(), mIsShowCamera);
        // 是否显示选择指示器
        mImageAdapter.showSelectIndicator(false);//不显示指示器

        titleBar = (TitleBar) view.findViewById(R.id.title_bar);
        titleBar.initTitleBarInfo("相册", -1, -1, "", "目录");
        titleBar.setOnTitleBarClickListener(new TitleBar.OnTitleBarClickListener() {
            @Override
            public void onLeftButtonClick(View v) {

            }

            @Override
            public void onRightButtonClick(View v) {

                if (mFolderPopupWindow == null) {
                    createPopupFolderList(mGridWidth, mGridHeight);
                }
                if (mFolderPopupWindow.isShowing()) {
                    mFolderPopupWindow.dismiss();
                } else {
                    mFolderPopupWindow.show();
                    int index = mFolderAdapter.getSelectIndex();
                    index = index == 0 ? index : index - 1;
                    mFolderPopupWindow.getListView().setSelection(index);
                }
            }
        });
        mTimeLineText = (TextView) view.findViewById(R.id.timeline_area);
        // 初始化，先隐藏当前timeline
        mTimeLineText.setVisibility(View.GONE);

        title_tv = (TextView) view.findViewById(R.id.title_tv);
        // 初始化，加载所有图片
        title_tv.setText(R.string.folder_all);

        mGridView = (GridView) view.findViewById(R.id.grid);
        mGridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int state) {
                final Picasso picasso = Picasso.with(getActivity());
                if (state == SCROLL_STATE_IDLE || state == SCROLL_STATE_TOUCH_SCROLL) {
                    picasso.resumeTag(getActivity());
                } else {
                    picasso.pauseTag(getActivity());
                }
                if (state == SCROLL_STATE_IDLE) {
                    // 停止滑动，日期指示器消失
                    mTimeLineText.setVisibility(View.GONE);
                } else if (state == SCROLL_STATE_FLING) {
                    mTimeLineText.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (mTimeLineText.getVisibility() == View.VISIBLE) {
                    int index = firstVisibleItem + 1 == view.getAdapter().getCount() ? view.getAdapter().getCount() - 1 : firstVisibleItem + 1;
                    Image image = (Image) view.getAdapter().getItem(index);
                    if (image != null) {
                        mTimeLineText.setText(TimeUtils.formatPhotoDate(image.path));
                    }
                }
            }
        });
        mGridView.setAdapter(mImageAdapter);
        mGridView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            public void onGlobalLayout() {
                final int width = mGridView.getWidth();
                final int height = mGridView.getHeight();
                mGridWidth = width;
                mGridHeight = height;
                final int desireSize = getResources().getDimensionPixelOffset(R.dimen.image_size);
                final int numCount = width / desireSize;
                final int columnSpace = getResources().getDimensionPixelOffset(R.dimen.space_size);
                int columnWidth = (width - columnSpace * (numCount - 1)) / numCount;
                mImageAdapter.setItemSize(columnWidth);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    mGridView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    mGridView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            }
        });
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Image image = (Image) adapterView.getAdapter().getItem(i);
                selectImageFromGrid(image, i);
            }
        });
        mFolderAdapter = new FolderAdapter(getActivity());
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && !hasImageLoaderStarted) {
            hasImageLoaderStarted = true;
            Log.e("sooglejay", "isVisibleToUser");
            getActivity().getSupportLoaderManager().initLoader(LOADER_ALL, null, mLoaderCallback);
        }
    }


    private int outIndex = 0;
    /**
     * 创建弹出的ListView
     */
    private void createPopupFolderList(int width, int height) {
        mFolderPopupWindow = new ListPopupWindow(getActivity());
        mFolderPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mFolderPopupWindow.setAdapter(mFolderAdapter);
        mFolderPopupWindow.setContentWidth(width);
        mFolderPopupWindow.setWidth(width);
        mFolderPopupWindow.setHeight(height * 5 / 8);
        mFolderPopupWindow.setAnchorView(titleBar);
        mFolderPopupWindow.setModal(true);
        mFolderPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                mFolderAdapter.setSelectIndex(i);
                outIndex = i;//传递索引

                final int index = i;
                final AdapterView v = adapterView;

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mFolderPopupWindow.dismiss();
                        if (index == 0) {//全部文件
                            getActivity().getSupportLoaderManager().restartLoader(LOADER_ALL, null, mLoaderCallback);
                            title_tv.setText(R.string.folder_all);
                        } else {
                            outerFolder = (Folder) v.getAdapter().getItem(index);
                            if (null != outerFolder) {
                                mImageAdapter.setData(outerFolder.images);
                                title_tv.setText(outerFolder.name);
                            }
                        }
                        //不显示拍照的图片
                        mImageAdapter.setShowCamera(mIsShowCamera);
                        // 滑动到最初始位置
                        mGridView.smoothScrollToPosition(0);
                    }
                }, 100);

            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 相机拍照完成后，返回图片路径
        if (requestCode == REQUEST_CAMERA) {
            if (resultCode == Activity.RESULT_OK) {

            } else {

            }
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.d(TAG, "on change");

        if (mFolderPopupWindow != null) {
            if (mFolderPopupWindow.isShowing()) {
                mFolderPopupWindow.dismiss();
            }
        }

        mGridView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            public void onGlobalLayout() {

                final int height = mGridView.getHeight();

                final int desireSize = getResources().getDimensionPixelOffset(R.dimen.image_size);
                Log.d(TAG, "Desire Size = " + desireSize);
                final int numCount = mGridView.getWidth() / desireSize;
                Log.d(TAG, "Grid Size = " + mGridView.getWidth());
                Log.d(TAG, "num count = " + numCount);
                final int columnSpace = getResources().getDimensionPixelOffset(R.dimen.space_size);
                int columnWidth = (mGridView.getWidth() - columnSpace * (numCount - 1)) / numCount;
                mImageAdapter.setItemSize(columnWidth);

                if (mFolderPopupWindow != null) {
                    mFolderPopupWindow.setHeight(height * 5 / 8);
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    mGridView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    mGridView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            }
        });
        super.onConfigurationChanged(newConfig);
    }

    /**
     * 选择相机
     */
    private void showCameraAction() {
        // 跳转到系统照相机
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // 设置系统相机拍照后的输出路径
            // 创建临时文件
            mTmpFile = FileUtils.createTmpFile(getActivity());
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mTmpFile));
            startActivityForResult(cameraIntent, REQUEST_CAMERA);
        } else {
            Toast.makeText(getActivity(), R.string.msg_no_camera, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 选择图片操作
     *
     * @param image
     */
    private void selectImageFromGrid(Image image, int position) {
        if(outIndex == 0 )//如果是全部的文件
        {
            String folderNameStr="";
            int index = 0 ;
            if(image!=null)
            {
                folderNameStr = new File(image.path).getParentFile().getName();
            }
            if(mResultFolder!=null)
            {
                ArrayList<String> urls = new ArrayList<>();
                for (Folder folder :mResultFolder)
                {
                    if(folder.name.equals(folderNameStr))
                    {
                        int j = 0;
                        for (Image image1 : folder.images) {
                            if(image1.equals(image))
                            {
                                index = j;
                            }
                            j++;
                            urls.add("file://" + image1.path);
                        }
                        break;
                    }
                }
                GalleryActivity.startActivity(getActivity(),folderNameStr, index, urls);
            }
        }
        else if (image != null && outerFolder != null) {
            ArrayList<String> urls = new ArrayList<>();
            for (Image image1 : outerFolder.images) {
                urls.add("file://" + image1.path);
            }
            GalleryActivity.startActivity(getActivity(),outerFolder.name, position, urls);
        }
    }

    private LoaderManager.LoaderCallbacks<Cursor> mLoaderCallback = new LoaderManager.LoaderCallbacks<Cursor>() {

        private final String[] IMAGE_PROJECTION = {
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.DATE_ADDED,
                MediaStore.Images.Media._ID};

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            if (id == LOADER_ALL) {
                CursorLoader cursorLoader = new CursorLoader(getActivity(),
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION,
                        null, null, IMAGE_PROJECTION[2] + " DESC");
                return cursorLoader;
            } else if (id == LOADER_CATEGORY) {
                CursorLoader cursorLoader = new CursorLoader(getActivity(),
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION,
                        IMAGE_PROJECTION[0] + " like '%" + args.getString("path") + "%'", null, IMAGE_PROJECTION[2] + " DESC");
                return cursorLoader;
            }

            return null;
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            if (data != null) {
                List<Image> images = new ArrayList<>();
                int count = data.getCount();
                if (count > 0) {
                    data.moveToFirst();
                    do {
                        String path = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[0]));
                        String name = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[1]));
                        long dateTime = data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[2]));
                        Image image = new Image(path, name, dateTime);
                        images.add(image);
                        if (!hasFolderGened) {
                            // 获取文件夹名称
                            File imageFile = new File(path);
                            File folderFile = imageFile.getParentFile();
                            Folder folder = new Folder();
                            folder.name = folderFile.getName();
                            folder.path = folderFile.getAbsolutePath();
                            folder.cover = image;
                            if (!mResultFolder.contains(folder)) {
                                List<Image> imageList = new ArrayList<>();
                                imageList.add(image);
                                folder.images = imageList;
                                mResultFolder.add(folder);
                            } else {
                                // 更新
                                Folder f = mResultFolder.get(mResultFolder.indexOf(folder));
                                f.images.add(image);
                            }
                        }

                    } while (data.moveToNext());
                    mFolderAdapter.setData(mResultFolder);
                    mImageAdapter.setData(images);
                    if (mResultFolder != null && mResultFolder.size() > 0) {
                        outerFolder = mResultFolder.get(0);


//                        //先获取所有文件的文件名，这个操作在 用户选择所有文件，并点击一个图片文件时有效
//                        for (Folder folder :mResultFolder)
//                        {
//                            for (Image image1 : folder.images) {
//                                urls.add("file://" + image1.path);
//                            }
//                        }
                    }
                    hasFolderGened = true;
                }
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    };


}

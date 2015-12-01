package sooglejay.youtu.utils;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import sooglejay.youtu.api.detectface.FaceItem;
import sooglejay.youtu.api.faceidentify.IdentifyItem;
import sooglejay.youtu.bean.GroupBean;

/**
 * Created by JammyQtheLab on 2015/11/27.
 */
public class CacheUtil {
    private static final String CACHE_DETECT_FILE_NAME = "object_detect.log";
    private static final String CACHE_IDENTIFY_FILE_NAME = "object_identify.log";
    private static final String CACHE_GROUP_IDS_FILE_NAME = "group_ids.log";//可选的用户组id 列表
    private String cacheFilePath;
    public CacheUtil(Context context)
    {
        cacheFilePath = ImageUtils.getImageFolderPath(context);
    }


    public  void saveDetectedObjectToFile(Context context, HashMap<String, ArrayList<FaceItem>> mBitMapCache) {
        FileOutputStream fileOutputStream = null;
        ObjectOutputStream objectOutputStream = null;
        File dir = new File(cacheFilePath);
        if(!dir.exists())
        {
            dir.mkdirs();
        }
        try {
            fileOutputStream = new FileOutputStream(dir.toString() + File.separator+ CACHE_DETECT_FILE_NAME);
            objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(mBitMapCache);
            Log.e("jwjw", "DetectedObject 写入文件成功："+mBitMapCache.toString());

        } catch (IOException e) {
            e.printStackTrace();
            Log.e("jwjw","DetectedObject 写入文件失败："+ e.toString());

        } finally {
            if (fileOutputStream != null && objectOutputStream != null)
                try {
                    fileOutputStream.close();
                    objectOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }

    public  HashMap<String,ArrayList<FaceItem>> getDetectedObjectFromFile() {
        FileInputStream fileInputStream = null;
        ObjectInputStream objectInputStream = null;
        HashMap<String, ArrayList<FaceItem>> object = null;
        File dir = new File(cacheFilePath);
        if(!dir.exists())
        {
            dir.mkdirs();
        }
        try {
            fileInputStream = new FileInputStream(dir.toString() + File.separator+ CACHE_DETECT_FILE_NAME);
            objectInputStream = new ObjectInputStream(fileInputStream);
            object = (HashMap<String, ArrayList<FaceItem>>) objectInputStream.readObject();
            Log.e("jwjw", "DetectedObject 读取文件成功："+object.toString());
            return object;
        } catch (IOException | ClassNotFoundException e) {
            Log.e("jwjw", "DetectedObject 读取文件失败："+ e.toString());
            e.printStackTrace();
        } finally {
            if (fileInputStream != null && objectInputStream != null)
                try {
                    fileInputStream.close();
                    objectInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            return object;
        }
    }


    public  void saveIdentifiedObjectToFile(Context context, HashMap<String, ArrayList<IdentifyItem>> mBitMapCache) {
        FileOutputStream fileOutputStream = null;
        ObjectOutputStream objectOutputStream = null;
        File dir = new File(cacheFilePath);
        if(!dir.exists())
        {
            dir.mkdirs();
        }
        try {
            fileOutputStream = new FileOutputStream(dir.toString() + File.separator+ CACHE_IDENTIFY_FILE_NAME);
            objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(mBitMapCache);
            Log.e("jwjw", "IdentifiedObject 写入文件成功："+mBitMapCache.toString());

        } catch (IOException e) {
            e.printStackTrace();
            Log.e("jwjw","IdentifiedObject 写入文件失败："+ e.toString());

        } finally {
            if (fileOutputStream != null && objectOutputStream != null)
                try {
                    fileOutputStream.close();
                    objectOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }

    public  HashMap<String,ArrayList<IdentifyItem>> getIdentifiedObjectFromFile() {
        FileInputStream fileInputStream = null;
        ObjectInputStream objectInputStream = null;
        HashMap<String, ArrayList<IdentifyItem>> object = null;
        File dir = new File(cacheFilePath);
        if(!dir.exists())
        {
            dir.mkdirs();
        }
        try {
            fileInputStream = new FileInputStream(dir.toString() + File.separator+ CACHE_IDENTIFY_FILE_NAME);
            objectInputStream = new ObjectInputStream(fileInputStream);
            object = (HashMap<String, ArrayList<IdentifyItem>>) objectInputStream.readObject();
            Log.e("jwjw", "IdentifiedObject 读取文件成功："+object.toString());
            return object;
        } catch (IOException | ClassNotFoundException e) {
            Log.e("jwjw", "IdentifiedObject 读取文件失败："+ e.toString());
            e.printStackTrace();
        } finally {
            if (fileInputStream != null && objectInputStream != null)
                try {
                    fileInputStream.close();
                    objectInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            return object;
        }
    }


    public  void saveGroupIdsToFile(Context context,  ArrayList<GroupBean> mBitMapCache) {
        FileOutputStream fileOutputStream = null;
        ObjectOutputStream objectOutputStream = null;
        File dir = new File(cacheFilePath);
        if(!dir.exists())
        {
            dir.mkdirs();
        }
        try {
            fileOutputStream = new FileOutputStream(dir.toString() + File.separator+ CACHE_GROUP_IDS_FILE_NAME);
            objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(mBitMapCache);
            Log.e("jwjw", "group ids 写入文件成功："+mBitMapCache.toString());

        } catch (IOException e) {
            e.printStackTrace();
            Log.e("jwjw","group ids 写入文件失败："+ e.toString());

        } finally {
            if (fileOutputStream != null && objectOutputStream != null)
                try {
                    fileOutputStream.close();
                    objectOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }

    public ArrayList<GroupBean> getAvailableGroupIdsFromFile() {
        FileInputStream fileInputStream = null;
        ObjectInputStream objectInputStream = null;
        ArrayList<GroupBean> object = null;
        File dir = new File(cacheFilePath);
        if(!dir.exists())
        {
            dir.mkdirs();
        }
        try {
            fileInputStream = new FileInputStream(dir.toString() + File.separator+ CACHE_GROUP_IDS_FILE_NAME);
            objectInputStream = new ObjectInputStream(fileInputStream);
            object = (ArrayList<GroupBean>) objectInputStream.readObject();
            Log.e("jwjw", "group ids 读取文件成功："+object.toString());
            return object;
        } catch (IOException | ClassNotFoundException e) {
            Log.e("jwjw", "group ids 读取文件失败："+ e.toString());
            e.printStackTrace();
        } finally {
            if (fileInputStream != null && objectInputStream != null)
                try {
                    fileInputStream.close();
                    objectInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            return object;
        }
    }
}

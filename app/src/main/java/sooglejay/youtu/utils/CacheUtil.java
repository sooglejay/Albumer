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
import java.util.List;

import sooglejay.youtu.api.detectface.FaceItem;

/**
 * Created by JammyQtheLab on 2015/11/27.
 */
public class CacheUtil {
    private static final String CACHE_FILE_NAME = "object.log";
    private String filePath;
    public CacheUtil(Context context)
    {
        filePath = ImageUtils.getImageFolderPath(context);
    }


    public  void saveObjectToFile(Context context, HashMap<String,ArrayList<FaceItem>> mBitMapCache) {
        FileOutputStream fileOutputStream = null;
        ObjectOutputStream objectOutputStream = null;
        File dir = new File(filePath);
        if(!dir.exists())
        {
            dir.mkdirs();
        }
        try {
            fileOutputStream = new FileOutputStream(dir.toString() + File.separator+CACHE_FILE_NAME);
            objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(mBitMapCache);
            Log.e("jwjw", "写入文件成功："+mBitMapCache.toString());

        } catch (IOException e) {
            e.printStackTrace();
            Log.e("jwjw","写入文件失败："+ e.toString());

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

    public  HashMap<String,ArrayList<FaceItem>> getObjectFromFile() {
        FileInputStream fileInputStream = null;
        ObjectInputStream objectInputStream = null;
        HashMap<String, ArrayList<FaceItem>> object = null;
        File dir = new File(filePath);
        if(!dir.exists())
        {
            dir.mkdirs();
        }
        try {
            fileInputStream = new FileInputStream(dir.toString() + File.separator+CACHE_FILE_NAME);
            objectInputStream = new ObjectInputStream(fileInputStream);
            object = (HashMap<String, ArrayList<FaceItem>>) objectInputStream.readObject();
            Log.e("jwjw", "读取文件成功："+object.toString());
            return object;
        } catch (IOException | ClassNotFoundException e) {
            Log.e("jwjw", "读取文件失败："+ e.toString());
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

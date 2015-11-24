package sooglejay.youtu.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.File;

import sooglejay.youtu.R;
import sooglejay.youtu.widgets.youtu.Youtu;


public class MainActivity extends Activity {
    public static final String APP_ID = "1001633";
    public static final String SECRET_ID = "AKIDy5bjHuCGAEM1cQsbQvxWaFBLb08VDpcS";
    public static final String SECRET_KEY = "VRnkyrPoey4x8jtrtt0FdU2GjeCT5iLG";
    Activity activity;
    private TextView textView2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        textView2 = (TextView)findViewById(R.id.textView2);

         activity = this;

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String path = getImageFolderPath(activity) + File.separator +"test.jpg";
                    Youtu faceYoutu = new Youtu(APP_ID, SECRET_ID, SECRET_KEY,Youtu.API_YOUTU_END_POINT);
                    JSONObject respose;
//                    respose= faceYoutu.FaceCompareUrl("http://open.youtu.qq.com/content/img/slide-1.jpg","http://open.youtu.qq.com/content/img/slide-1.jpg");
                        respose = faceYoutu.DetectFace(path, 1);
                    //get respose

                    System.out.println(respose);
                    Log.e("jwjw","haha try"+respose.toString());
                } catch (Exception e) {
                    Log.e("jwjw", "haha catch"+e.toString()) ;
                }
            }
        }).start();

    }

    public static String getImageFolderPath(Context context) {
        String packPath = "";

        try {
            packPath = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath();

        } catch (Exception e) {
            packPath = context.getFilesDir() + File.separator + "Pictures";

        } finally {
            return packPath;
        }
    }
}

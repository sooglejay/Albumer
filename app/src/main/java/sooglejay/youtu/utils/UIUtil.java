package sooglejay.youtu.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * Created by JammyQtheLab on 2015/11/30.
 */
public class UIUtil {
    /**
     * 拨打电话
     * @param context
     * @param phoneNumberString
     * @param requestCode onActivityResult 需要的code
     */
    public static void takePhoneCall(Activity context,String phoneNumberString,int requestCode)
    {
        if(!TextUtils.isEmpty(phoneNumberString))
        {
            Intent phoneIntent = new Intent("android.intent.action.CALL", Uri.parse("tel:" + phoneNumberString));
            context.startActivityForResult(phoneIntent, requestCode);
        }
    }


    public static void sendMessage(Activity context,String phoneNumberString,String messageStr,int requestCode)
    {

        if(TextUtils.isEmpty(phoneNumberString))
        {
            Toast.makeText(context,"电话号码不能为空！",Toast.LENGTH_SHORT).show();
        }else {
            Uri uri = Uri.parse("smsto:" + phoneNumberString);
            Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
            intent.putExtra("sms_body", messageStr);
            context.startActivityForResult(intent, requestCode);
        }

    }

}

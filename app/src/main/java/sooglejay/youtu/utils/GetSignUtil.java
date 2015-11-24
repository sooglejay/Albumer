package sooglejay.youtu.utils;

import android.text.TextUtils;

import sooglejay.youtu.constant.NetWorkConstant;
import sooglejay.youtu.widgets.youtu.sign.YoutuSign;

/**
 * Created by sooglejay on 2015/11/19.
 */
public class GetSignUtil {

    private static GetSignUtil instance = null;

    static {
        instance = new GetSignUtil();
    }

    private GetSignUtil() {
    }

    public static GetSignUtil getInstance() {
        return instance;
    }

    private static String signStr;

    public String getSignStr() {
        if (TextUtils.isEmpty(signStr)) {
            StringBuffer mySign = new StringBuffer("");
            YoutuSign.appSign(NetWorkConstant.APP_ID, NetWorkConstant.SECRET_ID, NetWorkConstant.SECRET_KEY,
                    System.currentTimeMillis() / 1000 + NetWorkConstant.EXPIRED_SECONDS,
                    "", mySign);
            return mySign.toString();
        } else {
            return signStr;
        }
    }

}

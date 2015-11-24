package sooglejay.youtu.api.detectface;

import android.content.Context;

import retrofit.RestAdapter;
import sooglejay.youtu.api.YouTuApi;
import sooglejay.youtu.model.NetCallback;
import sooglejay.youtu.utils.GetSignUtil;
import sooglejay.youtu.utils.RetrofitUtil;

/**
 1)接口
 http://api.youtu.qq.com/youtu/api/detectface
 2)
 检测给定图片(Image)中的所有人脸(FaceItem)的位置和相应的面部属性。位置包括(x, y, w, h)，面部属性包括性别(gender), 年龄(age), 表情(expression), 魅力(beauty), 眼镜(glass)和姿态(pitch，roll，yaw).
 */
public class DetectFaceUtil extends RetrofitUtil {

    /**
    * @param mContext
     * @param app_id
     * @param image
     * @param mode
     * @param callback 接口    http://api.youtu.qq.com/youtu/api/detectface
     *                 必须	app_id	String	App的 API ID
     *                 可选	image	String(Bytes)	base64编码的二进制图片数据
     *                 可选	url	String	图片的url, image 和url只提供一个就可以了,如果都提供,只使用url
     *                 可选	mode	Int	检测模式 0/1 正常/大脸模式
     */
    public static void detectFace(Context mContext,
                                  String app_id,
                                  String image,
                                  int mode,
                                  NetCallback<DetectFaceResponseBean> callback) {
        String sign = GetSignUtil.getInstance().getSignStr();
        RestAdapter restAdapter = getRestAdapter(mContext, sign);
        YouTuApi git = restAdapter.create(YouTuApi.class);
        DetectFaceRequestBean bean = new DetectFaceRequestBean();
        bean.setApp_id(app_id);
        bean.setImage(image);
        bean.setMode(mode);
        git.detectface(bean, callback);
    }
}

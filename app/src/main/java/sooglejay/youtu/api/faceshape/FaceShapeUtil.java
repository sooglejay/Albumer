package sooglejay.youtu.api.faceshape;

import android.content.Context;

import retrofit.RestAdapter;
import sooglejay.youtu.api.YouTuApi;
import sooglejay.youtu.model.NetCallback;
import sooglejay.youtu.utils.GetSignUtil;
import sooglejay.youtu.utils.RetrofitUtil;

/**
 * Created by sooglejay on 2015/11/19.
 */
public class FaceShapeUtil extends RetrofitUtil {

    /**
     *
     1)接口
     http://api.youtu.qq.com/youtu/api/faceshape
     2)
     对请求图片进行五官定位，计算构成人脸轮廓的88个点，包括眉毛（左右各8点）、眼睛（左右各8点）、鼻子（13点）、嘴巴（22点）、脸型轮廓（21点）。各个部分点的顺序如下图所示
     * @param mContext
     * @param app_id
     * @param image
     * @param url
     * @param mode
     * @param callback
     *   参数
        参数名	类型	参数说明
        必须	app_id	String	App的 API ID
        可选	image	String(Bytes)	base64编码的二进制图片数据
        可选	url	String	图片的url, image 和url只提供一个就可以了,如果都提供,只使用url
        可选	mode	Int	检测模式 0/1 正常/大脸模式

     */
    public static void faceShape(Context mContext,
                                  String app_id,
                                  String image,
                                  String url,
                                  int mode,
                                  NetCallback<FaceShapeResponseBean> callback) {
        String sign = GetSignUtil.getInstance().getSignStr();
        RestAdapter restAdapter = getRestAdapter(mContext, sign);
        YouTuApi git = restAdapter.create(YouTuApi.class);
        FaceShapeRequestBean bean = new FaceShapeRequestBean();
        bean.setApp_id(app_id);
        bean.setImage(image);
        bean.setUrl(url);
        bean.setMode(mode);
        git.faceshape(bean, callback);
    }
}

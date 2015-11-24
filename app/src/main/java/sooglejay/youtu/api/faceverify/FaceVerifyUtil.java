package sooglejay.youtu.api.faceverify;

import android.content.Context;

import retrofit.RestAdapter;
import sooglejay.youtu.api.YouTuApi;
import sooglejay.youtu.model.NetCallback;
import sooglejay.youtu.utils.GetSignUtil;
import sooglejay.youtu.utils.RetrofitUtil;

/**
 * Created by sooglejay on 2015/11/19.
 */
public class FaceVerifyUtil extends RetrofitUtil {

    /**
     * 接口
     * http://api.youtu.qq.com/youtu/api/faceverify
     * 2)
     * 描述
     * 给定一个Face和一个Person，返回是否是同一个人的判断以及置信度。
     * 3)
     * 参数
     * 参数名	类型	参数说明
     * 必须	app_id	String	App的 API ID
     * 必须	person_id	String	待验证的Person
     * 可选	image	String(Bytes)	base64编码的二进制图片数据
     * 可选	url	String	图片的url, image 和url只提供一个就可以了,如果都提供,只使用url
     *
     * @param mContext
     * @param app_id
     * @param person_id
     * @param image
     * @param url
     * @param callback
     */
    public static void faceVerify(Context mContext,
                                  String app_id,
                                  String person_id,
                                  String image,
                                  String url,
                                  NetCallback<FaceVerifyResponseBean> callback) {
        String sign = GetSignUtil.getInstance().getSignStr();
        RestAdapter restAdapter = getRestAdapter(mContext, sign);
        YouTuApi git = restAdapter.create(YouTuApi.class);
        FaceVerifyRequestBean bean = new FaceVerifyRequestBean();
        bean.setApp_id(app_id);
        bean.setImage(image);
        bean.setUrl(url);
        bean.setPerson_id(person_id);
        git.faceverify(bean, callback);
    }
}



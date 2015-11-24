package sooglejay.youtu.api.facecompare;

import android.content.Context;

import retrofit.RestAdapter;
import sooglejay.youtu.api.YouTuApi;
import sooglejay.youtu.model.NetCallback;
import sooglejay.youtu.utils.GetSignUtil;
import sooglejay.youtu.utils.RetrofitUtil;

/**
 * Created by sooglejay on 2015/11/19.
 */
public class FaceCompareUtil extends RetrofitUtil {

    /**
     *
     * @param mContext
     * @param urlA
     * @param urlB
     * @param app_id
     * @param callback
    接口    http://api.youtu.qq.com/youtu/api/facecompare
    必须	app_id	String	App的 API ID
    可选	imageA	String	使用base64编码的二进制图片数据A
    可选	imageB	String	使用base64编码的二进制图片数据B
    可选	urlA	String	A图片的url, image 和url只提供一个就可以了,如果都提供,只使用url
    可选	urlB	String	B图片的url, image 和url只提供一个就可以了,如果都提供,只使用url
    4)

     */
    public static void faceCompareUrl(Context mContext,
                                      String imageA,
                                      String imageB,
                                      String urlA,
                                      String urlB,
                                      String app_id,
                                      NetCallback<FaceCompareResponseBean> callback) {
        String sign = GetSignUtil.getInstance().getSignStr();
        RestAdapter restAdapter = getRestAdapter(mContext, sign);
        YouTuApi git = restAdapter.create(YouTuApi.class);
        FaceCompareRequestBean bean =  new FaceCompareRequestBean();
        bean.setApp_id(app_id);
        bean.setUrlA(urlA);
        bean.setUrlB(urlB);
        bean.setImageA(imageA);
        bean.setImageB(imageB);
        git.facecompare(bean, callback);
    }
}

package sooglejay.youtu.api.fuzzydetect;

import android.content.Context;

import retrofit.RestAdapter;
import sooglejay.youtu.api.YouTuApi;
import sooglejay.youtu.model.NetCallback;
import sooglejay.youtu.utils.GetSignUtil;
import sooglejay.youtu.utils.RetrofitUtil;

/**
 * Created by sooglejay on 2015/11/19.
 */
public class FuzzyDetectUtil extends RetrofitUtil {

    /**
     * 必须	app_id	String	App的 API ID
     * 可选	image	String	需要检测的图像base64编码，图像需要是JPG PNG BMP 其中之一的格式
     * 可选	url	String	图片可以下载的url, 如果url 和image 都提供, 仅使用ur
     * 可选	seq	String	标示识别请求的序列号
     *
     * @param mContext
     * @param app_id
     * @param image
     * @param url
     * @param seq
     * @param callback
     */
    public static void fuzzyDetect(Context mContext,
                                   String app_id,
                                   String image,
                                   String url,
                                   String seq,
                                   NetCallback<FuzzyDetectResponseBean> callback) {
        String sign = GetSignUtil.getInstance().getSignStr();
        RestAdapter restAdapter = getRestAdapter(mContext, sign);
        YouTuApi git = restAdapter.create(YouTuApi.class);
        FuzzyDetectRequestBean bean = new FuzzyDetectRequestBean();
        bean.setApp_id(app_id);
        bean.setImage(image);
        bean.setUrl(url);
        bean.setSeq(seq);
        git.fuzzydetect(bean, callback);
    }
}

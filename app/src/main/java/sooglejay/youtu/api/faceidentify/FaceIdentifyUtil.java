package sooglejay.youtu.api.faceidentify;

import android.content.Context;

import retrofit.RestAdapter;
import sooglejay.youtu.api.YouTuApi;
import sooglejay.youtu.model.NetCallback;
import sooglejay.youtu.utils.GetSignUtil;
import sooglejay.youtu.utils.RetrofitUtil;

/**
 * Created by sooglejay on 2015/11/19.
 */
public class FaceIdentifyUtil extends RetrofitUtil {


    /**
     * 参数
     * 参数名	类型	参数说明
     * 必须	app_id	String	App的 API ID
     * 必须	group_id	String	候选人组id
     * 可选	image	String(Bytes)	base64编码的二进制图片数据
     * 可选	url	String	图片的url, image 和url只提供一个就可以了,如果都提供,只使用url
     *
     * @param mContext
     * @param app_id
     * @param group_id
     * @param image
     * @param url
     * @param callback
     */
    public static void faceIdentify(Context mContext,
                                    String app_id,
                                    String group_id,
                                    String image,
                                    NetCallback<FaceIdentifyResponseBean> callback) {
        String sign = GetSignUtil.getInstance().getSignStr();
        RestAdapter restAdapter = getRestAdapter(mContext, sign);
        YouTuApi git = restAdapter.create(YouTuApi.class);
        FaceIdentifyRequestBean bean = new FaceIdentifyRequestBean();
        bean.setApp_id(app_id);
        bean.setImage(image);
        bean.setGroup_id(group_id);
        git.faceidentify(bean, callback);
    }
}

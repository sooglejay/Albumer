package sooglejay.youtu.api.getfaceinfo;

import android.content.Context;

import retrofit.RestAdapter;
import sooglejay.youtu.api.YouTuApi;
import sooglejay.youtu.model.NetCallback;
import sooglejay.youtu.utils.GetSignUtil;
import sooglejay.youtu.utils.RetrofitUtil;

/**
 * Created by sooglejay on 2015/11/19.
 */
public class GetFaceInfoUtil extends RetrofitUtil {


    /**
     * 参数
     * 参数名	类型	参数说明
     * 必须	app_id	String	App的 API ID
     * face_id	String	人脸id
     *
     * @param mContext
     * @param app_id
     * @param face_id
     * @param callback
     */
    public static void getFaceInfo(Context mContext,
                                   String app_id,
                                   String face_id,
                                   NetCallback<GetFaceInfoResponseBean> callback) {
        String sign = GetSignUtil.getInstance().getSignStr();
        RestAdapter restAdapter = getRestAdapter(mContext, sign);
        YouTuApi git = restAdapter.create(YouTuApi.class);
        GetFaceInfoRequestBean bean = new GetFaceInfoRequestBean();
        bean.setApp_id(app_id);
        bean.setFace_id(face_id);
        git.getfaceinfo(bean, callback);
    }
}

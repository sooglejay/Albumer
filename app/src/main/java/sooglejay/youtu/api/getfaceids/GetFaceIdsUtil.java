package sooglejay.youtu.api.getfaceids;

import android.content.Context;

import retrofit.RestAdapter;
import sooglejay.youtu.api.YouTuApi;
import sooglejay.youtu.model.NetCallback;
import sooglejay.youtu.utils.GetSignUtil;
import sooglejay.youtu.utils.RetrofitUtil;

/**
 * Created by sooglejay on 2015/11/19.
 */
public class GetFaceIdsUtil extends RetrofitUtil {

    /**
     * 参数
     * 参数名	类型	参数说明
     * 必须	app_id	String	App的 API ID
     * person_id	String	个体id
     *
     * @param mContext
     * @param app_id
     * @param person_id
     * @param callback
     */
    public static void getFaceIds(Context mContext,
                                  String app_id,
                                  String person_id,
                                  NetCallback<GetFaceIdsResponseBean> callback) {
        String sign = GetSignUtil.getInstance().getSignStr();
        RestAdapter restAdapter = getRestAdapter(mContext, sign);
        YouTuApi git = restAdapter.create(YouTuApi.class);
        GetFaceIdsRequestBean bean = new GetFaceIdsRequestBean();
        bean.setApp_id(app_id);
        bean.setPerson_id(person_id);
        git.getfaceids(bean, callback);
    }
}

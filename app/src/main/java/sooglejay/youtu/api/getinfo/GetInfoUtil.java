package sooglejay.youtu.api.getinfo;

import android.content.Context;

import retrofit.RestAdapter;
import sooglejay.youtu.api.YouTuApi;
import sooglejay.youtu.model.NetCallback;
import sooglejay.youtu.utils.GetSignUtil;
import sooglejay.youtu.utils.RetrofitUtil;

/**
 * Created by sooglejay on 2015/11/19.
 */
public class GetInfoUtil extends RetrofitUtil {

    /**
     * 接口
     * http://api.youtu.qq.com/youtu/api/getinfo
     * 2)
     * 描述
     * 获取一个Person的信息, 包括name, id, tag, 相关的face, 以及groups等信息。
     * 3)
     * 参数
     * 参数名	类型	参数说明
     * 必须	app_id	String	App的 API ID
     * person_id	String	待查询个体的ID
     *
     * @param mContext
     * @param app_id
     * @param person_id
     * @param callback
     */
    public static void getinfo(Context mContext,
                               String app_id,
                               String person_id,
                               NetCallback<GetInfoReponseBean> callback) {
        String sign = GetSignUtil.getInstance().getSignStr();
        RestAdapter restAdapter = getRestAdapter(mContext, sign);
        YouTuApi git = restAdapter.create(YouTuApi.class);
        GetInfoRequestBean bean = new GetInfoRequestBean();
        bean.setApp_id(app_id);
        bean.setPerson_id(person_id);
        git.getinfo(bean, callback);
    }
}

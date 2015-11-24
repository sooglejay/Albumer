package sooglejay.youtu.api.getpersonids;

import android.content.Context;

import retrofit.RestAdapter;
import sooglejay.youtu.api.YouTuApi;
import sooglejay.youtu.model.NetCallback;
import sooglejay.youtu.utils.GetSignUtil;
import sooglejay.youtu.utils.RetrofitUtil;

/**
 * Created by sooglejay on 2015/11/19.
 */


public class GetPersonIdsUtil extends RetrofitUtil {

    /**
     * /**
     * 信息查询
     * <p/>
     * • 获取人列表
     * 1)
     * 接口
     * http://api.youtu.qq.com/youtu/api/getpersonids
     * 2)
     * 描述
     * 获取一个组Group中所有person列表
     * 3)
     * 参数
     * 参数名	类型	参数说明
     * 必须	app_id	String	App的 API ID
     * group_id	String	组id
     * 4)
     * 返回值说明
     * 字段	类型	说明
     * person_ids	Array(String)	相应person的id列表
     * errorcode	Int	返回状态码
     * errormsg	String	返回错误消息
     *
     * @param mContext
     * @param app_id
     * @param group_id
     * @param callback
     */
    public static void getPersonIds(Context mContext,
                                    String app_id,
                                    String group_id,
                                    NetCallback<GetPersonIdsResponseBean> callback) {
        String sign = GetSignUtil.getInstance().getSignStr();
        RestAdapter restAdapter = getRestAdapter(mContext, sign);
        YouTuApi git = restAdapter.create(YouTuApi.class);
        GetPersonIdsRequestBean bean = new GetPersonIdsRequestBean();
        bean.setApp_id(app_id);
        bean.setGroup_id(group_id);
        git.getpersonids(bean, callback);
    }
}

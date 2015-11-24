package sooglejay.youtu.api.getgroupids;

import android.content.Context;

import retrofit.RestAdapter;
import sooglejay.youtu.api.YouTuApi;
import sooglejay.youtu.model.NetCallback;
import sooglejay.youtu.utils.GetSignUtil;
import sooglejay.youtu.utils.RetrofitUtil;

/**
 * Created by sooglejay on 2015/11/19.
 */
public class GetGroupIdsUtil extends RetrofitUtil {


    /**
     * 信息查询
     * <p/>
     * • 获取组列表
     * 1)
     * 接口
     * http://api.youtu.qq.com/youtu/api/getgroupids
     * 2)
     * 描述
     * 获取一个AppId下所有group列表
     * 3)
     * 参数
     * 参数名	类型	参数说明
     * 必须	app_id	String	App的 API ID
     * 4)
     * 返回值说明
     * 字段	类型	说明
     * group_ids	Array(String)	相应app_id的group_id列表
     * errorcode	Int	返回状态码
     * errormsg	String	返回错误消息
     *
     * @param mContext
     * @param app_id
     * @param callback
     */
    public static void getGroupIds(Context mContext,
                                   String app_id,
                                   NetCallback<GetGroupIdsResponseBean> callback) {
        String sign = GetSignUtil.getInstance().getSignStr();
        RestAdapter restAdapter = getRestAdapter(mContext, sign);
        YouTuApi git = restAdapter.create(YouTuApi.class);
        GetGroupIdsRequestBean bean = new GetGroupIdsRequestBean();
        bean.setApp_id(app_id);
        git.getgroupids(bean, callback);
    }
}

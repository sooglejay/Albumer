package sooglejay.youtu.api.setinfo;

import android.content.Context;

import retrofit.RestAdapter;
import sooglejay.youtu.api.YouTuApi;
import sooglejay.youtu.model.NetCallback;
import sooglejay.youtu.utils.GetSignUtil;
import sooglejay.youtu.utils.RetrofitUtil;

/**
 * Created by sooglejay on 2015/11/19.
 */
public class SetInfoUtil extends RetrofitUtil {

    /**
     * 个体(Person)管理
     * <p/>
     * • 设置信息
     * 1)
     * 接口
     * http://api.youtu.qq.com/youtu/api/setinfo
     * 2)
     * 描述
     * 设置Person的name.
     * 3)
     * 参数
     * 参数名	类型	参数说明
     * 必须	app_id	String	App的 API ID
     * person_id	String	相应person的id
     * 可选	person_name	String	新的name
     * tag	String	备注信息
     * 4)
     * 返回值说明
     * 字段	类型	说明
     * session_id	Int	相应请求的session标识符
     * person_id	String	相应person的id
     * errorcode	Int	返回状态码
     * errormsg	String	返回错误消息
     *
     * @param mContext
     * @param app_id
     * @param person_id
     * @param person_name
     * @param tag
     * @param callback
     */
    public static void setInfo(Context mContext,
                               String app_id,
                               String person_id,
                               String person_name,
                               String tag,
                               NetCallback<SetInfoResponseBean> callback) {
        String sign = GetSignUtil.getInstance().getSignStr();
        RestAdapter restAdapter = getRestAdapter(mContext, sign);
        YouTuApi git = restAdapter.create(YouTuApi.class);
        SetInfoRequestBean bean = new SetInfoRequestBean();
        bean.setApp_id(app_id);
        bean.setPerson_id(person_id);
        bean.setPerson_name(person_name);
        bean.setTag(tag);
        git.setinfo(bean, callback);
    }
}

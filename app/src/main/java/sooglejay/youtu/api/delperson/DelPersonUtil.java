package sooglejay.youtu.api.delperson;

import android.content.Context;

import retrofit.RestAdapter;
import sooglejay.youtu.api.YouTuApi;
import sooglejay.youtu.model.NetCallback;
import sooglejay.youtu.utils.GetSignUtil;
import sooglejay.youtu.utils.RetrofitUtil;

/**
 * Created by sooglejay on 2015/11/19.
 */
public class DelPersonUtil extends RetrofitUtil {

//    个体(Person)管理
//
//    • 删除个体
//    1)
//    接口
//    http://api.youtu.qq.com/youtu/api/delperson
//            2)
//    描述
//            删除一个Person
//    3)
//    参数
//    参数名	类型	参数说明
//    必须	app_id	String	App的 API ID
//    person_id	String	待删除个体ID
//    4)
//    返回值说明
//    字段	类型	说明
//    session_id	Int	相应请求的session标识符
//    person_id	String	成功删除的person_id
//    deleted	Int	成功删除的Person数量
//    errorcode	Int	返回状态码
//    errormsg	String	返回错误消息

    public static void delPerson(Context mContext,
                                 String app_id,
                                 String person_id,
                                 NetCallback<DelPersonResponseBean> callback) {
        String sign = GetSignUtil.getInstance().getSignStr();
        RestAdapter restAdapter = getRestAdapter(mContext, sign);
        YouTuApi git = restAdapter.create(YouTuApi.class);
        DelPersonRequestBean bean = new DelPersonRequestBean();
        bean.setApp_id(app_id);
        bean.setPerson_id(person_id);
        git.delperson(bean, callback);
    }
}

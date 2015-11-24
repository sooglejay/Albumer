package sooglejay.youtu.api.delface;

import android.content.Context;

import retrofit.RestAdapter;
import sooglejay.youtu.api.YouTuApi;
import sooglejay.youtu.model.NetCallback;
import sooglejay.youtu.utils.GetSignUtil;
import sooglejay.youtu.utils.RetrofitUtil;

/**
 * Created by sooglejay on 2015/11/19.
 */
public class DelFaceUtil extends RetrofitUtil {


    /**
     * 个体(Person)管理
     * • 删除人脸
     * 1)
     * 接口
     * http://api.youtu.qq.com/youtu/api/delface
     * 2)
     * 描述
     * 删除一个person下的face，包括特征，属性和face_id.
     * 3)
     * 参数
     * 参数名	类型	参数说明
     * 必须	app_id	String	App的 API ID
     * person_id	String	待删除人脸的person ID
     * face_ids	Array(String)	删除人脸id的列表
     * 4)
     * 返回值说明
     * 字段	类型	说明
     * session_id	Int	相应请求的session标识符
     * deleted	Int	成功删除的face数量
     * face_ids	Array(String)	成功删除的face_id列表
     * errorcode	Int	返回状态码
     * errormsg	String	返回错误消息
     *
     * @param mContext
     * @param app_id
     * @param person_id
     * @param callback
     */
    public static void delface(Context mContext,
                               String app_id,
                               String person_id,
                               NetCallback<DelFaceResponseBean> callback) {
        String sign = GetSignUtil.getInstance().getSignStr();
        RestAdapter restAdapter = getRestAdapter(mContext, sign);
        YouTuApi git = restAdapter.create(YouTuApi.class);
        DelFaceRequestBean bean = new DelFaceRequestBean();
        bean.setApp_id(app_id);
        bean.setPerson_id(person_id);
        git.delface(bean, callback);
    }
}

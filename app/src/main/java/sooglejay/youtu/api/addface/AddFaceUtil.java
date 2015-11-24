package sooglejay.youtu.api.addface;

import android.content.Context;

import retrofit.RestAdapter;
import sooglejay.youtu.api.YouTuApi;
import sooglejay.youtu.model.NetCallback;
import sooglejay.youtu.utils.GetSignUtil;
import sooglejay.youtu.utils.RetrofitUtil;

/**
 * Created by sooglejay on 2015/11/19.
 */
public class AddFaceUtil extends RetrofitUtil {
//    个体(Person)管理
//
//    • 增加人脸
//    1)
//    接口
//    http://api.youtu.qq.com/youtu/api/addface
//            2)
//    描述
//    将一组Face加入到一个Person中。注意，一个Face只能被加入到一个Person中。 一个Person最多允许包含20个Face；加入几乎相同的人脸会返回错误。
//            3)
//    参数
//    参数名	类型	参数说明
//    必须	app_id	String	App的 API ID
//    可选	person_id	String	待增加人脸的个体id
//    可选	images	String(Bytes)	base64编码的二进制图片数据
//    可选	url	String	图片的urls, image 和url只提供一个就可以了,如果都提供,只使用urls
//    可选	tag	String	备注信息
//    4)
//    返回值说明
//    字段	类型	说明
//    session_id	Int	相应请求的session标识符
//    added	Int	成功加入的face数量
//    face_ids	String	增加的人脸ID列表
//    ret_codes	Array(Int)	每张图片增加人脸的返回码
//    errorcode	Int	返回状态码
//    errormsg	String	返回错误消息

    public static void addFace(Context mContext,
                                 String app_id,
                                 String person_id,
                                 NetCallback<AddFaceResponseBean> callback) {
        String sign = GetSignUtil.getInstance().getSignStr();
        RestAdapter restAdapter = getRestAdapter(mContext, sign);
        YouTuApi git = restAdapter.create(YouTuApi.class);
        AddFaceRequestBean bean = new AddFaceRequestBean();
        bean.setApp_id(app_id);
        bean.setPerson_id(person_id);
        git.addface(bean, callback);
    }
}

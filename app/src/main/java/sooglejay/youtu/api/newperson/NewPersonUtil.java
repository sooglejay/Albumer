package sooglejay.youtu.api.newperson;

import android.content.Context;

import java.util.List;

import retrofit.RestAdapter;
import sooglejay.youtu.api.YouTuApi;
import sooglejay.youtu.model.NetCallback;
import sooglejay.youtu.utils.GetSignUtil;
import sooglejay.youtu.utils.RetrofitUtil;

/**
 * Created by sooglejay on 2015/11/19.
 */
public class NewPersonUtil extends RetrofitUtil {


    /**
     * 个体创建
     * 1)
     * 接口
     * http://api.youtu.qq.com/youtu/api/newperson
     * 2)
     * 描述
     * 创建一个Person，并将Person放置到group_ids指定的组当中
     * 个体创建
     * 1)
     * 接口
     * http://api.youtu.qq.com/youtu/api/newperson
     * 2)
     * 描述
     * 创建一个Person，并将Person放置到group_ids指定的组当中
     * 3)
     * 参数
     * 参数名	类型	参数说明
     * 必须	app_id	String	App的 API ID
     * 必须	group_ids	Array(String)	加入到组的列表
     * 必须	person_id	String	指定的个体id
     * 可选	image	String(Bytes)	base64编码的二进制图片数据
     * 可选	url	String	图片的url, image 和url只提供一个就可以了,如果都提供,只使用url
     * 可选	person_name	String	名字
     * 可选	tag	String	备注信息
     *
     * @param mContext
     * @param app_id
     * @param group_ids
     * @param person_id
     * @param image
     * @param url
     * @param person_name
     * @param tag
     * @param callback
     */
    public static void newPerson(Context mContext,
                                 String app_id,
                                 List<String> group_ids,
                                 String person_id,
                                 String image,
                                 String url,
                                 String person_name,
                                 String tag,
                                 NetCallback<NewPersonResponseBean> callback) {
        String sign = GetSignUtil.getInstance().getSignStr();
        RestAdapter restAdapter = getRestAdapter(mContext, sign);
        YouTuApi git = restAdapter.create(YouTuApi.class);
        NewPersonRequestBean bean = new NewPersonRequestBean();
        bean.setApp_id(app_id);
        bean.setImage(image);
        bean.setUrl(url);
        bean.setPerson_id(person_id);
        bean.setGroup_ids(group_ids);
        bean.setPerson_name(person_name);
        bean.setTag(tag);
        git.newperson(bean, callback);
    }

}

package sooglejay.youtu.api.faceverify;

import android.os.Parcel;
import android.os.Parcelable;

import sooglejay.youtu.model.Model;

/**
 * Created by sooglejay on 2015/11/19.
 */
public class FaceVerifyRequestBean implements Model,Parcelable {

//    接口
//    http://api.youtu.qq.com/youtu/api/faceverify
//            2)
//    描述
//    给定一个Face和一个Person，返回是否是同一个人的判断以及置信度。
//            3)
//    参数
//    参数名	类型	参数说明
//    必须	app_id	String	App的 API ID
//    必须	person_id	String	待验证的Person
//    可选	image	String(Bytes)	base64编码的二进制图片数据
//    可选	url	String	图片的url, image 和url只提供一个就可以了,如果都提供,只使用url
//

    private String app_id;
    private String person_id;
    private String image;
    private String url;

    @Override
    public String toString() {
        return "FaceVerifyRequestBean{" +
                "app_id='" + app_id + '\'' +
                ", person_id='" + person_id + '\'' +
                ", image='" + image + '\'' +
                ", url='" + url + '\'' +
                '}';
    }

    public String getApp_id() {
        return app_id;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }

    public String getPerson_id() {
        return person_id;
    }

    public void setPerson_id(String person_id) {
        this.person_id = person_id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.app_id);
        dest.writeString(this.person_id);
        dest.writeString(this.image);
        dest.writeString(this.url);
    }

    public FaceVerifyRequestBean() {
    }

    protected FaceVerifyRequestBean(Parcel in) {
        this.app_id = in.readString();
        this.person_id = in.readString();
        this.image = in.readString();
        this.url = in.readString();
    }

    public static final Creator<FaceVerifyRequestBean> CREATOR = new Creator<FaceVerifyRequestBean>() {
        public FaceVerifyRequestBean createFromParcel(Parcel source) {
            return new FaceVerifyRequestBean(source);
        }

        public FaceVerifyRequestBean[] newArray(int size) {
            return new FaceVerifyRequestBean[size];
        }
    };
}

package sooglejay.youtu.api.faceidentify;

import android.os.Parcel;
import android.os.Parcelable;

import sooglejay.youtu.model.Model;

/**
 * Created by sooglejay on 2015/11/19.
 */
public class FaceIdentifyRequestBean implements Model,Parcelable {

    //先检测是否有人脸，然后去人脸识别，默认都是使用group id =1
//
//    人脸识别
//    1)
//    接口
//    http://api.youtu.qq.com/youtu/api/faceidentify
//            2)
//    描述
//    对于一个待识别的人脸图片，在一个Group中识别出最相似的Top5 Person作为其身份返回，返回的Top5中按照相似度从大到小排列。
//            3)
//    参数
//    参数名	类型	参数说明
//    必须	app_id	String	App的 API ID
//    必须	group_id	String	候选人组id
//    可选	image	String(Bytes)	base64编码的二进制图片数据
//    可选	url	String	图片的url, image 和url只提供一个就可以了,如果都提供,只使用url
//
//

    private String app_id;
    private String group_id;
    private String image;

    @Override
    public String toString() {
        return "FaceIdentifyRequestBean{" +
                "app_id='" + app_id + '\'' +
                ", group_id='" + group_id + '\'' +
                ", image='" + image + '\'' +
                '}';
    }

    public String getApp_id() {
        return app_id;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.app_id);
        dest.writeString(this.group_id);
        dest.writeString(this.image);
    }

    public FaceIdentifyRequestBean() {
    }

    protected FaceIdentifyRequestBean(Parcel in) {
        this.app_id = in.readString();
        this.group_id = in.readString();
        this.image = in.readString();
    }

    public static final Creator<FaceIdentifyRequestBean> CREATOR = new Creator<FaceIdentifyRequestBean>() {
        public FaceIdentifyRequestBean createFromParcel(Parcel source) {
            return new FaceIdentifyRequestBean(source);
        }

        public FaceIdentifyRequestBean[] newArray(int size) {
            return new FaceIdentifyRequestBean[size];
        }
    };
}

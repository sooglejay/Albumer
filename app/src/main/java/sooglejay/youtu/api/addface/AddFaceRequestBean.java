package sooglejay.youtu.api.addface;

import android.os.Parcel;
import android.os.Parcelable;

import sooglejay.youtu.model.Model;

/**
 * Created by sooglejay on 2015/11/19.
 */
public class AddFaceRequestBean implements Model,Parcelable {

    //            3)
//    参数
//    参数名	类型	参数说明
//    必须	app_id	String	App的 API ID
//    可选	person_id	String	待增加人脸的个体id
//    可选	images	String(Bytes)	base64编码的二进制图片数据
//    可选	url	String	图片的urls, image 和url只提供一个就可以了,如果都提供,只使用urls
//    可选	tag	String	备注信息

    private String app_id;
    private String person_id;
    private String images;
    private String url;
    private String tag;

    @Override
    public String toString() {
        return "AddFaceRequestBean{" +
                "app_id='" + app_id + '\'' +
                ", person_id='" + person_id + '\'' +
                ", images='" + images + '\'' +
                ", url='" + url + '\'' +
                ", tag='" + tag + '\'' +
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

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.app_id);
        dest.writeString(this.person_id);
        dest.writeString(this.images);
        dest.writeString(this.url);
        dest.writeString(this.tag);
    }

    public AddFaceRequestBean() {
    }

    protected AddFaceRequestBean(Parcel in) {
        this.app_id = in.readString();
        this.person_id = in.readString();
        this.images = in.readString();
        this.url = in.readString();
        this.tag = in.readString();
    }

    public static final Creator<AddFaceRequestBean> CREATOR = new Creator<AddFaceRequestBean>() {
        public AddFaceRequestBean createFromParcel(Parcel source) {
            return new AddFaceRequestBean(source);
        }

        public AddFaceRequestBean[] newArray(int size) {
            return new AddFaceRequestBean[size];
        }
    };
}

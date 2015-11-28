package sooglejay.youtu.api.detectface;

import android.os.Parcel;
import android.os.Parcelable;

import sooglejay.youtu.model.Model;

/**
 • 人脸对比
 1)
 接口
 http://api.youtu.qq.com/youtu/api/detectface
 2)
 检测给定图片(Image)中的所有人脸(FaceItem)的位置和相应的面部属性。位置包括(x, y, w, h)，面部属性包括性别(gender), 年龄(age), 表情(expression), 魅力(beauty), 眼镜(glass)和姿态(pitch，roll，yaw).
 3)
 必须	app_id	String	App的 API ID
 可选	image	String(Bytes)	base64编码的二进制图片数据
 可选	url	    String	图片的url, image 和url只提供一个就可以了,如果都提供,只使用url
 可选	mode	Int	检测模式 0/1 正常/大脸模式
 */
public class DetectFaceRequestBean implements Model,Parcelable {

    private String app_id;
    private String image;
    private int mode;

    @Override
    public String toString() {
        return "DetectFaceRequestBean{" +
                "app_id='" + app_id + '\'' +
                ", image='" + image + '\'' +
                ", mode=" + mode +
                '}';
    }

    public String getApp_id() {
        return app_id;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }



    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.app_id);
        dest.writeString(this.image);

        dest.writeInt(this.mode);
    }

    public DetectFaceRequestBean() {
    }

    protected DetectFaceRequestBean(Parcel in) {
        this.app_id = in.readString();
        this.image = in.readString();
        this.mode = in.readInt();
    }

    public static final Creator<DetectFaceRequestBean> CREATOR = new Creator<DetectFaceRequestBean>() {
        public DetectFaceRequestBean createFromParcel(Parcel source) {
            return new DetectFaceRequestBean(source);
        }

        public DetectFaceRequestBean[] newArray(int size) {
            return new DetectFaceRequestBean[size];
        }
    };
}

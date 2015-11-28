package sooglejay.youtu.api.faceshape;

import android.os.Parcel;
import android.os.Parcelable;

import sooglejay.youtu.model.Model;

/**
 接口
 http://api.youtu.qq.com/youtu/api/faceshape
 2)
 对请求图片进行五官定位，计算构成人脸轮廓的88个点，包括眉毛（左右各8点）、眼睛（左右各8点）、鼻子（13点）、嘴巴（22点）、脸型轮廓（21点）。各个部分点的顺序如下图所示
 */
public class FaceShapeRequestBean implements Model,Parcelable {
//    参数
//    参数名	类型	参数说明
//    必须	app_id	String	App的 API ID
//    可选	image	String(Bytes)	base64编码的二进制图片数据
//    可选	url	String	图片的url, image 和url只提供一个就可以了,如果都提供,只使用url
//    可选	mode	Int	检测模式 0/1 正常/大脸模式

    private String app_id;
    private String image;
    private String url;
    private int mode;

    @Override
    public String toString() {
        return "FaceShapeRequestBean{" +
                "app_id='" + app_id + '\'' +
                ", image='" + image + '\'' +
                ", url='" + url + '\'' +
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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
        dest.writeString(this.url);
        dest.writeInt(this.mode);
    }

    public FaceShapeRequestBean() {
    }

    protected FaceShapeRequestBean(Parcel in) {
        this.app_id = in.readString();
        this.image = in.readString();
        this.url = in.readString();
        this.mode = in.readInt();
    }

    public static final Creator<FaceShapeRequestBean> CREATOR = new Creator<FaceShapeRequestBean>() {
        public FaceShapeRequestBean createFromParcel(Parcel source) {
            return new FaceShapeRequestBean(source);
        }

        public FaceShapeRequestBean[] newArray(int size) {
            return new FaceShapeRequestBean[size];
        }
    };
}

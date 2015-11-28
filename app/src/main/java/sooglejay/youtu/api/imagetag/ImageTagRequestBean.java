package sooglejay.youtu.api.imagetag;

import android.os.Parcel;
import android.os.Parcelable;

import sooglejay.youtu.model.Model;

/**
 * Created by sooglejay on 2015/11/19.
 */
public class ImageTagRequestBean implements Model,Parcelable {
//    必须	app_id	String	App的 API ID
//    可选	image	String	需要检测的图像base64编码，图像需要是JPG PNG BMP 其中之一的格式
//    可选	url	String	图片可以下载的url, 如果url 和image 都提供, 仅使用ur
//    可选	seq	String	标示识别请求的序列号

    private String app_id;
    private String image;
    private String url;
    private String seq;

    @Override
    public String toString() {
        return "ImageTagRequestBean{" +
                "app_id='" + app_id + '\'' +
                ", image='" + image + '\'' +
                ", url='" + url + '\'' +
                ", seq='" + seq + '\'' +
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

    public String getSeq() {
        return seq;
    }

    public void setSeq(String seq) {
        this.seq = seq;
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
        dest.writeString(this.seq);
    }

    public ImageTagRequestBean() {
    }

    protected ImageTagRequestBean(Parcel in) {
        this.app_id = in.readString();
        this.image = in.readString();
        this.url = in.readString();
        this.seq = in.readString();
    }

    public static final Creator<ImageTagRequestBean> CREATOR = new Creator<ImageTagRequestBean>() {
        public ImageTagRequestBean createFromParcel(Parcel source) {
            return new ImageTagRequestBean(source);
        }

        public ImageTagRequestBean[] newArray(int size) {
            return new ImageTagRequestBean[size];
        }
    };
}

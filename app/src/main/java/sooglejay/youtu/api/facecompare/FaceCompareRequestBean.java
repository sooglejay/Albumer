package sooglejay.youtu.api.facecompare;

import android.os.Parcel;
import android.os.Parcelable;

import sooglejay.youtu.model.Model;

/**
 • 人脸对比
 1)
 接口
 http://api.youtu.qq.com/youtu/api/facecompare
 2)
 描述
 计算两个Face的相似性以及五官相似度。
 3)
 参数
 参数名	类型	参数说明
 必须	app_id	String	App的 API ID
 可选	imageA	String	使用base64编码的二进制图片数据A
 可选	imageB	String	使用base64编码的二进制图片数据B
 可选	urlA	String	A图片的url, image 和url只提供一个就可以了,如果都提供,只使用url
 可选	urlB	String	B图片的url, image 和url只提供一个就可以了,如果都提供,只使用url
 */
public class FaceCompareRequestBean implements Model,Parcelable {

    private String app_id;
    private String imageA;
    private String imageB;
    private String urlA;
    private String urlB;

    @Override
    public String toString() {
        return "DetectFaceRequestBean{" +
                "app_id='" + app_id + '\'' +
                ", imageA='" + imageA + '\'' +
                ", imageB='" + imageB + '\'' +
                ", urlA='" + urlA + '\'' +
                ", urlB='" + urlB + '\'' +
                '}';
    }

    public String getImageA() {
        return imageA;
    }

    public void setImageA(String imageA) {
        this.imageA = imageA;
    }

    public String getImageB() {
        return imageB;
    }

    public void setImageB(String imageB) {
        this.imageB = imageB;
    }

    public static Creator<FaceCompareRequestBean> getCREATOR() {
        return CREATOR;
    }

    public String getApp_id() {
        return app_id;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }

    public String getUrlA() {
        return urlA;
    }

    public void setUrlA(String urlA) {
        this.urlA = urlA;
    }

    public String getUrlB() {
        return urlB;
    }

    public void setUrlB(String urlB) {
        this.urlB = urlB;
    }

    public FaceCompareRequestBean() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.app_id);
        dest.writeString(this.imageA);
        dest.writeString(this.imageB);
        dest.writeString(this.urlA);
        dest.writeString(this.urlB);
    }

    protected FaceCompareRequestBean(Parcel in) {
        this.app_id = in.readString();
        this.imageA = in.readString();
        this.imageB = in.readString();
        this.urlA = in.readString();
        this.urlB = in.readString();
    }

    public static final Creator<FaceCompareRequestBean> CREATOR = new Creator<FaceCompareRequestBean>() {
        public FaceCompareRequestBean createFromParcel(Parcel source) {
            return new FaceCompareRequestBean(source);
        }

        public FaceCompareRequestBean[] newArray(int size) {
            return new FaceCompareRequestBean[size];
        }
    };
}
